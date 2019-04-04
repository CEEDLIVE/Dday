package com.example.ceedlive.dday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.example.ceedlive.dday.activity.DetailActivity;
import com.example.ceedlive.dday.adapter.DdayListAdapter;
import com.example.ceedlive.dday.dto.AnniversaryInfo;

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
     *  데이터베이스 연동 (SharedPreferences 사용, 이후 SQLite, 외부 DB 연동 순으로 리팩토링 예정)
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

    private List<AnniversaryInfo> anniversaryInfoList = new ArrayList<>();
    private String anniversaryInfoKey;
    private String anniversaryInfoJsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();// 변수 초기화
        setEvent();// 이벤트 설정
//        setDummyData();// 데이터 세팅 [확장 리스트 뷰 (ExpandableListView) 사용]
        setSharedPreferencesData();// 데이터 세팅 [확장 리스트 뷰 (ExpandableListView) 사용]
    }

    @Override
    protected void initialize() {
        expandableListView = findViewById(R.id.expandableListView);
        btnCreate = findViewById(R.id.btnCreate);

        SharedPreferences sharedPreferences = getSharedPreferences("sFile", MODE_PRIVATE);

        // SharedPreferences 값 삭제
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
//        editor.commit();

        // How to get all keys of SharedPreferences programmatically in Android?
        Map<String, ?> allEntries = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("main map values", entry.getKey() + ": " + entry.getValue().toString());
            anniversaryInfoKey = entry.getKey();
            anniversaryInfoJsonString = entry.getValue().toString();
            AnniversaryInfo anniversaryInfo = gson.fromJson(anniversaryInfoJsonString, AnniversaryInfo.class);
            anniversaryInfo.setUniqueKey(anniversaryInfoKey);
            anniversaryInfoList.add(anniversaryInfo);
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

    private void setSharedPreferencesData() {
        for (AnniversaryInfo anniversaryInfo : anniversaryInfoList) {
            ArrayList<String> arrayChicken = new ArrayList<>();
            arrayChicken.add(anniversaryInfo.getTitle());
            arrayChicken.add(anniversaryInfo.getDate());
            arrayChicken.add(anniversaryInfo.getUniqueKey());

            arrayChild.put(anniversaryInfo.getUniqueKey(), arrayChicken);
        }
        expandableListView.setAdapter(new DdayListAdapter(anniversaryInfoList, arrayChild,this));
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

//        expandableListView.setAdapter(new DdayListAdapter(arrayGroup, arrayChild,this));
    }

}
