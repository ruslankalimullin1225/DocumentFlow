package com.docflow.ui.dialogs;

import com.docflow.models.Document;
import com.docflow.models.Payment;
import com.docflow.utils.ValidationUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentDialog {

    @FXML
    private TextField txtDocNumber;

    @FXML
    private DatePicker dpDocDate;

    @FXML
    private TextField txtUserName;

    @FXML
    private TextField txtPaymentAmount;

    @FXML
    private TextField txtEmployeeName;

    private Stage dialogStage;
    private Payment payment;
    private boolean okClicked = false;

    @FXML
    public void initialize() {
        dpDocDate.setValue(LocalDate.now());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public Document getDocument() {
        return payment;
    }

    @FXML
    private void handleOk() {
        if (validateInput()) {
            String docNumber = txtDocNumber.getText().trim();
            LocalDate docDate = dpDocDate.getValue();
            String userName = txtUserName.getText().trim();
            BigDecimal paymentAmount = new BigDecimal(txtPaymentAmount.getText().trim());
            String employeeName = txtEmployeeName.getText().trim();

            payment = new Payment(docNumber, docDate, userName, paymentAmount, employeeName);

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

        if (!ValidationUtils.isPositiveNumber(txtPaymentAmount.getText())) {
            errors.append("- Payment Amount must be a valid positive number\n");
        }

        if (ValidationUtils.isNullOrEmpty(txtEmployeeName.getText())) {
            errors.append("- Employee Name is required\n");
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
