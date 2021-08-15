/*
 * -------------------------------------
 * Copyright Lorenzo0111 2021
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.premium;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.lorenzo0111.farms.Farms;
import org.bukkit.Bukkit;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class LicenseHandler implements Runnable {
    private static final String SERVER = "https://license.rocketplugins.space/api.php";
    private final Farms plugin;

    public LicenseHandler(Farms plugin) {
        this.plugin = plugin;
        if (PremiumHandler.isPremium() && "null".equals(plugin.getConfig().getString("license"))) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin,this);
        }
    }

    @Override
    public void run() {
        try {
            HttpsURLConnection con = (HttpsURLConnection) new URL(SERVER + "?plugin=" + plugin.getName() + "&user=" + PremiumHandler.getUserID()).openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            int responseCode = con.getResponseCode();
            if (responseCode != 200) return;

            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                JsonObject json = new JsonParser().parse(response.toString()).getAsJsonObject();
                if (json.get("error").getAsBoolean()) {
                    plugin.debug("License server returned an error: " + json.get("message").getAsString());
                }

                String license = json.get("license").getAsString();
                plugin.getConfig().set("license", license);
                plugin.getConfig().save();
            }
        } catch (Exception ignored) {}
    }
}
