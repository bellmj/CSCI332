import mysql.connector
from mysql.connector import errorcode
from time import sleep
DB_NAME = 'stockSim'
TABLES = {}
TABLES['NasdaqCompanyInfo'] = (
    "CREATE TABLE NasdaqCompanyInfo ("
    "  symbol varchar(8) NOT NULL,"
    "  name varchar(255) NOT NULL,"
    "  sector varchar(255),"
    "  industry varchar(255),"
    "  PRIMARY KEY (symbol)"
    ") ENGINE=InnoDB")
TABLES['NyseCompanyInfo'] = (
    "CREATE TABLE NyseCompanyInfo ("
    "  symbol varchar(16) NOT NULL,"
    "  name varchar(255) NOT NULL,"
    "  sector varchar(255),"
    "  industry varchar(255),"
    "  PRIMARY KEY (symbol)"
    ") ENGINE=InnoDB")
TABLES['Users'] = (
    "CREATE TABLE Users ("
    "  email varchar(255) NOT NULL,"
    "  name varchar(255) NOT NULL,"
    "  accountBalance DECIMAL(14,2) DEFAULT 0.0,"
    "  password varchar(255) NOT NULL,"
    "  PRIMARY KEY (email),"
    "  CONSTRAINT Valid_Email CHECK(email LIKE \"%_@%__.__%\"),"##TODO these constraints aren't enforced by InnoDB and will have to be immplemented
    "  CONSTRAINT Acct_Bal_Not_Neg CHECK(accountBalance >= 0)"##     in a Trigger
    ") ENGINE=InnoDB")
TABLES['Roles'] = (
    "CREATE TABLE Roles("
    " role varchar(255) NOT NULL,"
    " description varchar(255) NOT NULL,"
    " PRIMARY KEY (role)"
    ") ENGINE=InnoDB")
TABLES['UserRoles'] = (
    "CREATE TABLE UserRoles("
    " email varchar(255) NOT NULL,"
    " userRole varchar(255) NOT NULL,"
    " PRIMARY KEY (email),"
    " FOREIGN KEY (email) REFERENCES Users(email) ON DELETE CASCADE ON UPDATE CASCADE,"
    " FOREIGN KEY (userRole) REFERENCES Roles(role) ON UPDATE CASCADE"
    ") ENGINE=InnoDB;")
TABLES['NysePricesByTheMinute'] = (
    "CREATE TABLE NysePricesByTheMinute("
    " symbol varchar(16) NOT NULL,"
    " timestamp TIMESTAMP NOT NULL,"
    " open DECIMAL(14,2) NOT NULL,"
    " close DECIMAL(14,2) NOT NULL,"
    " high DECIMAL(14,2) NOT NULL,"
    " low DECIMAL(14,2) NOT NULL,"
    " volume INT NOT NULL,"
    " PRIMARY KEY (symbol,timestamp),"
    " FOREIGN KEY (symbol) REFERENCES NyseCompanyInfo(symbol)"
    ") ENGINE=InnoDB;")
TABLES['NasdaqPricesByTheMinute'] = (
    "CREATE TABLE NasdaqPricesByTheMinute("
    " symbol varchar(8) NOT NULL,"
    " timestamp TIMESTAMP NOT NULL,"
    " open DECIMAL(14,2) NOT NULL,"
    " close DECIMAL(14,2) NOT NULL,"
    " high DECIMAL(14,2) NOT NULL,"
    " low DECIMAL(14,2) NOT NULL,"
    " volume INT NOT NULL,"
    " PRIMARY KEY (symbol,timestamp),"
    " FOREIGN KEY (symbol) REFERENCES NasdaqCompanyInfo(symbol)"
    ") ENGINE=InnoDB;")
TABLES['NasdaqPortfolioHoldings'] = (
    "CREATE TABLE NasdaqPortfolioHoldings("
    " nasdaqSymbol varchar(8) NOT NULL,"
    " ownerEmail varchar(255) NOT NULL,"
    " timestampWhenBought TIMESTAMP NOT NULL,"
    " timestampOfStock TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
    " quantityHeld INT NOT NULL,"
    " PRIMARY KEY( timestampWhenBought, ownerEmail, nasdaqSymbol),"
    " FOREIGN KEY (nasdaqSymbol) REFERENCES NasdaqCompanyInfo(symbol),"
    " FOREIGN KEY (ownerEmail) REFERENCES Users(email) ON DELETE CASCADE ON UPDATE CASCADE,"
    " FOREIGN KEY (nasdaqSymbol,timestampOfStock) REFERENCES NasdaqPricesByTheMinute (symbol,timestamp)"
    ") ENGINE=InnoDB;")
