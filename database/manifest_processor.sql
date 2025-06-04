BEGIN TRANSACTION;

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS Order_Number CASCADE;
DROP TABLE IF EXISTS Customer CASCADE;
DROP TABLE IF EXISTS Trailer CASCADE;
DROP TABLE IF EXISTS Shipper CASCADE;
DROP TABLE IF EXISTS Unloader CASCADE;

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

CREATE TABLE Trailer (
    trailer_id SERIAL,
    trailer_number VARCHAR(20) NOT NULL,
    trailer_type VARCHAR(50) NOT NULL,
    shipper_id INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Unassigned',
    CONSTRAINT PK_Trailer PRIMARY KEY (trailer_id),
    CONSTRAINT FK_Trailer_Shipper FOREIGN KEY (shipper_id) REFERENCES Shipper(shipper_id) ON DELETE CASCADE,
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
    CONSTRAINT PK_Unloader PRIMARY KEY (employee_id),
    CONSTRAINT UQ_Employee_number UNIQUE (employee_number)
);

CREATE TABLE Order_Number (
    order_id SERIAL,
    order_number VARCHAR(20) NOT NULL UNIQUE,
    customer_id INTEGER NOT NULL,
    shipper_id INTEGER NOT NULL,
    trailer_id INTEGER NOT NULL,
    door_number VARCHAR(10) NOT NULL,
    handling_unit INTEGER NOT NULL,
    weight INTEGER NOT NULL,
    status VARCHAR (20) NOT NULL,
    CONSTRAINT PK_Order_Number PRIMARY KEY (order_id),
    CONSTRAINT FK_Order_Number_Customer FOREIGN KEY (customer_id) REFERENCES Customer(customer_id) ON DELETE CASCADE,
    CONSTRAINT FK_Order_Number_Shipper FOREIGN KEY (shipper_id) REFERENCES Shipper(shipper_id) ON DELETE CASCADE,
    CONSTRAINT FK_Order_Number_Trailer FOREIGN KEY (trailer_id) REFERENCES Trailer(trailer_id) ON DELETE CASCADE
);
-- *************************************************************************************************
-- Insert some sample starting data
-- *************************************************************************************************

INSERT INTO Shipper (name, address, city, state, zip_code) VALUES
('Swift Logistics', '500 Shipping Lane', 'Chicago', 'IL', '60601'),
('Global Freight Solutions', '1200 Cargo Drive', 'Dallas', 'TX', '75201'),
('Elite Shipping Co.', '800 Transport Blvd', 'Los Angeles', 'CA', '90012');


