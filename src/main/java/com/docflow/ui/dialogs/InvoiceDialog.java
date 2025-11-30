package com.docflow.ui.dialogs;

import com.docflow.models.Document;
import com.docflow.models.Invoice;
import com.docflow.utils.ValidationUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InvoiceDialog {

    @FXML
    private TextField txtDocNumber;

    @FXML
    private DatePicker dpDocDate;

    @FXML
    private TextField txtUserName;

    @FXML
    private TextField txtTotalAmount;

    @FXML
    private TextField txtCurrency;

    @FXML
    private TextField txtExchangeRate;

    @FXML
    private TextField txtProductName;

    @FXML
    private TextField txtQuantity;

    private Stage dialogStage;
    private Invoice invoice;
    private boolean okClicked = false;

    @FXML
    public void initialize() {
        dpDocDate.setValue(LocalDate.now());
        txtExchangeRate.setText("1.0");
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public Document getDocument() {
        return invoice;
    }

    @FXML
    private void handleOk() {
        if (validateInput()) {
            String docNumber = txtDocNumber.getText().trim();
            LocalDate docDate = dpDocDate.getValue();
            String userName = txtUserName.getText().trim();
            BigDecimal totalAmount = new BigDecimal(txtTotalAmount.getText().trim());
            String currency = txtCurrency.getText().trim();
            BigDecimal exchangeRate = new BigDecimal(txtExchangeRate.getText().trim());
            String productName = txtProductName.getText().trim();
            BigDecimal quantity = new BigDecimal(txtQuantity.getText().trim());

            invoice = new Invoice(docNumber, docDate, userName, totalAmount, currency,
                    exchangeRate, productName, quantity);

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();

        if (ValidationUtils.isNullOrEmpty(txtDocNumber.getText())) {
            errors.append("- Document Number is required\n");
        }

        if (dpDocDate.getValue() == null) {
            errors.append("- Document Date is required\n");
        }

        if (ValidationUtils.isNullOrEmpty(txtUserName.getText())) {
            errors.append("- User Name is required\n");
        }

        if (!ValidationUtils.isNonNegativeNumber(txtTotalAmount.getText())) {
            errors.append("- Total Amount must be a valid non-negative number\n");
        }

        if (ValidationUtils.isNullOrEmpty(txtCurrency.getText())) {
            errors.append("- Currency is required\n");
        }

        if (!ValidationUtils.isPositiveNumber(txtExchangeRate.getText())) {
            errors.append("- Exchange Rate must be a valid positive number\n");
        }

        if (ValidationUtils.isNullOrEmpty(txtProductName.getText())) {
            errors.append("- Product Name is required\n");
        }

        if (!ValidationUtils.isPositiveNumber(txtQuantity.getText())) {
            errors.append("- Quantity must be a valid positive number\n");
        }

        if (errors.length() > 0) {
            showValidationError("Please fix the following errors:\n\n" + errors.toString());
            return false;
        }

        return true;
    }

    private void showValidationError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(dialogStage);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Invalid Input");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
