package com.example.ceedlive.dday.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ceedlive.dday.R;
import com.example.ceedlive.dday.activity.MainActivity;
import com.example.ceedlive.dday.dto.AnniversaryInfo;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class DdayListAdapter extends BaseAdapter {
    /**
     * ListView에 세팅할 Item 정보들
     */
    private List<AnniversaryInfo> mArrayGroup;


    /**
     * ListView에 Item을 세팅할 요청자의 정보가 들어감
     */
    private Context context;

    private Calendar mTargetCalendar, mBaseCalendar;

    /**
     * 생성자
     * @param arrayGroup
     * @param context
     */
    public DdayListAdapter(List<AnniversaryInfo> arrayGroup, Context context) {
        this.mArrayGroup = arrayGroup;
        this.context = context;

        // 날짜와 시간을 가져오기위한 Calendar 인스턴스 선언
        this.mTargetCalendar = new GregorianCalendar();
        this.mBaseCalendar = new GregorianCalendar();
    }

    @Override
    public int getCount() {
        return mArrayGroup.size();
    }

    @Override
    public Object getItem(int i) {
        return mArrayGroup.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // Item Cell에 Layout을 적용시킬 Inflator 객체를 생성한다.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

            // Item Cell에 Layout을 적용시킨다.
            // 실제 객체는 이곳에 있다.
            convertView = inflater.inflate(R.layout.listview_group, parent, false);
        }

        AnniversaryInfo anniversaryInfo = mArrayGroup.get(position);

        final RelativeLayout relativeLayout = convertView.findViewById(R.id.listview_group_general);
        final LinearLayout linearLayout = convertView.findViewById(R.id.listview_group_detail);

        relativeLayout.setTag(false);
        linearLayout.setVisibility(View.GONE);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        TextView tvDay = (TextView) convertView.findViewById(R.id.tvDay);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.listview_group_tv_description);

        final Button mBtnEdit = (Button) convertView.findViewById(R.id.listview_group_btn_edit);
        final Button mBtnDelete = (Button) convertView.findViewById(R.id.listview_group_btn_delete);

        tvTitle.setText(anniversaryInfo.getTitle());
        tvDate.setText(anniversaryInfo.getDate());
        tvDescription.setText(anniversaryInfo.getDescription());

        // Set Date
        String selectedDate = anniversaryInfo.getDate();
        String[] arrDate = selectedDate.split("/");

        String strYear = arrDate[0];
        String strMonth = arrDate[1];
        String strDay = arrDate[2];

        int year = Integer.parseInt(strYear);
        int month = Integer.parseInt(strMonth);
        int day = Integer.parseInt(strDay);

        tvDay.setText(getDiffDays(year, month - 1, day));

        mTargetCalendar.set(Calendar.YEAR, year);
        mTargetCalendar.set(Calendar.MONTH, month - 1);
        mTargetCalendar.set(Calendar.DAY_OF_MONTH, day);

        mBtnEdit.setTag(anniversaryInfo.getUniqueKey());
        mBtnDelete.setTag(anniversaryInfo.getUniqueKey());

        onClickLayout(relativeLayout, linearLayout);
        onClickButtonEdit(mBtnEdit);
        onClickButtonDelete(mBtnDelete);

        return convertView;
    }

    private void onClickLayout(final View parentView, final View childView) {
        parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isVisible = (boolean) parentView.getTag();
                if (!isVisible) {
                    childView.setVisibility(View.VISIBLE);

                    // 안드로이드 레이어 visible 설정 시 사르륵 애니메이션 넣기
//                    Animation animation = new AlphaAnimation(0, 1);
//                    animation.setDuration(1000);
//                    childView.setVisibility(View.VISIBLE);
//                    childView.setAnimation(animation);

                } else {
                    childView.setVisibility(View.GONE);
                }
                parentView.setTag(!isVisible);
            }
        });
    }

    private void onClickButtonEdit(final View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) context;
                activity.onClickEdit(view);
            }
        });
    }

    private void onClickButtonDelete(final View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) context;
                activity.onClickDelete(view);
            }
        });
    }

    private String getDiffDays(int year, int month, int day) {
        // Calendar 두 날짜 간 차이 구하기
        mTargetCalendar.set(Calendar.YEAR, year);
        mTargetCalendar.set(Calendar.MONTH, month);
        mTargetCalendar.set(Calendar.DAY_OF_MONTH, day);

        // 밀리초(1000분의 1초) 단위로 두 날짜 간 차이를 변환 후 초 단위로 다시 변환
        long diffSec = (mTargetCalendar.getTimeInMillis() - mBaseCalendar.getTimeInMillis()) / 1000;
        // 1분(60초), 1시간(60분), 1일(24시간) 이므로 다음과 같이 나누어 1일 단위로 다시 변환
        long diffDays = diffSec / (60 * 60 * 24);

        int flag = diffDays > 0 ? 1 : diffDays < 0 ? -1 : 0;

        String msg = "";

        switch (flag) {
            case 1:
                msg = context.getString(R.string.dday_valid_prefix) + Math.abs(diffDays);
                break;
            case 0:
                msg = context.getString(R.string.dday_today);
                break;
            case -1:
                msg = context.getString(R.string.dday_invalid_prefix) + Math.abs(diffDays);
                break;
            default:
                msg = "";
        }

        return msg;
    }

}
