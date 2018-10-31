package com.quickbase.devint;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This DBManager implementation provides a connection to the database containing population data.
 *
 * Created by ckeswani on 9/16/15.
 */
public class DBManagerImpl implements DBManager {

    // query to retrieve country and its population from the database
    private final static String SQL_QUERIES = "SELECT Country.CountryName, SUM(City.Population) AS Population" +
            " FROM Country, City, State" +
            " WHERE Country.CountryId = State.CountryId AND " +
            " State.StateId = City.StateId" +
            " GROUP BY Country.CountryName";

    /**
     * Creates a connection with the database.
     * @return connection instance of the connection made with the database. It exits the system
     *  if connection is null.
     */
    public Connection getConnection() {
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:resources/data/citystatecountry.db");
            if (null == c ) {
                System.out.println("failed.");
                System.exit(1);
            }
            System.out.println("Opened database successfully");
        } catch (ClassNotFoundException cnf) {
            System.out.println("could not load driver");
        } catch (SQLException sqle) {
            System.out.println("sql exception:" + sqle.getStackTrace());
        }
        return c;
    }

    /**
     * Retrieves the countryName and its population from the database.
     * @param connection connection to the database.
     * @return A map with the countryName as key and its population as its value
     * as present in the database.
     */
    public Map<String, Integer> getPopulationByCountry(Connection connection){
        Statement statement = null;
        ResultSet resultSet = null;
        Map<String, Integer> hashMap = new HashMap<>();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SQL_QUERIES);

            while (resultSet.next()) {
                hashMap.put(resultSet.getString("CountryName"), resultSet.getInt("Population"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try{
                assert resultSet != null;
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return hashMap;
    }

}
