# CSCI332
This project is a stock market simulator written with Java servlet and JSP with a mySQL Backend.

This web application includes a custom build realm for tomcat authentication. This allows salted sha512 passwords. This realm is included as SaltySHA512JDBCRealm.java in /src/; this must be built as a jar and included in $CATALINA_HOME/lib for this application to work.

A python script is included in /src/marketInfoCreateDatabaseScript.py that will stand up the inital database required for this application to work. It is required to modify the user, password, and host fields of the mySql connector in marketInfoCreateDatabaseScript.py for each new mySql server. These fields are flagged with comments. 

It is reccomended to use the above python file to stand up the inital DB, however it is possiable to recreate the scheemas using the included mySQL dump in /SQLDump/stockSimDUMP.sql.

The connectionURL and connectionPassword fields in stockSim/web/META-INF/context.xml must be updated to reflect the mySql server that you are using.

## Dependencies:
+ python mysql module
+ apache-tomcat-8.5.20
  + SaltySHA512JDBCRealm.java packaged as a jar and included in $CATALINA_HOME/lib
+ JSTL 1.2.1
+ MySQL JDBC Driver5.1.23
