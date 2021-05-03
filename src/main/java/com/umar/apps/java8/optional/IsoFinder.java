package com.umar.apps.java8.optional;

import java.util.Optional;

public class IsoFinder {

    public static String isoFinderImperativeStyle(User user) {
        if(null != user) {
            User.Address address = user.address();
            if(null != address) {
                User.Address.Country country = address.country();
                if(null != country) {
                    String isoCode = country.isoCode();
                    return (null != isoCode) ? isoCode.toUpperCase() : null;
                }
                throw new NullPointerException("Country is null");
            }
            throw new NullPointerException("Address is null");
        }
        throw new NullPointerException("Incoming User object is null");
    }

    public static String isoFinderFunctionalStyle(User user) {
        return Optional.ofNullable(user)
                .map(User::address)
                .map(User.Address::country)
                .map(User.Address.Country::isoCode)
                .map(String::toUpperCase)
                .orElse("default");
    }
}
