package Farmared.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilDate {

    private static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    public static String parseDate(Date date){
        return format.format(date);
    }
}
