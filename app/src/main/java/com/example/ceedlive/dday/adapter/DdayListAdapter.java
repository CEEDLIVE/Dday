package com.example.ceedlive.dday.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.ceedlive.dday.R;
import com.example.ceedlive.dday.dto.AnniversaryInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class DdayListAdapter extends BaseExpandableListAdapter {
    /**
     * ListView에 세팅할 Item 정보들
     */
    private List<AnniversaryInfo> arrayGroup;

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
    public DdayListAdapter(List<AnniversaryInfo> arrayGroup, HashMap arrayChild, Context context) {
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
        AnniversaryInfo anniversaryInfo = arrayGroup.get(groupPosition);
        ArrayList childrenList = (ArrayList) arrayChild.get( anniversaryInfo.getUniqueKey() );
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
        AnniversaryInfo anniversaryInfo = arrayGroup.get(groupPosition);
        ArrayList childrenList = (ArrayList) arrayChild.get( anniversaryInfo.getUniqueKey() );
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

        AnniversaryInfo anniversaryInfo = arrayGroup.get(groupPosition);

        tvTitle.setText(anniversaryInfo.getTitle());
        tvDate.setText(anniversaryInfo.getDate());
        tvDay.setText(anniversaryInfo.getUniqueKey());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        AnniversaryInfo anniversaryInfo = arrayGroup.get(groupPosition);
        ArrayList<String> detail = (ArrayList<String>) arrayChild.get(anniversaryInfo.getUniqueKey());
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
