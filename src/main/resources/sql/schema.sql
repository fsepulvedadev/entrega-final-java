
CREATE DATABASE ventas;

CREATE TABLE ventas.clients (
    clientid INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    doc_number VARCHAR(30) NOT NULL
);

CREATE TABLE ventas.products (
    productid INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    description VARCHAR(255) NOT NULL,
    code VARCHAR(50) NOT NULL,
    stock INT NOT NULL,
    price DOUBLE NOT NULL
);

CREATE TABLE ventas.invoices (

    invoiceid INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    clients_id INT,
    quantity int,
    created_at DATETIME,
    total DOUBLE NOT NULL,
    CONSTRAINT fk_clients_id FOREIGN KEY (clients_id) REFERENCES clients(clientid)
);

CREATE TABLE ventas.line (
    lineid INT PRIMARY KEY AUTO_INCREMENT,
    invoice_id INT,
    quantity INT ,
    product_id INT ,
    description VARCHAR(250),
    price FLOAT(5,2),
    CONSTRAINT fk_invoice_id FOREIGN KEY (invoice_id) REFERENCES invoice(invoiceid),
    CONSTRAINT fk_products_id FOREIGN KEY (product_id) REFERENCES products(productid)
);