INSERT INTO Trailer (trailer_number, trailer_type, shipper_id, status) VALUES
('OF1234', 'Local/City', 1, 'Unassigned'),
('LE4567', 'Lift-Gate', 2, 'Unassigned'),
('B2389', 'Box Truck', 3, 'Unassigned');


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
('Ruby Carter', '1717 Elm Boulevard', 'Boston', 'MA', '02116'),
('Steve Parker', '1818 Cedar Lane', 'Nashville', 'TN', '37203'),
('Tina Scott', '1919 Birch Drive', 'Baltimore', 'MD', '21202'),
('Ursula Evans', '2020 Walnut Way', 'Oklahoma City', 'OK', '73102'),
('Victor Price', '2121 Ash Terrace', 'Louisville', 'KY', '40202'),
('Wendy Bell', '2222 Redwood Place', 'Portland', 'OR', '97204'),
('Xavier Young', '2323 Cypress Court', 'Memphis', 'TN', '38103'), -- Added missing customer 26
('Zachary Howard', '2525 Spruce Hill', 'Milwaukee', 'WI', '53202'),
('Angela Bryant', '2626 Magnolia Circle', 'Albuquerque', 'NM', '87102'),
('Brian Lewis', '2727 Cherry Loop', 'Tucson', 'AZ', '85701'),
('Charlotte Hayes', '2828 Poplar Crescent', 'Fresno', 'CA', '93721'),
('Derek Long', '2929 Fir Avenue', 'Sacramento', 'CA', '95814'),
('Emily Murphy', '3030 Maple Street', 'Kansas City', 'MO', '64106'),
('Fiona Sanders', '3131 Oak Avenue', 'Mesa', 'AZ', '85201'),
('George Reed', '3232 Pine Road', 'Atlanta', 'GA', '30303'),
('Hannah Collins', '3333 Elm Boulevard', 'Omaha', 'NE', '68102'),
('Ian Powell', '3434 Cedar Lane', 'Colorado Springs', 'CO', '80903'),
('Jenna Ross', '3535 Birch Drive', 'Raleigh', 'NC', '27601'),
('Kevin Mitchell', '3636 Walnut Way', 'Miami', 'FL', '33128'),
('Laura Perez', '3737 Ash Terrace', 'Long Beach', 'CA', '90802'),
('Matt Bennett', '3838 Redwood Place', 'Virginia Beach', 'VA', '23451'),
('Nora Ward', '3939 Cypress Court', 'Oakland', 'CA', '94607'),
('Oscar Hughes', '4040 Willow Bend', 'Minneapolis', 'MN', '55402'),
('Paula Rogers', '4141 Spruce Hill', 'Tulsa', 'OK', '74103'),
('Quinn Murphy', '4242 Magnolia Circle', 'Arlington', 'TX', '76010'),
('Riley Gray', '4343 Cherry Loop', 'New Orleans', 'LA', '70112'),
('Sophie Cooper', '4444 Poplar Crescent', 'Wichita', 'KS', '67202'),
('Tom Campbell', '4545 Fir Avenue', 'Cleveland', 'OH', '44113'),
('Una Grant', '4646 Maple Street', 'Tampa', 'FL', '33602'),
('Victor Johnson', '4747 Oak Avenue', 'Bakersfield', 'CA', '93301'),
('Wendy Bell', '4848 Pine Road', 'Aurora', 'CO', '80010'),
('Xander Turner', '4949 Cypress Court', 'Anaheim', 'CA', '92805'),
('Yuri Foster', '5050 Walnut Way', 'Honolulu', 'HI', '96813'),
('Zachary Howard', '5151 Maple Street', 'Henderson', 'NV', '89011'),
('Abigail Clark', '5252 Oak Avenue', 'Stockton', 'CA', '95202'),
('Blake Parker', '5353 Birch Drive', 'Lexington', 'KY', '40507'),
('Chloe Collins', '5454 Redwood Place', 'Corpus Christi', 'TX', '78401'),
('Dominic Lewis', '5555 Cherry Loop', 'Riverside', 'CA', '92501'),
('Evelyn Brown', '5656 Spruce Hill', 'Santa Ana', 'CA', '92701'),
('Fred Thompson', '5757 Willow Bend', 'Orlando', 'FL', '32801'),
('Gina Carter', '5858 Elm Boulevard', 'Irvine', 'CA', '92612'),
('Henry Powell', '5959 Cedar Lane', 'Cincinnati', 'OH', '45202'),
('Isabella Ross', '6060 Fir Avenue', 'Pittsburgh', 'PA', '15222'),
('Jason Ward', '6161 Maple Street', 'St. Louis', 'MO', '63101'),
('Kyle Foster', '6262 Pine Road', 'Greensboro', 'NC', '27401'),
('Lily Hughes', '6363 Oak Avenue', 'Newark', 'NJ', '07102'),
('Michael Scott', '6464 Birch Drive', 'Buffalo', 'NY', '14203'),
('Nancy Price', '6565 Willow Bend', 'Plano', 'TX', '75074'),
('Olivia Parker', '6666 Spruce Hill', 'Lincoln', 'NE', '68508'),
('Paul Rogers', '6767 Elm Boulevard', 'Anchorage', 'AK', '99501'),
('Quincy Lewis', '6868 Cedar Lane', 'Durham', 'NC', '27701'),
('Rachel Gray', '6969 Poplar Crescent', 'Jersey City', 'NJ', '07302'),
('Samuel Reed', '7070 Maple Street', 'Chandler', 'AZ', '85224'),
('Taylor Bell', '7171 Oak Avenue', 'Lubbock', 'TX', '79401'),
('Uma Green', '7272 Birch Drive', 'Madison', 'WI', '53703'),
('Vincent King', '7373 Cedar Lane', 'Reno', 'NV', '89501'),
('Willow Adams', '7474 Pine Road', 'Fort Wayne', 'IN', '46802'),
('Xavier Turner', '7575 Cypress Court', 'Baton Rouge', 'LA', '70801');


