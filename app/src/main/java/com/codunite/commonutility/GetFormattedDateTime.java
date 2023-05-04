package com.codunite.commonutility;

import android.content.Context;
import android.text.format.DateUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GetFormattedDateTime {
    private Context context;
    public GetFormattedDateTime(Context context) {
        this.context = context;
    }

    public static String getFormatedcurrentDate() {
        Calendar today = Calendar.getInstance();
        int date = today.get(Calendar.DATE);
        int month = today.get(Calendar.MONTH);
        int year = today.get(Calendar.YEAR);

        String mon = "";
        String[] monthdayArray = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        String[] monthArray = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        for (int i = 0; i < monthdayArray.length; i++) {
            if (("" + (month + 1)).equals(monthdayArray[i])) {
                mon = monthArray[i];
                break;
            }
        }
        return (mon) + " " + date + ", " + year + ", ";
    }

    public static String getFormatedcurrentDate(int date, int month, int year) {
        String mon = "", dat = "";
        if (date < 10) {
            dat = "0" + date;
        } else {
            dat = "" + date;
        }

        if (month < 10) {
            mon = "0" + month;
        } else {
            mon = "" + month;
        }

        return (dat) + "-" + mon + "-" + year + "";
    }

    public static String returnMonthAlphabet(int month) {
        String mon = "";
        String[] monthdayArray = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        String[] monthArray = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        for (int i = 0; i < monthdayArray.length; i++) {
            if (("" + (month + 1)).equals(monthdayArray[i])) {
                mon = monthArray[i];
                break;
            }
        }
        return mon;
    }

    public static String getFormatedcurrentTime() {
        Calendar today = Calendar.getInstance();
        int hour = today.get(Calendar.HOUR);
        int minute = today.get(Calendar.MINUTE);
        //int second 	= today.get(Calendar.SECOND);
        int amorpm = today.get(Calendar.AM_PM);

        String strAMORPM = "";
        if (amorpm == 0) {
            strAMORPM = "am";
        } else {
            strAMORPM = "pm";
        }

        if (hour == 0) {
            hour = 12;
        }

        return (hour >= 10 ? hour : "0" + hour) + ":" + (minute >= 10 ? minute : "0" + minute) + " " + strAMORPM;
    }

    public static String getcurrentDate() {
        Calendar today = Calendar.getInstance();
        int date = today.get(Calendar.DATE);
        int month = today.get(Calendar.MONTH);
        int year = today.get(Calendar.YEAR);


        return (date >= 10 ? date : "0" + date) + "-" + (month >= 10 ? month : "0" + month) + "-" + year;
    }
    public static String getcurrentcalDate() {
        Calendar today = Calendar.getInstance();
        int date = today.get(Calendar.DATE);
        int month = today.get(Calendar.MONTH);
        int year = today.get(Calendar.YEAR);
        month++;

        return  year + "-" + (month >= 10 ? month : "0" + month)
                + "-" + (date >= 10 ? date : "0" + date);

    }

    public static String getOneMonthDate() {
        Calendar today = Calendar.getInstance();
        int date = today.get(Calendar.DATE);
        int month = today.get(Calendar.MONTH);
        int year = today.get(Calendar.YEAR);

        return  year + "-" + (month >= 10 ? month : "0" + month)
                + "-" + (date >= 10 ? date : "0" + date);

    }



    public static String getUniqueString() {
        Calendar today = Calendar.getInstance();
        int date = today.get(Calendar.DATE);
        int month = today.get(Calendar.MONTH);
        int year = today.get(Calendar.YEAR);
        int hour = today.get(Calendar.HOUR);
        int minute = today.get(Calendar.MINUTE);
        int second = today.get(Calendar.SECOND);

        return date + "" + month + "" + year + "" + hour + "" + minute + "" + second;
    }

    public static String getDateTimeForLog() {
        return getcurrentDate() + "  " + getFormatedcurrentTime();
    }

    public static String getcurrentTime() {
        Calendar today = Calendar.getInstance();
        int hour = today.get(Calendar.HOUR);
        int minute = today.get(Calendar.MINUTE);
        int second = today.get(Calendar.SECOND);

        return hour + ":" + minute + ":" + second;
    }

    public static String getcurrentTimeFormat() {
        Calendar today = Calendar.getInstance();
        int hour = today.get(Calendar.HOUR);
        int minute = today.get(Calendar.MINUTE);
        int second = today.get(Calendar.SECOND);

        return hour + ":" + minute;
    }

    public static String replaceMonthString(String dateTime) {
        String month = dateTime.substring(0, 2);
        dateTime = dateTime.substring(2);

        String mon = "";
        String[] monthdayArray = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        String[] monthArray = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        for (int i = 0; i < monthdayArray.length; i++) {
            if (("" + (month)).equals(monthdayArray[i])) {
                mon = monthArray[i];
                break;
            }
        }

        return mon + dateTime;
    }

    public static String getMinuteInDayHourMin(String strparkingTime) {
        int parkingTime = Integer.parseInt(strparkingTime);
        if (parkingTime < 60) {
            return parkingTime + " minute";
        } else {
            if (parkingTime / 60 > 24) {
                return (parkingTime / 60) / 24 + " day " + (parkingTime % 60) + " hour " + parkingTime % 60 + " min";
            } else {
                return parkingTime / 60 + " hour " + parkingTime % 60 + " min";
            }
        }
    }

    public static String getDisplayableTime(long delta) {
        long difference = 0;
        Long mDate = System.currentTimeMillis();

        if (mDate > delta) {
            difference = mDate - delta;
            final long seconds = difference / 1000;
            final long minutes = seconds / 60;
            final long hours = minutes / 60;
            final long days = hours / 24;
            final long months = days / 31;
            final long years = days / 365;

            if (seconds < 0) {
                return "not yet";
            } else if (seconds < 60) {
                return seconds == 1 ? "one second ago" : seconds + " seconds ago";
            } else if (seconds < 120) {
                return "a minute ago";
            } else if (seconds < 2700) // 45 * 60
            {
                return minutes + " minutes ago";
            } else if (seconds < 5400) // 90 * 60
            {
                return "an hour ago";
            } else if (seconds < 86400) // 24 * 60 * 60
            {
                return hours + " hours ago";
            } else if (seconds < 172800) // 48 * 60 * 60
            {
                return "yesterday";
            } else if (seconds < 2592000) // 30 * 24 * 60 * 60
            {
                return days + " days ago";
            } else if (seconds < 31104000) // 12 * 30 * 24 * 60 * 60
            {

                return months <= 1 ? "one month ago" : days + " months ago";
            } else {

                return years <= 1 ? "one year ago" : years + " years ago";
            }
        }
        return null;
    }

    public static String getDisplayableDay(long delta) {
        long difference = 0;
        Long mDate = System.currentTimeMillis();

        if (mDate > delta) {
            difference = mDate - delta;
            final long seconds = difference / 1000;
            final long minutes = seconds / 60;
            final long hours = minutes / 60;
            final long days = hours / 24;
            final long months = days / 31;
            final long years = days / 365;

            if (seconds < 0) {
                return "not yet";
            } else if (DateUtils.isToday(delta)) {
                return "TODAY";
            }/* else if (seconds < 120) {
                return "TODAY";
            } else if (seconds < 2700) // 45 * 60
            {
                return "TODAY";
            } else if (seconds < 5400) // 90 * 60
            {
                return "TODAY";
            } else if (seconds < 86400) // 24 * 60 * 60
            {
                return "TODAY";
            }*/ else if (seconds < 172800) // 48 * 60 * 60
            {
                return "yesterday";
            } else if (seconds < 2592000) // 30 * 24 * 60 * 60
            {
                return days + " days ago";
            } else if (seconds < 31104000) // 12 * 30 * 24 * 60 * 60
            {

                return months <= 1 ? "one month ago" : days + " months ago";
            } else {

                return years <= 1 ? "one year ago" : years + " years ago";
            }
        }
        return null;
    }

    public static String changeDateFormate(String time) {//2018-07-18 09:48:39
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

      /* public static double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }*/

    public static String convertTimestampToTime(long timestamp) {
        Timestamp tStamp = new Timestamp(timestamp);
        SimpleDateFormat simpleDateFormat;
        if (DateUtils.isToday(timestamp)) {
            simpleDateFormat = new SimpleDateFormat("hh:mm a");
            return simpleDateFormat.format(tStamp);
        } else {
            simpleDateFormat = new SimpleDateFormat("hh:mm a");
            return simpleDateFormat.format(tStamp);
        }
    }

    public static String convertTimestampDateToTime(long timestamp) {
        Timestamp tStamp = new Timestamp(timestamp);
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("dd MMM, yyyy hh:mm a");
        return simpleDateFormat.format(tStamp);
    }

    public static long correctTimestamp(long timestampInMessage) {
        long correctedTimestamp = timestampInMessage;

        if (String.valueOf(timestampInMessage).length() < 13) {
            int difference = 13 - String.valueOf(timestampInMessage).length(), i;
            String differenceValue = "1";
            for (i = 0; i < difference; i++) {
                differenceValue += "0";
            }
            correctedTimestamp = (timestampInMessage * Integer.parseInt(differenceValue))
                    + (System.currentTimeMillis() % (Integer.parseInt(differenceValue)));
        }
        return correctedTimestamp;
    }


}
