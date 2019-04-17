package com.example.ceedlive.dday.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ceedlive.dday.BaseActivity;
import com.example.ceedlive.dday.Constant;
import com.example.ceedlive.dday.R;
import com.example.ceedlive.dday.adapter.DdayListAdapter;
import com.example.ceedlive.dday.data.DdayItem;
import com.example.ceedlive.dday.service.NotificationService;
import com.example.ceedlive.dday.sqlite.DatabaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    /*
     * 디데이 프로젝트 관련 내용은 Dday 프로젝트 README.md 에 정리
     */

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private DatabaseHelper mDatabaseHelper;

    private LinearLayout mLayoutNoContent;
    private ListView mListViewContent;
    private FloatingActionButton mFabBtn;
    private FloatingActionButton mFabBtn2;

    private List<DdayItem> mDdayItemList;
    private String mAnniversaryInfoKey;
    private String mAnniversaryInfoJsonString;

    private String mSharedPreferencesDataKey;
    private int _id;

    private SharedPreferences mSharedPreferences;

    private AlertDialog.Builder mAlertDialogBuilder;
    private AlertDialog mAlertDialog;

    private List<Integer> mDynamicCheckedItemIdList;

    private NotificationManager mNotificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();// 변수 초기화
        setEvent();// 이벤트 설정
        setSQLiteData(); // (SQLite)
    }

    @Override
    protected void initialize() {
        mLayoutNoContent = findViewById(R.id.main_ll_no_content);
        mListViewContent = findViewById(R.id.expandableListView);

        mFabBtn = findViewById(R.id.fab);
        mFabBtn2 = findViewById(R.id.fab2);

        // https://stackoverflow.com/questions/30969455/android-changing-floating-action-button-color
        mFabBtn.setBackgroundTintList(ColorStateList.valueOf( getResources().getColor(R.color.colorWhite) ));
        mFabBtn2.setBackgroundTintList(ColorStateList.valueOf( getResources().getColor(R.color.colorWhite) ));

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        mDdayItemList = new ArrayList<>();
        mDynamicCheckedItemIdList = new CopyOnWriteArrayList<>();
    }

    private void setEvent() {
        onClickFabButtonCreate();
        onClickLayoutNoContent();


        mFabBtn2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
                Log.e("삭제 FAB 클릭", "삭제삭제");
                onClickFabDelete();
            }
        });
    }

    //

    /**
     * 뒤로가기 버튼으로 내비게이션 닫기
     * 내비게이션 드로어가 열려 있을 때 뒤로가기 버튼을 누르면 내비게이션을 닫고,
     * 닫혀 있다면 기존 뒤로가기 버튼으로 작동한다.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Add Custom
     */



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

        Log.e("onActivityResult", String.format("requestCode %d resultCode %d", requestCode, resultCode));

        // requestCode: 송신자 Activity 구별하기 위한 값
        // resultCode: 수신자 Activity 에서 송신자 Activity 로 어떠한 결과코드를 주었는지를 나타냄
        // Intent data: 수신자 Activity 에서 송신자 Activity 로 보낸 결과 데이터
        if (Constant.REQUEST_CODE_MAIN_ACTIVITY == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
//                setSharedPreferencesData();

                Snackbar.make(getWindow().getDecorView().getRootView(), getString(R.string.snackbar_msg_add_dday), Snackbar.LENGTH_SHORT).show();
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

        mListViewContent.setAdapter(new DdayListAdapter(mDdayItemList, this));
    }

    private void setSQLiteData() {
        // TODO SQLite
        // ==================================================

        mDatabaseHelper = DatabaseHelper.getInstance(this);

        mDdayItemList.clear();

        List<DdayItem> ddayItemList = mDatabaseHelper.getDdayList();

        for (DdayItem ddayItem : ddayItemList) {
            Log.e("SQLite item.toString()", ddayItem.toString());
            mDdayItemList.add(ddayItem);
        }

        SortDescending sortDescending = new SortDescending();
        Collections.sort(mDdayItemList, sortDescending);

        if (mDdayItemList.isEmpty()) {
            mLayoutNoContent.setVisibility(View.VISIBLE);
            mListViewContent.setVisibility(View.INVISIBLE);
            mFabBtn2.setVisibility(View.GONE);
        } else {
            mListViewContent.setVisibility(View.VISIBLE);
            mLayoutNoContent.setVisibility(View.INVISIBLE);
        }

        mListViewContent.setAdapter(new DdayListAdapter(mDdayItemList, this));
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

    public void onClickFabDelete() {
        Log.e("MainActivity", "onClickFabDelete");

        // 다이얼로그
        mAlertDialogBuilder = new AlertDialog.Builder(this);

        // 다이얼로그 값/옵션 세팅
        mAlertDialogBuilder
                .setTitle(R.string.alert_title_delete_dday_checked)
                .setMessage(R.string.alert_message_delete_dday_checked)
                .setCancelable(false)
                .setPositiveButton(R.string.btn_delete,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 일정 삭제
                                if ( !mDynamicCheckedItemIdList.isEmpty() ) {
                                    for (Integer _id : mDynamicCheckedItemIdList) {
                                        doDeleteItem(_id);
                                    }
                                    Snackbar.make(getWindow().getDecorView().getRootView(), getString(R.string.snackbar_msg_delete_dday), Snackbar.LENGTH_SHORT).show();
                                    handleFabVisibility(false);
                                    setSQLiteData();
                                }
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

    public void addChecked(int targetId) {
        Log.e("MainActivity", "addChecked: " + targetId + "");
        if ( mDynamicCheckedItemIdList.isEmpty() || !mDynamicCheckedItemIdList.contains(targetId) ) {
            mDynamicCheckedItemIdList.add(targetId);
        }
        Log.e("MainActivity", "addChecked: " + mDynamicCheckedItemIdList.toString());
    }

    public void removeChecked(int targetId) {
        // FIXME ConcurrentModificationException
        for (Integer id : mDynamicCheckedItemIdList) {
            if (id == targetId) {
                int index = mDynamicCheckedItemIdList.indexOf(id);
                mDynamicCheckedItemIdList.remove(index);
            }
        }
        Log.e("MainActivity", "removeChecked: " + mDynamicCheckedItemIdList.toString());
    }

    /**
     * onLongClick 전부 체크 해제 시 이벤트 핸들러
     * @param visible
     */
    public void handleFabVisibility(boolean visible) {
        if (visible) {
            mFabBtn2.setVisibility(View.VISIBLE);
        } else {
            mFabBtn2.setVisibility(View.GONE);
        }
    }

    /**
     * 노티 버튼 클릭 시 이벤트 핸들러
     * @param isNotification
     * @param _id
     */
    public void onClickNoti(boolean isNotification, int _id) {
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

                if (isNotification) {
                    ddayItem.setNotification(Constant.NOTIFICATION.UNREGISTERED);
                    mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.cancel(_id);
                    mDatabaseHelper.updateDday(ddayItem);

                    Snackbar.make(getWindow().getDecorView().getRootView(), getString(R.string.snackbar_msg_remove_notification), Snackbar.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(this, NotificationService.class);
//                intent.putExtra(Constant.INTENT_DATA_NAME_SHARED_PREFERENCES, ddayItem.getUniqueKey()); //전달할 값
                    intent.putExtra(Constant.INTENT_DATA_SQLITE_TABLE_DDAY_ID, ddayItem.get_id()); //전달할 값
                    startService(intent);
                    ddayItem.setNotification(Constant.NOTIFICATION.REGISTERED);
                    mDatabaseHelper.updateDday(ddayItem);

                    // 우리는 보통 알림을 띄울때 Toast를 이용해서 많이 이용했을 겁니다.
                    // 하지만 안드로이드 오레오부터 알림을 끄게되면 Toast가 보이지 않습니다.
                    // 따라서 스낵바로 넘어갈 상황이 필요한것 같습니다.
                    Snackbar.make(getWindow().getDecorView().getRootView(), getString(R.string.snackbar_msg_add_notification), Snackbar.LENGTH_SHORT).show();
                }

                setSQLiteData();

                // intent 객체
                // 서비스와 연결에 대한 정의
//                mIsService = bindService(intent, mServiceConnection, Context.BIND_DEBUG_UNBIND);
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
                                        Snackbar.make(getWindow().getDecorView().getRootView(), getString(R.string.snackbar_msg_delete_dday), Snackbar.LENGTH_SHORT).show();
                                        setSQLiteData();
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
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancel(_id);
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
