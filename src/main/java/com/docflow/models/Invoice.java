package com.docflow.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "INVOICE")
@DiscriminatorValue("Invoice")
public class Invoice extends Document {

    @Column(name = "total_amount", precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "currency", length = 10)
    private String currency;

    @Column(name = "exchange_rate", precision = 10, scale = 4)
    private BigDecimal exchangeRate;

    @Column(name = "product_name", length = 200)
    private String productName;

    @Column(name = "quantity", precision = 10, scale = 2)
    private BigDecimal quantity;

    public Invoice() {
        super();
        this.exchangeRate = BigDecimal.ONE;
        this.quantity = BigDecimal.ZERO;
        this.totalAmount = BigDecimal.ZERO;
    }

    public Invoice(String documentNumber, LocalDate documentDate, String userName,
                   BigDecimal totalAmount, String currency, BigDecimal exchangeRate,
                   String productName, BigDecimal quantity) {
        super(documentNumber, documentDate, userName);
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.exchangeRate = exchangeRate != null ? exchangeRate : BigDecimal.ONE;
        this.productName = productName;
        this.quantity = quantity;
    }

    @Override
    public String getDisplayName() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dateStr = getDocumentDate() != null ? getDocumentDate().format(formatter) : "";
        return String.format("Накладная №%s от %s", getDocumentNumber(), dateStr);
    }

    @Override
    public String getDocumentType() {
        return "Invoice";
    }

    @Override
    public boolean validate() {
        return super.validate()
                && totalAmount != null && totalAmount.compareTo(BigDecimal.ZERO) >= 0
                && currency != null && !currency.trim().isEmpty()
                && exchangeRate != null && exchangeRate.compareTo(BigDecimal.ZERO) > 0
                && productName != null && !productName.trim().isEmpty()
                && quantity != null && quantity.compareTo(BigDecimal.ZERO) > 0;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
