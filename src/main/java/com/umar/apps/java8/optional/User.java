package com.umar.apps.java8.optional;

public record User(String username, String fullName, Address address) {
    public static record Address(String street, String zip, Country country) {
        public static record Country(String countryName, String isoCode) {}
    }
}
