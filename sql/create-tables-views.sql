use aimsio;

/*DROP TABLE signal_detection;*/
CREATE TABLE signal_detection (
   id INT NOT NULL AUTO_INCREMENT,
   asset_un VARCHAR(100) NOT NULL,
   status VARCHAR(40) NOT NULL,
   entry_date DATE,
   PRIMARY KEY ( id )
);

		