TABLES['NysePortfolioHoldings'] = (
    "CREATE TABLE NysePortfolioHoldings("
    " nyseSymbol varchar(16) NOT NULL,"
    " ownerEmail varchar(255) NOT NULL,"
    " timestampWhenBought TIMESTAMP NOT NULL,"
    " timestampOfStock TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
    " quantityHeld INT NOT NULL,"
    " PRIMARY KEY( timestampWhenBought, ownerEmail, nyseSymbol),"
    " FOREIGN KEY (nyseSymbol) REFERENCES NyseCompanyInfo(symbol),"
    " FOREIGN KEY (ownerEmail) REFERENCES Users(email) ON DELETE CASCADE ON UPDATE CASCADE,"
    " FOREIGN KEY (nyseSymbol,timestampOfStock) REFERENCES NysePricesByTheMinute(symbol,timestamp)"
    ") ENGINE=InnoDB;")

SCRIPTS= {}
SCRIPTS['CreateRoles'] = (
    "INSERT INTO Roles (role,description) VALUES "
    "('admin','website administrator'),('user','a standard user');")
SCRIPTS['InsertAdmins'] = (
    "INSERT into Users ("
    "email,name,accountBalance,password) "
    "VALUES ('bellmj@g.cofc.edu','Matt Bell',1000.00,'password');")
SCRIPTS['InsertAdminsRoles'] = (
    "INSERT INTO UserRoles (email,userRole) "
    "VALUES ('bellmj@g.cofc.edu','admin');")
SCRIPTS['portfolioHoldingsView'] = (#create portfilo Holdings View
    "CREATE VIEW portfolioHoldingsView AS"
    "(SELECT nyseSymbol AS symbol,ownerEmail,timestampWhenBought,timestampOfStock,quantityHeld FROM NysePortfolioHoldings) UNION"
    "(SELECT * FROM NasdaqPortfolioHoldings);")
SCRIPTS['companyInfoView'] = (#create portfilo Holdings View
    "CREATE VIEW companyInfoView AS"
    "(SELECT symbol,name,sector,industry FROM NyseCompanyInfo) UNION"
    "(SELECT * FROM NasdaqCompanyInfo);")
SCRIPTS['AddUserRoleTrigger'] = (
    "CREATE TRIGGER AddUserRoleTrigger AFTER INSERT ON Users"
    " FOR EACH ROW"
    " INSERT INTO UserRoles(email,userRole)"
    " VALUES (NEW.email,'user');")
SCRIPTS['stockPricesByTheMinute'] = (
    "CREATE VIEW stockPricesByTheMinute AS"
    " (SELECT * FROM NysePricesByTheMinute) UNION"
    " (SELECT * FROM NasdaqPricesByTheMinute);")
SCRIPTS['getPrice'] =(
    " CREATE FUNCTION getPrice(timme TIMESTAMP,givenSymbol VARCHAR(255)) RETURNS decimal(14,2)"
        " BEGIN"
            " DECLARE stockPrice DECIMAL(14,2);"
            " SET stockPrice = (SELECT MAX(close) FROM stockPricesByTheMinute WHERE symbol = givenSymbol AND timestamp = timme);"
            " return stockPrice;"
        " END;")
SCRIPTS['symbolInNasdaq'] =(
    " CREATE FUNCTION symbolInNasdaq (givenSymbol VARCHAR(255)) RETURNS BOOLEAN"
        " BEGIN"
            " DECLARE symbolExists BOOLEAN;"
            " SET symbolExists = (1=(SELECT COUNT(symbol) FROM NasdaqCompanyInfo WHERE symbol = givenSymbol));"
            " return symbolExists;"
        " END;")
SCRIPTS['getOldestPortfolioHolding'] =(
    " CREATE FUNCTION getOldestPortfolioHoldingTimestamp (givenSymbol VARCHAR(255)) RETURNS TIMESTAMP"
        " BEGIN"
            " DECLARE oldestTimeStamp TIMESTAMP;"
            " SET oldestTimeStamp = (SELECT timestampWhenBought FROM portfolioHoldingsView WHERE symbol = givenSymbol ORDER BY timestampWhenBought  ASC LIMIT 1);"
            " return oldestTimeStamp;"
        " END;")
SCRIPTS['symbolInNyse'] =(
    " CREATE FUNCTION symbolInNyse (givenSymbol VARCHAR(255)) RETURNS BOOLEAN"
        " BEGIN"
            " DECLARE symbolExists BOOLEAN;"
            " SET symbolExists = (1=(SELECT COUNT(symbol) FROM NyseCompanyInfo WHERE symbol = givenSymbol));"
            " return symbolExists;"
        " END;")
SCRIPTS['getNumberOfStockHeld'] = (
    "CREATE FUNCTION getNumberOfStockHeld (givenSymbol VARCHAR(255),email VARCHAR(255)) RETURNS INT"
    " BEGIN"
    " return (SELECT s1.quantityHeld FROM simpleUserHoldings as s1 WHERE s1.symbol = givenSymbol AND s1.ownerEmail = email LIMIT 1);"
    " END;")
