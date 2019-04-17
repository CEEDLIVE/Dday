package com.example.ceedlive.dday.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ceedlive.dday.R;
import com.example.ceedlive.dday.activity.MainActivity;
import com.example.ceedlive.dday.data.DdayItem;
import com.example.ceedlive.dday.holder.DdayViewHolder;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class DdayListAdapter extends BaseAdapter {
    /**
     * ListView에 세팅할 Item 정보들
     */
    private List<DdayItem> mArrayGroup;

    /**
     * ListView에 Item을 세팅할 요청자의 정보가 들어감
     */
    private Context context;

    private Calendar mTargetCalendar, mBaseCalendar;

    private DdayViewHolder ddayViewHolder;

    private int mChedkedItemSize = 0;

    /**
     * 생성자
     * @param arrayGroup
     * @param context
     */
    public DdayListAdapter(List<DdayItem> arrayGroup, Context context) {
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

    /**
     * Adapter 가 가지고 있는 data 를 어떻게 보여줄 것인가를 정의하는 데 쓰인다.
     * 리스트뷰를 예를 들면 하나의 리스트 아이템의 모양을 결정하는 역할을 하는 것이다.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // 뷰홀더 패턴 적용
        if (convertView == null) {
            // Item Cell에 Layout을 적용시킬 Inflator 객체를 생성한다.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

            // Item Cell에 Layout을 적용시킨다.
            // 실제 객체는 이곳에 있다.
            convertView = inflater.inflate(R.layout.listview_group, parent, false);
            ddayViewHolder = new DdayViewHolder();

            // 화면에 표시될 view로부터 위젯에 대한 데이터 획득
            ddayViewHolder.checkBox = convertView.findViewById(R.id.lv_group_checkbox);
            ddayViewHolder.textViewTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            ddayViewHolder.textViewDate = (TextView) convertView.findViewById(R.id.tvDate);
            ddayViewHolder.textViewDay = (TextView) convertView.findViewById(R.id.tvDay);
            ddayViewHolder.textViewDescription = (TextView) convertView.findViewById(R.id.listview_group_tv_description);

            ddayViewHolder.btnEdit = (ImageView) convertView.findViewById(R.id.listview_group_btn_edit);
            ddayViewHolder.btnDelete = (ImageView) convertView.findViewById(R.id.listview_group_btn_delete);
            ddayViewHolder.btnNoti = (ImageView) convertView.findViewById(R.id.listview_group_btn_noti);

            ddayViewHolder.generalLayout = convertView.findViewById(R.id.listview_group_general);
            ddayViewHolder.detailLayout = convertView.findViewById(R.id.listview_group_detail);

            ddayViewHolder.ivStatusIcon = convertView.findViewById(R.id.iv_status_icon);

            convertView.setTag(ddayViewHolder);
        } else {
            // 캐시된 뷰가 있을 경우 저장된 뷰홀더를 사용한다.
            ddayViewHolder = (DdayViewHolder) convertView.getTag();
        }

        // Data Set 에서 position 에 위치한 데이터 획득
        final DdayItem ddayItem = mArrayGroup.get(position);

        // 각 위젯에 데이터 반영
        ddayViewHolder.textViewTitle.setText(ddayItem.getTitle());
        ddayViewHolder.textViewDate.setText(ddayItem.getDate());
        ddayViewHolder.textViewDescription.setText(ddayItem.getDescription());

        // Set Date
        String selectedDate = ddayItem.getDate();
        String[] arrDate = selectedDate.split("/");

        String strYear = arrDate[0];
        String strMonth = arrDate[1];
        String strDay = arrDate[2];

        int year = Integer.parseInt(strYear);
        int month = Integer.parseInt(strMonth);
        int day = Integer.parseInt(strDay);

        ddayViewHolder.textViewDay.setText(getDiffDays(year, month - 1, day));

        mTargetCalendar.set(Calendar.YEAR, year);
        mTargetCalendar.set(Calendar.MONTH, month - 1);
        mTargetCalendar.set(Calendar.DAY_OF_MONTH, day);

        ddayViewHolder.generalLayout.setTag("generalLayout" + position);
        ddayViewHolder.detailLayout.setTag("detailLayout" + position);
        ddayViewHolder.checkBox.setTag("checkBox" + position);

        ddayViewHolder.btnEdit.setTag(ddayItem.get_id());
        ddayViewHolder.btnDelete.setTag(ddayItem.get_id());
        ddayViewHolder.btnNoti.setTag(ddayItem.get_id());

        boolean isNotification = ddayItem.getNotification() == 1 ? true : false;
//        ddayViewHolder.btnNoti.setEnabled(isEnabled);

        ddayViewHolder.btnNoti.setImageResource(isNotification ?
                R.drawable.ic_twotone_notifications_off_24px : R.drawable.ic_twotone_notifications_active_24px);

        ddayViewHolder.btnEdit.setImageResource(R.drawable.ic_twotone_edit_24px);
        ddayViewHolder.btnDelete.setImageResource(R.drawable.ic_twotone_delete_24px);

        ddayViewHolder.ivStatusIcon.setImageResource(isNotification ?
                R.drawable.ic_twotone_notifications_active_24px : R.drawable.ic_twotone_notifications_off_24px);

        // 롱클릭/온클릭
        // 로우별 isChecked, isVisibleDetail 값에 따른 체크상태를 표시
        ddayViewHolder.checkBox.setChecked(ddayItem.getIsChecked());
        ddayViewHolder.checkBox.setVisibility(ddayItem.getIsChecked() ? View.VISIBLE : View.GONE);
        ddayViewHolder.detailLayout.setVisibility(ddayItem.getIsVisibleDetail() ? View.VISIBLE : View.GONE);

        // 롱클릭
        ddayViewHolder.generalLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                CheckBox checkBox = view.findViewWithTag("checkBox" + position);
                // findViewWithTag

                if ( checkBox.isChecked() ) {
                    checkBox.setVisibility(View.GONE);
                    checkBox.setChecked(false);
                    ddayItem.setIsChecked(false);

                    if (mChedkedItemSize > 0) {
                        mChedkedItemSize--;
                        MainActivity activity = (MainActivity) context;
                        activity.removeChecked(ddayItem.get_id());
                    }

                    if (mChedkedItemSize < 1) {
                        Log.e("onLongClick", "전부 체크 해제됨, 삭제 버튼 사라짐");
                        MainActivity activity = (MainActivity) context;
                        activity.handleFabVisibility(false);
                    }

                    Log.e("if checkBox.isChecked()", mChedkedItemSize + "");

                } else {
                    checkBox.setVisibility(View.VISIBLE);
                    checkBox.setChecked(true);
                    ddayItem.setIsChecked(true);

                    mChedkedItemSize++;

                    MainActivity activity = (MainActivity) context;
                    activity.addChecked(ddayItem.get_id());
                    activity.handleFabVisibility(true);

                    Log.e("else", mChedkedItemSize + "");
                }

                return true;
                // 롱클릭 시 온클릭 이벤트 발생 방지: return true 이어야 함.
                // 다음 이벤트 계속 진행 false, 이벤트 완료 true
            }
        });

        // 온클릭
        ddayViewHolder.generalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout parent = (LinearLayout) view.getParent();
                LinearLayout detail = parent.findViewWithTag("detailLayout" + position);

                // View.GONE: 8 - 값이 0이 아닌 8인 것에 주의
                if ( View.VISIBLE == detail.getVisibility() ) {
                    detail.setVisibility(View.GONE);
                    ddayItem.setIsVisibleDetail(false);
                } else {
                    detail.setVisibility(View.VISIBLE);
                    ddayItem.setIsVisibleDetail(true);
                }
            }
        });

        // 노티
        ddayViewHolder.btnNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) context;
                activity.onClickNoti(isNotification, ddayItem.get_id());
            }
        });

        // 수정
        ddayViewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) context;
                activity.onClickEdit(ddayItem.get_id());
            }
        });

        // 삭제
        ddayViewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) context;
                activity.onClickDelete(ddayItem.get_id());
            }
        });

        return convertView;
    }

    /**
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
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
