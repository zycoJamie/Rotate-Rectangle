package com.android.zycojamie.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zckya on 2017/7/11.
 */

public class BoxDrawingView extends View {
    private static final String TAG="BoxDrawingView";
    private Box mCurrentBox;
    private List<Box> mBoxen=new ArrayList<Box>();
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;
    private Float mAngle=null;
    int pointer1=-1;
    int pointer2=-1;
    float fx,fy,sx,sy,nfx,nfy,nsx,nsy;
    int removed=-1;

    public BoxDrawingView(Context context){
        this(context,null);
    }
    public BoxDrawingView(Context context, AttributeSet attrs){
        super(context,attrs);
        mBoxPaint=new Paint();
        mBoxPaint.setColor(0x22ff0000);
        mBackgroundPaint=new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }
    public boolean onTouchEvent(MotionEvent event){
        PointF current=new PointF(event.getX(),event.getY());
        String action="";
        switch(event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                action="ACTION_DOWN";
                mCurrentBox=new Box(current);
                mBoxen.add(mCurrentBox);
                pointer1=event.getPointerId(event.getActionIndex());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                action="ACTION_POINTER_DOWN";
                pointer2=event.getPointerId(event.getActionIndex());
                fx=event.getX(event.findPointerIndex(pointer1));
                fy=event.getY(event.findPointerIndex(pointer1));
                sx=event.getX(event.findPointerIndex(pointer2));
                sy=event.getY(event.findPointerIndex(pointer2));
                mCurrentBox=null;
                break;
            case MotionEvent.ACTION_UP:
                action="ACTION_UP";
                pointer1=-1;
                mCurrentBox=null;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                action="ACTION_POINTER_UP";
                pointer2=-1;
                break;
            case MotionEvent.ACTION_MOVE:
                action="ACTION_MOVE";
                if(pointer1!=removed && pointer2!=removed){
                    nfx=event.getX(event.findPointerIndex(pointer1));
                    nfy=event.getY(event.findPointerIndex(pointer1));
                    nsx=event.getX(event.findPointerIndex(pointer2));
                    nsy=event.getY(event.findPointerIndex(pointer2));
                    mAngle=angleBetweenLines(fx,fy,sx,sy,nfx,nfy,nsx,nsy);
                }
                if(mCurrentBox!=null && pointer2==removed){
                    mCurrentBox.setCurrent(current);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                action="ACTION_CANCEL";
                mCurrentBox=null;
                pointer1=-1;
                pointer2=-1;
                break;
            default:;
        }
        Log.d(TAG,"action "+action+" at x="+current.x+" y="+current.y);
        return true;
    }
    public void onDraw(Canvas canvas){
        canvas.drawPaint(mBackgroundPaint);
        for(Box box:mBoxen){
            if(mAngle!=null){
                canvas.rotate(mAngle);
                mAngle=null;
            }
            float leftX=Math.min(box.getCurrent().x,box.getOrigin().x);
            float leftY=Math.min(box.getCurrent().y,box.getOrigin().y);
            float rightX=Math.max(box.getCurrent().x,box.getOrigin().x);
            float rightY=Math.max(box.getCurrent().y,box.getOrigin().y);
            canvas.drawRect(leftX,leftY,rightX,rightY,mBoxPaint);
        }
    }

    public float angleBetweenLines(float fx, float fy, float sx, float sy, float nfx, float nfy, float nsx, float nsy){
        float radian1=(float)Math.atan2(fy-sy,fx-sx);
        float radian2=(float)Math.atan2(nfy-nsy,nfx-nsx);
        float angle=(float)(Math.toDegrees(radian2-radian1)%360);
        if(angle<0){
            angle+=360;
        }
        return angle;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle=new Bundle();
        bundle.putParcelableArrayList("BOXLIST",(ArrayList<Box>) mBoxen);
        bundle.putParcelable("PARENT_VIEW",super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle){
            Parcelable parentViewState=((Bundle)state).getParcelable("PARENT_VIEW");
            super.onRestoreInstanceState(parentViewState);
            mBoxen=((Bundle)state).getParcelableArrayList("BOXLIST");
        }
    }
}
