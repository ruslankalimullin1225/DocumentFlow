package com.docflow.services;

import com.docflow.models.Document;
import com.docflow.models.Invoice;
import com.docflow.models.Payment;
import com.docflow.models.PaymentRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceTest {

    private FileService fileService;
    private File testFile;

    @BeforeEach
    void setUp() {
        fileService = new FileService();
        testFile = new File("test_document.txt");
    }

    @AfterEach
    void tearDown() {
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    void testExportAndImportInvoice() throws IOException {
        Invoice originalInvoice = new Invoice("INV-001", LocalDate.of(2025, 11, 30), "Admin",
                BigDecimal.valueOf(1500.00), "USD", BigDecimal.valueOf(1.0),
                "Laptop", BigDecimal.valueOf(2.0));

        fileService.exportDocument(originalInvoice, testFile);
        assertTrue(testFile.exists());

        Document importedDoc = fileService.importDocument(testFile);
        assertNotNull(importedDoc);
        assertInstanceOf(Invoice.class, importedDoc);

        Invoice importedInvoice = (Invoice) importedDoc;
        assertEquals(originalInvoice.getDocumentNumber(), importedInvoice.getDocumentNumber());
        assertEquals(originalInvoice.getUserName(), importedInvoice.getUserName());
        assertEquals(originalInvoice.getTotalAmount(), importedInvoice.getTotalAmount());
        assertEquals(originalInvoice.getCurrency(), importedInvoice.getCurrency());
        assertEquals(originalInvoice.getProductName(), importedInvoice.getProductName());
    }

    @Test
    void testExportAndImportPayment() throws IOException {
        Payment originalPayment = new Payment("PAY-001", LocalDate.of(2025, 11, 30), "Admin",
                BigDecimal.valueOf(5000.00), "John Doe");

        fileService.exportDocument(originalPayment, testFile);
        assertTrue(testFile.exists());

        Document importedDoc = fileService.importDocument(testFile);
        assertNotNull(importedDoc);
        assertInstanceOf(Payment.class, importedDoc);

        Payment importedPayment = (Payment) importedDoc;
        assertEquals(originalPayment.getDocumentNumber(), importedPayment.getDocumentNumber());
        assertEquals(originalPayment.getUserName(), importedPayment.getUserName());
        assertEquals(originalPayment.getPaymentAmount(), importedPayment.getPaymentAmount());
        assertEquals(originalPayment.getEmployeeName(), importedPayment.getEmployeeName());
    }

    @Test
    void testExportAndImportPaymentRequest() throws IOException {
        PaymentRequest originalRequest = new PaymentRequest("REQ-001", LocalDate.of(2025, 11, 30), "Admin",
                "ABC Company", BigDecimal.valueOf(10000.00), "EUR",
                BigDecimal.valueOf(1.1), BigDecimal.valueOf(50.00));

        fileService.exportDocument(originalRequest, testFile);
        assertTrue(testFile.exists());

        Document importedDoc = fileService.importDocument(testFile);
        assertNotNull(importedDoc);
        assertInstanceOf(PaymentRequest.class, importedDoc);

        PaymentRequest importedRequest = (PaymentRequest) importedDoc;
        assertEquals(originalRequest.getDocumentNumber(), importedRequest.getDocumentNumber());
        assertEquals(originalRequest.getUserName(), importedRequest.getUserName());
        assertEquals(originalRequest.getCounterpartyName(), importedRequest.getCounterpartyName());
        assertEquals(originalRequest.getRequestAmount(), importedRequest.getRequestAmount());
        assertEquals(originalRequest.getCurrency(), importedRequest.getCurrency());
    }

    @Test
    void testExportNullDocument() {
        assertThrows(IllegalArgumentException.class, () -> {
            fileService.exportDocument(null, testFile);
        });
    }

    @Test
    void testImportNonExistentFile() {
        File nonExistentFile = new File("non_existent_file.txt");
        assertThrows(IOException.class, () -> {
            fileService.importDocument(nonExistentFile);
        });
    }
}
