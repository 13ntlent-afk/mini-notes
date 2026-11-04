package com.rocku.gateway.security;

public final class BffSessionAttributes {
    public static final String ACCESS_TOKEN = BffSessionAttributes.class.getName() + ".accessToken";
    public static final String USER = BffSessionAttributes.class.getName() + ".user";

    private BffSessionAttributes() {
    }
}