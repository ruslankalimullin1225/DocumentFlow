package com.docflow.ui.dialogs;

import com.docflow.models.Document;
import com.docflow.models.PaymentRequest;
import com.docflow.utils.ValidationUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentRequestDialog {

    @FXML
    private TextField txtDocNumber;

    @FXML
    private DatePicker dpDocDate;

    @FXML
    private TextField txtUserName;

    @FXML
    private TextField txtCounterpartyName;

    @FXML
    private TextField txtRequestAmount;

    @FXML
    private TextField txtCurrency;

    @FXML
    private TextField txtExchangeRate;

    @FXML
    private TextField txtCommissionAmount;

    private Stage dialogStage;
    private PaymentRequest paymentRequest;
    private boolean okClicked = false;

    @FXML
    public void initialize() {
        dpDocDate.setValue(LocalDate.now());
        txtExchangeRate.setText("1.0");
        txtCommissionAmount.setText("0.0");
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public Document getDocument() {
        return paymentRequest;
    }

    @FXML
    private void handleOk() {
        if (validateInput()) {
            String docNumber = txtDocNumber.getText().trim();
            LocalDate docDate = dpDocDate.getValue();
            String userName = txtUserName.getText().trim();
            String counterpartyName = txtCounterpartyName.getText().trim();
            BigDecimal requestAmount = new BigDecimal(txtRequestAmount.getText().trim());
            String currency = txtCurrency.getText().trim();
            BigDecimal exchangeRate = new BigDecimal(txtExchangeRate.getText().trim());
            BigDecimal commissionAmount = new BigDecimal(txtCommissionAmount.getText().trim());

            paymentRequest = new PaymentRequest(docNumber, docDate, userName, counterpartyName,
                    requestAmount, currency, exchangeRate, commissionAmount);

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

        if (ValidationUtils.isNullOrEmpty(txtCounterpartyName.getText())) {
            errors.append("- Counterparty Name is required\n");
        }

        if (!ValidationUtils.isPositiveNumber(txtRequestAmount.getText())) {
            errors.append("- Request Amount must be a valid positive number\n");
        }

        if (ValidationUtils.isNullOrEmpty(txtCurrency.getText())) {
            errors.append("- Currency is required\n");
        }

        if (!ValidationUtils.isPositiveNumber(txtExchangeRate.getText())) {
            errors.append("- Exchange Rate must be a valid positive number\n");
        }

        if (!ValidationUtils.isNonNegativeNumber(txtCommissionAmount.getText())) {
            errors.append("- Commission Amount must be a valid non-negative number\n");
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
