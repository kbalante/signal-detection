/* create the database that will house the raw application data from the csv file */
create database signal_detection;
select count(*) from signal_detection
/* create the raw signal detection table */
CREATE TABLE signal_detection.signal_detection (
   id INT NOT NULL AUTO_INCREMENT,
   asset_un VARCHAR(100) NOT NULL,
   status VARCHAR(40) NOT NULL,
   entry_date DATE,
   PRIMARY KEY ( id )
);

/* IMPORTANT: Run mysqlimport on command line to import from the cvs file to the above db table */
/* mysqlimport --ignore-lines=1 --fields-terminated-by=, --fields-optionally-enclosed-by=\" --columns=asset_un,status,entry_date --local -u root -p signal_detection /PATH_TO_CSV/signal_detection.csv */

/* Create report tables that the application will run queries against */

/* signal detection report by day */
create table signal_detection.signal_detection_report_by_day as
SELECT count(*) as num_signals, date(entry_date) as date, asset_un
    FROM signal_detection.signal_detection
GROUP BY entry_date, asset_un;
/* create index for this report to speed up queries */
CREATE INDEX daily_sd_report_index ON signal_detection.signal_detection_report_by_day (asset_un);

/* signal detection report by month - the day is set to the 1st for that particular month */
create table signal_detection.signal_detection_report_by_month as
SELECT count(*) as num_signals, str_to_date(concat(date_format(entry_date,'%Y-%m'), '-01'), '%Y-%m-%d') as date, asset_un
    FROM signal_detection.signal_detection
GROUP BY DATE_FORMAT(entry_date,'%Y-%m'), asset_un;
/* create index for this report to speed up queries */
CREATE INDEX monthly_sd_report_index ON signal_detection.signal_detection_report_by_month (asset_un);

/* signal detection report by year - the month and day is set to Jan 1st for that particular year */
create table signal_detection.signal_detection_report_by_year as 
SELECT count(*) as num_signals, str_to_date(concat(cast(year(entry_date) as char),'-01-01'), '%Y-%m-%d') as date, asset_un
    FROM signal_detection.signal_detection
GROUP BY year(entry_date), asset_un;
/* create index for this report to speed up queries */
CREATE INDEX yearly_sd_report_index ON signal_detection.signal_detection_report_by_year (asset_un);

/* add autoincrement id's to the new report tables */
ALTER TABLE signal_detection.signal_detection_report_by_day ADD COLUMN id INT AUTO_INCREMENT UNIQUE FIRST;
ALTER TABLE signal_detection.signal_detection_report_by_month ADD COLUMN id INT AUTO_INCREMENT UNIQUE FIRST;
ALTER TABLE signal_detection.signal_detection_report_by_year ADD COLUMN id INT AUTO_INCREMENT UNIQUE FIRST;

/* upload data to the production database */
/* mysqldump -u root --databases signal_detection --single-transaction --compress --order-by-primary -padmin | mysql -u root --port=3306 --host=kahuna.cvg6xsoeyl2s.us-west-2.rds.amazonaws.com -pmatatas7 */

select * from signal_detection.signal_detection where asset_un = "3106" order by entry_date
select * from signal_detection.signal_detection_report_by_day where asset_un = "3106" order by date
select * from signal_detection.signal_detection_report_by_month where asset_un = "3106" order by date
select * from signal_detection.signal_detection_report_by_year where asset_un = "3106" order by date