package cn.edu.cqu.countdown.util;


import java.util.Calendar;
import java.util.GregorianCalendar;

import cn.edu.cqu.countdown.entity.DayItem;

public class GetCountDown {
    public static long getCountDown(int year, int month, int day){
        Calendar calendar = new GregorianCalendar(year,month-1,day);
        Calendar today = new GregorianCalendar();

        long t = today.getTimeInMillis();
        long c = calendar.getTimeInMillis();
        return (c - t) / (3600 * 24 * 1000);
    }


    public static void main(String[] args) {
        System.out.println(GetCountDown.getCountDown(2000,12,2));
    }

}
