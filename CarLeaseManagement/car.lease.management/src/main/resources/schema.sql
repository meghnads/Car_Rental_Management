-- EndCustomer table
CREATE TABLE EndCustomer (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15) UNIQUE NOT NULL,
    address TEXT
);

-- Car table
CREATE TABLE Car (
    id SERIAL PRIMARY KEY,
    make VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    available BOOLEAN DEFAULT TRUE
);

-- Lease table
CREATE TABLE Lease (
    id SERIAL PRIMARY KEY,
    car_id INT NOT NULL,
    customer_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    FOREIGN KEY (car_id) REFERENCES Car(id),
    FOREIGN KEY (customer_id) REFERENCES EndCustomer(id)
);

-- LeaseHistory table
CREATE TABLE LeaseHistory (
    id SERIAL PRIMARY KEY,
    lease_id INT NOT NULL,
    customer_id INT NOT NULL,
    car_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    FOREIGN KEY (lease_id) REFERENCES Lease(id),
    FOREIGN KEY (customer_id) REFERENCES EndCustomer(id),
    FOREIGN KEY (car_id) REFERENCES Car(id)
);