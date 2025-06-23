BEGIN TRANSACTION;

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS Order_Number CASCADE;
DROP TABLE IF EXISTS Customer CASCADE;
DROP TABLE IF EXISTS Trailer CASCADE;
DROP TABLE IF EXISTS Shipper CASCADE;
DROP TABLE IF EXISTS Unloader CASCADE;
DROP TABLE IF EXISTS Dock_Door CASCADE;

-- *************************************************************************************************
-- Create the tables and constraints
-- *************************************************************************************************

-- users (name is pluralized because 'user' is a SQL keyword)
CREATE TABLE users (
    user_id SERIAL,
    username varchar(50) NOT NULL UNIQUE,
    password_hash varchar(200) NOT NULL,
    role varchar(50) NOT NULL,
    CONSTRAINT PK_user PRIMARY KEY (user_id)
);

CREATE TABLE Shipper (
    shipper_id SERIAL,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(2) NOT NULL,
    zip_code VARCHAR(10) NOT NULL,
    CONSTRAINT PK_Shipper PRIMARY KEY (shipper_id)
);

CREATE TABLE Dock_Door (
    door_id SERIAL,
    door_number VARCHAR(10) NOT NULL,
    door_location VARCHAR(15) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Unassigned',
    CONSTRAINT PK_Dock_Door PRIMARY KEY (door_id),
    CONSTRAINT UQ_Door_number UNIQUE (door_number)
);

CREATE TABLE Trailer (
    trailer_id SERIAL,
    trailer_number VARCHAR(20) NOT NULL,
    trailer_type VARCHAR(50) NOT NULL,
    shipper_id INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Unassigned',
    door_id INTEGER NULL,
    CONSTRAINT PK_Trailer PRIMARY KEY (trailer_id),
    CONSTRAINT FK_Trailer_Shipper FOREIGN KEY (shipper_id) REFERENCES Shipper(shipper_id) ON DELETE CASCADE,
    CONSTRAINT FK_Trailer_Dock_Door FOREIGN KEY (door_id) REFERENCES Dock_Door(door_id),
    CONSTRAINT UQ_Trailer_number UNIQUE (trailer_number)
);

CREATE TABLE Customer (
    customer_id SERIAL,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(2) NOT NULL,
    zip_code VARCHAR(10) NOT NULL,
    CONSTRAINT PK_Customer PRIMARY KEY (customer_id)
);

CREATE TABLE Unloader (
    employee_id SERIAL,
    name VARCHAR(100) NOT NULL,
    shift VARCHAR(100) NOT NULL,
    employee_number VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Unassigned',
    door_id INTEGER NULL,
    CONSTRAINT PK_Unloader PRIMARY KEY (employee_id),
    CONSTRAINT FK_Unloader_Dock_Door FOREIGN KEY (door_id) REFERENCES Dock_Door(door_id),
    CONSTRAINT UQ_Employee_number UNIQUE (employee_number)
);

CREATE TABLE Order_Number (
    order_id SERIAL,
    order_number VARCHAR(20) NOT NULL UNIQUE,
    customer_id INTEGER NOT NULL,
    shipper_id INTEGER NOT NULL,
    trailer_id INTEGER NOT NULL,
    door_id INTEGER NULL,
    handling_unit INTEGER NOT NULL,
    weight INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT PK_Order_Number PRIMARY KEY (order_id),
    CONSTRAINT FK_Order_Number_Customer FOREIGN KEY (customer_id) REFERENCES Customer(customer_id) ON DELETE CASCADE,
    CONSTRAINT FK_Order_Number_Shipper FOREIGN KEY (shipper_id) REFERENCES Shipper(shipper_id) ON DELETE CASCADE,
    CONSTRAINT FK_Order_Number_Trailer FOREIGN KEY (trailer_id) REFERENCES Trailer(trailer_id) ON DELETE CASCADE,
    CONSTRAINT FK_Order_Number_Dock_Door FOREIGN KEY (door_id) REFERENCES Dock_Door(door_id)
);

-- *************************************************************************************************
-- Insert sample data
-- *************************************************************************************************

-- Insert Shippers
INSERT INTO Shipper (name, address, city, state, zip_code) VALUES
('Swift Logistics', '500 Shipping Lane', 'Chicago', 'IL', '60601'),
('Global Freight Solutions', '1200 Cargo Drive', 'Dallas', 'TX', '75201'),
('Elite Shipping Co.', '800 Transport Blvd', 'Los Angeles', 'CA', '90012');

-- Insert Dock Doors (now with both door_id and door_number)
INSERT INTO Dock_Door (door_number, door_location, status) VALUES
-- Building 1 (North Side)
('N1', 'North', 'Assigned'), ('N2', 'North', 'Assigned'),
('N3', 'North', 'Assigned'), ('N4', 'North', 'Unassigned'),
('N5', 'North', 'Unassigned'), ('N6', 'North', 'Unassigned'),
('N7', 'North', 'Unassigned'), ('N8', 'North', 'Unassigned'),
('N9', 'North', 'Unassigned'), ('N10', 'North', 'Unassigned'),

