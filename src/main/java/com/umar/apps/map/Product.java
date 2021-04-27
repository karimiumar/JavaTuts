package com.umar.apps.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Product {

    private final String name;
    private final String description;
    private final List<String> tags;

    private Product(String name, String description) {
        this.name = name;
        this.description = description;
        this.tags = new ArrayList<>();
    }

    public static Product create(String name, String description) {
        return new Product(name, description);
    }

    public void addTagsOfOtherProduct(Product product) {
        tags.addAll(product.getTags());
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public List<String> getTags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name) &&
                Objects.equals(description, product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
