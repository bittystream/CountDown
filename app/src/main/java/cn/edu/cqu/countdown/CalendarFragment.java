package cn.edu.cqu.countdown;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import cn.edu.cqu.countdown.entity.DateBean;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    DateBeanAdapter dateBeanAdapter;
    RecyclerView recyclerView;
    ImageView lastMonth, nextMonth;
    TextView currentYearMonth;
    GridLayoutManager gridLayoutManager;
    List<DateBean> beanList;
    static String GET_MY_DAY_SQL = "select * from my_day where year=%d and month=%d";
    int currentYear, currentMonth;


    void init(View root) throws ParseException {
        recyclerView = root.findViewById(R.id.recycler_view);
        currentYearMonth = root.findViewById(R.id.current_year_month);
        lastMonth = root.findViewById(R.id.last_month);
        nextMonth = root.findViewById(R.id.next_month);

        lastMonth.setOnClickListener(new ArrowOnClickListener());
        nextMonth.setOnClickListener(new ArrowOnClickListener());

        beanList = new ArrayList<>();

        Calendar calendar = new GregorianCalendar();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH) + 1;
        currentYearMonth.setText(currentYear+"年"+currentMonth+"月");

        gridLayoutManager = new GridLayoutManager(mContext,7,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);

        dateBeanAdapter = new DateBeanAdapter(beanList,mContext);
        recyclerView.setAdapter(dateBeanAdapter);

        updateList(currentYear,currentMonth);
    }

    // 月份变换之后，对beanList做出更新，notifydatasetchanged，recyclerView中显示该月日期和事件
    class ArrowOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.last_month){
                currentYear = currentMonth == 1 ? currentYear - 1 : currentYear;
                currentMonth = currentMonth == 1 ? 12 : currentMonth - 1;
            }
            else{
                currentYear = currentMonth == 12 ? currentYear + 1 : currentYear;
                currentMonth = currentMonth == 12 ? 1 : currentMonth + 1;
            }
            currentYearMonth.setText(currentYear+"年"+currentMonth+"月");
            onResume();
        }
    }

    // 根据年、月获取月的天数
    int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        return a.get(Calendar.DATE);
    }

    int getDayOfWeek(int year, int month, int day) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d H:m:s");
        Date date = dateFormat.parse(year+"-"+month+"-"+day+" 00:00:00");
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    void updateList(int year, int month) throws ParseException {
        beanList.clear();
        int maxDays = getDaysByYearMonth(year,month);
        Log.i(TAG, "updateList: max day of "+year+" "+month+" is "+maxDays);
        int dayOfWeek = getDayOfWeek(year,month,1) - 1;
        Log.i(TAG, "updateList: 1st day of "+year+" "+month+" is "+dayOfWeek);
        for (int i = 0; i < dayOfWeek; i++){
            DateBean bean = new DateBean(0,0,0,"");
            beanList.add(bean);
        }
        for (int i = 1; i <= maxDays; i++){
            DateBean bean = new DateBean(year,month,i,"");
            beanList.add(bean);
        }
        DatabaseHelper helper = new DatabaseHelper(mContext,"db",null,1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(String.format(Locale.CHINA,GET_MY_DAY_SQL,year,month),null);
        if (cursor.moveToFirst()){
            do{
                int day = cursor.getInt(cursor.getColumnIndex("day"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                Log.i(TAG, "updateList: descrition of "+currentMonth+"/"+day+" : "+description);
                if (!beanList.get(day-1+dayOfWeek).getDescription().equals("")){
                    description = "; " + description;
                }
                beanList.get(day-1+dayOfWeek).setDescription(beanList.get(day-1+dayOfWeek).getDescription()+description);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        dateBeanAdapter.notifyDataSetChanged();
        Log.i(TAG, "updateList: done update dataset");
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        try {
            init(root);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            updateList(currentYear,currentMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "onResume: sizeof list"+beanList.size());
    }
}