package com.cl.learn.redis.model.entity;

import java.util.Objects;

public class Product {
    private Integer id;

    private String productName;

    private String category;

    private String productType;

    private Integer functionEntry;

    public Product(Integer id, String productName, String category, String productType, Integer functionEntry) {
        this.id = id;
        this.productName = productName;
        this.category = category;
        this.productType = productType;
        this.functionEntry = functionEntry;
    }

    public Product() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType == null ? null : productType.trim();
    }

    public Integer getFunctionEntry() {
        return functionEntry;
    }

    public void setFunctionEntry(Integer functionEntry) {
        this.functionEntry = functionEntry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(productName, product.productName) &&
                Objects.equals(category, product.category) &&
                Objects.equals(productType, product.productType) &&
                Objects.equals(functionEntry, product.functionEntry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName, category, productType, functionEntry);
    }
}