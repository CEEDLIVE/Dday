package com.example.ceedlive.dday.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ceedlive.dday.BaseActivity;
import com.example.ceedlive.dday.Constant;
import com.example.ceedlive.dday.R;
import com.example.ceedlive.dday.adapter.DdayListAdapter;
import com.example.ceedlive.dday.data.DdayItem;
import com.example.ceedlive.dday.helper.DatabaseHelper;
import com.example.ceedlive.dday.service.NotificationService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


public class MainActivity extends BaseActivity {

    /*
     * 디데이 프로젝트 관련 내용은 Dday 프로젝트 README.md 에 정리
     */

    private DatabaseHelper mDatabaseHelper;

    private LinearLayout mLayoutNoContent;
    private ListView mListViewContent;
    private Button mBtnCreate;
    private FloatingActionButton mFabBtn;

    private List<DdayItem> mDdayItemList = new ArrayList<>();
    private String mAnniversaryInfoKey;
    private String mAnniversaryInfoJsonString;

    private String mSharedPreferencesDataKey;
    private int _id;

    private SharedPreferences mSharedPreferences;

    private AlertDialog.Builder mAlertDialogBuilder;
    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();// 변수 초기화
        setEvent();// 이벤트 설정

//        setSharedPreferencesData();// 데이터 세팅 (SharedPreferences 사용)
        setSQLiteData(); // (SQLite)

