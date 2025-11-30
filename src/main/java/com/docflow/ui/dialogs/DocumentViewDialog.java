package com.docflow.ui.dialogs;

import com.docflow.models.Document;
import com.docflow.models.Invoice;
import com.docflow.models.Payment;
import com.docflow.models.PaymentRequest;
import com.docflow.utils.DateUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class DocumentViewDialog {

    @FXML
    private TextArea txtDocumentDetails;

    private Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setDocument(Document document) {
        if (document == null) {
            return;
        }

        StringBuilder details = new StringBuilder();
        details.append("=== ").append(document.getDisplayName()).append(" ===\n\n");

        details.append("Document Type: ").append(document.getDocumentType()).append("\n");
        details.append("Document Number: ").append(document.getDocumentNumber()).append("\n");
        details.append("Document Date: ").append(DateUtils.formatDate(document.getDocumentDate())).append("\n");
        details.append("User Name: ").append(document.getUserName()).append("\n");
        details.append("\n");

        if (document instanceof Invoice invoice) {
            details.append("Total Amount: ").append(invoice.getTotalAmount()).append("\n");
            details.append("Currency: ").append(invoice.getCurrency()).append("\n");
            details.append("Exchange Rate: ").append(invoice.getExchangeRate()).append("\n");
            details.append("Product Name: ").append(invoice.getProductName()).append("\n");
            details.append("Quantity: ").append(invoice.getQuantity()).append("\n");
        } else if (document instanceof Payment payment) {
            details.append("Payment Amount: ").append(payment.getPaymentAmount()).append("\n");
            details.append("Employee Name: ").append(payment.getEmployeeName()).append("\n");
        } else if (document instanceof PaymentRequest request) {
            details.append("Counterparty Name: ").append(request.getCounterpartyName()).append("\n");
            details.append("Request Amount: ").append(request.getRequestAmount()).append("\n");
            details.append("Currency: ").append(request.getCurrency()).append("\n");
            details.append("Exchange Rate: ").append(request.getExchangeRate()).append("\n");
            details.append("Commission Amount: ").append(request.getCommissionAmount()).append("\n");
        }

        txtDocumentDetails.setText(details.toString());
    }

    @FXML
    private void handleClose() {
        dialogStage.close();
    }
}
