CREATE TABLE User (
    userID INTEGER PRIMARY KEY,
    firstName VARCHAR(20),
    lastName VARCHAR(20),
    email VARCHAR(50) UNIQUE,
    password VARCHAR(20),
    phoneNumber VARCHAR(15),
    streetAddress VARCHAR(20),
    country VARCHAR(20),
    state VARCHAR(10),
    postcode VARCHAR(4),
    suburb VARCHAR(10),
    createdDate DATE DEFAULT CURRENT_TIMESTAMP,
    lastUpdated DATE DEFAULT CURRENT_TIMESTAMP,
    role VARCHAR(20)
);

CREATE TABLE Product (
    productID INTEGER PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    imageUrl VARCHAR(255),
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    quantity INTEGER DEFAULT 0,
    favourited BOOLEAN DEFAULT 0
);

CREATE TABLE Cart (
    cartID INTEGER PRIMARY KEY,
    customerID INTEGER NOT NULL,
    isActive BOOLEAN DEFAULT 1,
    createdDate DATE DEFAULT CURRENT_TIMESTAMP,
    updatedDate DATE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customerID) REFERENCES User(userID)
);

CREATE TABLE CartItem (
    cartItemID INTEGER PRIMARY KEY,
    cartID INTEGER NOT NULL,
    productID INTEGER NOT NULL,
    quantity INTEGER DEFAULT 1,
    createdDate DATE DEFAULT CURRENT_TIMESTAMP,
    updatedDate DATE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cartID) REFERENCES Cart(cartID),
    FOREIGN KEY (productID) REFERENCES Product(productID)
);

CREATE TABLE "Order" (
    orderID INTEGER PRIMARY KEY,
    customerID INTEGER NOT NULL,
    orderStatus VARCHAR(20) DEFAULT 'Pending',
    orderDate DATE DEFAULT CURRENT_TIMESTAMP,
    createdDate DATE DEFAULT CURRENT_TIMESTAMP,
    updatedDate DATE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customerID) REFERENCES User(userID)
);

CREATE TABLE OrderLine (
    orderID INTEGER NOT NULL,
    productID INTEGER NOT NULL,
    quantity INTEGER DEFAULT 1,
    requests TEXT,
    createdDate DATE DEFAULT CURRENT_TIMESTAMP,
    updatedDate DATE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (orderID, productID),
    FOREIGN KEY (orderID) REFERENCES "Order"(orderID),
    FOREIGN KEY (productID) REFERENCES Product(productID)
);

CREATE TABLE Payment (
    paymentID INTEGER PRIMARY KEY,
    orderID INTEGER NOT NULL,
    customerID INTEGER NOT NULL,
    paymentDate DATE DEFAULT CURRENT_TIMESTAMP,
    paymentMethod VARCHAR(20),
    paymentAmount VARCHAR(20),
    billingStreetAddress VARCHAR(100),
    billingPostcode VARCHAR(10),
    billingCity VARCHAR(50),
    billingState VARCHAR(20),
    billingPhoneNumber VARCHAR(15),
    createdDate DATE DEFAULT CURRENT_TIMESTAMP,
    updatedDate DATE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (orderID) REFERENCES "Order"(orderID),
    FOREIGN KEY (customerID) REFERENCES User(userID)
);

CREATE TABLE Invoice (
    invoiceID INTEGER PRIMARY KEY,
    paymentID INTEGER NOT NULL,
    orderID INTEGER NOT NULL,
    customerID INTEGER NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    title VARCHAR(100),
    description TEXT,
    createdDate DATE DEFAULT CURRENT_TIMESTAMP,
    updatedDate DATE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (paymentID) REFERENCES Payment(paymentID),
    FOREIGN KEY (orderID) REFERENCES "Order"(orderID),
    FOREIGN KEY (customerID) REFERENCES User(userID)
);

-- Drop the existing table and recreate with proper foreign keys
DROP TABLE IF EXISTS Shipment;

CREATE TABLE Shipment (
    shipmentID INTEGER PRIMARY KEY AUTOINCREMENT,
    orderID INTEGER NOT NULL,
    customerID INTEGER NOT NULL,
    shipmentMethod VARCHAR(50),
    shipmentDate DATE,
    streetAddress VARCHAR(100),
    city VARCHAR(50),
    state VARCHAR(20),
    postcode VARCHAR(10),
    status VARCHAR(20) DEFAULT 'Pending',
    trackingNumber VARCHAR(50),
    finalized BOOLEAN DEFAULT 0,
    deliveryAllocation VARCHAR(100),
    deliveryDate DATE,
    createdDate DATE DEFAULT CURRENT_TIMESTAMP,
    updatedDate DATE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (orderID) REFERENCES "Order"(orderID),
    FOREIGN KEY (customerID) REFERENCES User(userID)
);