        // 이렇게 하면 안 된다.
//        testThread(); // 강제 예외 발생 코드, 잘못된 코드임을 알려주는 코드 블럭, 실행 시 앱이 강제종료됨
    }

    /**
     * 다음과 같이 액티비티 메인 쓰레드에서 새로 쓰레드를 생성해서 UI 업데이트를 하면 에러 발생
     */
    @SuppressWarnings("unused")
    private void testThread() {
        // TEST 다음과 같이 액티비티 메인 쓰레드에서 새로 쓰레드를 생성해서 UI 업데이트를 하면 에러 발생
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mBtnCreate = findViewById(R.id.btnCreate);
                mBtnCreate.setVisibility(View.VISIBLE);
                mBtnCreate.setText("Thread test");
            }
        }).start();;
    }

    @Override
    protected void initialize() {
        mLayoutNoContent = findViewById(R.id.main_ll_no_content);
        mListViewContent = findViewById(R.id.expandableListView);

        mBtnCreate = findViewById(R.id.btnCreate);
        mFabBtn = findViewById(R.id.main_btn_fab);
    }

    private void setEvent() {
        onClickButtonCreate();
        onClickFabButtonCreate();
        onClickLayoutNoContent();
    }

    private void onClickButtonCreate() {
        mBtnCreate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                moveDetailActivity(0);
            }
        });
        // Annoymous class(익명 클래스)를 통한 클릭이벤트
        // reference: https://medium.com/@henen/%EB%B9%A0%EB%A5%B4%EA%B2%8C-%EB%B0%B0%EC%9A%B0%EB%8A%94-%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-clickevent%EB%A5%BC-%EB%A7%8C%EB%93%9C%EB%8A%94-3%EA%B0%80%EC%A7%80-%EB%B0%A9%EB%B2%95-annoymous-class-%EC%9D%B5%EB%AA%85-%ED%81%B4%EB%9E%98%EC%8A%A4-implements-1b1fbe1a74c0
    }

    /**
     * Floating Action Button Click Event
     */
    private void onClickFabButtonCreate() {
        mFabBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                moveDetailActivity(0);
            }
        });
    }

    private void onClickLayoutNoContent() {
        // 등록된 일정이 하나도 없는 경우
        mLayoutNoContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                moveDetailActivity(0);
            }
        });
    }

    /**
     * 상세화면 액티비티로 이동
     * @param _id
     */
    private void moveDetailActivity(int _id) {
        // 액티비티 전환 코드
        // 인텐트 선언 -> 현재 액티비티, 넘어갈 액티비티
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);

        // 수정/삭제
        if (_id > 0) {
//            intent.putExtra(Constant.INTENT_DATA_NAME_SHARED_PREFERENCES, _id);
            intent.putExtra(Constant.INTENT_DATA_SQLITE_TABLE_DDAY_ID, _id);
        }

        // 인텐트 실행
        startActivityForResult(intent, Constant.REQUEST_CODE_MAIN_ACTIVITY);
    }

    /**
     * 인텐트 실행 결과 처리 메소드
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("onActivityResult", String.format("requestCode %d resultCode %d", requestCode, resultCode));

        // requestCode: 송신자 Activity 구별하기 위한 값
        // resultCode: 수신자 Activity 에서 송신자 Activity 로 어떠한 결과코드를 주었는지를 나타냄
        // Intent data: 수신자 Activity 에서 송신자 Activity 로 보낸 결과 데이터
        if (Constant.REQUEST_CODE_MAIN_ACTIVITY == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
//                setSharedPreferencesData();
                setSQLiteData();
            }
            if (Activity.RESULT_CANCELED == resultCode) {
                // 만약 반환값이 없을 경우의 코드를 여기에 작성하세요.
                Toast.makeText(this, "만약 반환값이 없을 경우의 코드를 여기에 작성하세요.", Toast.LENGTH_LONG);
            }
        }
    }

    /**
     * SharedPreferences 에 저장된 key/value pair 데이터 세팅하고 출력하기
     */
    private void setSharedPreferencesData() {
        // 출처: https://itpangpang.xyz/143 [ITPangPang]
        // 출처: https://bitnori.tistory.com/entry/Android-다른-레이아웃Layout의-위젯-제어하기-LayoutInflater-사용 [Bitnori's Blog]
        // https://stackoverflow.com/questions/28193552/null-pointer-exception-on-setonclicklistener
        mDdayItemList.clear();

        mSharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        // How to get all keys of SharedPreferences programmatically in Android?
        Map<String, ?> allEntries = mSharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("main map values", entry.getKey() + ": " + entry.getValue().toString());
            mAnniversaryInfoKey = entry.getKey();
            mAnniversaryInfoJsonString = entry.getValue().toString();
            DdayItem ddayItem = gson.fromJson(mAnniversaryInfoJsonString, DdayItem.class);
            ddayItem.setUniqueKey(mAnniversaryInfoKey);
            mDdayItemList.add(ddayItem);
        }

        SortDescending sortDescending = new SortDescending();
        Collections.sort(mDdayItemList, sortDescending);

        if (mDdayItemList.isEmpty()) {
            mLayoutNoContent.setVisibility(View.VISIBLE);
            mListViewContent.setVisibility(View.INVISIBLE);
        } else {
            mListViewContent.setVisibility(View.VISIBLE);
            mLayoutNoContent.setVisibility(View.INVISIBLE);
        }

        mListViewContent.setAdapter(new DdayListAdapter(mDdayItemList, MainActivity.this));
    }

    private void setSQLiteData() {
        // TODO SQLite
        // ==================================================

        mDatabaseHelper = DatabaseHelper.getInstance(MainActivity.this);

        mDdayItemList.clear();

        List<DdayItem> ddayItemList = mDatabaseHelper.getDdayList();

        for (DdayItem ddayItem : ddayItemList) {
            Log.d("SQLite item.toString()", ddayItem.toString());
            mDdayItemList.add(ddayItem);
        }

        SortDescending sortDescending = new SortDescending();
        Collections.sort(mDdayItemList, sortDescending);

        if (mDdayItemList.isEmpty()) {
            mLayoutNoContent.setVisibility(View.VISIBLE);
            mListViewContent.setVisibility(View.INVISIBLE);
        } else {
            mListViewContent.setVisibility(View.VISIBLE);
            mLayoutNoContent.setVisibility(View.INVISIBLE);
        }

        mListViewContent.setAdapter(new DdayListAdapter(mDdayItemList, MainActivity.this));
    }

    class SortDescending implements Comparator<DdayItem> {
        @Override
        public int compare(DdayItem ddayItem1, DdayItem ddayItem2) {
            // SharedPreferences
//            int first = Integer.parseInt( ddayItem1.getUniqueKey().replace(Constant.SHARED_PREFERENCES_KEY_PREFIX, "") );
//            int second = Integer.parseInt( ddayItem2.getUniqueKey().replace(Constant.SHARED_PREFERENCES_KEY_PREFIX, "") );

            // SQLite
            int first = ddayItem1.get_id();
            int second = ddayItem2.get_id();

            int compareValue = 0;

            if (second > first) {
                compareValue = 1;
            }
            if (second == first) {
                compareValue = 0;
            }
            if (second < first) {
                compareValue = -1;
            }

            return compareValue;
        }
    }

    /**
     * 노티 버튼 클릭 시 이벤트 핸들러
     * @param _id
     */
    public void onClickNoti(int _id) {
        try {
//            mSharedPreferencesDataKey = uniqueKey;
//            if (null != mSharedPreferencesDataKey) {
            if (_id > 0) {

                // SharedPreferences
//                SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
//                // TODO SharedPreferences 더 알아보기
//                // 첫번째 인자 name 은 해당 SharedPreferences 의 이름입니다.
//                // 특정 이름으로 생성할수 있고 해당 이름으로 xml 파일이 생성된다고 생각하시면 됩니다.
//
//                String jsonStringValue = sharedPreferences.getString(mSharedPreferencesDataKey, "");
//                DdayItem ddayItem = gson.fromJson(jsonStringValue, DdayItem.class);

                // SQLite
                DdayItem ddayItem = mDatabaseHelper.getDday(_id);

                // FIXME TEST 토스트 띄우기
                Toast.makeText(getApplicationContext(), ddayItem.getTitle() + " 서비스 시작", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, NotificationService.class);
//                intent.putExtra(Constant.INTENT_DATA_NAME_SHARED_PREFERENCES, ddayItem.getUniqueKey()); //전달할 값
                intent.putExtra(Constant.INTENT_DATA_SQLITE_TABLE_DDAY_ID, ddayItem.get_id()); //전달할 값
                startService(intent);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 수정 버튼 클릭 시 이벤트 핸들러
     * @param _id
     */
    public void onClickEdit(int _id) {
        try {
//            mSharedPreferencesDataKey = uniqueKey;

            // TEST
            Log.d("Main onClickEdit" ,  "id: " + _id);

            if (_id > 0) {
                moveDetailActivity(_id);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 삭제 버튼 클릭 시 이벤트 핸들러
     * @param _id
     */
    public void onClickDelete(final int _id) {
        try {
//            mSharedPreferencesDataKey = uniqueKey;

            // TEST
            Log.d("Main onClickDelete" ,  "id: " + _id);

            if (_id > 0) {
                // 다이얼로그
                mAlertDialogBuilder = new AlertDialog.Builder(this);

                // 다이얼로그 값/옵션 세팅
                mAlertDialogBuilder
                        .setTitle(R.string.alert_title_delete_dday)
                        .setMessage(R.string.alert_message_delete_dday)
                        .setCancelable(false)
                        .setPositiveButton(R.string.btn_delete,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // 일정 삭제
                                        doDeleteItem(_id);
                                    }
                                })
                        .setNegativeButton(R.string.btn_cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // 다이얼로그를 취소한다.
                                        dialog.cancel();
                                    }
                                });

                mAlertDialog = mAlertDialogBuilder.create();
                mAlertDialog.show();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * SQLite
     * 일정 삭제
     * @param _id
     */
    private void doDeleteItem(int _id) {
        int result = mDatabaseHelper.deleteDday(_id);
        if (result > 0) {
            setSQLiteData();
        }
    }

    /**
     * SharedPreferences
     * 일정 삭제
     * @param sharedPreferencesDataKey
     */
    private void doDeleteItemSharedPreferences(String sharedPreferencesDataKey) {
        // How to remove some key/value pair from SharedPreferences?
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(sharedPreferencesDataKey);
        editor.apply();

        // ? editor.apply VS editor.commit

        setSharedPreferencesData();
    }

    /**
     * 다이얼로그 출력
     */
    @SuppressWarnings("unused")
    private void showDialog() {
        // 다이얼로그
        mAlertDialogBuilder = new AlertDialog.Builder(this);

        // 다이얼로그 값/옵션 세팅
        mAlertDialogBuilder
                .setTitle(R.string.alert_title_delete_dday)
                .setMessage(R.string.alert_message_delete_dday)
                .setCancelable(false)
                .setPositiveButton(R.string.btn_delete,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 일정 삭제
                                doDeleteItemSharedPreferences(mSharedPreferencesDataKey);
                            }
                        })
                .setNegativeButton(R.string.btn_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 다이얼로그를 취소한다.
                                dialog.cancel();
                            }
                        });

        mAlertDialog = mAlertDialogBuilder.create();
        mAlertDialog.show();

        // reference: https://mainia.tistory.com/2017
        // reference: https://m.blog.naver.com/PostView.nhn?blogId=sgepyh2916&logNo=221176134263&proxyReferer=https%3A%2F%2Fwww.google.com%2F
    }

}
