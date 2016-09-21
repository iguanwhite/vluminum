/*

 * Classe Utilitária referente a conversões de Data

* @author Henrique Frederico

* @version 1.0

* <objetivo>"Converter estruturas de data e hora em diferentes formatos." </objetivo>

* <data_criacao> "05/11/2012" </data_criacao>

* <data_ultima_alteracao> "05/11/2012" </data_ultima_alteracao>

* <requisito_afetado> "RF004-01" </requisito_afetado>

*/


package br.com.velp.vluminum.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public abstract class DateTimeUtils {

    public final static long SECOND_MILLIS = 1000;
    public final static long MINUTE_MILLIS = SECOND_MILLIS*60;
    public final static long HOUR_MILLIS = MINUTE_MILLIS*60;
    public final static long DAY_MILLIS = HOUR_MILLIS*24;
    public final static long YEAR_MILLIS = DAY_MILLIS*365;

    private static String formatoPadrao = "dd/MM/yyyy";
    private static String formatoCompleto = "yyyy-MM-dd HH:mm:ss";
    private static String formatoCompleto2 = "dd/MM/yyyy HH:mm:ss";
    private static String formatoCompleto3 = "dd/MM/yyyy HH:mm";
    private static String formatoHora = "HH:mm";

    public static int daysDiff(Date earlierDate, Date laterDate) {
        if (earlierDate == null || laterDate == null) {
            return 0;
        }

        return (int)((laterDate.getTime() / DAY_MILLIS) - (earlierDate.getTime() / DAY_MILLIS));
    }

    public static Date getDateHoursMinutesSeconds(int year, int month, int day,
                                                  int hours, int minutes, int seconds) {
        Calendar calendario = Calendar.getInstance();
        calendario.set(year, month - 1, day, hours, minutes, seconds);
        calendario.set(Calendar.MILLISECOND, 0);

        return calendario.getTime();
    }

    public static int getDay(Date dateTime) {
        return getDateTimeField(dateTime, Calendar.DAY_OF_MONTH);
    }

    public static int getMonth(Date dateTime) {
        return getDateTimeField(dateTime, Calendar.MONTH);
    }

    public static int getYear(Date dateTime) {
        return getDateTimeField(dateTime, Calendar.YEAR);
    }

    public static String converterStringParaStringSQL(String d) {
        String dia = d.substring(0, 2);
        String mes = d.substring(3, 5);
        String ano = d.substring(6, 10);


        String df = ano + "-" + mes + "-" + dia;

        return df;
    }

    public static Integer getCurrentYear() {

        return Calendar.getInstance().getTime().getYear();
    }

    public static int getHour(Date dateTime) {
        return getDateTimeField(dateTime, Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(Date dateTime) {
        return getDateTimeField(dateTime, Calendar.MINUTE);
    }

    public static int getSecond(Date dateTime) {
        return getDateTimeField(dateTime, Calendar.SECOND);
    }

    public static int getYear() {
        Date data = new Date();
        return getDateTimeField(data, Calendar.YEAR);
    }

    public static int getDateTimeField(Date dateTime, int fieldDateTime) {
        int fieldValue = 0;

        if (dateTime != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateTime);

            fieldValue = calendar.get(fieldDateTime);
            if (fieldDateTime == Calendar.MONTH) {
                fieldValue++;
            }
        }

        return fieldValue;
    }

    public static Date getDataAtualFormated() {
        //Calendar data = Calendar.getInstance();
        GregorianCalendar gc = new GregorianCalendar();
        //gc.add(GregorianCalendar.HOUR, 3);
        return gc.getTime();
    }

    public static String formatoCompleto(Date date) {
        return converterDataParaString("yyyy-MM-dd HH:mm:ss", date);
    }

    public static String formatoCompleto2(Date date) {
        return converterDataParaString("dd/MM/yyyy HH:mm:ss", date);
    }

    public static String formatoCompleto3(Date date) {
        return converterDataParaString("dd/MM/yyyy HH:mm", date);
    }

    public static String formatoHora(Date date) {
        return converterDataParaString(formatoHora, date);
    }

    public static String formatoExibicao(String data) {
        int ano = Integer.parseInt(data.substring(0, data.indexOf("-")));
        int mes = Integer.parseInt(data.substring(data.indexOf("-") + 1, 7));
        int dia = Integer.parseInt(data.substring(8, data.indexOf(" ")));

        return dia + "/" + mes + "/" + ano;
    }

    private static String converterDataParaString(String formato, Date data) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(formato);
            return formatter.format(data);
        } catch (Exception e) {
            return null;
        }
    }

    public static String converterDataParaStringFormatoPadrao(Date data) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(formatoCompleto3);
            return formatter.format(data);
        } catch (Exception e) {
            return null;
        }
    }

    public static String converterDataParaStringFormatoPadrao2(Date data) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(formatoPadrao);
            return formatter.format(data);
        } catch (Exception e) {
            return null;
        }
    }

    public static String converterDataParaStringFormatoCompleto(Date data) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(formatoCompleto);
            return formatter.format(data);
        } catch (Exception e) {
            return null;
        }
    }

    public static String converterDataParaStringFormatoCompleto2(Date data) {
        try {

            SimpleDateFormat in = new SimpleDateFormat(formatoCompleto);
            SimpleDateFormat out = new SimpleDateFormat(formatoCompleto2);

            return out.format(in.parse(data.toString()));
        } catch (Exception e) {
            return null;
        }
    }

    public static String alterarFormatoDataEmString(String dataFormato1) {
        String dia = dataFormato1.substring(0, 1);
        String mes = dataFormato1.substring(0, 1);

        return mes;
    }

    public static Date converteStringParaDateFormatoPadrao(String data) throws Exception {
        if (data == null || data.equals(""))
            return null;

        Date date = null;
        try {
            DateFormat formatter = new SimpleDateFormat(formatoPadrao);
            date = (Date) formatter.parse(data);
        } catch (ParseException e) {
            throw e;
        }
        return date;
    }

    public static Date converteStringParaDateFormatoPadrao3(String data) throws Exception {
        if (data == null || data.equals(""))
            return null;

        Date date = null;
        try {
            DateFormat formatter = new SimpleDateFormat(formatoCompleto3);
            date = (Date) formatter.parse(data);
        } catch (ParseException e) {
            throw e;
        }
        return date;
    }

    public static Date converteStringParaDateFormatoCompleto(String data) {
        if (data == null || data.equals(""))
            return null;
        GregorianCalendar gc = new GregorianCalendar();

        Date date = null;
        try {
            DateFormat formatter = new SimpleDateFormat(formatoCompleto);
            date = (Date) formatter.parse(data);

            gc = new GregorianCalendar();
            gc.setTime(date);
            gc.add(GregorianCalendar.HOUR, -3);

        } catch (ParseException e) {
            return null;
        }
        return date; //gc.getTime();
    }

    public static Date converteStringParaDateFormatoCompleto2(String data) throws Exception {
        if (data == null || data.equals(""))
            return null;
        GregorianCalendar gc = new GregorianCalendar();

        Date date = null;
        try {
            DateFormat formatter = new SimpleDateFormat(formatoCompleto2);
            date = (Date) formatter.parse(data);

            gc = new GregorianCalendar();
            gc.setTime(date);
            gc.add(GregorianCalendar.HOUR, -3);

        } catch (ParseException e) {
            throw e;
        }
        return date;
    }

}
