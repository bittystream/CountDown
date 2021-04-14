package cn.edu.cqu.countdown.entity;

public class DayItem {
    private int year, month, day;
    private String description, tag, memo, id;

    public DayItem(int year, int month, int day, String description, String tag, String memo, String id){
        this.year = year;
        this.month = month;
        this.day = day;
        this.description = description;
        this.tag = tag;
        this.memo = memo;
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public String getTag() {
        return tag;
    }

    public String getDescription() {
        return description;
    }

    public String getMemo() {
        return memo;
    }
}
