package cn.edu.cqu.countdown;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.cqu.countdown.entity.DayItem;
import cn.edu.cqu.countdown.util.SortMyDay;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public MyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance(String param1, String param2) {
        MyFragment fragment = new MyFragment();
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

    ImageView addButton;
    ListView listView;
    MyDayAdapter myDayAdapter;
    List<DayItem> myDayList;

    void init(View root){
        addButton = root.findViewById(R.id.add_my_day_button);
        listView = root.findViewById(R.id.my_list_view);

        myDayList = new ArrayList<DayItem>();
        myDayAdapter = new MyDayAdapter(myDayList,mContext);
        listView.setAdapter(myDayAdapter);

        addButton.setOnClickListener(new AddOnClickListener());
    }

    class AddOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext,InputActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean("is2Edit",false);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_my, container, false);
        init(root);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        initDatabase();
    }

    private void initDatabase(){
        String id, description, tag, memo;
        int year, month, day;
        myDayList.clear();
        DatabaseHelper helper = new DatabaseHelper(mContext,"db",null,1);
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "select * from my_day";
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.moveToFirst()){
            do {
                id = cursor.getString(cursor.getColumnIndex("id"));
                description = cursor.getString(cursor.getColumnIndex("description"));
                tag = cursor.getString(cursor.getColumnIndex("tag"));
                year = cursor.getInt(cursor.getColumnIndex("year"));
                month = cursor.getInt(cursor.getColumnIndex("month"));
                day = cursor.getInt(cursor.getColumnIndex("day"));
                memo = cursor.getString(cursor.getColumnIndex("memo"));
                DayItem item = new DayItem(year,month,day,description,tag,memo,id);
                myDayList.add(item);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        SortMyDay.sortMyDay(myDayList);
        myDayAdapter.notifyDataSetChanged();
    }
}