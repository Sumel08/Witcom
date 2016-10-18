package com.upiita.witcom;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.upiita.witcom.dataBaseHelper.Controller;
import com.upiita.witcom.dataBaseHelper.WitcomDataBase;
import com.upiita.witcom.pager.WitcomPagerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class WitcomLogoActivity extends AppCompatActivity {

    public static WitcomDataBase wdb;
    public static String URL_BASE = "http://www.sum08.omniversoft.com";
    public static String CONTENT_VERSION = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_witcom_logo);

        wdb = new WitcomDataBase(getApplicationContext());

        //getTables();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(getApplicationContext(), WitcomPagerActivity.class));
                //startActivity(new Intent(getApplicationContext(), WitcomStreetViewActivity.class));
            }
        }, 1680);
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }
}
