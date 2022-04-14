/*
 * -------------------------------------
 * Copyright Lorenzo0111 2022
 * https://github.com/Lorenzo0111
 * -------------------------------------
 */

package me.lorenzo0111.farms.premium;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class PremiumHandler {

    @Contract(pure = true)
    public static @NotNull String getUserID() {
        return "%%__USER__%%";
    }

    @Contract(pure = true)
    public static @NotNull String getResourceID() {
        return "%%__RESOURCE__%%";
    }

    @Contract(pure = true)
    public static @NotNull String getDownloadID() {
        return "%%__NONCE__%%";
    }

    @Contract(pure = true)
    public static @NotNull String isSongoda() {
        return "%%__SONGODA__%%";
    }

    public static @NotNull String formatUserURL() {
        return isSongoda().equals("true") ? "https://songoda.com/profile/" + "%%__USERNAME__%%" : "https://www.spigotmc.org/members/" + getUserID();
    }

    public static boolean isPremium() {
        return !getUserID().contains("__USER__");
    }

}
