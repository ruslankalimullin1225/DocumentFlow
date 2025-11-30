package com.docflow.services;

import com.docflow.models.Document;
import com.docflow.models.Invoice;
import com.docflow.models.Payment;
import com.docflow.models.PaymentRequest;
import com.docflow.utils.DateUtils;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class FileService {

    private static final String SECTION_DOCUMENT_TYPE = "[DOCUMENT_TYPE]";
    private static final String SECTION_METADATA = "[METADATA]";
    private static final String SECTION_DATA = "[DATA]";

    public void exportDocument(Document document, File file) throws IOException {
        if (document == null) {
            throw new IllegalArgumentException("Document cannot be null");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(SECTION_DOCUMENT_TYPE);
            writer.newLine();
            writer.write(document.getDocumentType());
            writer.newLine();
            writer.newLine();

            writer.write(SECTION_METADATA);
            writer.newLine();
            writer.write("DocumentNumber=" + document.getDocumentNumber());
            writer.newLine();
            writer.write("DocumentDate=" + DateUtils.formatDate(document.getDocumentDate()));
            writer.newLine();
            writer.write("UserName=" + document.getUserName());
            writer.newLine();
            writer.newLine();

            writer.write(SECTION_DATA);
            writer.newLine();
            writeDocumentData(document, writer);
        }
    }

    private void writeDocumentData(Document document, BufferedWriter writer) throws IOException {
        if (document instanceof Invoice invoice) {
            writer.write("TotalAmount=" + invoice.getTotalAmount());
            writer.newLine();
            writer.write("Currency=" + invoice.getCurrency());
            writer.newLine();
            writer.write("ExchangeRate=" + invoice.getExchangeRate());
            writer.newLine();
            writer.write("ProductName=" + invoice.getProductName());
            writer.newLine();
            writer.write("Quantity=" + invoice.getQuantity());
            writer.newLine();
        } else if (document instanceof Payment payment) {
            writer.write("PaymentAmount=" + payment.getPaymentAmount());
            writer.newLine();
            writer.write("EmployeeName=" + payment.getEmployeeName());
            writer.newLine();
        } else if (document instanceof PaymentRequest request) {
            writer.write("CounterpartyName=" + request.getCounterpartyName());
            writer.newLine();
            writer.write("RequestAmount=" + request.getRequestAmount());
            writer.newLine();
            writer.write("Currency=" + request.getCurrency());
            writer.newLine();
            writer.write("ExchangeRate=" + request.getExchangeRate());
            writer.newLine();
            writer.write("CommissionAmount=" + request.getCommissionAmount());
            writer.newLine();
        }
    }

    public Document importDocument(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String documentType = null;
            Map<String, String> metadata = new HashMap<>();
            Map<String, String> data = new HashMap<>();

            String currentSection = null;
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                if (line.equals(SECTION_DOCUMENT_TYPE)) {
                    currentSection = SECTION_DOCUMENT_TYPE;
                } else if (line.equals(SECTION_METADATA)) {
                    currentSection = SECTION_METADATA;
                } else if (line.equals(SECTION_DATA)) {
                    currentSection = SECTION_DATA;
                } else {
                    if (SECTION_DOCUMENT_TYPE.equals(currentSection)) {
                        documentType = line;
                    } else if (SECTION_METADATA.equals(currentSection)) {
                        parseLine(line, metadata);
                    } else if (SECTION_DATA.equals(currentSection)) {
                        parseLine(line, data);
                    }
                }
            }

            return createDocumentFromData(documentType, metadata, data);
        }
    }

    private void parseLine(String line, Map<String, String> map) {
        int separatorIndex = line.indexOf('=');
        if (separatorIndex > 0) {
            String key = line.substring(0, separatorIndex).trim();
            String value = line.substring(separatorIndex + 1).trim();
            map.put(key, value);
        }
    }

    private Document createDocumentFromData(String documentType, Map<String, String> metadata, Map<String, String> data) {
        String docNumber = metadata.get("DocumentNumber");
        LocalDate docDate = DateUtils.parseDate(metadata.get("DocumentDate"));
        String userName = metadata.get("UserName");

        return switch (documentType) {
            case "Invoice" -> createInvoice(docNumber, docDate, userName, data);
            case "Payment" -> createPayment(docNumber, docDate, userName, data);
            case "PaymentRequest" -> createPaymentRequest(docNumber, docDate, userName, data);
            default -> throw new IllegalArgumentException("Unknown document type: " + documentType);
        };
    }

    private Invoice createInvoice(String docNumber, LocalDate docDate, String userName, Map<String, String> data) {
        BigDecimal totalAmount = new BigDecimal(data.get("TotalAmount"));
        String currency = data.get("Currency");
        BigDecimal exchangeRate = new BigDecimal(data.get("ExchangeRate"));
        String productName = data.get("ProductName");
        BigDecimal quantity = new BigDecimal(data.get("Quantity"));

        return new Invoice(docNumber, docDate, userName, totalAmount, currency, exchangeRate, productName, quantity);
    }

    private Payment createPayment(String docNumber, LocalDate docDate, String userName, Map<String, String> data) {
        BigDecimal paymentAmount = new BigDecimal(data.get("PaymentAmount"));
        String employeeName = data.get("EmployeeName");

        return new Payment(docNumber, docDate, userName, paymentAmount, employeeName);
    }

    private PaymentRequest createPaymentRequest(String docNumber, LocalDate docDate, String userName, Map<String, String> data) {
        String counterpartyName = data.get("CounterpartyName");
        BigDecimal requestAmount = new BigDecimal(data.get("RequestAmount"));
        String currency = data.get("Currency");
        BigDecimal exchangeRate = new BigDecimal(data.get("ExchangeRate"));
        BigDecimal commissionAmount = new BigDecimal(data.get("CommissionAmount"));

        return new PaymentRequest(docNumber, docDate, userName, counterpartyName, requestAmount, currency, exchangeRate, commissionAmount);
    }
}
