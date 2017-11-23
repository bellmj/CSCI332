import mysql.connector
from mysql.connector import errorcode
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
    "  CONSTRAINT Valid_Email CHECK(email LIKE \"%_@%__.__%\"),"
    "  CONSTRAINT Acct_Bal_Not_Neg CHECK(accountBalance >= 0)"
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
    " quantityHeld INT NOT NULL,"
    " FOREIGN KEY (nasdaqSymbol) REFERENCES NasdaqCompanyInfo(symbol),"
    " FOREIGN KEY (ownerEmail) REFERENCES Users(email) ON DELETE CASCADE ON UPDATE CASCADE,"
    " FOREIGN KEY (nasdaqSymbol,timestampWhenBought) REFERENCES NasdaqPricesByTheMinute (symbol,timestamp)"
    ") ENGINE=InnoDB;")
TABLES['NysePortfolioHoldings'] = (
    "CREATE TABLE NysePortfolioHoldings("
    " nyseSymbol varchar(16) NOT NULL,"
    " ownerEmail varchar(255) NOT NULL,"
    " timestampWhenBought TIMESTAMP NOT NULL,"
    " quantityHeld INT NOT NULL,"
    " FOREIGN KEY (nyseSymbol) REFERENCES NyseCompanyInfo(symbol),"
    " FOREIGN KEY (ownerEmail) REFERENCES Users(email) ON DELETE CASCADE ON UPDATE CASCADE,"
    " FOREIGN KEY (nyseSymbol,timestampWhenBought) REFERENCES NysePricesByTheMinute(symbol,timestamp)"
    ") ENGINE=InnoDB;")

SCRIPTS= {}
SCRIPTS['CreateRoles'] = (
    "INSERT INTO Roles (role,description) VALUES "
    "('admin','website administrator'),('user','a standard user');")
SCRIPTS['InsertAdmins'] = (
    "INSERT into Users ("
    "email,name,accountBalance,password) "
    "VALUES ('bellmj@g.cofc.edu','Matt Bell',0.00,'password');")
SCRIPTS['InsertAdminsRoles'] = (
    "INSERT INTO UserRoles (email,userRole) "
    "VALUES ('bellmj@g.cofc.edu','admin');")


def main():
    cnx = mysql.connector.connect(user='root', password='password', host='127.0.0.1')
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
        returnDDL += "(\'" + tupleList[0] + "\',\'" + tupleList[1].replace(';',',') + "\'" + sector + industry + "),"
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
