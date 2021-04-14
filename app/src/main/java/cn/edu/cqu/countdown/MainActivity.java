package cn.edu.cqu.countdown;

import android.graphics.Color;
import android.media.Image;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import cn.edu.cqu.countdown.util.GetHistory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private FragmentManager fragmentManager;
    private TodayFragment todayFragment;
    private MyFragment myFragment;
    private CalendarFragment calendarFragment;
    private FrameLayout frameLayout;
    private ImageView myIcon, calendarIcon, todayIcon;
    private int unselectedColor;
    private List<List<String>> tmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(){
            @Override
            public void run() {
                Calendar calendar = new GregorianCalendar();
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                tmp = GetHistory.getEvent(month, day);
            }
        }.start();

        GridLayout gridLayout = findViewById(R.id.main_navi_bar);
        // 添加点击事件监听器
        for (int i = 0; i < gridLayout.getChildCount(); i++){
            gridLayout.getChildAt(i).setOnClickListener(this);
        }
        myIcon = findViewById(R.id.my_icon);
        calendarIcon = findViewById(R.id.calendar_icon);
        todayIcon = findViewById(R.id.today_icon);

        unselectedColor = this.getResources().getColor(R.color.colorPrimaryDark,null);
        // 创建时默认显示第一个fragment(我的)
        selectedColor(gridLayout.getChildAt(0));
        myIcon.setImageDrawable(getDrawable(R.drawable.my_icon));
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        myFragment = new MyFragment();
        fragmentTransaction.add(R.id.fragment,myFragment);
        fragmentTransaction.commit();
    }

    public List<List<String>> getHistoryLists(){
        return tmp;
    }




    void clearColor(){
        GridLayout gridLayout = findViewById(R.id.main_navi_bar);
        for (int i = 0; i < gridLayout.getChildCount(); i++){
            View view = gridLayout.getChildAt(i);
            TextView text = (TextView)((ViewGroup)view).getChildAt(1);
            text.setTextColor(unselectedColor);
        }
        todayIcon.setImageDrawable(getDrawable(R.drawable.today_icon_0));
        myIcon.setImageDrawable(getDrawable(R.drawable.my_icon_0));
        calendarIcon.setImageDrawable(getDrawable(R.drawable.calendar_icon_0));
    }

    // 为选中的navibar item更改颜色
    void selectedColor(View view){
        ImageView icon = (ImageView) ((ViewGroup)view).getChildAt(0);
        TextView text = (TextView)((ViewGroup)view).getChildAt(1);
        text.setTextColor(getResources().getColor(R.color.colorPrimary,null));
    }

    @Override
    public void onClick(View view) {
        // fragment的切换
        clearColor();
        TextView text = (TextView)((ViewGroup)view).getChildAt(1);
        text.setTextColor(getResources().getColor(R.color.colorPrimary,null));
        switch (view.getId()){
            case R.id.today_item:
                Log.i(TAG, "onClick: today clicked");
                if (todayFragment == null) todayFragment = new TodayFragment();
                todayIcon.setImageDrawable(getDrawable(R.drawable.today_icon));
                FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
                fragmentTransaction1.replace(R.id.fragment,todayFragment);
                fragmentTransaction1.commit();
                break;
            case R.id.my_item:
                Log.i(TAG, "onClick: record clicked");
                myIcon.setImageDrawable(getDrawable(R.drawable.my_icon));
                FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                fragmentTransaction2.replace(R.id.fragment,myFragment);
                fragmentTransaction2.commit();
                break;
            case R.id.calendar_item:
                Log.i(TAG, "onClick: stat clicked");
                if (calendarFragment == null) calendarFragment = new CalendarFragment();
                calendarIcon.setImageDrawable(getDrawable(R.drawable.calendar_icon));
                FragmentTransaction fragmentTransaction3 = fragmentManager.beginTransaction();
                fragmentTransaction3.replace(R.id.fragment,calendarFragment);
                fragmentTransaction3.commit();
                break;
        }
    }

    public void createMenu(ContextMenu menu){
        menu.add("修改");
        menu.add("删除");
    }
}