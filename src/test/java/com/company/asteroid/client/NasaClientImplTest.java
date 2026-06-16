package com.company.asteroid.client;


import com.company.asteroid.config.NasaProperties;
import com.company.asteroid.dto.nasa.NasaFeedResponse;
import com.company.asteroid.dto.nasa.NasaLookupResponse;
import com.company.asteroid.exception.NasaApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class NasaClientImplTest {

    private MockWebServer mockWebServer;
    private NasaClientImpl nasaClient;

    @Mock
    private NasaProperties nasaProperties;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        when(nasaProperties.getApiKey()).thenReturn("TEST_API_KEY");

        nasaClient = new NasaClientImpl(webClient, nasaProperties);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getFeed_success() throws Exception {
        String responseBody = """
                {
                  "element_count": 2,
                  "near_earth_objects": {}
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(responseBody));

        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end   = LocalDate.of(2024, 1, 7);

        NasaFeedResponse response = nasaClient.getFeed(start, end);

        assertThat(response).isNotNull();
        assertThat(response.getElementCount()).isEqualTo(2);
    }

    @Test
    void getFeed_shouldSendCorrectQueryParams() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"element_count\":0,\"near_earth_objects\":{}}"));

        LocalDate start = LocalDate.of(2024, 3, 15);
        LocalDate end   = LocalDate.of(2024, 3, 22);

        nasaClient.getFeed(start, end);

        RecordedRequest recorded = mockWebServer.takeRequest();
        String path = recorded.getPath();

        assertThat(path).contains("/neo/rest/v1/feed");
        assertThat(path).contains("start_date=2024-03-15");
        assertThat(path).contains("end_date=2024-03-22");
        assertThat(path).contains("api_key=TEST_API_KEY");
    }

    @Test
    void getFeed_clientError_throwsNasaApiException() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"error\":\"Bad Request\"}"));

        assertThatThrownBy(() -> nasaClient.getFeed(LocalDate.now(), LocalDate.now()))
                .isInstanceOf(NasaApiException.class)
                .hasMessageContaining("NASA API returned error:");
    }

    @Test
    void getFeed_serverError_throwsNasaApiException() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"error\":\"Internal Server Error\"}"));

        assertThatThrownBy(() -> nasaClient.getFeed(LocalDate.now(), LocalDate.now()))
                .isInstanceOf(NasaApiException.class)
                .hasMessageContaining("NASA API returned error:");
    }

    @Test
    void getFeed_unauthorized_throwsNasaApiException() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(401)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"error\":\"API key invalid\"}"));

        assertThatThrownBy(() -> nasaClient.getFeed(LocalDate.now(), LocalDate.now()))
                .isInstanceOf(NasaApiException.class)
                .hasMessageContaining("401");
    }

    @Test
    void getLookup_success() throws Exception {
        // language=JSON
        String responseBody = """
                {
                  "id": "3542519",
                  "name": "433 Eros",
                  "nasa_jpl_url": "https://ssd.jpl.nasa.gov/tools/sbdb_lookup.html#/?sstr=3542519"
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(responseBody));

        NasaLookupResponse response = nasaClient.getLookup("3542519");

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo("3542519");
        assertThat(response.getName()).isEqualTo("433 Eros");
    }

    @Test
    void getLookup_shouldSendCorrectRequest() throws Exception {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"id\":\"3542519\",\"name\":\"433 Eros\"}"));

        nasaClient.getLookup("3542519");

        RecordedRequest recorded = mockWebServer.takeRequest();
        String path = recorded.getPath();

        assertThat(path).contains("/neo/rest/v1/neo/3542519");
        assertThat(path).contains("api_key=TEST_API_KEY");
    }

    @Test
    void getLookup_notFound_throwsNasaApiException() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"error\":\"Asteroid not found\"}"));

        assertThatThrownBy(() -> nasaClient.getLookup("invalid-id"))
                .isInstanceOf(NasaApiException.class)
                .hasMessageContaining("NASA Lookup API returned error:")
                .hasMessageContaining("404");
    }

    @Test
    void getLookup_serverError_throwsNasaApiExceptionWithBody() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"error\":\"Upstream failure\"}"));

        assertThatThrownBy(() -> nasaClient.getLookup("3542519"))
                .isInstanceOf(NasaApiException.class)
                .hasMessageContaining("NASA Lookup API returned error:")
                .hasMessageContaining("body:");
    }

    @Test
    void getLookup_forbidden_throwsNasaApiException() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(403)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("{\"error\":\"Forbidden\"}"));

        assertThatThrownBy(() -> nasaClient.getLookup("3542519"))
                .isInstanceOf(NasaApiException.class)
                .hasMessageContaining("403");
    }
}
