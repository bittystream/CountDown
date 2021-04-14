package cn.edu.cqu.countdown;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;
    private List<String> yearList;
    private List<String> eventList;

    public TodayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodayFragment newInstance(String param1, String param2) {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        List<List<String>> tmp = ((MainActivity)context).getHistoryLists();
        if (tmp == null){
            yearList = new ArrayList<>();
            eventList = new ArrayList<>();
            return;
        }
        yearList = tmp.get(0);
        eventList = tmp.get(1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    TextView todayDate;
    LinearLayout historyYear, historyEvent;
    LinearLayout.LayoutParams layoutParams;
    int itemHeight;
    static String DATE_FORMAT = "%4d年%2d月%2d日";

    void init(View root){
        todayDate = root.findViewById(R.id.today_date);
        historyYear = root.findViewById(R.id.today_history_year);
        historyEvent = root.findViewById(R.id.today_history_event);

        itemHeight = root.getResources().getDimensionPixelSize(R.dimen.historyItemHeight);

        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,itemHeight);

        Calendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        todayDate.setText(String.format(Locale.CHINA,DATE_FORMAT,year,month,day).replace(" ","0"));
    }

    void addHistory2View(View root, String year, String event){
        TextView yearItem = new TextView(mContext);
        TextView eventItem = new TextView(mContext);

        yearItem.setTextSize(36);
        eventItem.setTextSize(18);
        yearItem.setTextColor(root.getResources().getColor(R.color.colorAccent,null));
        eventItem.setTextColor(root.getResources().getColor(R.color.colorPrimaryDark,null));
        yearItem.setText(year);
        eventItem.setText(event);
        yearItem.setGravity(Gravity.END);
        eventItem.setPadding(0,20,0,0);

        historyYear.addView(yearItem, layoutParams);
        historyEvent.addView(eventItem,layoutParams);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_today, container, false);
        init(root);
        for (int i = 0; i < yearList.size(); i++){
            addHistory2View(root, yearList.get(i),eventList.get(i));
        }

        return root;
    }
}