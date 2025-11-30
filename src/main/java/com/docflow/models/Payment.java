package com.docflow.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "PAYMENT")
@DiscriminatorValue("Payment")
public class Payment extends Document {

    @Column(name = "payment_amount", precision = 15, scale = 2)
    private BigDecimal paymentAmount;

    @Column(name = "employee_name", length = 100)
    private String employeeName;

    public Payment() {
        super();
        this.paymentAmount = BigDecimal.ZERO;
    }

    public Payment(String documentNumber, LocalDate documentDate, String userName,
                   BigDecimal paymentAmount, String employeeName) {
        super(documentNumber, documentDate, userName);
        this.paymentAmount = paymentAmount;
        this.employeeName = employeeName;
    }

    @Override
    public String getDisplayName() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dateStr = getDocumentDate() != null ? getDocumentDate().format(formatter) : "";
        return String.format("Платёжка №%s от %s", getDocumentNumber(), dateStr);
    }

    @Override
    public String getDocumentType() {
        return "Payment";
    }

    @Override
    public boolean validate() {
        return super.validate()
                && paymentAmount != null && paymentAmount.compareTo(BigDecimal.ZERO) > 0
                && employeeName != null && !employeeName.trim().isEmpty();
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}