-- OF1234 Trailer Orders
INSERT INTO Order_Number (order_number, customer_id, shipper_id, trailer_id, door_number, handling_unit, weight,status) VALUES
('123456789', 1, 1, 1, 'Door 7', 2, 61, 'shipped'),
('234567891', 2, 1, 1, 'Door 33', 4, 113, 'shipped'),
('345678911', 3, 1, 1, 'Door 19', 6, 99, 'shipped'),
('456789123', 4, 1, 1, 'Door 28', 5, 65, 'shipped'),
('567891234', 5, 1, 1, 'Door 50', 8, 71, 'shipped'),
('678912345', 6, 1, 1, 'Door 14', 3, 103, 'shipped'),
('789123456', 7, 1, 1, 'Door 37', 2, 58, 'shipped'),
('891234567', 8, 1, 1, 'Door 1', 9, 75, 'shipped'),
('912345678', 9, 1, 1, 'Door 22', 1, 89, 'shipped'),
('123456780', 10, 1, 1, 'Door 40', 7, 91, 'shipped'),
('234567801', 11, 1, 1, 'Door 5', 3, 102, 'shipped'),
('345678902', 12, 1, 1, 'Door 48', 2, 63, 'shipped'),
('456789103', 13, 1, 1, 'Door 26', 4, 111, 'shipped'),
('567891214', 14, 1, 1, 'Door 11', 6, 93, 'shipped'),
('678912325', 15, 1, 1, 'Door 30', 5, 61, 'shipped'),
('789123436', 16, 1, 1, 'Door 49', 3, 83, 'shipped'),
('891234547', 17, 1, 1, 'Door 17', 8, 116, 'shipped'),
('912345658', 18, 1, 1, 'Door 36', 7, 80, 'shipped'),
('123456769', 19, 1, 1, 'Door 8', 9, 57, 'shipped'),
('234567890', 20, 1, 1, 'Door 44', 2, 66, 'shipped'),
('345678901', 21, 1, 1, 'Door 21', 4, 109, 'shipped'),
('456789012', 22, 1, 1, 'Door 39', 5, 98, 'shipped'),
('567891123', 23, 1, 1, 'Door 25', 3, 82, 'shipped'),
('678912234', 24, 1, 1, 'Door 16', 6, 105, 'shipped'),
('789123345', 25, 1, 1, 'Door 34', 7, 117, 'shipped'),
('123456677', 26, 1, 1, 'Door 27', 5, 77, 'shipped'),
('234567788', 27, 2, 2, 'Door 34', 7, 117, 'shipped'),
('345678899', 28, 2, 2, 'Door 27', 5, 77, 'shipped'),
('456789900', 29, 2, 2, 'Door 10', 4, 102, 'shipped'),
('567891011', 30, 2, 2, 'Door 11', 4, 100, 'shipped'),
('678912122', 31, 2, 2, 'Door 12', 4, 101, 'shipped'),
('789123233', 32, 2, 2, 'Door 13', 5, 99, 'shipped'),
('891234344', 33, 2, 2, 'Door 14', 5, 103, 'shipped'),
('912345455', 34, 2, 2, 'Door 15', 6, 96, 'shipped'),
('123456566', 35, 2, 2, 'Door 16', 6, 105, 'shipped'),
('234567677', 36, 2, 2, 'Door 17', 4, 95, 'shipped'),
('345678788', 37, 2, 2, 'Door 18', 5, 100, 'shipped'),
('456789899', 38, 2, 2, 'Door 19', 5, 97, 'shipped'),
('567891000', 39, 2, 2, 'Door 20', 4, 98, 'shipped'),
('678912111', 40, 2, 2, 'Door 21', 4, 99, 'shipped'),
('789123222', 41, 2, 2, 'Door 22', 4, 100, 'shipped'),
('891234333', 42, 2, 2, 'Door 23', 4, 101, 'shipped'),
('912345444', 43, 2, 2, 'Door 24', 4, 102, 'shipped'),
('123456555', 44, 2, 2, 'Door 25', 4, 103, 'shipped'),
('234567666', 45, 2, 2, 'Door 26', 4, 104, 'shipped'),
('345678777', 46, 2, 2, 'Door 27', 4, 105, 'shipped'),
('456789888', 47, 2, 2, 'Door 28', 4, 106, 'shipped'),
('567890999', 48, 2, 2, 'Door 29', 4, 107, 'shipped'),
('678911111', 49, 2, 2, 'Door 30', 4, 108, 'shipped'),
('789122222', 50, 2, 2, 'Door 31', 4, 109, 'shipped'),
('891233333', 51, 2, 2, 'Door 32', 4, 110, 'shipped'),
('912345442', 52, 2, 2, 'Door 33', 4, 111, 'shipped'),
('123456553', 53, 3, 3, 'Door 11', 5, 103, 'shipped'),
('234567664', 54, 3, 3, 'Door 12', 5, 97, 'shipped'),
('345678775', 55, 3, 3, 'Door 13', 6, 105, 'shipped'),
('456789886', 56, 3, 3, 'Door 14', 4, 94, 'shipped'),
('567891997', 57, 3, 3, 'Door 15', 4, 91, 'shipped'),
('678912108', 58, 3, 3, 'Door 16', 5, 108, 'shipped'),
('789123219', 59, 3, 3, 'Door 17', 6, 95, 'shipped'),
('891234330', 60, 3, 3, 'Door 18', 7, 99, 'shipped'),
('912345441', 61, 3, 3, 'Door 19', 5, 102, 'shipped'),
('123456552', 62, 3, 3, 'Door 20', 5, 100, 'shipped'),
('234567663', 63, 3, 3, 'Door 21', 6, 101, 'shipped'),
('345678774', 64, 3, 3, 'Door 22', 5, 97, 'shipped'),
('456789885', 65, 3, 3, 'Door 23', 4, 96, 'shipped'),
('567891996', 66, 3, 3, 'Door 24', 6, 106, 'shipped'),
('678912107', 67, 3, 3, 'Door 25', 5, 99, 'shipped'),
('789123218', 68, 3, 3, 'Door 26', 5, 104, 'shipped'),
('891234329', 69, 3, 3, 'Door 27', 4, 93, 'shipped'),
('912345440', 70, 3, 3, 'Door 28', 6, 98, 'shipped'),
('123456551', 71, 3, 3, 'Door 29', 4, 91, 'shipped'),
('234567662', 72, 3, 3, 'Door 30', 5, 107, 'shipped'),
('345678773', 73, 3, 3, 'Door 31', 5, 96, 'shipped'),
('456789884', 74, 3, 3, 'Door 32', 6, 103, 'shipped'),
('567891995', 75, 3, 3, 'Door 33', 4, 94, 'shipped'),
('678912106', 76, 3, 3, 'Door 34', 5, 100, 'shipped');

