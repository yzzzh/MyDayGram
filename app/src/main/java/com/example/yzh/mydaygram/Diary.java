package com.example.yzh.mydaygram;

import android.app.Dialog;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by YZH on 2016/9/24.
 */
public class Diary implements Parcelable {

    private int isWritten;
    private String year;//年
    private String month;//月
    private String dayOfMonth;//日
    private String dayOfWeek;//星期
    private String content;

    public static final int IS_WRITTEN = 1;
    public static final int IS_NOT_WRITTEN = 0;

    public Diary(String year,String month,String dayOfMonth,String dayOfWeek,String content){
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.dayOfWeek = dayOfWeek;
        this.content = content;
        this.isWritten = Diary.IS_NOT_WRITTEN;
    }

    public Diary(String year,String month,String dayOfMonth,String dayOfWeek,String content,int isWritten){
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.dayOfWeek = dayOfWeek;
        this.content = content;
        this.isWritten = isWritten;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setWritten(boolean isWritten){
        if (isWritten)
            this.isWritten = Diary.IS_WRITTEN;
        else
            this.isWritten = Diary.IS_NOT_WRITTEN;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getContent() {
        return content;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public boolean getWritten(){
        if (this.isWritten == Diary.IS_WRITTEN)
            return true;
        else
            return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(year);
        dest.writeString(month);
        dest.writeString(dayOfMonth);
        dest.writeString(dayOfWeek);
        dest.writeString(content);
        dest.writeInt(isWritten);
    }

    public static final Parcelable.Creator<Diary> CREATOR = new Parcelable.Creator<Diary>() {
        @Override
        public Diary createFromParcel(Parcel source) {
            //从Parcel中读取数据
            //此处read顺序依据write顺序
            return new Diary(source.readString(), source.readString(),source.readString(),
                    source.readString(),source.readString(),source.readInt());
        }
        @Override
        public Diary[] newArray(int size) {

            return new Diary[size];
        }

    };
}
