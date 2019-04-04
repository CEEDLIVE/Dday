package com.example.ceedlive.dday;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.ceedlive.dday.dto.DdayDto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity2 extends AppCompatActivity {

    /*
     * 디데이 앱
     *
     * 1단계
     *  화면 구성
     *  - 리스트 화면 (스크롤)
     *  - 입력 화면
     *      : 캘린더
     *      : 디데이 계산
     *
     * 2단계
     *  데이터베이스 연동
     *  - 리스트 조회
     *  - 저장
     *  - 수정
     *  - 삭제
     *
     * 3단계
     *  마무리
     *  - 앱 이름
     *  - 앱 아이콘
     *  - 스플래시 화면: 앱이 시작할 때 잠깐 표시되는 스플래시 화면
     *
     */

    private ExpandableListView expandableListView;
    private List<DdayDto> arrayGroup = new ArrayList<>();
    private List<Map<String, String>> arrayChild = new ArrayList<>();
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById();

        // this: 현재 Activity의 context

        setDummyData();// 디데이 일정 더미 데이터 세팅하기 (확장 리스트 뷰 사용)
    }

    private void findViewById() {
        handler = new Handler();
        expandableListView = findViewById(R.id.expandableListView);
    }

    private void setDummyData() {
        // set dummy data
        for (int i=0; i<300; i++) {
            arrayGroup.add(new DdayDto(i, i + "번째 일정 제목",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                    new Random().nextInt(9999) + ""));

            Map dummy = new HashMap();
            dummy.put("one", "짜장");
            dummy.put("two", "짬뽕");
            arrayChild.add(dummy);
        }
        expandableListView.setAdapter(new CustomExpandableListViewAdapter(arrayGroup, arrayChild,this));
    }

    /**
     * Set Adapter
     */
    private class CustomExpandableListViewAdapter extends BaseExpandableListAdapter {

        /**
         * ListView에 세팅할 Item 정보들
         */
        private List arrayGroup;

        private List arrayChild;

        /**
         * ListView에 Item을 세팅할 요청자의 정보가 들어감
         */
        private Context context;

        /**
         * 생성자
         * @param arrayGroup
         * @param context
         */
        public CustomExpandableListViewAdapter(List arrayGroup, List arrayChild, Context context) {
            this.arrayGroup = arrayGroup;
            this.arrayChild = arrayChild;
            this.context = context;
        }

        /**
         * ListView에 세팅할 아이템의 갯수
         * @return
         */
        @Override
        public int getGroupCount() {
            return arrayGroup.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 0;
        }

        /**
         * groupPosition 번째 Item 정보를 가져옴
         * @param groupPosition
         * @return
         */
        @Override
        public Object getGroup(int groupPosition) {
            return arrayGroup.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return arrayChild.get( groupPosition );
        }

        /**
         * 아이템의 index를 가져옴
         * Item index == i (position)
         * @param groupPosition
         * @return
         */
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        /**
         * ListView에 Item을 세팅함
         * position 번째 있는 아이템을 가져와서 convertView에 넣은 다음 parent에서 보여주면 된다?
         * @param groupPosition : 현재 보여질 아이템의 인덱스, 0 ~ getCount()까지 증가
         * @param isExpanded : 현재 보여질 아이템의 인덱스, 0 ~ getCount()까지 증가
         * @param convertView : ListView의 Item Cell(한 칸) 객체를 가져옴
         * @param parent : ListView
         * @return
         */
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            /**
             * 가장 간단한 방법
             * 사용자가 처음으로 Flicking 을 할 때, 아래쪽에 만들어지는 Cell(한 칸)은 Null이다.
             */
            if (convertView == null) {
                // Item Cell에 Layout을 적용시킬 Inflator 객체를 생성한다.
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

                // Item Cell에 Layout을 적용시킨다.
                // 실제 객체는 이곳에 있다.
                convertView = inflater.inflate(R.layout.listview_group, parent, false);
            }

            TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            TextView tvDay = (TextView) convertView.findViewById(R.id.tvDay);

            DdayDto dDay = (DdayDto) getGroup(groupPosition);
            tvTitle.setText(dDay.getTitle());
            tvDate.setText(dDay.getDate());
            tvDay.setText(dDay.getDay());

            Log.d("ceedlive", dDay.toString());

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            DdayDto ddayVO = (DdayDto) arrayGroup.get(groupPosition);
            Map detail = (HashMap) arrayChild.get(ddayVO.getId());

            if (convertView == null) {
                // Item Cell에 Layout을 적용시킬 Inflator 객체를 생성한다.
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

                // Item Cell에 Layout을 적용시킨다.
                // 실제 객체는 이곳에 있다.
                convertView = inflater.inflate(R.layout.listview_child, parent, false);
            }

            TextView tvChild = (TextView) convertView.findViewById(R.id.listview_child_tv_description);
            tvChild.setText(detail.get("one").toString());

            Log.d("ceedlive", detail.get("one").toString());

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
