package cn.edu.cqu.countdown;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class InputActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "InputActivity";
    static String GET_DAY_SQL = "select * from my_day where id=%s";
    static String DEL_DAY_SQL = "delete from my_day where id=%s";
    ImageView returnButton;
    LinearLayout tagAnniversiry, tagBirthday, tagFestival, tagStudy, tagWork, tagGoal, tagTravel, tagLife, tagSocial;
    ConstraintLayout tagGroup;
    EditText inputDescription, inputMemo;
    DatePicker datePicker;
    Button submitButton;
    TextView inputTitle;

    String id, description, tag, memo;
    int year, month, day;
    boolean is2Edit;

    void init(){
        is2Edit = getIntent().getExtras().getBoolean("is2Edit");

        inputTitle = findViewById(R.id.input_title);
        tagGroup = findViewById(R.id.tag_group);
        returnButton = findViewById(R.id.return_to_my);
        tagAnniversiry = findViewById(R.id.tag_anniversary);
        tagBirthday = findViewById(R.id.tag_birthday);
        tagFestival = findViewById(R.id.tag_festival);
        tagStudy = findViewById(R.id.tag_study);
        tagWork = findViewById(R.id.tag_work);
        tagGoal = findViewById(R.id.tag_goal);
        tagTravel = findViewById(R.id.tag_travel);
        tagLife = findViewById(R.id.tag_life);
        tagSocial = findViewById(R.id.tag_social);
        inputDescription = findViewById(R.id.input_description);
        inputMemo = findViewById(R.id.input_memo);
        datePicker = findViewById(R.id.date_picker);
        submitButton = findViewById(R.id.input_submit);

        returnButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);
        tagAnniversiry.setOnClickListener(this);
        tagBirthday.setOnClickListener(this);
        tagFestival.setOnClickListener(this);
        tagStudy.setOnClickListener(this);
        tagWork.setOnClickListener(this);
        tagGoal.setOnClickListener(this);
        tagTravel.setOnClickListener(this);
        tagLife.setOnClickListener(this);
        tagSocial.setOnClickListener(this);
    }


    String getId(){
        String idFormat = "%4d%2d%2d%2d%2d%2d";
        Calendar calendar = new GregorianCalendar();
        int yearId = calendar.get(Calendar.YEAR);
        int monthId = calendar.get(Calendar.MONTH) + 1;
        int dayId = calendar.get(Calendar.DAY_OF_MONTH);
        int hourId = calendar.get(Calendar.HOUR);
        int minId = calendar.get(Calendar.MINUTE);
        int secId = calendar.get(Calendar.SECOND);

        return String.format(Locale.CHINA,idFormat,yearId,monthId,dayId,hourId,minId,secId).replace(" ","0");
    }

    // 编辑已有信息的时候，要填上已有信息
    void fillView(){
        id = getIntent().getExtras().getString("id");
        DatabaseHelper helper = new DatabaseHelper(this,"db",null,1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(String.format(Locale.CHINA,GET_DAY_SQL,id),null);
        if (cursor.moveToFirst()){
            description = cursor.getString(cursor.getColumnIndex("description"));
            memo = cursor.getString(cursor.getColumnIndex("memo"));
            tag = cursor.getString(cursor.getColumnIndex("tag"));
            year = cursor.getInt(cursor.getColumnIndex("year"));
            month = cursor.getInt(cursor.getColumnIndex("month"));
            day = cursor.getInt(cursor.getColumnIndex("day"));
        }
        cursor.close();
        db.close();

        inputDescription.setText(description);
        if (memo == null) { memo = ""; }
        inputMemo.setText(memo);
        String tagName = "tag_" + tag;
        int tagViewId = getResources().getIdentifier(tagName,"id",getPackageName());
        LinearLayout tagView = findViewById(tagViewId);
        clearTagStyle();
        setTagStyle(tagView,true);
    }


    void add2db(){
        DatabaseHelper helper = new DatabaseHelper(this,"db",null,1);
        SQLiteDatabase db = helper.getWritableDatabase();
        if (is2Edit){
            db.execSQL(String.format(Locale.CHINA,DEL_DAY_SQL,id));
        }
        else id = getId();
        ContentValues cv = new ContentValues();
        cv.put("id",id);
        cv.put("description",description);
        cv.put("tag",tag);
        cv.put("year",year);
        cv.put("month",month);
        cv.put("day",day);
        cv.put("memo",memo);
        db.insert("my_day",null,cv);
        Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        init();

        if (is2Edit){
            fillView();
            inputTitle.setText("修改倒数日");

        } else{
            Calendar calendar = new GregorianCalendar();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        datePicker.init(year, month - 1, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                year = datePicker.getYear();
                month = datePicker.getMonth() + 1;
                day = datePicker.getDayOfMonth();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.return_to_my){
            finish();
        }
        else if (view.getId() == R.id.input_submit){
            // 提交数据，存入数据库
            description = inputDescription.getText().toString();
            memo = inputMemo.getText().toString();
            Log.i(TAG, "onClick: date is "+year+"/"+month+"/"+day);
            Log.i(TAG, "onClick: tag is "+tag);
            Log.i(TAG, "onClick: description is "+description);
            Log.i(TAG, "onClick: memo is "+memo);
            add2db();
            String hint = is2Edit ? "修改成功" : "添加成功";
            Toast.makeText(this,hint,Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            // 点击了类别，改变类别样式，且给tag属性赋值
            tag = view.getTag().toString();
            clearTagStyle();
            setTagStyle((LinearLayout)view,true);
        }
    }

    void setTagStyle(LinearLayout selectedTag, boolean isSelected){
        ImageView tagIcon = (ImageView) selectedTag.getChildAt(0);
        TextView tagText = (TextView) selectedTag.getChildAt(1);
        String tagTitle = tagIcon.getTag().toString();
        if (!isSelected) {
            // 改成灰色
            selectedTag.setBackground(getDrawable(R.drawable.class_option_background_0));
            tagText.setTextColor(getColor(R.color.colorPrimaryDark));
            tagTitle = tagTitle + "_0";
        }
        else{
            selectedTag.setBackground(getDrawable(R.drawable.class_option_background));
            tagText.setTextColor(getColor(R.color.colorPrimary));
        }

        int tagIconId = getResources().getIdentifier(tagTitle,"drawable",getPackageName());
        tagIcon.setImageDrawable(getDrawable(tagIconId));
        int padding = getResources().getDimensionPixelSize(R.dimen.tagPadding);
        selectedTag.setPadding(padding,padding,padding,padding);
    }

    void clearTagStyle(){
        for (int i = 0; i < tagGroup.getChildCount(); i++){
            LinearLayout temp = (LinearLayout) tagGroup.getChildAt(i);
            setTagStyle(temp,false);
        }
    }
}