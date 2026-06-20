package Farmared.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilDate {

    private static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    public static String parseDate(Date date){
        return format.format(date);
    }

    public static Date stringToDate(String dateStr) {
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            // Manejo de error si el texto no tiene formato dd/MM/yyyy
            System.err.println("Error: El formato de fecha debe ser dd/MM/yyyy. " + e.getMessage());
            return null;
        }
    }
}
