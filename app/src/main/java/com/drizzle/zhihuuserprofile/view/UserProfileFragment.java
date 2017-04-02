package com.drizzle.zhihuuserprofile.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drizzle.zhihuuserprofile.R;

public class UserProfileFragment extends Fragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private UserInfoFragment mHomepageFragment, mDetailFragment;
    private InfoPagerAdapter mPagerAdapter;

    public UserProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        initViewAndData(parentView);
        return parentView;
    }

    private void initViewAndData(View parent) {
        mTabLayout = (TabLayout) parent.findViewById(R.id.user_profile_tablayout);
        mViewPager = (ViewPager) parent.findViewById(R.id.user_profile_viewpager);
        mHomepageFragment = UserInfoFragment.newInstance(UserInfoFragment.TYPE_USER_HOMEPAGE);
        mDetailFragment = UserInfoFragment.newInstance(UserInfoFragment.TYPE_USER_DETAIL);
        mPagerAdapter = new InfoPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    class InfoPagerAdapter extends FragmentPagerAdapter {

        public InfoPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return mHomepageFragment;
                case 1:
                    return mDetailFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.homepage);
                case 1:
                    return getString(R.string.info_detail);
            }
            return super.getPageTitle(position);
        }
    }

}
