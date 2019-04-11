package com.example.ceedlive.dday.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.ceedlive.dday.Constant;
import com.example.ceedlive.dday.data.DdayItem;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context mContext;

    private static DatabaseHelper mDatabaseHelper = null;

    private static final String CLT_DDAY = "CLT_DDAY";

    private static final String CLT_DDAY_ID = "_ID";

    private static final String CLT_DDAY_DATE = "DATE";

    private static final String CLT_DDAY_TITLE = "TITLE";

    private static final String CLT_DDAY_DESCRIPTION = "DESCRIPTION";

    private static final String CLT_DDAY_DIFF_DAYS = "DIFF_DAYS";

    private static final String TAG = "DatabaseHelper";

    // SQLiteOpenHelper
    // 주요 함수는 onCreate, onUpgrade, onOpen 이며 데이타베이스 생성과 관리, 존재여부에 대한 역할을 한다.

    // SQLiteDatabase
    // 실질적으로 CRUD 를 수행하는데 쓰인다.

    // 출처: https://mainia.tistory.com/670 [녹두장군 - 상상을 현실로]

    // Is it OK to have one instance of SQLiteOpenHelper shared by all Activities in an Android application?
    // https://stackoverflow.com/questions/8888530/is-it-ok-to-have-one-instance-of-sqliteopenhelper-shared-by-all-activities-in-an

    private DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = context;

        Log.d(TAG, "DatabaseHelper Constructor");
    }

    public static DatabaseHelper getInstance(Context context) {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = new DatabaseHelper(context.getApplicationContext(),
                    Constant.SQLITE_DB_FILE_NAME,
                    null,
                    Constant.SQLITE_DB_VERSION);
        }
        return mDatabaseHelper;
    }

    /**
     * Database 가 존재하지 않을 때, 딱 한번 실행된다.
     * DB를 만드는 역할을 한다.
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // String 보다 StringBuffer가 Query 만들기 편하다.
        StringBuffer sb = new StringBuffer();
        sb.append(" CREATE TABLE IF NOT EXISTS CLT_DDAY ");
        sb.append(" ( ");
        sb.append(" _ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(" DATE TEXT, ");
        sb.append(" TITLE TEXT, ");
        sb.append(" DESCRIPTION TEXT, ");
        sb.append(" DIFF_DAYS TEXT ");
        sb.append(" ) ");

        // SQLite Database로 쿼리 실행
        sqLiteDatabase.execSQL(sb.toString());

        Log.d(TAG, "onCreate Table 생성완료");

        // 출처: https://cocomo.tistory.com/409 [Cocomo Coding]
    }

    /**
     * Application의 버전이 올라가서 Table 구조가 변경되었을 때 실행된다.
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Toast.makeText(mContext, "버전이 올라갔습니다.", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onUpgrade 버전이 올라갔습니다.");
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);

        Toast.makeText(mContext, "버전이 내려갔습니다..", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onDowngrade 버전이 올라갔습니다.");
    }

    /**
     *
     * @param ddayItem
     */
    public long addDday(DdayItem ddayItem) {

        // 방법1
//        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
//        StringBuffer stringBuffer = new StringBuffer();
//        stringBuffer.append(" INSERT INTO CLT_DDAY ");
//        stringBuffer.append(" ( TITLE, DESCRIPTION, DATE, DIFF_DAYS ) ");
//        stringBuffer.append(" VALUES ");
//        stringBuffer.append(" ( ?, ?, ?, ?) ");
//
//        sqLiteDatabase.execSQL(stringBuffer.toString(),
//                new Object[] {
//                        ddayItem.getTitle(),
//                        ddayItem.getDescription(),
//                        ddayItem.getDate(),
//                        ddayItem.getDiffDays()
//                });

        // 방법2
        long result = 0;

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TITLE", ddayItem.getTitle()); // TITLE 필드명
        values.put("DESCRIPTION", ddayItem.getDescription()); // DESCRIPTION 필드명
        values.put("DATE", ddayItem.getDate()); // DATE 필드명
//        values.put("DIFF_DAYS", ddayItem.getDiffDays()); // DIFF_DAYS 필드명

        try {
            // 새로운 Row 추가
            result = db.insert("CLT_DDAY", null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != db) {
                db.close(); // 연결종료
            }
        }

        Log.d("addDday", "INSERT 완료");

        return result;
    }

    /**
     *
     * @return
     */
    public List<DdayItem> getDdayList() {

        // FIXME 다음 주석처리된 코드는 버그 발생 코드, 수정 필요
//        StringBuffer sb = new StringBuffer();
//        sb.append(" SELECT _ID, DATE, TITLE, DESCRIPTION, DIFF_DAYS FROM CLT_DDAY ");
//
//        // 읽기 전용 DB 객체를 만든다.
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = db.rawQuery(sb.toString(), null);

//        List<DdayItem> ddayItemList = new ArrayList<>();
//        DdayItem ddayItem;
//        while ( cursor.moveToNext() ) {
//            ddayItem = new DdayItem();
//
//            ddayItem.set_id( cursor.getInt(0) );
//            ddayItem.setTitle( cursor.getString(1) );
//            ddayItem.setDescription( cursor.getString(2) );
//            ddayItem.setDate( cursor.getString(3) );
//            ddayItem.setDiffDays( cursor.getString(4) );
//
//            ddayItemList.add(ddayItem);
//        }

        Log.d("DbHelper getDdayList", " DdayItem 목록 가져오기");

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;

        List<DdayItem> ddayItemList = new ArrayList<>();
        DdayItem ddayItem;

        try {
            cursor = db.query("CLT_DDAY", null, null, null, null, null, null);
            if (cursor != null) {
                while ( cursor.moveToNext() ) {
                    int id = cursor.getInt(cursor.getColumnIndex("_ID"));
                    String date = cursor.getString(cursor.getColumnIndex("DATE"));
                    String title = cursor.getString(cursor.getColumnIndex("TITLE"));
                    String description = cursor.getString(cursor.getColumnIndex("DESCRIPTION"));

                    ddayItem = new DdayItem(id, date, title, description);
                    ddayItemList.add(ddayItem);

                    Log.d("getDdayList", "id: " + id + ", date: " + date + ", title: " + title + ", description: " + description);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return ddayItemList;
    }

    /**
     * id에 해당하는 DdayItem 객체 얻어오기
     * @param _id
     * @return
     */
    public DdayItem getDday(int _id) {

        Log.d("DbHelper getDday", _id + " id에 해당하는 DdayItem 객체 얻어오기");

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        DdayItem ddayItem = null;

        try {
            cursor = db.query(CLT_DDAY,
                    new String[] { CLT_DDAY_ID, CLT_DDAY_TITLE, CLT_DDAY_DESCRIPTION, CLT_DDAY_DATE, CLT_DDAY_DIFF_DAYS },
                    CLT_DDAY_ID + " = ?",
                    new String[] { String.valueOf(_id) }, null, null, null, null);

            if (cursor.moveToFirst()) {

                int id = cursor.getInt(cursor.getColumnIndex("_ID"));
                String date = cursor.getString(cursor.getColumnIndex("DATE"));
                String title = cursor.getString(cursor.getColumnIndex("TITLE"));
                String description = cursor.getString(cursor.getColumnIndex("DESCRIPTION"));

                ddayItem = new DdayItem(id, date, title, description);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return ddayItem;
    }

    /**
     * Dday 정보 업데이트
     * @param ddayItem
     * @return
     */
    public int updateDday(DdayItem ddayItem) {

        Log.d("DbHelper updateDday", ddayItem.toString());

        SQLiteDatabase db = this.getWritableDatabase();

        // SQLite in Android How to update a specific row
        // This is the cleanes solution to update a specific row.
        ContentValues contentValues = new ContentValues();
        contentValues.put("DATE", ddayItem.getDate());
        contentValues.put("TITLE", ddayItem.getTitle());
        contentValues.put("DESCRIPTION", ddayItem.getDescription());

        int result = db.update("CLT_DDAY",
                contentValues,
                "_ID = ?",
                new String[] { String.valueOf(ddayItem.get_id()) });

//        StringBuffer sb = new StringBuffer();
//        sb.append(" UPDATE CLT_DDAY ");
//        sb.append(" SET ");
//        sb.append("   DATE = ? ");
//        sb.append(" , TITLE = ? ");
//        sb.append(" , DESCRIPTION = ? ");
//        sb.append(" WHERE _ID = ? ");
//
//        db.execSQL(sb.toString(),
//                new Object[] {
//                        ddayItem.getDate(),
//                        ddayItem.getTitle(),
//                        ddayItem.getDescription(),
//                        ddayItem.get_id()
//                });

//        db.execSQL("update mytable set name='Park' where id=5;");

        // updating row
        return result;
    }

    /**
     * Dday 정보 삭제하기
     * @param _id
     */
    public int deleteDday(int _id) {

        Log.d("DbHelper deleteDday", _id + " 삭제");

        SQLiteDatabase db = this.getWritableDatabase();
        int result = 0;

        try {
            result = db.delete(CLT_DDAY,
                    CLT_DDAY_ID + " = ?",
                    new String[] { String.valueOf(_id) });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return result;
    }

    /**
     * Dday 정보 카운트
     * @return
     */
    public int getDdayListCount() {
        String countQuery = "SELECT * FROM " + CLT_DDAY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
