# DocumentFlow - Document Management System

A JavaFX-based document management application for handling three types of financial documents: Invoices, Payments, and Payment Requests.

## Features

- **Three Document Types**:
  - Invoice (Накладная): Track sales with product details, amounts, currency, and exchange rates
  - Payment (Платёжка): Manage employee payments
  - Payment Request (Заявка на оплату): Handle payment requests with counterparty information

- **Full CRUD Operations**: Create, Read, Update, and Delete documents
- **Persistent Storage**: File-based H2 database saves data between application runs
- **Import/Export**: Save and load individual documents to/from text files
- **Flexible UI**: Responsive interface that adapts to window size changes
- **Batch Operations**: Select multiple documents via checkboxes for batch deletion

## Technology Stack

- **Java 22**
- **JavaFX 22.0.1** - Modern UI framework
- **Hibernate 6.5.2** - ORM for database operations
- **H2 Database 2.3.230** - Embedded SQL database
- **JUnit 5.10.2** - Unit testing framework
- **Maven** - Build and dependency management

## Architecture

The application follows a simplified MVC (Model-View-Controller) pattern:

- **Models** (com.docflow.models): JPA entities with JOINED inheritance
- **Services** (com.docflow.services): Business logic and file I/O
- **Controllers** (com.docflow.ui): JavaFX controllers for UI interaction
- **Database** (com.docflow.database): Database connection management

## OOP Principles Demonstrated

- **Inheritance**: `Document` abstract base class extended by three document types
- **Encapsulation**: Private fields with public getters/setters, service layer abstraction
- **Polymorphism**: Uniform handling of different document types through base class

## Prerequisites

- JDK 22 or higher
- Maven 3.6+

## Building the Project

```bash
mvn clean compile
```

## Running Tests

```bash
mvn test
```

All tests should pass successfully:
- DocumentTest: Tests document creation, validation, and display
- DocumentServiceTest: Tests JPA persistence operations
- FileServiceTest: Tests import/export functionality
- DateUtilsTest: Tests date formatting and parsing

## Running the Application

```bash
mvn javafx:run
```

Or use your IDE to run the `com.docflow.App` main class.

## Usage

### Creating Documents

1. Click one of the three "New" buttons (Invoice, Payment, or Payment Request)
2. Fill in the required fields in the dialog
3. Click "OK" to save the document

### Viewing Documents

1. Select a document from the list
2. Click the "View" button to see all document details

### Saving Documents

1. Select a document from the list
2. Click the "Save" button
3. Choose a location and filename
4. The document will be saved in structured text format

### Loading Documents

1. Click the "Load" button
2. Select a previously saved document file
3. The document will be imported and added to the list

### Deleting Documents

1. Check the boxes next to the documents you want to delete
2. Click the "Delete" button
3. Confirm the deletion

## File Format

Documents are saved in a structured text format with three sections:

```
[DOCUMENT_TYPE]
Invoice

[METADATA]
DocumentNumber=INV-001
DocumentDate=30.11.2025
UserName=Admin

[DATA]
TotalAmount=1500.00
Currency=USD
ExchangeRate=1.0
ProductName=Laptop
Quantity=2.0
```

## Project Structure

```
DocumentFlow/
├── src/main/
│   ├── java/com/docflow/
│   │   ├── models/          # JPA entities
│   │   ├── services/        # Business logic
│   │   ├── database/        # Database management
│   │   ├── ui/              # Controllers and components
│   │   ├── utils/           # Utility classes
│   │   └── App.java         # Main application
│   └── resources/
│       ├── META-INF/
│       │   └── persistence.xml
│       ├── fxml/            # UI layouts
│       └── styles/          # CSS stylesheets
├── src/test/
│   └── java/com/docflow/    # Unit tests
├── data/                    # H2 database files (auto-created)
├── pom.xml
└── README.md
```

## Database Schema

The application uses JPA JOINED inheritance strategy:

- **DOCUMENT** (base table): id, document_number, document_date, user_name
- **INVOICE**: extends DOCUMENT with total_amount, currency, exchange_rate, product_name, quantity
- **PAYMENT**: extends DOCUMENT with payment_amount, employee_name
- **PAYMENT_REQUEST**: extends DOCUMENT with counterparty_name, request_amount, currency, exchange_rate, commission_amount

## License

This project is created as a demonstration of OOP principles and JavaFX development.

## Author

Created as a practical assignment demonstrating:
- Object-Oriented Programming principles
- JavaFX UI development
- Hibernate ORM usage
- Maven project structure
- Unit testing with JUnit 5
