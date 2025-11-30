module com.docflow {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.sql;
    requires com.h2database;

    opens com.docflow to javafx.fxml;
    opens com.docflow.ui to javafx.fxml;
    opens com.docflow.ui.dialogs to javafx.fxml;
    opens com.docflow.models to org.hibernate.orm.core;

    exports com.docflow;
    exports com.docflow.ui;
    exports com.docflow.ui.dialogs;
    exports com.docflow.models;
    exports com.docflow.services;
    exports com.docflow.database;
    exports com.docflow.utils;
}
