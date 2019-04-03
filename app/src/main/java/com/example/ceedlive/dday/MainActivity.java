package com.example.ceedlive.dday;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

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

    private ListView lvDDayListView;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();
        lvDDayListView = findViewById(R.id.dDayListView);

        // this: 현재 Activity의 context

        // 디데이 세팅하기
        setDDay();
    }

    private void setDDay() {
        // set dummy data
        List dDayList = new ArrayList();
        for (int i=0; i<300; i++) {
            dDayList.add(new DdayVO("제목입니다." + i, "글쓴이입니다.", new Random().nextInt(9999)));
        }
        lvDDayListView.setAdapter(new DdayListViewAdapter(dDayList, this));
    }

    /**
     * Set Adapter
     */
    private class DdayListViewAdapter extends BaseAdapter {

        /**
         * ListView에 세팅할 Item 정보들
         */
        private List dDayList;

        /**
         * ListView에 Item을 세팅할 요청자의 정보가 들어감
         */
        private Context context;

        /**
         * 생성자
         * @param dDayList
         * @param context
         */
        public DdayListViewAdapter(List dDayList, Context context) {
            this.dDayList = dDayList;
            this.context = context;
        }

        /**
         * ListView에 세팅할 아이템의 갯수
         * @return
         */
        @Override
        public int getCount() {
            return dDayList.size();
        }

        /**
         * position 번째 Item 정보를 가져옴
         * @param i
         * @return
         */
        @Override
        public Object getItem(int i) {
            return dDayList.get(i);
        }

        /**
         * 아이템의 index를 가져옴
         * Item index == i (position)
         * @param i
         * @return
         */
        @Override
        public long getItemId(int i) {
            return i;
        }

        /**
         * ListView에 Item을 세팅함
         * position 번째 있는 아이템을 가져와서 convertView에 넣은 다음 parent에서 보여주면 된다?
         * @param i : 현재 보여질 아이템의 인덱스, 0 ~ getCount()까지 증가
         * @param view : ListView의 Item Cell(한 칸) 객체를 가져옴
         * @param viewGroup : ListView
         * @return
         */
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            /**
             * 가장 간단한 방법
             * 사용자가 처음으로 Flicking 을 할 때, 아래쪽에 만들어지는 Cell(한 칸)은 Null이다.
             */
            if (view == null) {
                // Item Cell에 Layout을 적용시킬 Inflator 객체를 생성한다.
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

                // Item Cell에 Layout을 적용시킨다.
                // 실제 객체는 이곳에 있다.
                view = inflater.inflate(R.layout.list_item, viewGroup, false);
            }

            TextView tvSubject = (TextView) view.findViewById(R.id.tvSubject);
            TextView tvAuthor = (TextView) view.findViewById(R.id.tvAuthor);
            TextView tvHitCount = (TextView) view.findViewById(R.id.tvHitCount);

            DdayVO dDay = (DdayVO) getItem(i);
            tvSubject.setText(dDay.getSubject());
            tvAuthor.setText(dDay.getAuthor());
            tvHitCount.setText(dDay.getHitCount() + "");

            return view;
        }
    }
}
