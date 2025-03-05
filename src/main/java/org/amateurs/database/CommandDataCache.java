package org.amateurs.database;

public interface CommandDataCache {
    /**
     * Inserts new command data into the cache
     * @param key       key for the new entry
     * @param data   current data to cache
     */
    void cacheNewCommandData(String key, String data);

    /**
     * Gets an existing command data from the cache for a given key
     * @param key       key for the cache entry
     * @return          command data cached for given key
     */
    String getCommandDataForKey(String key);

    /**
     * Updates an existing command in the cache for a given key
     * @param key       key for the cache entry
     * @param data      command data to update to
     */
    void updateCommandData(String key, String data);
}
