/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bellcsci332.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import bellcsci332.business.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.CallableStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.DatatypeConverter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Matthew Bell
 */
public class DBUtil {

    public static final String NAME_REGEX = "([a-z]|[A-Z]|'|-)+ ([a-z]|[A-Z]|'|-)+";
    public static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";//Taken from http://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
    private static final String ALPHA_VANTAGE_API_KEY = "IRW9";
    //^^Please modify this field if you plan to build this project yourself

    /**
     * a method to obtain a user from the database using their email
     *
     * @param email the email of the user to obtain
     * @return A User object that represents the user in the database; null if
     * this user does not exist
     */
    public static User getUser(String email) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM Users "
                + "WHERE email = ?";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, email);
            rs = ps.executeQuery();
            User user = null;
            if (rs.next()) {
                user = new User();
                user.setEmail(rs.getString("email"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setAccountBalance(rs.getBigDecimal("accountBalance"));
            }
            return user;
        } catch (SQLException e) {
            System.err.println(e);
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            pool.freeConnection(connection);
        }
    }

    public static List<User> getUsers() {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM Users;";
        try {
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();
            List<User> userList = new ArrayList<>();
            while (rs.next()) {
                User user = new User();
                user.setEmail(rs.getString("email"));
                user.setName(rs.getString("name"));
                user.setAccountBalance(rs.getBigDecimal("accountBalance"));
                userList.add(user);
            }
            return userList;
        } catch (SQLException e) {
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            pool.freeConnection(connection);
        }
    }

    public static UserRole getUserRole(String email) {
        //TODO implement this method
        return null;
    }

    public static void addUser(User newUser) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;

        //generating a random secure 32 byte salt for each user
        byte[] randomSaltByteArray = new byte[32];
        new SecureRandom().nextBytes(randomSaltByteArray);
        String salt = DatatypeConverter.printHexBinary(randomSaltByteArray);

        //hashing users pass with the new generated salt to store in the db
        newUser.setPassword(salt + newUser.getPassword() + salt);
        MessageDigest digest;
        boolean passwordHasBeenHashed = false;
        try {
            digest = MessageDigest.getInstance("SHA-512");
            byte[] hashedPassByteArray = digest.digest(newUser.getPassword().getBytes("UTF-8"));
            newUser.setPassword(DatatypeConverter.printHexBinary(hashedPassByteArray));
            passwordHasBeenHashed = true;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        if (!passwordHasBeenHashed) {
            return;
        }

        String query = "INSERT INTO Users(email, name, accountBalance, password, salt) "
                + "VALUES(?,?,?,?,?)";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, newUser.getEmail());
            ps.setString(2, newUser.getName());
            ps.setBigDecimal(3, newUser.getAccountBalance());
            ps.setString(4, newUser.getPassword());
            ps.setString(5, salt);//inserting our secure random salt into db
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            pool.freeConnection(connection);
        }
    }

    public static List<CompanyInfo> getNyseCompaniesInfo() {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM NyseCompanyInfo;";
        try {
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();
            List<CompanyInfo> companyInfoList = new ArrayList<>();
            while (rs.next()) {
                CompanyInfo nyseCompany = new CompanyInfo();
                nyseCompany.setSymbol(rs.getString("symbol"));
                nyseCompany.setName(rs.getString("name"));
                nyseCompany.setSector(rs.getString("sector"));
                nyseCompany.setIndustry(rs.getString("industry"));
                companyInfoList.add(nyseCompany);

            }
            return companyInfoList;
        } catch (SQLException e) {
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            pool.freeConnection(connection);
        }
    }

    public static List<CompanyInfo> getNasdaqCompaniesInfo() {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM NasdaqCompanyInfo;";
        try {
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();
            List<CompanyInfo> companyInfoList = new ArrayList<>();
            while (rs.next()) {
                CompanyInfo nasdaqCompany = new CompanyInfo();
                nasdaqCompany.setSymbol(rs.getString("symbol"));
                nasdaqCompany.setName(rs.getString("name"));
                nasdaqCompany.setSector(rs.getString("sector"));
                nasdaqCompany.setIndustry(rs.getString("industry"));
                companyInfoList.add(nasdaqCompany);

            }
            return companyInfoList;
        } catch (SQLException e) {
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            pool.freeConnection(connection);
        }
    }

    public static List<String> getAllTickerSymbols() {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT symbol FROM companyInfoView;";
        try {
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();
            List<String> symbolList = new ArrayList<>();
            while (rs.next()) {
                symbolList.add(rs.getString("symbol"));
            }
            return symbolList;
        } catch (SQLException e) {
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            pool.freeConnection(connection);
        }
    }

    public static boolean symbolInNasdaq(String symbol) {
        List<CompanyInfo> nasdaqCompanyInfoList = getNasdaqCompaniesInfo();
        for (CompanyInfo c : nasdaqCompanyInfoList) {
            if (c.getSymbol().equals(symbol)) {
                return true;
            }
        }
        return false;
    }

    public static boolean symbolInNyse(String symbol) {
        List<CompanyInfo> nyseCompanyInfoList = getNyseCompaniesInfo();
        for (CompanyInfo c : nyseCompanyInfoList) {
            if (c.getSymbol().equals(symbol)) {
                return true;
            }
        }
        return false;
    }

    public static boolean symbolIsValid(String symbol) {
        return symbolInNasdaq(symbol) || symbolInNyse(symbol);
    }

    public static List<PortfolioHolding> getUsersHoldings(String email) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM portfolioHoldingsView WHERE "
                + "ownerEmail = ?;";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, email);
            rs = ps.executeQuery();
            List<PortfolioHolding> userHoldingsList = new ArrayList<>();
            while (rs.next()) {
                PortfolioHolding userHolding = new PortfolioHolding();
                userHolding.setOwnerEmail(rs.getString("ownerEmail"));
                userHolding.setQuantityHeld(rs.getInt("quantityHeld"));
                userHolding.setSymbol(rs.getString("symbol"));
                userHolding.setTimeStamp(rs.getTimestamp("timestampOfStock"));
                userHoldingsList.add(userHolding);
            }
            return userHoldingsList;
        } catch (SQLException e) {
            Logger.getLogger(DBUtil.class.getName()).log(Level.INFO, null, e);
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            pool.freeConnection(connection);
        }
    }

    public static Portfolio getPortfolio(String email) {
        Portfolio returnPortfolio = new Portfolio();
        returnPortfolio.setOwnerEmail(email);
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM simpleUserHoldings"
                + " WHERE ownerEmail = ?;";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, email);
            rs = ps.executeQuery();
            while (rs.next()) {
                SimplePortfolioHolding newHolding = new SimplePortfolioHolding();
                newHolding.setSymbolOwned(rs.getString("symbol"));
                newHolding.setQuantityHeld(rs.getInt("quantityHeld"));
                newHolding.setAveragePricePerShare(rs.getBigDecimal("avgPricePerShare"));
                returnPortfolio.addPortfolioHolding(newHolding);
            }
            return returnPortfolio;
        } catch (SQLException e) {
            Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, e);
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            pool.freeConnection(connection);
        }
    }

    public static List<StockPrice> getPriceList(String symbol) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM stockPricesByTheMinute "
                + "WHERE symbol = ?"
                + "ORDER BY timestamp DESC;";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, symbol);
            rs = ps.executeQuery();
            List<StockPrice> priceList = new ArrayList<>();
            while (rs.next()) {
                StockPrice stockPrice = new StockPrice();
                stockPrice.setSymbol(symbol);
                stockPrice.setTimeStamp(rs.getTimestamp("timestamp"));
                stockPrice.setHigh(rs.getBigDecimal("high"));
                stockPrice.setLow(rs.getBigDecimal("low"));
                stockPrice.setOpen(rs.getBigDecimal("open"));
                stockPrice.setClose(rs.getBigDecimal("close"));
                stockPrice.setVolume(rs.getInt("volume"));
                priceList.add(stockPrice);
            }
            return priceList;
        } catch (SQLException e) {
            Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, "Mysql Exception", e);
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            pool.freeConnection(connection);
        }
    }

    /**
     * Method to add a batch of StockPrices to the Database. These prices must
     * be from the same api call and all have the same symbol
     *
     * @param stockPrices List of Stock Prices of the same symbol to add
     */
    public static void addStockPrices(List<StockPrice> stockPrices) {
        if (stockPrices.size() == 0) {
            return;
        }
        if (symbolInNyse(stockPrices.get(0).getSymbol())) {
            ConnectionPool pool = ConnectionPool.getInstance();
            Connection connection = pool.getConnection();
            PreparedStatement ps = null;

            String query = "INSERT INTO NysePricesByTheMinute(symbol,timestamp,high,low,open,close,volume) "
                    + "VALUES(?,?,?,?,?,?,?)";

            try {
                ps = connection.prepareStatement(query);
                for (StockPrice stockPrice : stockPrices) {
                    ps.setString(1, stockPrice.getSymbol());
                    ps.setTimestamp(2, stockPrice.getTimeStamp());
                    ps.setBigDecimal(3, stockPrice.getHigh());
                    ps.setBigDecimal(4, stockPrice.getLow());
                    ps.setBigDecimal(5, stockPrice.getOpen());
                    ps.setBigDecimal(6, stockPrice.getClose());
                    ps.setInt(7, stockPrice.getVolume());
                    ps.addBatch();
                }

                ps.executeBatch();

            } catch (SQLException e) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, e);
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
                pool.freeConnection(connection);
            }
        } else if (symbolInNasdaq(stockPrices.get(0).getSymbol())) {
            //todo add price to NasdaqPricesByTheMinute
            ConnectionPool pool = ConnectionPool.getInstance();
            Connection connection = pool.getConnection();
            PreparedStatement ps = null;

            String query = "INSERT INTO NasdaqPricesByTheMinute(symbol,timestamp,high,low,open,close,volume) "
                    + "VALUES(?,?,?,?,?,?,?)";
            try {
                ps = connection.prepareStatement(query);
                for (StockPrice stockPrice : stockPrices) {
                    ps.setString(1, stockPrice.getSymbol());
                    ps.setTimestamp(2, stockPrice.getTimeStamp());
                    ps.setBigDecimal(3, stockPrice.getHigh());
                    ps.setBigDecimal(4, stockPrice.getLow());
                    ps.setBigDecimal(5, stockPrice.getOpen());
                    ps.setBigDecimal(6, stockPrice.getClose());
                    ps.setInt(7, stockPrice.getVolume());
                    ps.addBatch();
                }

                ps.executeBatch();
            } catch (SQLException e) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, e);
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
                pool.freeConnection(connection);
            }
        }

    }

    public static void addStockPrice(StockPrice stockPrice) {
        if (symbolInNyse(stockPrice.getSymbol())) {
            ConnectionPool pool = ConnectionPool.getInstance();
            Connection connection = pool.getConnection();
            PreparedStatement ps = null;

            String query = "INSERT INTO NysePricesByTheMinute(symbol,timestamp,high,low,open,close,volume) "
                    + "VALUES(?,?,?,?,?,?,?)";
            try {
                ps = connection.prepareStatement(query);
                ps.setString(1, stockPrice.getSymbol());
                ps.setTimestamp(2, stockPrice.getTimeStamp());
                ps.setBigDecimal(3, stockPrice.getHigh());
                ps.setBigDecimal(4, stockPrice.getLow());
                ps.setBigDecimal(5, stockPrice.getOpen());
                ps.setBigDecimal(6, stockPrice.getClose());
                ps.setInt(7, stockPrice.getVolume());

                ps.executeUpdate();
            } catch (SQLException e) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, e);
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
                pool.freeConnection(connection);
            }
        } else if (symbolInNasdaq(stockPrice.getSymbol())) {
            //todo add price to NasdaqPricesByTheMinute
            ConnectionPool pool = ConnectionPool.getInstance();
            Connection connection = pool.getConnection();
            PreparedStatement ps = null;

            String query = "INSERT INTO NasdaqPricesByTheMinute(symbol,timestamp,high,low,open,close,volume) "
                    + "VALUES(?,?,?,?,?,?,?)";
            try {
                ps = connection.prepareStatement(query);
                ps.setString(1, stockPrice.getSymbol());
                ps.setTimestamp(2, stockPrice.getTimeStamp());
                ps.setBigDecimal(3, stockPrice.getHigh());
                ps.setBigDecimal(4, stockPrice.getLow());
                ps.setBigDecimal(5, stockPrice.getOpen());
                ps.setBigDecimal(6, stockPrice.getClose());
                ps.setInt(7, stockPrice.getVolume());

                ps.executeUpdate();
            } catch (SQLException e) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, e);
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
                pool.freeConnection(connection);
            }
        }

    }

    public static List<StockPrice> getStockInfoFromAPI(String symbol) {

        StringBuilder strBuf = new StringBuilder();

        HttpsURLConnection conn = null;

        BufferedReader reader = null;
        try {
            //Declare the connection to weather api url
            URL url = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" 
                    + symbol + "&interval=1min&outputsize=compact&apikey=" + ALPHA_VANTAGE_API_KEY);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP GET Request Failed with Error code : "
                        + conn.getResponseCode());
            }

            //Read the content from the defined connection
            //Using IO Stream with Buffer raise highly the efficiency of IO
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String output = null;
            while ((output = reader.readLine()) != null) {
                strBuf.append(output);
            }
        } catch (MalformedURLException e) {
            Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, e);
        } catch (UnknownHostException e){
            Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, e);
        }catch (IOException e) {
            Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, e);
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

        //Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, strBuf.toString());
        JSONParser parser = new JSONParser();
        Object obj;
        List<StockPrice> returnList = new ArrayList<>();
        try {
            obj = parser.parse(strBuf.toString());
            JSONObject jo = (JSONObject) obj;
            JSONObject timeObject = (JSONObject) jo.get("Time Series (1min)");
            if (timeObject == null) {//trying to prevent a method call on a null
                //time object
                return null;
            }
            Object[] keyArray = timeObject.keySet().toArray();
            for (Object k : keyArray) {
                String key = (String) k;
                StockPrice stockPrice = new StockPrice();
                stockPrice.setSymbol(symbol);
                //Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, Timestamp.valueOf(key).toString());
                stockPrice.setTimeStamp(Timestamp.valueOf(key));
                JSONObject priceObject = (JSONObject) timeObject.get(key);

                String openStr = (String) priceObject.get("1. open");
                String highStr = (String) priceObject.get("2. high");
                String lowStr = (String) priceObject.get("3. low");
                String closeStr = (String) priceObject.get("4. close");
                String volumeStr = (String) priceObject.get("5. volume");

                BigDecimal open = new BigDecimal(openStr == null ? "0" : openStr);
                BigDecimal high = new BigDecimal(highStr == null ? "0" : highStr);
                BigDecimal low = new BigDecimal(lowStr == null ? "0" : lowStr);
                BigDecimal close = new BigDecimal(closeStr == null ? "0" : closeStr);
                Integer volume = Integer.parseInt(volumeStr == null ? "0" : volumeStr);
                stockPrice.setOpen(open);
                stockPrice.setClose(close);
                stockPrice.setLow(low);
                stockPrice.setHigh(high);
                stockPrice.setVolume(volume);
                returnList.add(stockPrice);
            }
        } catch (ParseException ex) {
            Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return returnList;
    }

    public static StockPrice getLatestStockPrice(String symbol) {
        //TODO check the DB to see if an api call is necessary
        StockPrice returnPrice = null;
        if (symbolIsValid(symbol)) {
            if (!symbolIsCurrent(symbol)) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.INFO, "getLatestStockPrice called for symbol " + symbol + " symbol is not current");
                long startTime = System.nanoTime();
                List<StockPrice> apiPriceList;
                apiPriceList = getStockInfoFromAPI(symbol);
                if(apiPriceList.size() == 0){
                    return null;
                }
                int apiCallCount = 1;
                while (apiPriceList == null) {//check if we actually got data back
                    Logger.getLogger(DBUtil.class.getName()).log(Level.WARNING, "API call failed for " + symbol + ". retrying");
                    apiPriceList = getStockInfoFromAPI(symbol);//retry
                    apiCallCount++;
                    try {
                        Thread.sleep(200);//wait 200ms before retrying
                    } catch (InterruptedException ex) {
                        Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (apiCallCount >= 5) {//retry call 4 times then return null
                        Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, "API call failed for " + symbol + " five times giving up");
                        return null;//not sure what issues this will cause TODO
                    }
                }

                Logger.getLogger(DBUtil.class.getName()).log(Level.INFO, "Time to call api:" + ((System.nanoTime() - startTime) / 1000000000.0));
                Collections.sort(apiPriceList);
                Collections.reverse(apiPriceList);
                startTime = System.nanoTime();
                List<StockPrice> dataBaseList = getPriceList(symbol);
                Logger.getLogger(DBUtil.class.getName()).log(Level.INFO, "Time to call DB:" + ((System.nanoTime() - startTime) / 1000000000.0));
                returnPrice = apiPriceList.get(0);
                List<Timestamp> databaseTimeStampList = new ArrayList<>();
                for (StockPrice databasePrice : dataBaseList) {
                    databaseTimeStampList.add(databasePrice.getTimeStamp());
                }
                List<StockPrice> stocksToAddToDB = new ArrayList<>();
                startTime = System.nanoTime();
                for (StockPrice apiPrice : apiPriceList) {
                    if (dataBaseList == null || !databaseTimeStampList.contains(apiPrice.getTimeStamp())) {
                        stocksToAddToDB.add(apiPrice);
                    } else {
                        break;
                    }

                }
                DBUtil.addStockPrices(stocksToAddToDB);
                Logger.getLogger(DBUtil.class.getName()).log(Level.INFO, "Time to add stocks:" + ((System.nanoTime() - startTime) / 1000000000.0));
            } else {
                Logger.getLogger(DBUtil.class.getName()).log(Level.INFO, "getLatestStockPrice called for symbol " + symbol + " symbol is current");
                returnPrice = getPriceList(symbol).get(0);
            }
        }
        return returnPrice;
    }

    public static boolean buyStock(String userEmail, int quantityToBuy, String symbol) {
        StockPrice latestStockPrice = DBUtil.getLatestStockPrice(symbol);
        User buyer = DBUtil.getUser(userEmail);
        BigDecimal newAccountBalance = buyer.getAccountBalance().subtract(
                latestStockPrice.getClose().multiply(new BigDecimal(quantityToBuy)));
        if (newAccountBalance.compareTo(BigDecimal.ZERO) < 0 || latestStockPrice == null) {
            //add a more graceful failure mode here and let the user know what goign on
            return false;
        }
        buyer.setAccountBalance(newAccountBalance);
        updateUser(buyer);
        PortfolioHolding newHolding = new PortfolioHolding();
        newHolding.setOwnerEmail(userEmail);
        newHolding.setQuantityHeld(quantityToBuy);
        newHolding.setSymbol(symbol);
        newHolding.setTimeStamp(latestStockPrice.getTimeStamp());
        addUserHolding(newHolding);
        return true;
    }

    public static boolean sellStock(String userEmail, String symbolToSell, int quantityToSell) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        CallableStatement stmt = null;
        try {
            stmt = connection.prepareCall("{CALL sellStock(?,?,?)}");
            stmt.setString(1, userEmail);
            stmt.setString(2, symbolToSell);
            stmt.setInt(3, quantityToSell);
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
            pool.freeConnection(connection);
        }
        return true;
    }

    public static void updateUser(User user) { //todo update this method to use salt correctly
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;

        String query = "UPDATE Users SET name = ?,accountBalance = ?,password = ?"
                + "WHERE email = ?;";
        try {
            ps = connection.prepareStatement(query);

            ps.setString(1, user.getName());
            ps.setBigDecimal(2, user.getAccountBalance());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            pool.freeConnection(connection);
        }
    }

    private static void addUserHolding(PortfolioHolding holding) {
        //TODO allow users to buy more of stock that they already own some of with the same time stamp
        String table = "";
        String symbolName = "";
        if (DBUtil.symbolInNasdaq(holding.getSymbol())) {
            table = "NasdaqPortfolioHoldings";
            symbolName = "nasdaqSymbol";
        } else if (DBUtil.symbolInNyse(holding.getSymbol())) {
            table = "NysePortfolioHoldings";
            symbolName = "nyseSymbol";
        } else {
            return;
        }
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;

        String query = "INSERT INTO " + table + " (" + symbolName + ",ownerEmail,timestampOfStock,quantityHeld) "
                + "VALUES(?,?,?,?)";
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, holding.getSymbol());
            ps.setString(2, holding.getOwnerEmail());
            ps.setTimestamp(3, holding.getTimeStamp());
            ps.setInt(4, holding.getQuantityHeld());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            pool.freeConnection(connection);
        }
    }

    public static int getNumberOfHoldingsForStock(String symbol, String ownerEmail) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement stmt = null;
        int returnVal = 0;
        try {
            stmt = connection.prepareStatement("SELECT getNumberOfStockHeld(?,?)");
            stmt.setString(1, symbol);
            stmt.setString(2, ownerEmail);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                returnVal = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            pool.freeConnection(connection);
        }
        return returnVal;
    }

    /**
     * queries the database and determines if an API call is necessary in order
     * to use the most up to date data
     *
     * @param symbol
     * @return
     */
    public static boolean symbolIsCurrent(String symbol) {
        List<StockPrice> priceList = getPriceList(symbol);
        if (priceList.size() == 0) {
            Logger.getLogger(DBUtil.class.getName()).log(Level.INFO, null, "Symbol " + symbol + " not current;price list empty");
            return false;
        }
        if (symbolIsValid(symbol)) {
            GregorianCalendar now = new GregorianCalendar();
            now.setTimeInMillis(System.currentTimeMillis());
            now.setTimeZone(TimeZone.getTimeZone("America/New_York"));
            if (marketsAreTypicallyOpen()) {
                GregorianCalendar latestStockInDB = new GregorianCalendar();
                latestStockInDB.setTimeInMillis(priceList.get(0).getTimeStamp().getTime());
                int stockYear = latestStockInDB.get(Calendar.YEAR);
                int stockMonth = latestStockInDB.get(Calendar.MONTH);
                int stockDay = latestStockInDB.get(Calendar.DATE);
                int stockHour = latestStockInDB.get(Calendar.HOUR_OF_DAY);
                int stockMinute = latestStockInDB.get(Calendar.MINUTE);
                if (stockYear == now.get(Calendar.YEAR) //if the latest stock price is from within the minute
                        && stockMonth == now.get(Calendar.MONTH) && stockDay == now.get(Calendar.DAY_OF_MONTH)
                        && stockHour == now.get(Calendar.HOUR_OF_DAY) && stockMinute == now.get(Calendar.MINUTE)) {
                    Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE,
                            null, "Symbol  " + symbol + " current; stock price "
                            + "is from within the minute");
                    return true;
                } else {
                    return false;
                }
            } else {//check to see if we have the price from 15:59 on the last day of trading

                GregorianCalendar latestStockInDB = new GregorianCalendar();
                GregorianCalendar latestPriceTime = new GregorianCalendar();
                latestPriceTime.setTimeInMillis(now.getTimeInMillis());
                latestPriceTime.setTimeZone(TimeZone.getTimeZone("America/New_York"));
                latestStockInDB.setTimeInMillis(priceList.get(0).getTimeStamp().getTime());

                switch (now.get(Calendar.DAY_OF_WEEK)) {
                    case Calendar.SUNDAY:
                        latestPriceTime.add(Calendar.DATE, -2);
                        return latestStockInDB.get(Calendar.DATE) == latestPriceTime.get(Calendar.DATE)//return if there is a stock price for 3:59
                                && latestStockInDB.get(Calendar.MONTH) == latestPriceTime.get(Calendar.MONTH)
                                && latestStockInDB.get(Calendar.YEAR) == latestPriceTime.get(Calendar.YEAR)
                                && (latestStockInDB.get(Calendar.HOUR_OF_DAY) == 16
                                    || (latestStockInDB.get(Calendar.HOUR_OF_DAY) == 15
                                        && latestStockInDB.get(Calendar.MINUTE) >= 58));
                    case Calendar.SATURDAY:
                        latestPriceTime.add(Calendar.DATE, -1);
                        return latestStockInDB.get(Calendar.DATE) == latestPriceTime.get(Calendar.DATE)
                                && latestStockInDB.get(Calendar.MONTH) == latestPriceTime.get(Calendar.MONTH)
                                && latestStockInDB.get(Calendar.YEAR) == latestPriceTime.get(Calendar.YEAR)
                                && (latestStockInDB.get(Calendar.HOUR_OF_DAY) == 16
                                    || (latestStockInDB.get(Calendar.HOUR_OF_DAY) == 15
                                        && latestStockInDB.get(Calendar.MINUTE) >= 58));
                    default:
                        //check to see if there is an entry from either 16:00 that day if it's after 16:00 or from 16:00 the previous day if it's before 9:30
                        if (now.get(Calendar.HOUR_OF_DAY) > 16 || now.get(Calendar.HOUR_OF_DAY) == 16 && now.get(Calendar.MINUTE) != 0) {
                            return latestStockInDB.get(Calendar.DATE) == now.get(Calendar.DATE)
                                    && latestStockInDB.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                                    && latestStockInDB.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                                    && (latestStockInDB.get(Calendar.HOUR_OF_DAY) == 16
                                        || (latestStockInDB.get(Calendar.HOUR_OF_DAY) == 15
                                            && latestStockInDB.get(Calendar.MINUTE) >= 58));
                        } else { // it's before 9:30
                            latestPriceTime.add(Calendar.DATE, -1);
                            return latestStockInDB.get(Calendar.DATE) == latestPriceTime.get(Calendar.DATE)
                                    && latestStockInDB.get(Calendar.MONTH) == latestPriceTime.get(Calendar.MONTH)
                                    && latestStockInDB.get(Calendar.YEAR) == latestPriceTime.get(Calendar.YEAR)
                                    && (latestStockInDB.get(Calendar.HOUR_OF_DAY) == 16
                                        || (latestStockInDB.get(Calendar.HOUR_OF_DAY) == 15
                                            && latestStockInDB.get(Calendar.MINUTE) >= 58));
                        }
                }
            }
        }
        return false;
    }

    /**
     * uses current system time to determine if the nyse & nasdaq are typically
     * open a this current time, disregarding holidays
     *
     * @return boolean value if the stock market is currently open, disregarding
     * holidays since these values change every year
     */
    public static boolean marketsAreTypicallyOpen() {
        GregorianCalendar now = new GregorianCalendar();
        now.setTimeInMillis(System.currentTimeMillis());
        now.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        int dayOfWeek = now.get(Calendar.DAY_OF_WEEK);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        Logger.getLogger(DBUtil.class.getName()).log(Level.INFO, "Current Time New York;" + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND));
        if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {
            Logger.getLogger(DBUtil.class.getName()).log(Level.INFO, "markets closed");
            return false;
        } else if (hour >= 16) {
            Logger.getLogger(DBUtil.class.getName()).log(Level.INFO, "markets closed");
            return false;
        } else if (hour < 9 || (hour == 9 && minute < 30)) {
            Logger.getLogger(DBUtil.class.getName()).log(Level.INFO, "markets closed");
            return false;

        }
        Logger.getLogger(DBUtil.class.getName()).log(Level.INFO, "markets open");
        return true;
    }
}
