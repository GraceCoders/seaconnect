package com.sea.seaconnect.controller.Utills;

/*
 * Created by Nishant  Bhardwaj on 15-06-2017.
 */


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import androidx.recyclerview.widget.RecyclerView;


public class App extends MultiDexApplication {
    private static App appInstance;
    private static AppPreferences appPreferences;





    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        appInstance = this;
        appPreferences = AppPreferences.init(appInstance);

    }



    public static App getAppContext() {
        return appInstance;

    }

    public static AppPreferences getAppPreferences() {
        return appPreferences;
    }

    public static void getTotalHeightofLinearRecyclerView(Context con, RecyclerView recyclerView, int layout, int paddingValue) {
        try {
            RecyclerView.Adapter mAdapter = recyclerView.getAdapter();

            int totalHeight = 0;
            int paddingDp = paddingValue;

            View v = LayoutInflater.from(con).inflate(layout, null);

            for (int i = 0; i < mAdapter.getItemCount(); i++) {
                v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalHeight += v.getMeasuredHeight();
            }

//        if (totalHeight > 100) {
            ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
            params.height = totalHeight;
            recyclerView.setLayoutParams(params);
        } catch (Exception e) {
        }
        //        }
    }









}
