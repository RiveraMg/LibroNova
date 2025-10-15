/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  Coder
 * Created: 14/10/2025
 */

/* ENTIDADES: Libro, Usuario, Socio, Préstamo
Book, User, Member, Loan*/

CREATE DATABASE if not exists LibroNova;
USE LibroNova;

-- Table structure for users
DROP TABLE IF EXISTS Users;
CREATE TABLE users (
    id_user INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(200) NOT NULL,
    password VARCHAR(250) NOT NULL,
    role ENUM('ADMIN','ASSISTANT') DEFAULT 'ASSISTANT',
    state ENUM('Active','Inactive') DEFAULT 'Active',
	created_at timestamp default current_timestamp,
	updated_at timestamp default current_timestamp on update current_timestamp

);

INSERT INTO users (username, password, role, state)
VALUES
('admin', 'admin123', 'ADMIN', 'Active'),
('jonh', 'jonh123', 'ASSISTANT', 'Active');


-- Table structure for Members
DROP TABLE IF EXISTS Members;
CREATE TABLE Members (
    id_member INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(50),
    state ENUM('Active','Inactive') DEFAULT 'Active',
	created_at timestamp default current_timestamp,
	updated_at timestamp default current_timestamp on update current_timestamp
);

INSERT INTO Members (name, email, phone, state)
VALUES
('Juan Perez', 'juanperez@email.com', '3001234567', 'Active'),
('Maria Gomez', 'mariagomez@email.com', '3017654321', 'Active');



-- Table structure for Books
DROP TABLE IF EXISTS Books;
CREATE TABLE Books (
  id_book INT AUTO_INCREMENT PRIMARY KEY,
  isbn VARCHAR (200) UNIQUE NOT NULL,
  title VARCHAR(250) NOT NULL,
  author VARCHAR(200) NOT NULL,
  category VARCHAR (200) NOT NULL,
  copies INT NOT NULL,
  availablecopies INT NOT NULL,
  priceReference DOUBLE NOT NULL,
  isActivo BOOLEAN DEFAULT TRUE,
  created_at timestamp default current_timestamp,
  updated_at timestamp default current_timestamp on update current_timestamp
);

INSERT INTO Books (isbn, title, author, category, copies, availablecopies, priceReference)
VALUES
('1234', 'El Quijote', 'Miguel de Cervantes', 'Novela', 5, 5, 45.50),
('1230', 'La Odisea', 'Homero', 'Clásico', 3, 3, 39.99);


-- Table structure for Loans
DROP TABLE IF EXISTS Loans;
CREATE TABLE Loans (
    id_loan INT AUTO_INCREMENT PRIMARY KEY,
	id_member INT,
	id_book INT,
	loan_datetime DATE NOT NULL,
	refund_datetime DATE NOT NULL,
    penalty DOUBLE DEFAULT 0,
    state ENUM('BORROWED','REPAY') DEFAULT 'BORROWED',
	created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp,
    
  FOREIGN KEY (id_member) REFERENCES Members (id_member) on delete set null on update cascade,
  FOREIGN KEY (id_book) REFERENCES Books (id_book) on delete set null on update cascade
  );
INSERT INTO Loans (id_member, id_book, loan_datetime, refund_datetime, penalty, state)
VALUES
(1, 1, '2025-10-10', '2025-10-20', 0, 'BORROWED');

SHOW TABLES;
SELECT * FROM Members;
SELECT * FROM Books;
SELECT * FROM users;
SELECT * FROM Loans;

