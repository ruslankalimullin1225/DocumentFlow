package com.docflow.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "PAYMENT_REQUEST")
@DiscriminatorValue("PaymentRequest")
public class PaymentRequest extends Document {

    @Column(name = "counterparty_name", length = 100)
    private String counterpartyName;

    @Column(name = "request_amount", precision = 15, scale = 2)
    private BigDecimal requestAmount;

    @Column(name = "currency", length = 10)
    private String currency;

    @Column(name = "exchange_rate", precision = 10, scale = 4)
    private BigDecimal exchangeRate;

    @Column(name = "commission_amount", precision = 15, scale = 2)
    private BigDecimal commissionAmount;

    public PaymentRequest() {
        super();
        this.exchangeRate = BigDecimal.ONE;
        this.requestAmount = BigDecimal.ZERO;
        this.commissionAmount = BigDecimal.ZERO;
    }

    public PaymentRequest(String documentNumber, LocalDate documentDate, String userName,
                          String counterpartyName, BigDecimal requestAmount, String currency,
                          BigDecimal exchangeRate, BigDecimal commissionAmount) {
        super(documentNumber, documentDate, userName);
        this.counterpartyName = counterpartyName;
        this.requestAmount = requestAmount;
        this.currency = currency;
        this.exchangeRate = exchangeRate != null ? exchangeRate : BigDecimal.ONE;
        this.commissionAmount = commissionAmount;
    }

    @Override
    public String getDisplayName() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dateStr = getDocumentDate() != null ? getDocumentDate().format(formatter) : "";
        return String.format("Заявка на оплату №%s от %s", getDocumentNumber(), dateStr);
    }

    @Override
    public String getDocumentType() {
        return "PaymentRequest";
    }

    @Override
    public boolean validate() {
        return super.validate()
                && counterpartyName != null && !counterpartyName.trim().isEmpty()
                && requestAmount != null && requestAmount.compareTo(BigDecimal.ZERO) > 0
                && currency != null && !currency.trim().isEmpty()
                && exchangeRate != null && exchangeRate.compareTo(BigDecimal.ZERO) > 0
                && commissionAmount != null && commissionAmount.compareTo(BigDecimal.ZERO) >= 0;
    }

    public String getCounterpartyName() {
        return counterpartyName;
    }

    public void setCounterpartyName(String counterpartyName) {
        this.counterpartyName = counterpartyName;
    }

    public BigDecimal getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(BigDecimal requestAmount) {
        this.requestAmount = requestAmount;
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

    public BigDecimal getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(BigDecimal commissionAmount) {
        this.commissionAmount = commissionAmount;
    }
}
