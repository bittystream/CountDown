package cn.edu.cqu.countdown.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.edu.cqu.countdown.entity.DayItem;

public class SortMyDay {
    public static void sortMyDay(List<DayItem> list){
        Collections.sort(list, new Comparator<DayItem>() {
            @Override
            public int compare(DayItem i1, DayItem i2) {
                long r1 = GetCountDown.getCountDown(i1.getYear(),i1.getMonth(),i1.getDay());
                long r2 = GetCountDown.getCountDown(i2.getYear(),i2.getMonth(),i2.getDay());
                if (r1 * r2 < 0) return (int)(r2 - r1);
                if (r1 < 0) return (int)(r2 - r1);
                return (int)(r1 - r2);
            }
        });
    }
}
