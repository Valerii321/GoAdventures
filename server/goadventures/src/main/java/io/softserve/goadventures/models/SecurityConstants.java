package io.softserve.goadventures.models;
//TODO This class should be moved to some util package, because it is not a model
public class SecurityConstants {
    public static final String SECRET = "SecretKey";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public  static final String TOKEN_PREFIX = "Bearer ";

    public static final long REFRESH_TIME = 300_000;        // 5 minutes
    public static final String REFRESH_SECRET = "KonnanJaKittoMonotarinai";
}
