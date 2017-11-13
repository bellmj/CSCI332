import mysql.connector
from mysql.connector import errorcode
DB_NAME = 'stockSim'
TABLES = {}
TABLES['nasdaqCompanyInfo'] = (
    "CREATE TABLE `nasdaqCompanyInfo` ("
    "  `symbol` varchar(8) NOT NULL,"
    "  `name` varchar(255) NOT NULL,"
    "  `sector` varchar(255),"
    "  `industry` varchar(255),"
    "  PRIMARY KEY (`symbol`)"
    ") ENGINE=InnoDB")
TABLES['nyseCompanyInfo'] = (
    "CREATE TABLE `nyseCompanyInfo` ("
    "  `symbol` varchar(16) NOT NULL,"
    "  `name` varchar(255) NOT NULL,"
    "  `sector` varchar(255),"
    "  `industry` varchar(255),"
    "  PRIMARY KEY (`symbol`)"
    ") ENGINE=InnoDB")


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
        print("Inserted New Data")
    try:
        cursor.execute(getNYSEInsertDDLFromFile())
    except mysql.connector.Error as err:
        print(err.msg)
    else:
        print("Inserted New Data")
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
