package com.umar.apps.hibernate.inverse;

import java.util.HashSet;
import java.util.Set;

public class Stock {
    private Long id;
    private String stockCode;
    private String stockName;

    private Set<StockDailyRecord> stockDailyRecords = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public void addStockDailyRecord(StockDailyRecord stockDailyRecord) {
        stockDailyRecords.add(stockDailyRecord);
        stockDailyRecord.setStock(this);
    }

    public void removeStockDailyRecord(StockDailyRecord stockDailyRecord) {
        stockDailyRecords.remove(stockDailyRecord);
        stockDailyRecord.setStock(null);
    }

    public Set<StockDailyRecord> getStockDailyRecords() {
        return stockDailyRecords;
    }

    public void setStockDailyRecords(Set<StockDailyRecord> stockDailyRecords) {
        this.stockDailyRecords = stockDailyRecords;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", stockCode='" + stockCode + '\'' +
                ", stockName='" + stockName + '\'' +
                '}';
    }
}
