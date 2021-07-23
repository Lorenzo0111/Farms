package me.lorenzo0111.farms.premium;

public final class PremiumHandler {

    public static String getUserID() {
        return "%%__USER__%%";
    }

    public static String getResourceID() {
        return "%%__RESOURCE__%%";
    }

    public static String getDownloadID() {
        return "%%__NONCE__%%";
    }

    private static boolean isPremium() {
        return !getUserID().contains("__USER__");
    }

}
