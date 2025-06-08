package com.home.rellotgedigital;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {
    // get the supported ids for GMT-08:00 (Pacific Standard Time)

    GregorianCalendar mTime;
    Handler handler;
    Runnable r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

      //  mTime = new Time("Europe/Paris");
        mTime = new GregorianCalendar(TimeZone.getTimeZone("Europe/Paris"));
        r = new Runnable(){
            @Override
            public void run() {
                mTime.setTime(Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris")).getTime());
                drawingView dv = new drawingView(MainActivity.this,mTime.get(Calendar.HOUR_OF_DAY), mTime.get(Calendar.MINUTE), mTime.get(Calendar.SECOND), mTime.get(Calendar.DAY_OF_WEEK), mTime.get(Calendar.YEAR), mTime.get(Calendar.MONTH) + 1, mTime.get(Calendar.DAY_OF_MONTH),  getBatteryLevel());
                setContentView(dv);
                handler.postDelayed(r, 1000);
            }
        };
        handler = new Handler();
        handler.postDelayed(r, 1000);
    }

    public float getBatteryLevel(){
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        if (level == -1 || scale == 1){
            return 50.0f;
        }
        return ((float) level / (float) scale) * 100.0f;
    }

    public class drawingView extends View {
        Typeface tf;
        Paint mBackgroundPaint, mTextPaint, mTextPaintBack;
        int hours, minutes, seconds, weekday, year, month, day;
        float battery;
        public drawingView(Context context, int hours, int minutes, int seconds, int weekday, int year, int month, int day, float battery){
            super(context);
            tf = Typeface.createFromAsset(getAssets(), "digi.ttf");
            mBackgroundPaint = new Paint();
            mBackgroundPaint.setColor(ContextCompat.getColor(getApplicationContext(), R.color.background));

            mTextPaint = new Paint();
            mTextPaint.setColor(ContextCompat.getColor(getApplicationContext(), R.color.text));
            mTextPaint.setAntiAlias(true);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            mTextPaint.setTextSize(getResources().getDimension(R.dimen.text_size));
            mTextPaint.setTypeface(tf);

            mTextPaintBack = new Paint();
            mTextPaintBack.setColor(ContextCompat.getColor(getApplicationContext(), R.color.text_back));
            mTextPaintBack.setAntiAlias(true);
            mTextPaintBack.setTextAlign(Paint.Align.CENTER);
            mTextPaintBack.setTextSize(getResources().getDimension(R.dimen.text_size));
            mTextPaintBack.setTypeface(tf);

            this.hours = hours;
            this.minutes = minutes;
            this.seconds = seconds;
            this.weekday = weekday;
            this.year = year;
            this.month = month;
            this.day = day;
            this.battery = battery;
        }

        @Override
        protected void onDraw(Canvas canvas){
            super.onDraw(canvas);
            float width =  getWidth();
            float height = getHeight();
            canvas.drawRect(0,0, width, height, mBackgroundPaint);

            float centerX = width / 2f;
            float centerY = height / 2f;

       /*     int cur_hour = hours;
            String cur_ampm = "AM";
            if (cur_hour == 0){
                cur_hour = 12;
            }
            if (cur_hour > 12){
                cur_hour = cur_hour - 12;
                cur_ampm = "PM";
            }*/

            String text = String.format("%02d:%02d", hours, minutes);

            String day_of_week = "";

            if (weekday == GregorianCalendar.MONDAY) {
                day_of_week = "DL.";
            } else if (weekday == GregorianCalendar.TUESDAY) {
                day_of_week = "DM.";
            } else if (weekday == GregorianCalendar.WEDNESDAY) {
                day_of_week = "DC.";
            } else if (weekday == GregorianCalendar.THURSDAY) {
                day_of_week = "DJ.";
            } else if (weekday == GregorianCalendar.FRIDAY) {
                day_of_week = "DV.";
            } else if (weekday == GregorianCalendar.SATURDAY) {
                day_of_week = "DS.";
            } else if (weekday == GregorianCalendar.SUNDAY) {
                day_of_week = "DG.";
            }

            String text2 = String.format("%s %02d-%02d-%d", day_of_week, day, month, year);

            //String batteryLevel = "Batería: " + (int) battery + "%";
           // canvas.drawText("00 00 00", centerX, centerY, mTextPaintBack);

            mTextPaint.setColor(ContextCompat.getColor(getApplicationContext(), R.color.text));
            mTextPaint.setTextSize(getResources().getDimension(R.dimen.text_size));

            canvas.drawText(text, centerX, centerY, mTextPaint);

            mTextPaint.setColor(ContextCompat.getColor(getApplicationContext(), R.color.text));
            mTextPaint.setTextSize(getResources().getDimension(R.dimen.text_size_small));
            canvas.drawText( text2,
                    centerX,
                    centerY + getResources().getDimension(R.dimen.text_size_small),
                    mTextPaint);
        }
    }
}