package com.example.ceedlive.dday.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ceedlive.dday.BaseActivity;
import com.example.ceedlive.dday.R;
import com.example.ceedlive.dday.adapter.DdayListAdapter;
import com.example.ceedlive.dday.dto.AnniversaryInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends BaseActivity {

    /*
     * 디데이 프로젝트 관련 내용은 Dday 프로젝트 README.md 에 정리
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

    private String sharedPreferencesDataKey;

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
    }

    private void setEvent() {
        // Annoymous class(익명 클래스)를 통한 클릭이벤트
        mBtnCreate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                moveDetailActivity(null);
            }
        });
        // reference: https://medium.com/@henen/%EB%B9%A0%EB%A5%B4%EA%B2%8C-%EB%B0%B0%EC%9A%B0%EB%8A%94-%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-clickevent%EB%A5%BC-%EB%A7%8C%EB%93%9C%EB%8A%94-3%EA%B0%80%EC%A7%80-%EB%B0%A9%EB%B2%95-annoymous-class-%EC%9D%B5%EB%AA%85-%ED%81%B4%EB%9E%98%EC%8A%A4-implements-1b1fbe1a74c0

        // 등록된 일정이 하나도 없는 경우
        mLayoutNoContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                moveDetailActivity(null);
            }
        });

        // FIXME 다른 레이아웃 위젯 제어하는 방법 찾고 구현해보기
        // 부모뷰
        // ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);

        // 인플레이터 획득
        // LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 인플레이트 되어서 부모뷰(scrollView)에 붙음
        // View subLayout = inflater.inflate(R.layout.sub_layout, scrollView);

        // 서브레이아웃에 있는 텍스트뷰 변경 test
        // TextView subTextView = (TextView) subLayout.findViewById(R.id.textView);
        // subTextView.setText("서브테스트");

        // root
        // attachToRoot가 true일경우 생성되는 View가 추가될 부모 뷰
        // attachToRoot가 false일 경우에는 LayoutParams값을 설정해주기 위한 상위 뷰
        // null로 설정할경우 android:layout_xxxxx값들이 무시됨.

        // attachToRoot
        // true일 경우 생성되는 뷰를 root의 자식으로 만든다.
        // false일 경우 root는 생성되는 View의 LayoutParam을 생성하는데만 사용된다.
    }

    private void moveDetailActivity(@Nullable String sharedPreferencesDataKey) {
        // 액티비티 전환 코드
        // 인텐트 선언 -> 현재 액티비티, 넘어갈 액티비티
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);

        // 수정/삭제
        if (null != sharedPreferencesDataKey) {
            intent.putExtra("sharedPreferencesDataKey", sharedPreferencesDataKey);
        }

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
//                String jsonStringAddedAnniversaryInfo = data.getStringExtra("newItem");
//                AnniversaryInfo newItem = gson.fromJson(jsonStringAddedAnniversaryInfo, AnniversaryInfo.class);
//                mAnniversaryInfoList.add(newItem);
                setSharedPreferencesData();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // 만약 반환값이 없을 경우의 코드를 여기에 작성하세요.
                Toast.makeText(this, "만약 반환값이 없을 경우의 코드를 여기에 작성하세요.", Toast.LENGTH_LONG);
            }
        }
    }

    private void setSharedPreferencesData() {

        // 출처: https://itpangpang.xyz/143 [ITPangPang]
        // 출처: https://bitnori.tistory.com/entry/Android-다른-레이아웃Layout의-위젯-제어하기-LayoutInflater-사용 [Bitnori's Blog]
        // https://stackoverflow.com/questions/28193552/null-pointer-exception-on-setonclicklistener
        mAnniversaryInfoList.clear();

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

        for (AnniversaryInfo anniversaryInfo : mAnniversaryInfoList) {
            Map<String, String> detail = new HashMap();
            detail.put("description", anniversaryInfo.getDescription());
            mAnniversaryInfoChild.put(anniversaryInfo.getUniqueKey(), detail);
        }
        mListViewContent.setAdapter(new DdayListAdapter(mAnniversaryInfoList, mAnniversaryInfoChild,this));
    }

    /**
     *
     * @param view
     */
    public void onClickEdit(View view) {
        final String sharedPreferencesDataKey = (String) view.getTag();
        Toast.makeText(getApplicationContext(), sharedPreferencesDataKey + " 수정", Toast.LENGTH_SHORT).show();

        moveDetailActivity(sharedPreferencesDataKey);
    }

    /**
     *
     * @param view
     */
    public void onClickDelete(View view) {
        sharedPreferencesDataKey = (String) view.getTag();
        Toast.makeText(getApplicationContext(), sharedPreferencesDataKey + " 삭제", Toast.LENGTH_SHORT).show();

        showDialog();
    }

    private void deleteItem() {

    }

    private void showDialog() {
        // 다이얼로그
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // 다이얼로그 값/옵션 세팅
        alertDialogBuilder
                .setTitle(R.string.alert_title_delete_dday)
                .setMessage(R.string.alert_message_delete_dday)
                .setCancelable(false)
                .setPositiveButton("삭제",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 일정 삭제
                                deleteItem();
                            }
                        })
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 다이얼로그를 취소한다.
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        // reference: https://mainia.tistory.com/2017
        // reference: https://m.blog.naver.com/PostView.nhn?blogId=sgepyh2916&logNo=221176134263&proxyReferer=https%3A%2F%2Fwww.google.com%2F
    }

}
