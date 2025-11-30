package com.docflow.services;

import com.docflow.database.DatabaseManager;
import com.docflow.models.Document;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DocumentService {

    private final DatabaseManager databaseManager;

    public DocumentService() {
        this.databaseManager = DatabaseManager.getInstance();
    }

    public DocumentService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public List<Document> getAllDocuments() {
        EntityManager em = databaseManager.getEntityManager();
        try {
            TypedQuery<Document> query = em.createQuery("SELECT d FROM Document d", Document.class);
            List<Document> documents = query.getResultList();
            documents.sort(Comparator.comparing(Document::getDocumentDate).reversed());
            return documents;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    public void saveDocument(Document document) {
        if (document == null) {
            throw new IllegalArgumentException("Document cannot be null");
        }

        if (!document.validate()) {
            throw new IllegalArgumentException("Document validation failed");
        }

        EntityManager em = databaseManager.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            if (document.getId() == null) {
                em.persist(document);
            } else {
                em.merge(document);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error saving document: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public void deleteDocuments(List<Document> documents) {
        if (documents == null || documents.isEmpty()) {
            return;
        }

        EntityManager em = databaseManager.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            for (Document document : documents) {
                Document managedDoc = em.find(Document.class, document.getId());
                if (managedDoc != null) {
                    em.remove(managedDoc);
                }
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error deleting documents: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public Document getDocumentById(Long id) {
        if (id == null) {
            return null;
        }

        EntityManager em = databaseManager.getEntityManager();
        try {
            return em.find(Document.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public boolean documentNumberExists(String documentNumber) {
        if (documentNumber == null || documentNumber.trim().isEmpty()) {
            return false;
        }

        EntityManager em = databaseManager.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(d) FROM Document d WHERE d.documentNumber = :number", Long.class);
            query.setParameter("number", documentNumber);
            Long count = query.getSingleResult();
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }
}
