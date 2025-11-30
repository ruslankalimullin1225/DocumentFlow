package com.docflow.services;

import com.docflow.models.Document;
import com.docflow.models.Invoice;
import com.docflow.models.Payment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DocumentServiceTest {

    private EntityManagerFactory testFactory;
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        testFactory = Persistence.createEntityManagerFactory("DocumentFlowTestPU");
        entityManager = testFactory.createEntityManager();
    }

    @AfterEach
    void tearDown() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
        if (testFactory != null && testFactory.isOpen()) {
            testFactory.close();
        }
    }

    @Test
    void testSaveAndRetrieveInvoice() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Invoice invoice = new Invoice("INV-TEST-001", LocalDate.now(), "TestUser",
                BigDecimal.valueOf(1000.00), "USD", BigDecimal.ONE,
                "Test Product", BigDecimal.valueOf(1.0));

        entityManager.persist(invoice);
        transaction.commit();

        assertNotNull(invoice.getId());

        Invoice retrieved = entityManager.find(Invoice.class, invoice.getId());
        assertNotNull(retrieved);
        assertEquals("INV-TEST-001", retrieved.getDocumentNumber());
        assertEquals("TestUser", retrieved.getUserName());
    }

    @Test
    void testSaveAndRetrievePayment() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Payment payment = new Payment("PAY-TEST-001", LocalDate.now(), "TestUser",
                BigDecimal.valueOf(500.00), "Test Employee");

        entityManager.persist(payment);
        transaction.commit();

        assertNotNull(payment.getId());

        Payment retrieved = entityManager.find(Payment.class, payment.getId());
        assertNotNull(retrieved);
        assertEquals("PAY-TEST-001", retrieved.getDocumentNumber());
        assertEquals("Test Employee", retrieved.getEmployeeName());
    }

    @Test
    void testInvoiceValidation() {
        Invoice validInvoice = new Invoice("INV-TEST-002", LocalDate.now(), "TestUser",
                BigDecimal.valueOf(1000.00), "USD", BigDecimal.ONE,
                "Test Product", BigDecimal.valueOf(1.0));
        assertTrue(validInvoice.validate());

        Invoice invalidInvoice = new Invoice("", LocalDate.now(), "",
                BigDecimal.valueOf(-100), "USD", BigDecimal.ONE,
                "Test Product", BigDecimal.valueOf(1.0));
        assertFalse(invalidInvoice.validate());
    }

    @Test
    void testPaymentValidation() {
        Payment validPayment = new Payment("PAY-TEST-002", LocalDate.now(), "TestUser",
                BigDecimal.valueOf(500.00), "Test Employee");
        assertTrue(validPayment.validate());

        Payment invalidPayment = new Payment("", LocalDate.now(), "",
                BigDecimal.ZERO, "Test Employee");
        assertFalse(invalidPayment.validate());
    }

    @Test
    void testDeleteDocument() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Invoice invoice = new Invoice("INV-TEST-003", LocalDate.now(), "TestUser",
                BigDecimal.valueOf(1000.00), "USD", BigDecimal.ONE,
                "Test Product", BigDecimal.valueOf(1.0));

        entityManager.persist(invoice);
        transaction.commit();

        Long id = invoice.getId();
        assertNotNull(id);

        transaction.begin();
        Invoice managedInvoice = entityManager.find(Invoice.class, id);
        entityManager.remove(managedInvoice);
        transaction.commit();

        Invoice deleted = entityManager.find(Invoice.class, id);
        assertNull(deleted);
    }
}