SCRIPTS['userHoldings'] = (
    "CREATE VIEW userHoldings AS SELECT ownerEmail,symbol,quantityHeld,timestampWhenBought,getPrice(timestampOfStock,symbol)"
    " AS pricePerShare,(quantityHeld*getPrice(timestampOfStock,symbol)) AS amountPaid"
    " FROM portfolioholdingsview;")
SCRIPTS['simpleUserHoldings'] = (
    "CREATE VIEW simpleUserHoldings AS SELECT ownerEmail,symbol,SUM(quantityHeld) AS quantityHeld,AVG(getPrice(timestampOfStock,symbol))"
    " AS avgPricePerShare"
    " FROM portfolioholdingsview"
    " GROUP BY ownerEmail,symbol")
SCRIPTS['sellStockProcedure'] = (
    "CREATE PROCEDURE sellStock(email VARCHAR(255),symbolToSell VARCHAR(16),sharesToSell INT)"
        " BEGIN"
        	" DECLARE numberOfStockHeld Int;"
            " DECLARE latestStockPrice DECIMAL(14,2);"
            " DECLARE amountToDeductFromAccount DECIMAL(14,2);"
            " DECLARE cursorTimestampWhenBought TIMESTAMP;"
            " DECLARE cursorQuantityHeld Int;"
            " DECLARE stock_holdings_cursor CURSOR FOR (SELECT timestampWhenBought,quantityHeld FROM portfolioHoldingsView AS phv WHERE phv.symbol = symbolToSell AND phv.ownerEmail = email);"
            " DECLARE CONTINUE HANDLER FOR NOT FOUND "
                "BEGIN "
                    "SELECT 1 INTO @handler_invoked FROM (SELECT 1) AS t; "
                "END;"
            " START TRANSACTION;"
        	" SET numberOfStockHeld = (SELECT s1.quantityHeld FROM simpleUserHoldings as s1 WHERE s1.symbol = symbolToSell AND s1.ownerEmail = email LIMIT 1);"
        	" SET latestStockPrice = (SELECT close FROM stockPricesByTheMinute WHERE symbol = symbolToSell ORDER BY TIMESTAMP DESC LIMIT 1);"
            " SET amountToDeductFromAccount = latestStockPrice * sharesToSell;"
            # " SELECT latestStockPrice,sharesToSell,amountToDeductFromAccount;"#I'm not quite sure what this line is doing
        	" IF (numberOfStockHeld >= sharesToSell AND symbolInNyse(symbolToSell)) THEN"
        		" OPEN stock_holdings_cursor;"
        		" update_holdings: LOOP"
        			" FETCH stock_holdings_cursor INTO cursorTimestampWhenBought,cursorQuantityHeld;"
                    " if sharesToSell >= cursorQuantityHeld THEN"
        				" DELETE FROM NysePortfolioHoldings"
        					" WHERE nyseSymbol = symbolToSell"
                            " AND ownerEmail = email"
                            " AND timestampWhenBought = cursorTimestampWhenBought;"
                        " SET sharesToSell = sharesToSell - cursorQuantityHeld;"
                    " ELSE "
        				" UPDATE NysePortfolioHoldings SET quantityHeld = quantityHeld - sharesToSell"
        					" WHERE nyseSymbol = symbolToSell AND ownerEmail = email AND timestampWhenBought = cursorTimestampWhenBought;"
        				" LEAVE update_holdings;"
                    " END IF;"
        		" END LOOP update_holdings;"
        		" CLOSE stock_holdings_cursor;"
        	" ELSEIF (numberOfStockHeld >= sharesToSell AND symbolInNasdaq(symbolToSell)) THEN"
        		" OPEN stock_holdings_cursor;"
        		" update_holdings: LOOP"
        			" FETCH stock_holdings_cursor INTO cursorTimestampWhenBought,cursorQuantityHeld;"
                    " if sharesToSell >= cursorQuantityHeld THEN"
        				" DELETE FROM NasdaqPortfolioHoldings"
        					" WHERE nasdaqSymbol = symbolToSell"
                            " AND ownerEmail = email"
                            " AND timestampWhenBought = cursorTimestampWhenBought;"
                        " SET sharesToSell = sharesToSell - cursorQuantityHeld;"
                    " ELSE"
        				" UPDATE NasdaqPortfolioHoldings SET quantityHeld = quantityHeld - sharesToSell"
        					" WHERE nasdaqSymbol = symbolToSell AND ownerEmail = email AND timestampWhenBought = cursorTimestampWhenBought;"
        				" LEAVE update_holdings;"
                    " END IF;"
        		" END LOOP update_holdings;"
        		" CLOSE stock_holdings_cursor;"
        	" ELSE"
        		" SET amountToDeductFromAccount = 0;"
        	" END IF;"
            " UPDATE Users SET Users.accountBalance = Users.accountBalance + amountToDeductFromAccount WHERE Users.email = email;"
            " COMMIT;"
        " END")



