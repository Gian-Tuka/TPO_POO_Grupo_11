package Farmared.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilDate {

    private static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    public static String parseDate(Date date){
        return format.format(date);
    }

    public static Date toDate(String fechaStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            return sdf.parse(fechaStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use dd/MM/yyyy");
        }
    }
}
