# CSCI332
This project is a stock market simulator written with Java servlet and JSP with
a mySQL Backend.

This web application includes a custom build realm for tomcat authentication.
This allows salted sha512 passwords. This realm is included as
SaltySHA512JDBCRealm.java in /src/; this must be built as a jar and included in
$CATALINA_HOME/lib for this application to work.

A python script is included in /src/marketInfoCreateDatabaseScript.py that will
stand up the initial database required for this application to work. It is
required to modify the user, password, and host fields of the mySql connector
in marketInfoCreateDatabaseScript.py for each new mySql server. These fields are
flagged with comments.

It is recommended to use the above python file to stand up the initial DB,
however it is possible to recreate the schema using the included mySQL dump
in /SQLDump/stockSimDUMP.sql.

The connectionURL and connectionPassword fields in
stockSim/web/META-INF/context.xml must be updated to reflect the mySql server
that you are using.

This project uses a free API to get near real time stock information. If you
plan to use build this project for your own purposes please head over to
[Alpha Vantage](https://www.alphavantage.co/support/#api-key) and claim your
own free api key. Then reflect this change in
stockSim/src/java/bellcsci332/data/DBUtil.java's class variable
ALPHA_VANTAGE_API_KEY  

## Dependencies:
+ python mysql module
+ apache-tomcat-8.5.20
  + SaltySHA512JDBCRealm.java packaged as a jar and included in $CATALINA_HOME/lib
+ JSTL 1.2.1
+ MySQL JDBC Driver5.1.23
