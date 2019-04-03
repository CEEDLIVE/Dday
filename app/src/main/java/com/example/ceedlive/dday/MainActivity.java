package com.example.ceedlive.dday;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.ceedlive.dday.activity.DetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {

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
     *  데이터베이스 연동 (SharedPreferences 사용, 이후 SQLite, 외부 DB 연동 순으로 리팩토링)
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
    private Button btnCreate;
    private ArrayList<String> arrayGroup = new ArrayList<>();
    private HashMap<String, ArrayList<String>> arrayChild = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        setEvent();
        setDummyData();// 디데이 일정 더미 데이터 세팅하기 (확장 리스트 뷰 사용)
    }

    @Override
    protected void initialize() {
        expandableListView = findViewById(R.id.expandableListView);
        btnCreate = findViewById(R.id.btnCreate);

        SharedPreferences sharedPreferences = getSharedPreferences("sFile", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
//        editor.commit();

        // How to get all keys of SharedPreferences programmatically in Android?
        Map<String, ?> allEntries = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("main map values", entry.getKey() + ": " + entry.getValue().toString());
        }
    }

    private void setEvent() {
        // Annoymous class(익명 클래스)를 통한 클릭이벤트
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 액티비티 전환 코드
                // 인텐트 선언 -> 현재 액티비티, 넘어갈 액티비티
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                // 인텐트실행
                startActivity(intent);
            }
        });
        // reference: https://medium.com/@henen/%EB%B9%A0%EB%A5%B4%EA%B2%8C-%EB%B0%B0%EC%9A%B0%EB%8A%94-%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-clickevent%EB%A5%BC-%EB%A7%8C%EB%93%9C%EB%8A%94-3%EA%B0%80%EC%A7%80-%EB%B0%A9%EB%B2%95-annoymous-class-%EC%9D%B5%EB%AA%85-%ED%81%B4%EB%9E%98%EC%8A%A4-implements-1b1fbe1a74c0
    }

    private void setDummyData() {
        // set dummy data
        for (int i=0; i<100; i++) {
            if (i % 3 == 0) {
                arrayGroup.add(i+1 + "번가 피자");

                ArrayList<String> arrayPizza = new ArrayList<>();
                arrayPizza.add(i+1 + "번가 치즈");
                arrayPizza.add(i+1 + "번가 고구마");
                arrayPizza.add(i+1 + "번가 콤비네이션");

                arrayChild.put(arrayGroup.get(i), arrayPizza);

            } else if (i % 3 == 1) {
                arrayGroup.add(i+1 + "번가 치킨");

                ArrayList<String> arrayChicken = new ArrayList<>();
                arrayChicken.add(i+1 + "번가 후라이드");
                arrayChicken.add(i+1 + "번가 양념");
                arrayChicken.add(i+1 + "번가 반반");

                arrayChild.put(arrayGroup.get(i), arrayChicken);

            } else if (i % 3 == 2) {
                arrayGroup.add(i+1 + "번가 중식");

                ArrayList<String> arrayChinese = new ArrayList<>();
                arrayChinese.add(i+1 + "번가 짜장면");
                arrayChinese.add(i+1 + "번가 짬뽕");
                arrayChinese.add(i+1 + "번가 볶음밥");

                arrayChild.put(arrayGroup.get(i), arrayChinese);
            }
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

        private Map arrayChild;

        /**
         * ListView에 Item을 세팅할 요청자의 정보가 들어감
         */
        private Context context;

        /**
         * 생성자
         * @param arrayGroup
         * @param context
         */
        public CustomExpandableListViewAdapter(ArrayList arrayGroup, HashMap arrayChild, Context context) {
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
            ArrayList childrenList = (ArrayList) arrayChild.get( arrayGroup.get(groupPosition) );
            return childrenList.size();
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
            ArrayList childrenList = (ArrayList) arrayChild.get( arrayGroup.get(groupPosition) );
            Object child = childrenList.get(childPosition);
            return child;
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
            return childPosition;
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

            String groupName = (String) arrayGroup.get(groupPosition);

            tvTitle.setText(groupName);
            tvDate.setText("2019-04-03");
            tvDay.setText("D-31");

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            String groupName = (String) arrayGroup.get(groupPosition);
            ArrayList<String> detail = (ArrayList<String>) arrayChild.get(groupName);
            String childName = detail.get(childPosition);

            if (convertView == null) {
                // Item Cell에 Layout을 적용시킬 Inflator 객체를 생성한다.
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

                // Item Cell에 Layout을 적용시킨다.
                // 실제 객체는 이곳에 있다.
                convertView = inflater.inflate(R.layout.listview_child, parent, false);
            }

            TextView tvChild = (TextView) convertView.findViewById(R.id.tvChild);
            tvChild.setText(childName);

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
