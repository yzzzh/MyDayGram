package com.example.yzh.mydaygram;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class EditActivity extends Activity {

    EditText editText;
    TextView dayOfWeek;
    TextView month;
    TextView dayOfMonth;
    TextView year;
    String oldContent;
    MyDatabaseHelper dbHelper;
    SQLiteDatabase db;
    String mYear;
    String mMonth;
    String mDayOfMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();

        editText = (EditText) findViewById(R.id.editText);
        dayOfWeek = (TextView) findViewById(R.id.dayOfWeek);
        month = (TextView) findViewById(R.id.month);
        dayOfMonth = (TextView) findViewById(R.id.dayOfMonth);
        year = (TextView) findViewById(R.id.year);


        mYear = intent.getStringExtra("year");
        mMonth = intent.getStringExtra("month");
        mDayOfMonth = intent.getStringExtra("dayOfMonth");
        oldContent = intent.getStringExtra("content");

        editText.setText(oldContent);
        dayOfWeek.setText(intent.getStringExtra("dayOfWeek"));
        dayOfMonth.setText(mDayOfMonth);
        month.setText(mMonth);
        year.setText(mYear);

        Button btnDone = (Button) findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newContent = editText.getText().toString();
                if (!newContent.equals(oldContent))
                {
                    dbHelper = new MyDatabaseHelper(EditActivity.this,"Diary.db",null,1);
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("content",newContent);
                    if (newContent.equals(""))
                        values.put("isWritten",Diary.IS_NOT_WRITTEN);
                    else
                        values.put("isWritten",Diary.IS_WRITTEN);
                    db.update("Diary",values,"year = ? and month = ? and dayOfMonth = ?",new String[] {mYear,mMonth,mDayOfMonth});
                }
                setResult(RESULT_OK);
                finish();
            }
        });

    }
}
