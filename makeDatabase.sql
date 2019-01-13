DROP DATABASE IF EXISTS db;
CREATE DATABASE db;

USE db;
CREATE TABLE Users (
    userID INT(11) PRIMARY KEY AUTO_INCREMENT,
    fullName VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    hashPass VARCHAR(64) NOT NULL
);

CREATE TABLE Files (
    fileID INT(11) PRIMARY KEY AUTO_INCREMENT,
    rawData Text(64000) NOT NULL,
    fileName VARCHAR(50) NOT NULL
);

CREATE TABLE Access (
    userID INT(11) NOT NULL,
    fileID INT(11) NOT NULL,
    accessID INT(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    foreign key fk1(userID) REFERENCES Users(userID),
    foreign key fk2(fileID) REFERENCES Files(fileID)
);

CREATE TABLE Notifications (
    notificationID INT(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    userID INT(11) NOT NULL,
    fromName VARCHAR(50) NOT NULL,
    isRead INT(1) NOT NULL,
    fileName VARCHAR(50) NOT NULL,
    rawData Text(64000) NOT NULL,
    foreign key fk3(userID) REFERENCES Users(userID)
);

SELECT * FROM Users;