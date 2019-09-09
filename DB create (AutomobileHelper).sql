CREATE DATABASE AutomobileHelperDB;

USE AutomobileHelperDB;

CREATE TABLE MileageHistory(
   ID   		int          NOT NULL AUTO_INCREMENT,
   MileageValue int 	     NOT NULL,
   DateStamp    date    	 NOT NULL,
   PRIMARY KEY (ID)
);

CREATE TABLE AutoElements(
   ID   	    int          NOT NULL AUTO_INCREMENT,
   ElementName  varchar(30)  NOT NULL,
   ElementDesc  varchar(200),
   PRIMARY KEY (ID)
);

CREATE TABLE ServiceHistory(
   ID   		int     NOT NULL AUTO_INCREMENT,
   ServiceCost  decimal(12,2),
   ElementName  varchar(30),
   ElementCost  decimal(12,2),
   MileageStamp int 	     NOT NULL,
   DateStamp    date		 NOT NULL,
   Commentary   varchar(200),
   PRIMARY KEY (ID)
);