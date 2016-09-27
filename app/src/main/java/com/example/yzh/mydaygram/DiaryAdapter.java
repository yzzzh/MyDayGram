package com.example.yzh.mydaygram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Time;
import java.util.List;
import java.util.jar.Pack200;

/**
 * Created by YZH on 2016/9/24.
 */
public class DiaryAdapter extends ArrayAdapter<Diary> {
    private int resourceId;

    public DiaryAdapter(Context context, int textViewResourceId, List<Diary> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Diary diary = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.dayOfMonth = (TextView) view.findViewById(R.id.viewDayOfMonth);
            viewHolder.dayOfWeek = (TextView) view.findViewById(R.id.viewDayOfWeek);
            viewHolder.content = (TextView) view.findViewById(R.id.viewContent);
            view.setTag(viewHolder);
        }
        else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.dayOfWeek.setText(diary.getDayOfWeek());
        viewHolder.dayOfMonth.setText(diary.getDayOfMonth());
        viewHolder.content.setText(diary.getContent());

        //若有写日记，则显示出来，没写就隐藏
        LinearLayout layoutNotHidden = (LinearLayout) view.findViewById(R.id.layoutNotHidden);
        LinearLayout layoutHidden = (LinearLayout) view.findViewById(R.id.layoutHidden);
        if (diary.getWritten()){
            layoutNotHidden.setVisibility(View.VISIBLE);
            layoutHidden.setVisibility(View.GONE);
        }else {
            layoutNotHidden.setVisibility(View.GONE);
            layoutHidden.setVisibility(View.VISIBLE);
        }

        return view;
    }

    class ViewHolder{
        TextView dayOfMonth;
        TextView dayOfWeek;
        TextView content;
    }
}
