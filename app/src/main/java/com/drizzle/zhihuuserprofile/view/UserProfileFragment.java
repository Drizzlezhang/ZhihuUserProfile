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
import com.drizzle.zhihuuserprofile.widget.ZhihuUserProfileLayout;

public class UserProfileFragment extends Fragment {

    private static final String TAG = "UserProfileFragment";

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private View mAvatar, mDescription, mCover;
    private UserInfoFragment mHomepageFragment, mDetailFragment;
    private InfoPagerAdapter mPagerAdapter;
    private ZhihuUserProfileLayout mZhihuUserProfileLayout;

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
        mAvatar = parent.findViewById(R.id.user_profile_avatar);
        mDescription = parent.findViewById(R.id.user_profile_description);
        mCover = parent.findViewById(R.id.user_profile_header_cover);
        mCover.setAlpha(0.5f);
        mHomepageFragment = UserInfoFragment.newInstance(UserInfoFragment.TYPE_USER_HOMEPAGE);
        mDetailFragment = UserInfoFragment.newInstance(UserInfoFragment.TYPE_USER_DETAIL);
        mPagerAdapter = new InfoPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mZhihuUserProfileLayout = (ZhihuUserProfileLayout) parent.findViewById(R.id.user_profile_md_layout);
        mZhihuUserProfileLayout.setNestedScrollViewProvider(mHomepageFragment);
        mZhihuUserProfileLayout.setOnCollapsingListener(new ZhihuUserProfileLayout.OnCollapsingListener() {
            @Override
            public void onCollapsing(float progress) {
                mCover.setAlpha(1.0f - progress / 2);
                if (progress < 0.8f) {
                    mAvatar.setAlpha(0f);
                    mDescription.setAlpha(0f);
                } else if (progress > 0.9f) {
                    mAvatar.setAlpha(1f);
                    mDescription.setAlpha(1f);
                } else {
                    mAvatar.setAlpha((progress - 0.8f) * 10);
                    mDescription.setAlpha((progress - 0.8f) * 10);
                }
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mZhihuUserProfileLayout.setNestedScrollViewProvider(mHomepageFragment);
                        mDetailFragment.getNestedScrollView().scrollToPosition(0);
                        break;
                    case 1:
                        mZhihuUserProfileLayout.setNestedScrollViewProvider(mDetailFragment);
                        mHomepageFragment.getNestedScrollView().scrollToPosition(0);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class InfoPagerAdapter extends FragmentPagerAdapter {

        InfoPagerAdapter(FragmentManager fm) {
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
