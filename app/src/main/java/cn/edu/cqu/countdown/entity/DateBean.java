package cn.edu.cqu.countdown.entity;

public class DateBean {
    private int year, month, day;
    private String description;

    public DateBean(int year, int month, int day, String description){
        this.year = year;
        this.month = month;
        this.day = day;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
