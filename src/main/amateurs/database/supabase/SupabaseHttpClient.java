package amateurs.database.supabase;

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

    public HttpResponse<String> sendRequest(String tableName, Map<String, String> queryParams) {
        String url = String.format(SUPABASE_URL_TEMPLATE, SUPABASE_PROJECT_ID, tableName);
        final Optional<String> fullQueryParams = queryParams.entrySet().stream()
                .map(query -> query.getKey() + "=" + query.getValue())
                .reduce((a, b) -> a + "&" + b);
        if (fullQueryParams.isPresent()) {
            url += "?" + fullQueryParams.get();
        }

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
}