-- Building 2 (East Side)
('E1', 'East', 'Assigned'), ('E2', 'East', 'Assigned'),
('E3', 'East', 'Assigned'), ('E4', 'East', 'Unassigned'),
('E5', 'East', 'Unassigned'), ('E6', 'East', 'Unassigned'),
('E7', 'East', 'Unassigned'), ('E8', 'East', 'Unassigned'),
('E9', 'East', 'Unassigned'), ('E10', 'East', 'Unassigned'),

-- Building 3 (South Side)
('S1', 'South', 'Assigned'), ('S2', 'South', 'Assigned'),
('S3', 'South', 'Assigned'), ('S4', 'South', 'Unassigned'),
('S5', 'South', 'Unassigned'), ('S6', 'South', 'Unassigned'),
('S7', 'South', 'Unassigned'), ('S8', 'South', 'Unassigned'),
('S9', 'South', 'Unassigned'), ('S10', 'South', 'Unassigned'),

-- Building 4 (West Side)
('W1', 'West', 'Assigned'), ('W2', 'West', 'Assigned'),
('W3', 'West', 'Assigned'), ('W4', 'West', 'Unassigned'),
('W5', 'West', 'Unassigned'), ('W6', 'West', 'Unassigned'),
('W7', 'West', 'Unassigned'), ('W8', 'West', 'Unassigned'),
('W9', 'West', 'Unassigned'), ('W10', 'West', 'Unassigned'),

-- Building 5 (Central)
('C1', 'Central', 'Assigned'), ('C2', 'Central', 'Assigned'),
('C3', 'Central', 'Assigned'), ('C4', 'Central', 'Unassigned'),
('C5', 'Central', 'Unassigned'), ('C6', 'Central', 'Unassigned'),
('C7', 'Central', 'Unassigned'), ('C8', 'Central', 'Unassigned'),
('C9', 'Central', 'Unassigned'), ('C10', 'Central', 'Unassigned');

-- Insert Trailers with assigned doors (using door_id)
INSERT INTO Trailer (trailer_number, trailer_type, shipper_id, status, door_id) VALUES
('OF1234', 'Local/City', 1, 'Assigned', 1),  -- Assigned to N1 (door_id 1)
('LE4567', 'Lift-Gate', 2, 'Assigned', 2),  -- Assigned to N2 (door_id 2)
('B2389', 'Box Truck', 3, 'Assigned', 3),   -- Assigned to N3 (door_id 3)
('TR5678', 'Flatbed', 1, 'Unassigned', NULL),
('CV7890', 'Conestoga', 2, 'Unassigned', NULL);

-- Insert Customers
INSERT INTO Customer (name, address, city, state, zip_code) VALUES
('John Doe', '123 Maple Street', 'Boston', 'MA', '02108'),
('Jane Smith', '456 Oak Avenue', 'Chicago', 'IL', '60601'),
('Alice Johnson', '789 Pine Road', 'Houston', 'TX', '77002'),
('Bob Brown', '101 Elm Boulevard', 'Phoenix', 'AZ', '85004'),
('Carol White', '202 Cedar Lane', 'Philadelphia', 'PA', '19107'),
('Dave Wilson', '303 Birch Drive', 'San Antonio', 'TX', '78205'),
('Eve Davis', '404 Walnut Way', 'San Diego', 'CA', '92101'),
('Frank Miller', '505 Ash Terrace', 'Dallas', 'TX', '75201'),
('Grace Lee', '606 Redwood Place', 'San Jose', 'CA', '95113'),
('Henry Clark', '707 Cypress Court', 'Austin', 'TX', '78701'),
('Ivy Walker', '808 Willow Bend', 'Jacksonville', 'FL', '32202'),
('Jack Harris', '909 Spruce Hill', 'Fort Worth', 'TX', '76102'),
('Karen Lewis', '1010 Magnolia Circle', 'Columbus', 'OH', '43215'),
('Larry King', '1111 Cherry Loop', 'Charlotte', 'NC', '28202'),
('Mary Young', '1212 Poplar Crescent', 'San Francisco', 'CA', '94103'),
('Nick Hall', '1313 Fir Avenue', 'Indianapolis', 'IN', '46204'),
('Olivia Green', '1414 Maple Street', 'Seattle', 'WA', '98104'),
('Peter Adams', '1515 Oak Avenue', 'Denver', 'CO', '80202'),
('Quinn Baker', '1616 Pine Road', 'Washington', 'DC', '20001'),
('Ruby Carter', '1717 Elm Boulevard', 'Boston', 'MA', '02116');

