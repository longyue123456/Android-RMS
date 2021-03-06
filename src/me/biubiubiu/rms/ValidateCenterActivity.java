/*
 * ValidateCenterActivity.java
 * Copyright (C) 2013 garlic <garlic@meishixing>
 *
 * Distributed under terms of the MIT license.
 */
package me.biubiubiu.rms;

import android.util.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import android.app.*;
import android.os.*;
import android.database.*;
import android.net.*;
import android.opengl.*;
import android.graphics.*;
import android.view.animation.*;

import java.util.*;
import org.json.*;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import com.andreabaccega.widget.FormEditText;

import me.biubiubiu.rms.util.HttpHandler.ResponseHandler;
import me.biubiubiu.rms.util.*;
import me.biubiubiu.rms.ui.*;

public class ValidateCenterActivity extends BaseActivity implements ActionBar.OnNavigationListener {

    private String[] TITLES = {
        "入库",
        "出库",
        "产品",
    };

    private int mValidateStatus;

    private ContentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedBundleInstance) {
        super.onCreate(savedBundleInstance);
        setContentView(R.layout.tag_page_fragment);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.content_indicator);
        ViewPager pager = (ViewPager)findViewById(R.id.content_pager);

        if (mAdapter == null) {
            mAdapter = new ContentAdapter(getSupportFragmentManager());
        }

        pager.setAdapter(mAdapter);
        indicator.setOnPageChangeListener(mAdapter);
        indicator.setViewPager(pager);

        setActionBarTitle("");
        ActionBar ab = getSupportActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"全部", "未审核", "已审核"});
        ab.setListNavigationCallbacks(adapter, this);
    }

    public boolean onNavigationItemSelected(int itemPosition, long itemPositionemId) {
        mValidateStatus = itemPosition;
        mAdapter.refreshAll();
        return true;
    }

    private class ContentAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

        private int mPagePos;
        private ValidateFragment frag0;
        private ValidateFragment frag1;
        private ValidateFragment frag2;

        public ContentAdapter(FragmentManager fm) {
            super(fm);
            frag0 =  new ValidateFragment("import", R.layout.list_item_import);
            frag1 =  new ValidateFragment("export", R.layout.list_item_export);
            frag2 =  new ValidateFragment("product", R.layout.list_item_product);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return frag0;
            } else if (position == 1) {
                return frag1;
            } else if (position == 2) {
                return frag2;
            }

            return null;
        }

        private void refreshAll() {
            for (ValidateFragment frag : new ValidateFragment[]{frag0, frag1, frag2}) {
                if (mValidateStatus != 0) {
                    frag.mWhere = "validated == " + (mValidateStatus - 1);
                } else {
                    frag.mWhere = null;
                }
                frag.refresh();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position % TITLES.length];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public void onPageSelected(int position) {
            mPagePos = position;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
