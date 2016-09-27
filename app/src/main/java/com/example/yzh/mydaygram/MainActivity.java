package com.example.yzh.mydaygram;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends Activity {

    public static final int LV_A = 1;
    public static final int LV_B= 0;
    private List<Diary> diaryList = new ArrayList<Diary>();
    private int mPosition;
    private DiaryAdapter diaryAdapter;
    private DiaryAdapterB diaryAdapterB;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Spinner spnMonth;
    private Spinner spnYear;
    private List<String> listMonth = new ArrayList<String>();
    private List<String> listYear = new ArrayList<String>();
    private ListView listView;
    private int Tag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        dbHelper = new MyDatabaseHelper(MainActivity.this,"Diary.db",null,1);

        listView = (ListView) findViewById(R.id.listView);

        Calendar today = Calendar.getInstance();

        initSpinner(today);

        initDiary();

        ImageButton btnWrite = (ImageButton) findViewById(R.id.btnWrite);
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EditActivity.class);
                mPosition = diaryList.size()-1;
                Diary diary = diaryList.get(mPosition);
                intent.putExtra("year",diary.getYear());
                intent.putExtra("month",diary.getMonth());
                intent.putExtra("dayOfMonth",diary.getDayOfMonth());
                intent.putExtra("dayOfWeek",diary.getDayOfWeek());
                intent.putExtra("content",diary.getContent());
                intent.putExtra("isWritten",diary.getWritten());
                startActivityForResult(intent,1);
            }
        });

        ImageButton btnChangeView = (ImageButton) findViewById(R.id.btnChangeView);
        btnChangeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tag == MainActivity.LV_A){
                    Tag = MainActivity.LV_B;
                    refreshListView();
                }else if(Tag == MainActivity.LV_B){
                    Tag = MainActivity.LV_A;
                    refreshListView();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                    initDiary();
                }
                break;
            default:
                break;
        }
    }

//初始化日志
    private void initDiary(){
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Diary",null,"id = 1",null,null,null,null);
        //判断数据库是否为空
        //若为空，则加载近五年所有数据
        if (!cursor.moveToFirst())
            createTable();
        //加载今天的数据到diaryList
        loadDiaries();
    }

//根据年月日得出是星期几
    private String getDayOfWeekOfMonth(int year,int month,int date){
        Calendar c = Calendar.getInstance();
        c.set(year,month,date);
        String dayOfWeek = getDayOfWeek(c.get(Calendar.DAY_OF_WEEK));
        return dayOfWeek;
    }
//根据年份和月份得出当月的天数
    private int getDaysOfMonth(int year,int month){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DATE,1);
        c.add(Calendar.DATE,-1);
        return c.get(Calendar.DAY_OF_MONTH);
    }
//根据数字得到星期几
    private String getDayOfWeek(int i){
        if (i == 1)
            return "星期天";
        else if (i == 2)
            return "星期一";
        else if (i == 3)
            return "星期二";
        else if (i == 4)
            return "星期三";
        else if (i == 5)
            return "星期四";
        else if (i == 6)
            return "星期五";
        else if (i == 7)
            return "星期六";
        return "what?!";
    }
