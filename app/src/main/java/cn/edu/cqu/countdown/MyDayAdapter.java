package cn.edu.cqu.countdown;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import cn.edu.cqu.countdown.entity.DayItem;
import cn.edu.cqu.countdown.util.GetCountDown;
import cn.edu.cqu.countdown.util.GetHistory;

import static android.content.ContentValues.TAG;

public class MyDayAdapter extends BaseAdapter {

    private List<DayItem> myDayList;
    private Context mContext;

    public MyDayAdapter(List<DayItem> list, Context context){
        myDayList = list;
        mContext = context;
    }

    @Override
    public int getCount() {
        if (myDayList != null) return myDayList.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (myDayList != null) return myDayList.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.my_item_layout,viewGroup,false);
        TextView myDayDescription = view.findViewById(R.id.my_item_description);
        TextView myDayNumber = view.findViewById(R.id.my_item_day);
        TextView myDayYetToCome = view.findViewById(R.id.my_item_yet);
        ConstraintLayout bg = (ConstraintLayout) ((ViewGroup)view).getChildAt(0);
        DayItem item = (DayItem)this.getItem(i);

        long days = GetCountDown.getCountDown(item.getYear(),item.getMonth(),item.getDay());
        if (days < 0){
            days = -1 * days;
            myDayYetToCome.setText("已经");
            bg.setBackground(viewGroup.getResources().getDrawable(R.drawable.my_past_background,null));
        }
        else {
            myDayYetToCome.setText("还有");
            bg.setBackground(viewGroup.getResources().getDrawable(R.drawable.my_future_background,null));
        }
        myDayNumber.setText(String.valueOf(days));
        myDayDescription.setText(item.getDescription());
        view.setOnClickListener(new ListItemOnClickListener(item.getId()));
        return view;
    }

    class ListItemOnClickListener implements View.OnClickListener{

        String itemId;
        public ListItemOnClickListener(String id) {
            itemId = id;
        }
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext,DetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id",itemId);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
            Log.i(TAG, "onClick: id = "+itemId);
        }
    }
}
