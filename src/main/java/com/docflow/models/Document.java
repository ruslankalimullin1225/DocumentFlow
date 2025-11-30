package com.docflow.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "DOCUMENT")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
public abstract class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "document_number", nullable = false, unique = true, length = 50)
    private String documentNumber;

    @Column(name = "document_date", nullable = false)
    private LocalDate documentDate;

    @Column(name = "user_name", nullable = false, length = 100)
    private String userName;

    public Document() {
        this.documentDate = LocalDate.now();
    }

    public Document(String documentNumber, LocalDate documentDate, String userName) {
        this.documentNumber = documentNumber;
        this.documentDate = documentDate != null ? documentDate : LocalDate.now();
        this.userName = userName;
    }

    public abstract String getDisplayName();

    public abstract String getDocumentType();

    public boolean validate() {
        return documentNumber != null && !documentNumber.trim().isEmpty()
                && documentDate != null
                && userName != null && !userName.trim().isEmpty();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public LocalDate getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(LocalDate documentDate) {
        this.documentDate = documentDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return Objects.equals(id, document.id) &&
                Objects.equals(documentNumber, document.documentNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, documentNumber);
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
