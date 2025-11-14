package org.leavesmc.leaves.bot;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fun.bm.lophine.config.modules.function.FakeplayerConfig;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MojangAPI {

    private static final Map<String, String[]> CACHE = new HashMap<>();
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(2);
    private static final int CONNECT_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 10000;

    public static String[] getSkin(String name) {
        if (FakeplayerConfig.useSkinCache && CACHE.containsKey(name)) {
            return CACHE.get(name);
        }

        String[] values = pullFromAPI(name);
        if (values != null) {
            CACHE.put(name, values);
        }
        return values;
    }

    public static CompletableFuture<String[]> getSkinAsync(String name) {
        if (FakeplayerConfig.useSkinCache && CACHE.containsKey(name)) {
            return CompletableFuture.completedFuture(CACHE.get(name));
        }

        return CompletableFuture.supplyAsync(() -> {
            String[] values = pullFromAPI(name);
            if (values != null) {
                CACHE.put(name, values);
            }
            return values;
        }, EXECUTOR);
    }

    private static String[] pullFromAPI(String name) {
        HttpURLConnection uuidConnection = null;
        HttpURLConnection profileConnection = null;
        
        try {
            String uuidUrl = "https://htttp-proxy.262832.xyz/https://api.mojang.com/users/profiles/minecraft/" + name;
            uuidConnection = createConnection(uuidUrl);
            
            String uuid = JsonParser.parseReader(new InputStreamReader(uuidConnection.getInputStream()))
                    .getAsJsonObject().get("id").getAsString();
                    
            String profileUrl = "https://htttp-proxy.262832.xyz/https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false";
            profileConnection = createConnection(profileUrl);
            
            JsonObject property = JsonParser.parseReader(new InputStreamReader(profileConnection.getInputStream()))
                    .getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
                    
            return new String[]{property.get("value").getAsString(), property.get("signature").getAsString()};
            
        } catch (IOException | IllegalStateException | IllegalArgumentException e) {
            return null;
        } finally {
            if (uuidConnection != null) {
                uuidConnection.disconnect();
            }
            if (profileConnection != null) {
                profileConnection.disconnect();
            }
        }
    }

    private static HttpURLConnection createConnection(String urlString) throws IOException {
        URL url = URI.create(urlString).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(CONNECT_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "LeavesMC/ctn");
        return connection;
    }

    public static void clearCache() {
        CACHE.clear();
    }

    public static void clearCache(String name) {
        CACHE.remove(name);
    }

    public static void shutdown() {
        EXECUTOR.shutdown();
    }
}