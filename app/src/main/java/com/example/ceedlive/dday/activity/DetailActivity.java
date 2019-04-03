package com.example.ceedlive.dday.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.ceedlive.dday.R;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private TextView tvToday;
    private TextView tvSelectDate;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Button btnDetailMerge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initObject();
        setEvent();
    }

    private void initObject() {
        tvToday = findViewById(R.id.tvToday);
        tvSelectDate = findViewById(R.id.tv_detail_select_date);

        //현재 날짜와 시간을 가져오기위한 Calendar 인스턴스 선언
        Calendar cal = new GregorianCalendar();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);

        btnDetailMerge = findViewById(R.id.btn_detail_merge);
    }

    private void setEvent() {

        mDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub

                        //사용자가 입력한 값을 가져온뒤
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;

                        //텍스트뷰의 값을 업데이트함
                        doUpdateTextViewDate();
                    }
                };

        tvSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(DetailActivity.this, mDateSetListener, mYear,
                        mMonth, mDay).show();
            }
        });

        btnDetailMerge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void doUpdateTextViewDate() {
        tvSelectDate.setText(String.format(Locale.getDefault(), "%d/%d/%d", mYear, mMonth + 1, mDay));
    }
}
