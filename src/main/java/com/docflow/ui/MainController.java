package com.docflow.ui;

import com.docflow.models.Document;
import com.docflow.services.DocumentService;
import com.docflow.services.FileService;
import com.docflow.ui.components.DocumentListCell;
import com.docflow.ui.dialogs.DocumentViewDialog;
import com.docflow.ui.dialogs.InvoiceDialog;
import com.docflow.ui.dialogs.PaymentDialog;
import com.docflow.ui.dialogs.PaymentRequestDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainController {

    @FXML
    private ListView<Document> documentListView;

    private final DocumentService documentService;
    private final FileService fileService;
    private final ObservableList<Document> documents;
    private final Set<Document> selectedDocuments;

    public MainController() {
        this.documentService = new DocumentService();
        this.fileService = new FileService();
        this.documents = FXCollections.observableArrayList();
        this.selectedDocuments = new HashSet<>();
    }

    @FXML
    public void initialize() {
        documentListView.setItems(documents);
        documentListView.setCellFactory(lv -> new DocumentListCell(selectedDocuments));
        loadDocuments();
    }

    private void loadDocuments() {
        documents.clear();
        selectedDocuments.clear();
        List<Document> allDocuments = documentService.getAllDocuments();
        documents.addAll(allDocuments);
    }

    @FXML
    private void handleNewInvoice() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/invoice-dialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("New Invoice");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(documentListView.getScene().getWindow());
            dialogStage.setScene(new Scene(loader.load()));

            InvoiceDialog controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                Document invoice = controller.getDocument();
                if (invoice != null) {
                    documentService.saveDocument(invoice);
                    loadDocuments();
                    showInfo("Invoice created successfully.");
                }
            }
        } catch (IOException e) {
            showError("Error opening invoice dialog: " + e.getMessage());
        }
    }

    @FXML
    private void handleNewPayment() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/payment-dialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("New Payment");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(documentListView.getScene().getWindow());
            dialogStage.setScene(new Scene(loader.load()));

            PaymentDialog controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                Document payment = controller.getDocument();
                if (payment != null) {
                    documentService.saveDocument(payment);
                    loadDocuments();
                    showInfo("Payment created successfully.");
                }
            }
        } catch (IOException e) {
            showError("Error opening payment dialog: " + e.getMessage());
        }
    }

    @FXML
    private void handleNewPaymentRequest() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/payment-request-dialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("New Payment Request");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(documentListView.getScene().getWindow());
            dialogStage.setScene(new Scene(loader.load()));

            PaymentRequestDialog controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                Document paymentRequest = controller.getDocument();
                if (paymentRequest != null) {
                    documentService.saveDocument(paymentRequest);
                    loadDocuments();
                    showInfo("Payment Request created successfully.");
                }
            }
        } catch (IOException e) {
            showError("Error opening payment request dialog: " + e.getMessage());
        }
    }

    @FXML
    private void handleSave() {
        Document selectedDoc = documentListView.getSelectionModel().getSelectedItem();
        if (selectedDoc == null) {
            showWarning("Please select a document to save.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Document");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        fileChooser.setInitialFileName(selectedDoc.getDocumentNumber() + ".txt");

        File file = fileChooser.showSaveDialog(documentListView.getScene().getWindow());
        if (file != null) {
            try {
                fileService.exportDocument(selectedDoc, file);
                showInfo("Document saved successfully to " + file.getName());
            } catch (IOException e) {
                showError("Error saving document: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleLoad() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Document");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        File file = fileChooser.showOpenDialog(documentListView.getScene().getWindow());
        if (file != null) {
            try {
                Document document = fileService.importDocument(file);
                documentService.saveDocument(document);
                loadDocuments();
                showInfo("Document loaded successfully from " + file.getName());
            } catch (Exception e) {
                showError("Error loading document: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleView() {
        Document selectedDoc = documentListView.getSelectionModel().getSelectedItem();
        if (selectedDoc == null) {
            showWarning("Please select a document to view.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/view-dialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("View Document");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(documentListView.getScene().getWindow());
            dialogStage.setScene(new Scene(loader.load()));

            DocumentViewDialog controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setDocument(selectedDoc);

            dialogStage.showAndWait();
        } catch (IOException e) {
            showError("Error opening view dialog: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedDocuments.isEmpty()) {
            showWarning("Please select at least one document to delete.");
            return;
        }

        try {
            documentService.deleteDocuments(List.copyOf(selectedDocuments));
            loadDocuments();
            showInfo("Selected documents deleted successfully.");
        } catch (Exception e) {
            showError("Error deleting documents: " + e.getMessage());
        }
    }

    @FXML
    private void handleExit() {
        Stage stage = (Stage) documentListView.getScene().getWindow();
        stage.close();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
