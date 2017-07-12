package com.android.zycojamie.draganddraw;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zckya on 2017/7/11.
 */

public class Box implements Parcelable {
    private PointF mOrigin;
    private PointF mCurrent;
    public Box(){

    }
    public Box(PointF origin){
        mOrigin=origin;
        mCurrent=origin;
    }

    public PointF getCurrent() {
        return mCurrent;
    }

    public PointF getOrigin() {
        return mOrigin;
    }

    public void setCurrent(PointF mCurrent) {
        this.mCurrent = mCurrent;
    }

    public void setmOrigin(PointF mOrigin) {
        this.mOrigin = mOrigin;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest,int flags){
        dest.writeParcelable(mCurrent,flags);
        dest.writeParcelable(mOrigin,flags);
    }
    public static final Parcelable.Creator<Box> CREATOR=new Parcelable.Creator<Box>(){
        @Override
        public Box createFromParcel(Parcel source) {
            Box box=new Box();
            box.setCurrent((PointF)source.readParcelable(ClassLoader.getSystemClassLoader()));
            box.setmOrigin((PointF)source.readParcelable(ClassLoader.getSystemClassLoader()));
            return box;
        }

        @Override
        public Box[] newArray(int size) {
            return new Box[size];
        }
    };
}
