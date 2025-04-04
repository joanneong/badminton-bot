package amateurs.database;

import java.util.HashMap;
import java.util.Map;

public class InMemoryCommandDataCache implements CommandDataCache {
    final Map<String, String> cache = new HashMap<>();

    private static final InMemoryCommandDataCache inMemoryCache = new InMemoryCommandDataCache();

    private InMemoryCommandDataCache() {

    }

    public static InMemoryCommandDataCache getInstance() {
        return inMemoryCache;
    }

    @Override
    public void cacheNewCommandData(String key, String data) {
        cache.put(key, data);
    }

    @Override
    public String getCommandDataForKey(String key) {
        return cache.get(key);
    }

    @Override
    public void updateCommandData(String key, String data) {
        cache.put(key, data);
    }
}
