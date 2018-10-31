package com.quickbase.devint;

import java.sql.Connection;
import java.util.Map;

/**
 * Created by ckeswani on 9/16/15.
 */
public interface DBManager {
    Connection getConnection();
    Map<String, Integer> getPopulationByCountry(Connection connection);
}
