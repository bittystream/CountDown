package cn.edu.cqu.countdown.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GetHistory {
    static String url = "https://www.lssdjt.com/%s/%s/";

    public static List<List<String>> getEvent(int month, int day) {
        List<List<String>> list = new ArrayList<List<String>>();
        List<String> eventList = new ArrayList<String>();
        List<String> yearList = new ArrayList<String>();
        try{
            Document doc = Jsoup.connect(String.format(url,String.valueOf(month),String.valueOf(day))).get();
            Elements elements = doc.getElementsByClass("gong");
            Elements elements1 = doc.getElementsByClass("gong str3");
            for (Element element : elements){
                String yearStr = element.getElementsByTag("em").get(0).text(); // 年份
                String eventStr = element.getElementsByTag("i").get(0).text(); // 事件
                eventList.add(eventStr);
                yearList.add(yearStr.substring(0,yearStr.lastIndexOf("年")));
                System.out.println(yearStr);
            }
            for (Element element : elements1){
                String yearStr = element.getElementsByTag("em").get(0).text(); // 年份
                String eventStr = element.getElementsByTag("i").get(0).text(); // 事件
                eventList.add(eventStr);
                yearList.add(yearStr.substring(0,yearStr.lastIndexOf("年")));
            }
            list.add(yearList);
            list.add(eventList);
            return list;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        GetHistory.getEvent(12,6);
    }

}
