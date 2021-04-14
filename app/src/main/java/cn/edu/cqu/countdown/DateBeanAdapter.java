package cn.edu.cqu.countdown;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import cn.edu.cqu.countdown.entity.DateBean;
import cn.edu.cqu.countdown.util.GetCountDown;

import static android.content.ContentValues.TAG;

public class DateBeanAdapter extends RecyclerView.Adapter<DateBeanAdapter.ViewHolder> {

    private List<DateBean> beanList;
    private Context context;
    private int selectedYear, selectedMonth, selectedDay, todayYear, todayMonth, todayDay;
    private TextView calendarDetail;

    public DateBeanAdapter(List<DateBean> beanList, Context context){
        this.beanList = beanList;
        this.context = context;
        Calendar calendar = new GregorianCalendar();
        todayYear = calendar.get(Calendar.YEAR);
        todayMonth = calendar.get(Calendar.MONTH) + 1;
        todayDay = calendar.get(Calendar.DAY_OF_MONTH);
        selectedDay = todayDay;
        selectedMonth = todayMonth;
        selectedYear = todayYear;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateDay, dateCircle;
        public ViewHolder(View itemView) {
            super(itemView);
            dateDay = itemView.findViewById(R.id.date_day);
            dateCircle = itemView.findViewById(R.id.date_circle);
        }
    }

    @Override
    public DateBeanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_bean,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DateBeanAdapter.ViewHolder holder, int position) {
        DateBean bean = beanList.get(position);
        int year = bean.getYear(), month = bean.getMonth(), day = bean.getDay();
//        Log.i(TAG, "onBindViewHolder: "+month+"/"+day);
        String description = bean.getDescription();
        if (day == 0) {
            holder.dateDay.setText("");
            holder.dateDay.setBackgroundColor(context.getResources().getColor(R.color.background,null));
            holder.dateCircle.setVisibility(View.INVISIBLE);
            return;
        }
        else holder.dateDay.setText(String.valueOf(day));
        holder.dateDay.setOnClickListener(new DateOnClickListener(bean));
        if (year == todayYear && month == todayMonth && day == todayDay){
            holder.dateDay.setTextColor(context.getResources().getColor(R.color.colorPrimary,null));
        }
        if (year == selectedYear && month == selectedMonth && day == selectedDay){
            holder.dateDay.setBackgroundColor(context.getResources().getColor(R.color.colorAccent,null));
        }
        else{
            holder.dateDay.setBackgroundColor(context.getResources().getColor(R.color.background,null));
        }
        if (description.equals("")) {
            Log.i(TAG, "onBindViewHolder: no description on "+month+"/"+day);
            holder.dateCircle.setVisibility(View.INVISIBLE);
            return;
        }
        holder.dateCircle.setVisibility(View.VISIBLE);
        long days = GetCountDown.getCountDown(year,month,day);
        if (days > 0) holder.dateCircle.setTextColor(context.getResources().getColor(R.color.colorFuture,null));
        else holder.dateCircle.setTextColor(context.getResources().getColor(R.color.colorPast,null));

    }

    class DateOnClickListener implements View.OnClickListener{
        DateBean bean;
        public DateOnClickListener(DateBean bean){
            this.bean = bean;
        }

        @Override
        public void onClick(View view) {
            calendarDetail = view.getRootView().findViewById(R.id.calendar_detail);
            selectedDay = bean.getDay();
            selectedMonth = bean.getMonth();
            selectedYear = bean.getYear();
            Log.i(TAG, "onClick: clicked "+selectedMonth+"/"+selectedDay);
            notifyDataSetChanged();
            if (!bean.getDescription().equals("")){
                calendarDetail.setText(bean.getDescription());
            }
            else{
                calendarDetail.setText("无倒数日");
            }
        }
    }

    // 这里一定要写！！！！
    @Override
    public int getItemCount() {
        if (beanList == null) return 0;
        return beanList.size();
    }

}