INSERT INTO Unloader (name, shift, employee_number, status) VALUES
('John Smith', '2nd Shift', '45782', 'Unassigned'),
('Emily Johnson', '2nd Shift', '39821', 'Unassigned'),
('Michael Williams', '2nd Shift', '61245', 'Unassigned'),
('Sarah Brown', '2nd Shift', '53467', 'Unassigned'),
('David Jones', '2nd Shift', '78934', 'Unassigned'),
('Olivia Garcia', '2nd Shift', '24589', 'Unassigned'),
('James Martinez', '2nd Shift', '87653', 'Unassigned'),
('Emma Rodriguez', '2nd Shift', '32176', 'Unassigned'),
('Robert Hernandez', '2nd Shift', '65432', 'Unassigned'),
('Sophia Lopez', '2nd Shift', '98712', 'Unassigned'),
('William Gonzalez', '2nd Shift', '12345', 'Unassigned'),
('Ava Wilson', '2nd Shift', '56789', 'Unassigned'),
('Joseph Anderson', '2nd Shift', '43210', 'Unassigned'),
('Mia Thomas', '2nd Shift', '67895', 'Unassigned'),
('Charles Taylor', '2nd Shift', '34567', 'Unassigned'),
('Isabella Moore', '2nd Shift', '89012', 'Unassigned'),
('Daniel Jackson', '2nd Shift', '76543', 'Unassigned'),
('Amelia Martin', '2nd Shift', '21098', 'Unassigned'),
('Jane Davis', '2nd Shift', '87654', 'Unassigned'),
('Thomas Miller', '2nd Shift', '10987', 'Unassigned');

-- Insert Users
-- Password for all users is password
INSERT INTO users (username, password_hash, role) VALUES
('user', '$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem', 'ROLE_USER'),
('admin', '$2a$10$tmxuYYg1f5T0eXsTPlq/V.DJUKmRHyFbJ.o.liI1T35TFbjs2xiem', 'ROLE_ADMIN');

COMMIT TRANSACTION;
