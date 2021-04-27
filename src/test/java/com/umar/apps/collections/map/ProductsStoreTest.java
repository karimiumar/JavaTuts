package com.umar.apps.collections.map;

import com.umar.apps.map.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class ProductsStoreTest {

    @Test
    public void when_map_is_created_then_its_empty() {
        Map<String, Product> productsStore = new HashMap<>();
        Assertions.assertTrue(productsStore.isEmpty());
    }

    @Test
    public void when_a_key_is_added_then_map_contains_the_key(){
        Map<String, Product> productsStore = new HashMap<>();
        var eBike = Product.create("E-Bike", "Bike with a Battery");
        productsStore.put(eBike.getName(), eBike);
        Assertions.assertTrue(productsStore.containsKey("E-Bike"));
    }
}
