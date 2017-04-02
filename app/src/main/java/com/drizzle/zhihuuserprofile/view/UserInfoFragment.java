package com.drizzle.zhihuuserprofile.view;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drizzle.zhihuuserprofile.R;
import com.drizzle.zhihuuserprofile.data.InfoItem;
import com.drizzle.zhihuuserprofile.data.InfoItemProvider;

import java.util.List;

public class UserInfoFragment extends Fragment {

    private static final String KEY_TYPE_INFO = "key_type_info";

    public static final int TYPE_USER_HOMEPAGE = 101;
    public static final int TYPE_USER_DETAIL = 102;

    private int mTypeDetail;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private UserInfoAdapter mAdapter;

    public UserInfoFragment() {
    }

    public static UserInfoFragment newInstance(int type) {
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_TYPE_INFO, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTypeDetail = getArguments().getInt(KEY_TYPE_INFO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_user_info, container, false);
        initViewAndData(parentView);
        return parentView;
    }

    private void initViewAndData(View parent) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) parent.findViewById(R.id.user_info_swiperefresh);
        mSwipeRefreshLayout.setEnabled(false);
        mRecyclerView = (RecyclerView) parent.findViewById(R.id.user_info_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new UserInfoAdapter(mTypeDetail == TYPE_USER_HOMEPAGE ? InfoItemProvider.getHomepageInfo() : InfoItemProvider.getDetailInfo(), getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    class UserInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<InfoItem> mInfoList;
        private Context mContext;

        public UserInfoAdapter(List<InfoItem> infoList, Context context) {
            mInfoList = infoList;
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case InfoItem.TYPE_SIMPLE_INFO:
                    return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_simpe_info, parent, false));
                case InfoItem.TYPE_CUSTOM_TITLE:
                    return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_custom_title, parent, false));
                case InfoItem.TYPE_DETAIL_INFO:
                    return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_detail_info, parent, false));
                case InfoItem.TYPE_PERSONAL_ACHIEVEMENT:
                    return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_personal_achievement, parent, false));
                case InfoItem.TYPE_SIMPE_ITEM:
                    return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_simple_item, parent, false));
                case InfoItem.TYPE_QUESTION:
                    return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_question, parent, false));
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }


        @Override
        public int getItemViewType(int position) {
            return mInfoList.get(position).getInfoType();
        }

        @Override
        public int getItemCount() {
            return mInfoList == null ? 0 : mInfoList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View itemView) {
                super(itemView);
            }
        }

    }

}
