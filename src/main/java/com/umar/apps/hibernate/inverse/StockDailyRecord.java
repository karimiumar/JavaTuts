package com.umar.apps.hibernate.inverse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class StockDailyRecord {

    private Long id;
    private BigDecimal priceOpen;
    private BigDecimal priceClose;
    private BigDecimal priceChange;
    private int volume;
    private LocalDate date;
    private Stock stock;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPriceOpen() {
        return priceOpen;
    }

    public void setPriceOpen(BigDecimal priceOpen) {
        this.priceOpen = priceOpen;
    }

    public BigDecimal getPriceClose() {
        return priceClose;
    }

    public void setPriceClose(BigDecimal priceClose) {
        this.priceClose = priceClose;
    }

    public BigDecimal getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(BigDecimal priceChange) {
        this.priceChange = priceChange;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "StockDailyRecord{" +
                "id=" + id +
                ", priceOpen=" + priceOpen +
                ", priceClose=" + priceClose +
                ", priceChange=" + priceChange +
                ", volume=" + volume +
                ", date=" + date +
                ", stock=" + stock +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockDailyRecord that = (StockDailyRecord) o;
        if(Objects.equals(id, that.id)) return true;
        return volume == that.volume && Objects.equals(priceOpen, that.priceOpen) && Objects.equals(priceClose, that.priceClose) && Objects.equals(priceChange, that.priceChange) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, priceOpen, priceClose, priceChange, volume, date);
    }
}
