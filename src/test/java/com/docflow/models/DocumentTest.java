package com.docflow.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DocumentTest {

    @Test
    void testInvoiceCreation() {
        Invoice invoice = new Invoice("INV-001", LocalDate.now(), "Admin",
                BigDecimal.valueOf(1500.00), "USD", BigDecimal.ONE,
                "Laptop", BigDecimal.valueOf(2.0));

        assertEquals("INV-001", invoice.getDocumentNumber());
        assertEquals("Admin", invoice.getUserName());
        assertEquals(BigDecimal.valueOf(1500.00), invoice.getTotalAmount());
        assertEquals("USD", invoice.getCurrency());
        assertEquals("Laptop", invoice.getProductName());
        assertEquals("Invoice", invoice.getDocumentType());
    }

    @Test
    void testInvoiceDisplayName() {
        Invoice invoice = new Invoice("INV-001", LocalDate.of(2025, 11, 30), "Admin",
                BigDecimal.valueOf(1500.00), "USD", BigDecimal.ONE,
                "Laptop", BigDecimal.valueOf(2.0));

        String displayName = invoice.getDisplayName();
        assertTrue(displayName.contains("Накладная"));
        assertTrue(displayName.contains("INV-001"));
        assertTrue(displayName.contains("30.11.2025"));
    }

    @Test
    void testInvoiceValidation() {
        Invoice validInvoice = new Invoice("INV-001", LocalDate.now(), "Admin",
                BigDecimal.valueOf(1500.00), "USD", BigDecimal.ONE,
                "Laptop", BigDecimal.valueOf(2.0));
        assertTrue(validInvoice.validate());

        Invoice invalidInvoice = new Invoice("", LocalDate.now(), "Admin",
                BigDecimal.valueOf(-100), "USD", BigDecimal.ONE,
                "Laptop", BigDecimal.valueOf(2.0));
        assertFalse(invalidInvoice.validate());
    }

    @Test
    void testPaymentCreation() {
        Payment payment = new Payment("PAY-001", LocalDate.now(), "Admin",
                BigDecimal.valueOf(5000.00), "John Doe");

        assertEquals("PAY-001", payment.getDocumentNumber());
        assertEquals("Admin", payment.getUserName());
        assertEquals(BigDecimal.valueOf(5000.00), payment.getPaymentAmount());
        assertEquals("John Doe", payment.getEmployeeName());
        assertEquals("Payment", payment.getDocumentType());
    }

    @Test
    void testPaymentDisplayName() {
        Payment payment = new Payment("PAY-001", LocalDate.of(2025, 11, 30), "Admin",
                BigDecimal.valueOf(5000.00), "John Doe");

        String displayName = payment.getDisplayName();
        assertTrue(displayName.contains("Платёжка"));
        assertTrue(displayName.contains("PAY-001"));
        assertTrue(displayName.contains("30.11.2025"));
    }

    @Test
    void testPaymentValidation() {
        Payment validPayment = new Payment("PAY-001", LocalDate.now(), "Admin",
                BigDecimal.valueOf(5000.00), "John Doe");
        assertTrue(validPayment.validate());

        Payment invalidPayment = new Payment("PAY-001", LocalDate.now(), "",
                BigDecimal.ZERO, "John Doe");
        assertFalse(invalidPayment.validate());
    }

    @Test
    void testPaymentRequestCreation() {
        PaymentRequest request = new PaymentRequest("REQ-001", LocalDate.now(), "Admin",
                "ABC Company", BigDecimal.valueOf(10000.00), "EUR",
                BigDecimal.valueOf(1.1), BigDecimal.valueOf(50.00));

        assertEquals("REQ-001", request.getDocumentNumber());
        assertEquals("Admin", request.getUserName());
        assertEquals("ABC Company", request.getCounterpartyName());
        assertEquals(BigDecimal.valueOf(10000.00), request.getRequestAmount());
        assertEquals("EUR", request.getCurrency());
        assertEquals("PaymentRequest", request.getDocumentType());
    }

    @Test
    void testPaymentRequestDisplayName() {
        PaymentRequest request = new PaymentRequest("REQ-001", LocalDate.of(2025, 11, 30), "Admin",
                "ABC Company", BigDecimal.valueOf(10000.00), "EUR",
                BigDecimal.valueOf(1.1), BigDecimal.valueOf(50.00));

        String displayName = request.getDisplayName();
        assertTrue(displayName.contains("Заявка на оплату"));
        assertTrue(displayName.contains("REQ-001"));
        assertTrue(displayName.contains("30.11.2025"));
    }

    @Test
    void testPaymentRequestValidation() {
        PaymentRequest validRequest = new PaymentRequest("REQ-001", LocalDate.now(), "Admin",
                "ABC Company", BigDecimal.valueOf(10000.00), "EUR",
                BigDecimal.valueOf(1.1), BigDecimal.valueOf(50.00));
        assertTrue(validRequest.validate());

        PaymentRequest invalidRequest = new PaymentRequest("REQ-001", LocalDate.now(), "Admin",
                "", BigDecimal.valueOf(10000.00), "EUR",
                BigDecimal.valueOf(0), BigDecimal.valueOf(50.00));
        assertFalse(invalidRequest.validate());
    }

    @Test
    void testDocumentEquality() {
        Invoice invoice1 = new Invoice("INV-001", LocalDate.now(), "Admin",
                BigDecimal.valueOf(1500.00), "USD", BigDecimal.ONE,
                "Laptop", BigDecimal.valueOf(2.0));

        Invoice invoice2 = new Invoice("INV-001", LocalDate.now(), "Admin",
                BigDecimal.valueOf(1500.00), "USD", BigDecimal.ONE,
                "Laptop", BigDecimal.valueOf(2.0));

        assertEquals(invoice1.getDocumentNumber(), invoice2.getDocumentNumber());
    }
}