def main():
    #============================================================================================================
    #===TODO===TODO=================TODO===TODO=================TODO===TODO==========TODO===TODO=================
    #=TODO===TODO=================TODO===TODO=================TODO===TODO==========TODO===TODO===================
    #TODO===TODO=================TODO===TODO=================TODO===TODO==========TODO===TODO====================
    cnx = mysql.connector.connect(user='root', password='password', host='127.0.0.1')#This needs to be reconfigured for each new server
    #TODO===TODO=================TODO===TODO=================TODO===TODO==========TODO===TODO====================
    #=TODO===TODO=================TODO===TODO=================TODO===TODO==========TODO===TODO===================
    #===TODO===TODO=================TODO===TODO=================TODO===TODO==========TODO===TODO=================
    #============================================================================================================
    cursor = cnx.cursor()
    #connecting to stockSim database if It doesn't exist then create the database
    try:
        cnx.database = DB_NAME
    except mysql.connector.Error as err:
        if err.errno == errorcode.ER_BAD_DB_ERROR:
            create_database(cursor)
            cnx.database = DB_NAME
        else:
            print(err)
            exit(1)
    ##for each table in TABLES try to create the table if it doesn't exist
    for name, ddl in TABLES.items():
        try:
            print("Creating table {}: ".format(name), end='')

            cursor.execute(ddl)
        except mysql.connector.Error as err:
            if err.errno == errorcode.ER_TABLE_EXISTS_ERROR:
                print("already exists.")
            else:
                print(err.msg)
        else:
            print("OK")
    try:
        cursor.execute(getNasdaqInsertDDLFromFile())
    except mysql.connector.Error as err:
        print(err.msg)
    else:
        print("Inserted New NASDAQ Data")
    try:
        cursor.execute(getNYSEInsertDDLFromFile())
    except mysql.connector.Error as err:
        print(err.msg)
    else:
        print("Inserted New NYSE Data")
    for name, ddl in SCRIPTS.items():
        try:
            print("Running SQL Script {}: ".format(name), end='')
            cursor.execute(ddl)
        except mysql.connector.Error as err:
                print(err.msg)
        else:
            print("OK")
    cnx.commit()
    cursor.close()
    #close the connector
    cnx.close()
#this method reads the Nasdaqcompanylist.csv file in ../data and returns a String that is a sql command to insert the symbol
#name sector and industry of each tuple into the nasdaqCompanyInfo table
def getNasdaqInsertDDLFromFile():
    returnQuerry = "INSERT INTO nasdaqCompanyInfo (symbol,name,sector,industry) VALUES"
    readFile = open("../data/Nasdaqcompanylist.csv", 'r')
    for line in readFile:
        tupleList = line.split(',')
        if(tupleList[0]=="Symbol"):
            #were at the head of the file
            continue
        sector = "," + (("\'" + tupleList[6] + "\'") if tupleList[6] != "n/a" else "NULL")
        industry = "," + (("\'" + tupleList[7] + "\'") if tupleList[7] != "n/a" else "NULL")
        returnQuerry += "(\'" + tupleList[0] + "\',\'" + tupleList[1].replace(';',',') + "\'" + sector + industry + "),"
    returnQuerry = returnQuerry[0:len(returnQuerry)-1] + ";"
    return returnQuerry
#this method reads the NYSEcompanylist.csv file in ../data and returns a String that is a sql command to insert the symbol
#name sector and industry of each tuple into the nyseCompanyInfo table
def getNYSEInsertDDLFromFile():
    returnDDL = "INSERT INTO nyseCompanyInfo (symbol,name,sector,industry) VALUES"
    readFile = open("../data/NYSEcompanylist.csv", 'r')
    for line in readFile:
        tupleList = line.split(',')
        if(tupleList[0]=="Symbol"):
            #were at the head of the file
            continue
        sector = "," + (("\'" + tupleList[6] + "\'") if tupleList[6] != "n/a" else "NULL")
        industry = "," + (("\'" + tupleList[7] + "\'") if tupleList[7] != "n/a" else "NULL")
        returnDDL += "(\'" + tupleList[0] + "\',\'" + tupleList[1].replace("\"","") + "\'" + sector + industry + "),"
    returnDDL = returnDDL[0:len(returnDDL)-1] + ";"
    return returnDDL


def create_database(cursor):
    try:
        cursor.execute(
            "CREATE DATABASE {} DEFAULT CHARACTER SET 'utf8'".format(DB_NAME))
    except mysql.connector.Error as err:
        print("Failed creating database: {}".format(err))
        exit(1)

main()
