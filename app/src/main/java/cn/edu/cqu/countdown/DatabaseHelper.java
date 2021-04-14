package cn.edu.cqu.countdown;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    static String CREATE_MY_DAY = "create table my_day (" +
                                      "id varchar(20) primary key," +
                                      "description varchar(20)," +
                                      "tag varchar(20)," +
                                      "year int(4)," +
                                      "month int(2)," +
                                      "day int(2)," +
                                      "memo varchar(100)" +
                                  ")";

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_MY_DAY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
