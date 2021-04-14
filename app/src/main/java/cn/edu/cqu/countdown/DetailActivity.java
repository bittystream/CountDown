package cn.edu.cqu.countdown;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import cn.edu.cqu.countdown.entity.DayItem;
import cn.edu.cqu.countdown.util.GetCountDown;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    static String GET_ITEM_SQL = "select * from my_day where id=%s";
    static String DEL_ITEM_SQL = "delete from my_day where id=%s";
    static String DISPLAY_DATE = "目标日：%4d-%2d-%2d";
    List<String> tag_en;
    List<String> tag_cn;
    String id;
    DayItem dayItem;
    TextView detailDescription, detailMemo, detailDays, detailDate, detailTag;
    ImageView returnButton, moreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    void init(){
        tag_cn = new ArrayList<String>(Arrays.asList("纪念日", "生日", "节日", "学习", "工作", "目标", "出行", "生活","社交"));
        tag_en = new ArrayList<String>(Arrays.asList("anniversary","birthday","festival","study","work","goal","travel","life","social"));

        dayItem = getItem();
        detailDate = findViewById(R.id.detail_date);
        detailDays = findViewById(R.id.detail_days);
        detailDescription = findViewById(R.id.detail_description);
        detailMemo = findViewById(R.id.detail_memo);
        detailTag = findViewById(R.id.detail_tag);
        returnButton = findViewById(R.id.return_to_my_0);
        moreButton = findViewById(R.id.more_options);

        returnButton.setOnClickListener(this);
        moreButton.setOnClickListener(this);

        detailDescription.setText(dayItem.getDescription());
        detailTag.setText(tag_cn.get(tag_en.indexOf(dayItem.getTag())));
        if (dayItem.getMemo() == null || dayItem.getMemo().equals("")){
            detailMemo.setText("无");
        }
        else{
            detailMemo.setText(dayItem.getMemo());
        }
        detailDate.setText(String.format(Locale.CHINA,DISPLAY_DATE,dayItem.getYear(),dayItem.getMonth(),dayItem.getDay()).replace(" ","0"));
        long days = GetCountDown.getCountDown(dayItem.getYear(),dayItem.getMonth(),dayItem.getDay());
        if (days < 0) {
            days = -1 * days;
            detailDays.setBackgroundColor(getColor(R.color.colorPast));
        }
        else{
            detailDays.setBackgroundColor(getColor(R.color.colorFuture));
        }
        detailDays.setText(String.valueOf(days));
    }

    DayItem getItem(){
        id = getIntent().getExtras().getString("id");
        String description, memo, tag;
        int year, month, day;
        DatabaseHelper helper = new DatabaseHelper(this,"db",null,1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(String.format(Locale.CHINA,GET_ITEM_SQL,id),null);
        if (cursor.moveToFirst()){
            description = cursor.getString(cursor.getColumnIndex("description"));
            memo = cursor.getString(cursor.getColumnIndex("memo"));
            tag = cursor.getString(cursor.getColumnIndex("tag"));
            year = cursor.getInt(cursor.getColumnIndex("year"));
            month = cursor.getInt(cursor.getColumnIndex("month"));
            day = cursor.getInt(cursor.getColumnIndex("day"));
            cursor.close();
            db.close();
            return new DayItem(year,month,day,description,tag,memo,id);
        }
        cursor.close();
        db.close();
        return null;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.return_to_my_0){
            finish();
        }
        else{
            PopupMenu popupMenu = new PopupMenu(DetailActivity.this,(View)(view.getParent()), Gravity.END);
            popupMenu.getMenuInflater().inflate(R.menu.more_options_menu,popupMenu.getMenu());
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId() == R.id.edit_item){
                        Intent intent = new Intent(DetailActivity.this,InputActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("id",id);
                        bundle.putBoolean("is2Edit",true);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    else{ // 删除该day，需弹出dialog再次确认是否删除
//                        这样不对！
//                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                        builder.setTitle("确认删除？");
                        builder.setItems(new String[]{"是", "否"}, new DeleteDialogOnClickListener());
                        builder.show();
                    }
                    return true;
                }
            });
        }
    }

    class DeleteDialogOnClickListener implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (i == 1){
            } else{
                deleteItem();
                Toast.makeText(DetailActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    void deleteItem(){
        DatabaseHelper helper = new DatabaseHelper(this,"db",null,1);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL(String.format(Locale.CHINA,DEL_ITEM_SQL,id));
    }
}