-- Insert Unloaders with some assigned to doors (using door_id)
INSERT INTO Unloader (name, shift, employee_number, status, door_id) VALUES
('John Smith', '2nd Shift', '45782', 'Assigned', 1),   -- Assigned to N1
('Emily Johnson', '2nd Shift', '39821', 'Assigned', 2), -- Assigned to N2
('Michael Williams', '2nd Shift', '61245', 'Assigned', 3), -- Assigned to N3
('Sarah Brown', '2nd Shift', '53467', 'Assigned', 11),  -- Assigned to E1
('David Jones', '2nd Shift', '78934', 'Assigned', 12),  -- Assigned to E2
('Olivia Garcia', '2nd Shift', '24589', 'Assigned', 13), -- Assigned to E3
('James Martinez', '2nd Shift', '87653', 'Assigned', 21), -- Assigned to S1
('Emma Rodriguez', '2nd Shift', '32176', 'Assigned', 22), -- Assigned to S2
('Robert Hernandez', '2nd Shift', '65432', 'Assigned', 23), -- Assigned to S3
('Sophia Lopez', '2nd Shift', '98712', 'Assigned', 31), -- Assigned to W1
('William Gonzalez', '2nd Shift', '12345', 'Assigned', 32), -- Assigned to W2
('Ava Wilson', '2nd Shift', '56789', 'Assigned', 33),   -- Assigned to W3
('Joseph Anderson', '2nd Shift', '43210', 'Assigned', 41), -- Assigned to C1
('Mia Thomas', '2nd Shift', '67895', 'Assigned', 42),   -- Assigned to C2
('Charles Taylor', '2nd Shift', '34567', 'Assigned', 43), -- Assigned to C3
('Isabella Moore', '2nd Shift', '89012', 'Unassigned', NULL),
('Daniel Jackson', '2nd Shift', '76543', 'Unassigned', NULL),
('Amelia Martin', '2nd Shift', '21098', 'Unassigned', NULL),
('Jane Davis', '2nd Shift', '87654', 'Unassigned', NULL),
('Thomas Miller', '2nd Shift', '10987', 'Unassigned', NULL);

-- Insert Orders with proper door assignments (using door_id)
-- OF1234 Trailer Orders (assigned to door_id 1 - N1)
INSERT INTO Order_Number (order_number, customer_id, shipper_id, trailer_id, door_id, handling_unit, weight, status) VALUES
('123456789', 1, 1, 1, 1, 2, 61, 'unloading'),
('234567891', 2, 1, 1, 1, 4, 113, 'unloading'),
('345678911', 3, 1, 1, 1, 6, 99, 'unloading'),
('456789123', 4, 1, 1, 1, 5, 65, 'unloading'),
('567891234', 5, 1, 1, 1, 8, 71, 'unloading'),
('678912345', 6, 1, 1, 1, 3, 103, 'unloading'),
('789123456', 7, 1, 1, 1, 2, 58, 'unloading'),
('891234567', 8, 1, 1, 1, 9, 75, 'unloading'),
('912345678', 9, 1, 1, 1, 1, 89, 'unloading'),
('123456780', 10, 1, 1, 1, 7, 91, 'unloading');

-- LE4567 Trailer Orders (assigned to door_id 2 - N2)
INSERT INTO Order_Number (order_number, customer_id, shipper_id, trailer_id, door_id, handling_unit, weight, status) VALUES
('234567801', 11, 2, 2, 2, 3, 102, 'unloading'),
('345678902', 12, 2, 2, 2, 2, 63, 'unloading'),
('456789103', 13, 2, 2, 2, 4, 111, 'unloading'),
('567891214', 14, 2, 2, 2, 6, 93, 'unloading'),
('678912325', 15, 2, 2, 2, 5, 61, 'unloading'),
('789123436', 16, 2, 2, 2, 3, 83, 'unloading'),
('891234547', 17, 2, 2, 2, 8, 116, 'unloading'),
('912345658', 18, 2, 2, 2, 7, 80, 'unloading'),
('123456769', 19, 2, 2, 2, 9, 57, 'unloading'),
('234567890', 20, 2, 2, 2, 2, 66, 'unloading');

-- B2389 Trailer Orders (assigned to door_id 3 - N3)
INSERT INTO Order_Number (order_number, customer_id, shipper_id, trailer_id, door_id, handling_unit, weight, status) VALUES
('345678901', 1, 3, 3, 3, 4, 109, 'unloading'),
('456789012', 2, 3, 3, 3, 5, 98, 'unloading'),
('567891123', 3, 3, 3, 3, 3, 82, 'unloading'),
('678912234', 4, 3, 3, 3, 6, 105, 'unloading'),
('789123345', 5, 3, 3, 3, 7, 117, 'unloading'),
('123456677', 6, 3, 3, 3, 5, 77, 'unloading'),
('234567788', 7, 3, 3, 3, 7, 117, 'unloading'),
('345678899', 8, 3, 3, 3, 5, 77, 'unloading'),
('456789900', 9, 3, 3, 3, 4, 102, 'unloading'),
('567891011', 10, 3, 3, 3, 4, 100, 'unloading');

-- Insert Users
-- Password for all users is "password"
INSERT INTO users (username, password_hash, role) VALUES
('user', '$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem', 'ROLE_USER'),
('admin', '$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem', 'ROLE_ADMIN');

COMMIT TRANSACTION;