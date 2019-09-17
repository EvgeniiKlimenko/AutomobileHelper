CREATE DATABASE AutomobileHelperDB;

USE AutomobileHelperDB;

CREATE TABLE MileageHistory(
   ID   		int          NOT NULL AUTO_INCREMENT,
   MileageValue int 	     NOT NULL,
   DateStamp    date    	 NOT NULL,
   PRIMARY KEY (ID)
);
INSERT INTO MileageHistory (MileageValue, DateStamp)
VALUES (0, CURDATE());

CREATE TABLE AutoElements(
   ID   	    int          NOT NULL AUTO_INCREMENT,
   ElementName  varchar(30)  NOT NULL,
   ElementDesc  varchar(250),
   PRIMARY KEY (ID)
);
INSERT INTO AutoElements (ElementName, ElementDesc)
VALUES ('Engine Oil', 'Engine Oil reduce a friction and wear on moving parts and to clean the engine from sludge and varnish.');

INSERT INTO AutoElements (ElementName, ElementDesc)
VALUES ('Oil filter', 'An oil filter is a filter designed to remove contaminants from engine oil.');

INSERT INTO AutoElements (ElementName, ElementDesc)
VALUES ('Spark plug', 'It is a device for delivering electric current from an ignition system to the combustion chamber of a spark-ignition engine to ignite the compressed fuel/air mixture by an electric spark, while containing combustion pressure within the engine.');

INSERT INTO AutoElements (ElementName, ElementDesc)
VALUES ('Air filter', 'The combustion air filter prevents abrasive particulate matter from entering the engines cylinders, where it would cause mechanical wear and oil contamination.');

INSERT INTO AutoElements (ElementName, ElementDesc)
VALUES ('Engine coolant', 'The liquid cooler is used to cool the engine during operation.');

INSERT INTO AutoElements (ElementName, ElementDesc)
VALUES ('Fuses', 'Automotive fuses are a class of fuses used to protect the wiring and electrical equipment for vehicles.');

INSERT INTO AutoElements (ElementName, ElementDesc)
VALUES ('Wheels bearings', 'A bearing is a machine element that constrains relative movement to the desired motion and reduces friction between moving parts.');

INSERT INTO AutoElements (ElementName, ElementDesc)
VALUES ('Brake pads', 'Brake pads convert the kinetic energy of the vehicle to thermal energy through friction.');

INSERT INTO AutoElements (ElementName, ElementDesc)
VALUES ('Tyres', 'Tyres is a ring-shaped component that surrounds a wheels rim to transfer a vehicles load from the axle through the wheel to the ground and to provide traction on the surface traveled over.');

CREATE TABLE ServiceHistory(
   ID   		int     NOT NULL AUTO_INCREMENT,
   ServiceCost  decimal(12,2),
   ElementName  varchar(30),
   ElementCost  decimal(12,2),
   MileageStamp int 	     NOT NULL,
   DateStamp    date		 NOT NULL,
   Commentary   varchar(250),
   PRIMARY KEY (ID)
);