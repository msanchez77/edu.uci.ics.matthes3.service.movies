package edu.uci.ics.matthes3.service.movies.utilities;

import edu.uci.ics.matthes3.service.movies.logger.ServiceLogger;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SQLData {

    public static String[] buildArrayFromSQL(ResultSet rs, String column){
        try {
            String column_str = rs.getString(column);
            if (column_str == null)
                return new String[0];
            else {
                String[] column_arr = column_str.split(",");
                return column_arr;
            }
        } catch (SQLException e) {
            ServiceLogger.LOGGER.warning("Error building " + column + " array from SQL");
            return new String[0];
        }
    }

    public static String getSubstringText(String s) {
        if (s == null) {
            return null;
        }
        else {
            return "%" +s + "%";
        }
    }


    public static Integer[] integer(String[] array) {
        Integer[] integers = new Integer[array.length];

        for (int i = 0; i < array.length; i++) {
            integers[i] = Integer.parseInt(array[i]);
        }

        return integers;
    }

    public static Float[] floatArr(String[] array) {
        Float[] floats = new Float[array.length];

        for (int i = 0; i < array.length; i++) {
            floats[i] = Float.parseFloat(array[i]);
        }

        return floats;
    }

    public static Date[] date(String[] array) {
        Date[] dates = new Date[array.length];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < array.length; i++) {
            try {
                java.util.Date util = sdf.parse(array[i]);
                dates[i] = new Date(util.getTime());
            } catch (ParseException e) {
                ServiceLogger.LOGGER.warning("Date: " + array[i] + " threw a parsing error.");
            }
        }

        return dates;
    }
}
