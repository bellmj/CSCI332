# CSCI332
This project is a stock market simulator written with Java servlet and JSP with a mySQL Backend
It is recommended to execute the include python script to stand up the initial DB
This web application includes a custom build realm for tomcat authentication. This allows salted sha512 passwords. This realm is included as SaltySHA512JDBCRealm.java in /src; this must be built as a jar and included in $CATALINA_HOME/lib for this application to work
## Dependencies:
+ python mysql module
+ apache-tomcat-8.5.20
  + SaltySHA512JDBCRealm.java packaged as a jar and included in $CATALINA_HOME/lib
+ JSTL 1.2.1
+ MySQL JDBC Driver5.1.23
