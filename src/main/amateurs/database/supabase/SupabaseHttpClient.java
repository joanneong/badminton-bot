package amateurs.database.supabase;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Optional;

import static java.net.URI.create;

public class SupabaseHttpClient {
    private static final String SUPABASE_PROJECT_ID = System.getenv("SUPABASE_PROJECT_ID");

    private static final String SUPABASE_API_KEY = System.getenv("SUPABASE_API_KEY");

    private static final String SUPABASE_URL_TEMPLATE = "https://%s.supabase.co/rest/v1/%s";

    private static final String SUPABASE_API_KEY_HEADER = "apikey";

    private static final SupabaseHttpClient supabaseClient = new SupabaseHttpClient();

    private static final Logger LOG = LogManager.getLogger();

    private final HttpClient httpClient;

    private SupabaseHttpClient() {
        this.httpClient = HttpClient.newBuilder().build();
    }

    public static SupabaseHttpClient getInstance() {
        return supabaseClient;
    }

    public HttpResponse<String> sendGetRequest(String tableName, Map<String, String> queryParams) {
        final String url = constructFullUrl(tableName, queryParams);
        final HttpRequest req = HttpRequest.newBuilder()
                .setHeader(SUPABASE_API_KEY_HEADER, SUPABASE_API_KEY)
                .uri(create(url))
                .build();

        LOG.info("Full request: {}", req);

        try {
            return httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new RuntimeException("Error sending request to Supabase! %s", e);
        }
    }

    public HttpResponse<String> sendPostRequest(String tableName, String jsonData) {
        final String url = String.format(SUPABASE_URL_TEMPLATE, SUPABASE_PROJECT_ID, tableName);
        try {
            final HttpRequest req = HttpRequest.newBuilder()
                    .setHeader(SUPABASE_API_KEY_HEADER, SUPABASE_API_KEY)
                    .uri(create(url))
                    .header("Content-Type", "application/json")
                    .header("Prefer", "return=representation")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonData))
                    .build();

            LOG.info("Full request: {}", req);

            return httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        } catch (JsonProcessingException jpe) {
            LOG.error(jpe.getMessage());
            throw new RuntimeException("Error processing data for post request! %s", jpe);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new RuntimeException("Error sending request to Supabase! %s", e);
        }
    }

    public HttpResponse<String> sendDeleteRequest(String tableName, Map<String, String> queryParams) {
        final String url = constructFullUrl(tableName, queryParams);
        final HttpRequest req = HttpRequest.newBuilder()
                .setHeader(SUPABASE_API_KEY_HEADER, SUPABASE_API_KEY)
                .uri(create(url))
                .DELETE()
                .build();

        LOG.info("Full request: {}", req);

        try {
            return httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new RuntimeException("Error sending request to Supabase! %s", e);
        }
    }

    private String constructFullUrl(String tableName, Map<String, String> queryParams) {
        String url = String.format(SUPABASE_URL_TEMPLATE, SUPABASE_PROJECT_ID, tableName);
        final Optional<String> fullQueryParams = queryParams.entrySet().stream()
                .map(query -> query.getKey() + "=" + query.getValue())
                .reduce((a, b) -> a + "&" + b);
        if (fullQueryParams.isPresent()) {
            url += "?" + fullQueryParams.get();
        }
        return url;
    }
}