//根据数字得到月份
    private String getMonth(int i){
        if (i == 0)
            return "一月";
        else if (i == 1)
            return "二月";
        else if (i == 2)
            return "三月";
        else if (i == 3)
            return "四月";
        else if (i == 4)
            return "五月";
        else if (i == 5)
            return "六月";
        else if (i == 6)
            return "七月";
        else if (i == 7)
            return "八月";
        else if (i == 8)
            return "九月";
        else if (i == 9)
            return "十月";
        else if (i == 10)
            return "十一月";
        else if (i == 11)
            return "十二月";
        else
            return "fuck!?";
    }
    //根据月份转化回数字
    private int changeMonth(String month){
        if (month.equals("一月"))
            return 0;
        else if (month.equals("二月"))
            return 1;
        else if (month.equals("三月"))
            return 2;
        else if (month.equals("四月"))
            return 3;
        else if (month.equals("五月"))
            return 4;
        else if (month.equals("六月"))
            return 5;
        else if (month.equals("七月"))
            return 6;
        else if (month.equals("八月"))
            return 7;
        else if (month.equals("九月"))
            return 8;
        else if (month.equals("十月"))
            return 9;
        else if (month.equals("十一月"))
            return 10;
        else if (month.equals("十二月"))
            return 11;
        else
            return -1;
    }
    //求任意两天之间相差的天数
    private long getSpaceDays(Calendar oldC,Calendar newC){
        oldC.set(Calendar.HOUR,0);
        oldC.set(Calendar.MINUTE,0);
        oldC.set(Calendar.SECOND,0);
        newC.set(Calendar.HOUR,0);
        newC.set(Calendar.MINUTE,0);
        newC.set(Calendar.SECOND,0);

        long time1 = oldC.getTimeInMillis();
        long time2 = newC.getTimeInMillis();
        long spaceDays = (time2 - time1)/(1000*3600*24);
        return spaceDays;
    }
    //从数据库加载日志
    private void loadDiaries(){

        Calendar today = Calendar.getInstance();
        Calendar oldDay = getOldDay();
        checkedDay(oldDay,today);

        refreshListView();

    }

    private void createTable(){
        Calendar oldDay = Calendar.getInstance();
        Calendar newDay = Calendar.getInstance();
        int oldYear = newDay.get(Calendar.YEAR) - 3;
        //把旧日期改成五年前的1月1日
        oldDay.set(Calendar.YEAR,oldYear);
        oldDay.set(Calendar.MONTH,0);
        oldDay.set(Calendar.DAY_OF_MONTH,1);

        long spaceDays = getSpaceDays(oldDay,newDay);

        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        String year;
        String month;
        String dayOfMonth;
        String dayOfWeek;
        String content = "";
        int isWritten = Diary.IS_NOT_WRITTEN;

        for (int i = 0;i <= spaceDays;i++){
            year = oldDay.get(Calendar.YEAR)+"";
            month = getMonth(oldDay.get(Calendar.MONTH));
            dayOfMonth = oldDay.get(Calendar.DAY_OF_MONTH)+"";
            dayOfWeek = getDayOfWeek(oldDay.get(Calendar.DAY_OF_WEEK));
            values.put("year",year);
            values.put("month",month);
            values.put("dayOfMonth",dayOfMonth);
            values.put("dayOfWeek",dayOfWeek);
            values.put("content",content);
            values.put("isWritten",isWritten);
            db.insert("Diary",null,values);
            values.clear();

            oldDay.add(Calendar.DATE,1);
        }
    }
    //若有几天没写日志，则把这几天的日志加入数据库
    private void checkedDay(Calendar oldDay,Calendar newDay){
        long spaceDays = getSpaceDays(oldDay,newDay);
        if (spaceDays > 0){
            String year;
            String month;
            String dayOfMonth;
            String dayOfWeek;
            String content = "";
            int isWritten = Diary.IS_NOT_WRITTEN;

            ContentValues values = new ContentValues();
            db = dbHelper.getWritableDatabase();
            for (int i = 0;i < spaceDays;i++){
                oldDay.add(Calendar.DATE,1);

                year = oldDay.get(Calendar.YEAR)+"";
                month = getMonth(oldDay.get(Calendar.MONTH));
                dayOfMonth = oldDay.get(Calendar.DAY_OF_MONTH)+"";
                dayOfWeek = getDayOfWeek(oldDay.get(Calendar.DAY_OF_WEEK));

                values.put("year",year);
                values.put("month",month);
                values.put("dayOfMonth",dayOfMonth);
                values.put("dayOfWeek",dayOfWeek);
                values.put("content",content);
                values.put("isWritten",isWritten);

                db.insert("Diary",null,values);
                values.clear();
            }
        }
    }

    private void initSpinner(Calendar today){
        initList();

        spnYear = (Spinner) findViewById(R.id.spnYear);
        final ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item,listYear);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnYear.setAdapter(adapterYear);
        spnYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initDiary();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnMonth = (Spinner) findViewById(R.id.spnMonth);
        final ArrayAdapter<String> adapterMonth = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item,listMonth);
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMonth.setAdapter(adapterMonth);
        spnMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initDiary();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH);

        String sYear = year+"";
        String sMonth = getMonth(month);

        //为了根据文字的年月获取他们的position，并设置到对应的年月
        int posYear = 0;
        while(true){
            spnYear.setSelection(posYear);
            String mYear = spnYear.getSelectedItem().toString();
            if (mYear.equals(sYear))
                break;
            posYear++;
        }

        int posMonth = 0;
        while(true){
            spnMonth.setSelection(posMonth);
            String mMonth = spnMonth.getSelectedItem().toString();
            if (mMonth.equals(sMonth))
                break;
            posMonth++;
        }

    }

    private Calendar getOldDay(){
        Calendar oldDay = Calendar.getInstance();

        db = dbHelper.getWritableDatabase();

        Cursor cursor = null;
        cursor = db.rawQuery("select * from Diary where id=(select max(id) from Diary)", null);

        int year;
        int month;
        int dayOfMonth;
        cursor.moveToFirst();
        //获取年月日
        year = Integer.parseInt(cursor.getString(cursor.getColumnIndex("year")));
        month = changeMonth(cursor.getString(cursor.getColumnIndex("month")));
        dayOfMonth = Integer.parseInt(cursor.getString(cursor.getColumnIndex("dayOfMonth")));
        oldDay.set(Calendar.YEAR, year);
        oldDay.set(Calendar.MONTH, month);
        oldDay.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        oldDay.set(Calendar.HOUR, 0);
        oldDay.set(Calendar.MINUTE, 0);
        oldDay.set(Calendar.SECOND, 0);

        return oldDay;
    }

    private void initList(){
        listMonth.add("一月");
        listMonth.add("二月");
        listMonth.add("三月");
        listMonth.add("四月");
        listMonth.add("五月");
        listMonth.add("六月");
        listMonth.add("七月");
        listMonth.add("八月");
        listMonth.add("九月");
        listMonth.add("十月");
        listMonth.add("十一月");
        listMonth.add("十二月");

        listYear.add("2016");
        listYear.add("2015");
        listYear.add("2014");
        listYear.add("2013");
    }

    private void refreshListView(){
        if (Tag == MainActivity.LV_A) {
            getDiaryList();

            diaryAdapter = new DiaryAdapter(MainActivity.this, R.layout.diary_item, diaryList);
            listView.setAdapter(diaryAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    mPosition = position;
                    Diary diary = diaryList.get(position);
                    intent.putExtra("year",diary.getYear());
                    intent.putExtra("month",diary.getMonth());
                    intent.putExtra("dayOfMonth",diary.getDayOfMonth());
                    intent.putExtra("dayOfWeek",diary.getDayOfWeek());
                    intent.putExtra("content",diary.getContent());
                    intent.putExtra("isWritten",diary.getWritten());
                    startActivityForResult(intent,1);
                    }
                });
        }
        else if (Tag == MainActivity.LV_B){
            getWrittenDiaryList();

            diaryAdapterB = new DiaryAdapterB(MainActivity.this,R.layout.listview_b,diaryList);
            listView.setAdapter(diaryAdapterB);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    mPosition = position;
                    Diary diary = diaryList.get(position);
                    intent.putExtra("year",diary.getYear());
                    intent.putExtra("month",diary.getMonth());
                    intent.putExtra("dayOfMonth",diary.getDayOfMonth());
                    intent.putExtra("dayOfWeek",diary.getDayOfWeek());
                    intent.putExtra("content",diary.getContent());
                    intent.putExtra("isWritten",diary.getWritten());
                    startActivityForResult(intent,1);
                }
            });
        }
        listView.setSelection(diaryList.size());
    }

    private void getWrittenDiaryList(){
        Cursor cursor = null;

        String year = spnYear.getSelectedItem().toString();
        String month = spnMonth.getSelectedItem().toString();
        String dayOfMonth;
        String dayOfWeek;
        String content;
        int isWritten;

        db = dbHelper.getWritableDatabase();
        cursor = db.query("Diary",null,"year = ? and month = ? and isWritten = ?",new String[]{year,month,Diary.IS_WRITTEN+""},null,null,null);

        diaryList.clear();
        if (cursor.moveToFirst()){
            do {
                dayOfMonth = cursor.getString(cursor.getColumnIndex("dayOfMonth"));
                dayOfWeek = cursor.getString(cursor.getColumnIndex("dayOfWeek"));
                content = cursor.getString(cursor.getColumnIndex("content"));
                isWritten = cursor.getInt(cursor.getColumnIndex("isWritten"));
                Diary diary = new Diary(year,month,dayOfMonth,dayOfWeek,content,isWritten);
                diaryList.add(diary);
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void getDiaryList(){
        Cursor cursor = null;

        String year = spnYear.getSelectedItem().toString();
        String month = spnMonth.getSelectedItem().toString();
        String dayOfMonth;
        String dayOfWeek;
        String content;
        int isWritten;

        db = dbHelper.getWritableDatabase();
        cursor = db.query("Diary",null,"year = ? and month = ?",new String[]{year,month},null,null,null);

        diaryList.clear();
        if (cursor.moveToFirst()){
            do {
                dayOfMonth = cursor.getString(cursor.getColumnIndex("dayOfMonth"));
                dayOfWeek = cursor.getString(cursor.getColumnIndex("dayOfWeek"));
                content = cursor.getString(cursor.getColumnIndex("content"));
                isWritten = cursor.getInt(cursor.getColumnIndex("isWritten"));
                Diary diary = new Diary(year,month,dayOfMonth,dayOfWeek,content,isWritten);
                diaryList.add(diary);
            }while (cursor.moveToNext());
        }
        cursor.close();
    }
}
