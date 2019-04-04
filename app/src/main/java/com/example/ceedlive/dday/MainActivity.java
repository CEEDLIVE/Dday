package com.example.ceedlive.dday;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ceedlive.dday.activity.DetailActivity;
import com.example.ceedlive.dday.adapter.DdayListAdapter;
import com.example.ceedlive.dday.dto.AnniversaryInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends BaseActivity {

    /*
     * 디데이 프로젝트 관련 내용은 README.md 에 정리
     */
    private View mListViewChild;

    private LinearLayout mLayoutNoContent;
    private ExpandableListView mListViewContent;
    private Button mBtnCreate, mBtnEdit, mBtnDelete;

    private ArrayList<String> mArrayGroup = new ArrayList<>();
    private Map<String, Map<String, String>> mAnniversaryInfoChild = new HashMap<>();

    private List<AnniversaryInfo> mAnniversaryInfoList = new ArrayList<>();
    private String mAnniversaryInfoKey;
    private String mAnniversaryInfoJsonString;

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
        mLayoutNoContent = findViewById(R.id.main_ll_no_content);
        mListViewContent = findViewById(R.id.expandableListView);

        mBtnCreate = findViewById(R.id.btnCreate);

        // 출처: https://itpangpang.xyz/143 [ITPangPang]
        // 출처: https://bitnori.tistory.com/entry/Android-다른-레이아웃Layout의-위젯-제어하기-LayoutInflater-사용 [Bitnori's Blog]
        // https://stackoverflow.com/questions/28193552/null-pointer-exception-on-setonclicklistener

        SharedPreferences sharedPreferences = getSharedPreferences("sFile", MODE_PRIVATE);

        // SharedPreferences 값 삭제
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
//        editor.commit();

        // How to get all keys of SharedPreferences programmatically in Android?
        Map<String, ?> allEntries = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("main map values", entry.getKey() + ": " + entry.getValue().toString());
            mAnniversaryInfoKey = entry.getKey();
            mAnniversaryInfoJsonString = entry.getValue().toString();
            AnniversaryInfo anniversaryInfo = gson.fromJson(mAnniversaryInfoJsonString, AnniversaryInfo.class);
            anniversaryInfo.setUniqueKey(mAnniversaryInfoKey);
            mAnniversaryInfoList.add(anniversaryInfo);
        }

        if (mAnniversaryInfoList.isEmpty()) {
            mLayoutNoContent.setVisibility(View.VISIBLE);
            mListViewContent.setVisibility(View.INVISIBLE);
        } else {
            mListViewContent.setVisibility(View.VISIBLE);
            mLayoutNoContent.setVisibility(View.INVISIBLE);
        }
    }

    private void setEvent() {
        // Annoymous class(익명 클래스)를 통한 클릭이벤트
        mBtnCreate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                moveDetailActivity();
            }
        });
        // reference: https://medium.com/@henen/%EB%B9%A0%EB%A5%B4%EA%B2%8C-%EB%B0%B0%EC%9A%B0%EB%8A%94-%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-clickevent%EB%A5%BC-%EB%A7%8C%EB%93%9C%EB%8A%94-3%EA%B0%80%EC%A7%80-%EB%B0%A9%EB%B2%95-annoymous-class-%EC%9D%B5%EB%AA%85-%ED%81%B4%EB%9E%98%EC%8A%A4-implements-1b1fbe1a74c0

        // 등록된 일정이 하나도 없는 경우
        mLayoutNoContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                moveDetailActivity();
            }
        });

        // TODO 다른 레이아웃 위젯 제어하는 방법 찾고 구현해보기
        FrameLayout add_layout = (FrameLayout) findViewById(R.id.main_center_layout);

        // 인플레이션
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListViewChild = inflater.inflate(R.layout.listview_child, add_layout, false);

//        mListViewChild = getLayoutInflater().inflate(R.layout.listview_child, null, false);
        mBtnEdit = (Button) mListViewChild.findViewById(R.id.listview_child_btn_edit);
        mBtnDelete = (Button) mListViewChild.findViewById(R.id.listview_child_btn_delete);

        // 수정
        mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(this, "123", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "수정 버튼 클릭", Toast.LENGTH_SHORT).show();
                Log.d("mBtnEdit", "수정 버튼 클릭");
            }
        });

        // 삭제
        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "삭제 버튼 클릭", Toast.LENGTH_SHORT).show();
                Log.d("mBtnEdit", "삭제 버튼 클릭");
            }
        });
    }

    private void moveDetailActivity() {
        // 액티비티 전환 코드
        // 인텐트 선언 -> 현재 액티비티, 넘어갈 액티비티
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        // 인텐트 실행
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // requestCode: 송신자 Activity 구별하기 위한 값
        // resultCode: 수신자 Activity 에서 송신자 Activity 로 어떠한 결과코드를 주었는지를 나타냄
        // Intent data: 수신자 Activity 에서 송신자 Activity 로 보낸 결과 데이터
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String jsonStringAddedAnniversaryInfo = data.getStringExtra("newItem");
                AnniversaryInfo newItem = gson.fromJson(jsonStringAddedAnniversaryInfo, AnniversaryInfo.class);
                mAnniversaryInfoList.add(newItem);
                setSharedPreferencesData();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // 만약 반환값이 없을 경우의 코드를 여기에 작성하세요.
                Toast.makeText(this, "만약 반환값이 없을 경우의 코드를 여기에 작성하세요.", Toast.LENGTH_LONG);
            }
        }
    }

    private void setSharedPreferencesData() {
        for (AnniversaryInfo anniversaryInfo : mAnniversaryInfoList) {
            Map<String, String> detail = new HashMap();
            detail.put("description", anniversaryInfo.getDescription());
            mAnniversaryInfoChild.put(anniversaryInfo.getUniqueKey(), detail);
        }
        mListViewContent.setAdapter(new DdayListAdapter(mAnniversaryInfoList, mAnniversaryInfoChild,this));
    }

}
