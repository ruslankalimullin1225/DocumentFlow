package com.docflow.ui.components;

import com.docflow.models.Document;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

import java.util.HashSet;
import java.util.Set;

public class DocumentListCell extends ListCell<Document> {

    private final HBox content;
    private final CheckBox checkBox;
    private final Set<Document> selectedDocuments;

    public DocumentListCell(Set<Document> selectedDocuments) {
        this.selectedDocuments = selectedDocuments;
        this.checkBox = new CheckBox();
        this.content = new HBox(10);
        this.content.getChildren().add(checkBox);

        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            Document document = getItem();
            if (document != null) {
                if (newValue) {
                    selectedDocuments.add(document);
                } else {
                    selectedDocuments.remove(document);
                }
            }
        });
    }

    @Override
    protected void updateItem(Document document, boolean empty) {
        super.updateItem(document, empty);

        if (empty || document == null) {
            setGraphic(null);
            checkBox.setSelected(false);
        } else {
            checkBox.setText(document.getDisplayName());
            checkBox.setSelected(selectedDocuments.contains(document));
            setGraphic(content);
        }
    }
}
