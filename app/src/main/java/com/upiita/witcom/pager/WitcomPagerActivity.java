package com.upiita.witcom.pager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.upiita.witcom.R;
import com.upiita.witcom.WitcomLogoActivity;
import com.viewpagerindicator.IconPageIndicator;

/**
 * Created by oscar on 27/09/16.
 */

public class WitcomPagerActivity extends WitcomBaseActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_pager);

        mAdapter = new WitcomFragmentAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager_test);
        mPager.setAdapter(mAdapter);

        mIndicator = (IconPageIndicator) findViewById(R.id.indicator_test);
        mIndicator.setViewPager(mPager);

        mIndicator.setCurrentItem(getIntent().getIntExtra("page",1)-1);

        tvUpdate = (TextView) findViewById(R.id.tv_update);

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.fetch().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(WitcomPagerActivity.this, "Fetch Succeeded",
                            Toast.LENGTH_SHORT).show();

                    // Once the config is successfully fetched it must be activated before newly fetched
                    // values are returned.
                    firebaseRemoteConfig.activateFetched();
                } else {
                    Toast.makeText(WitcomPagerActivity.this, "Fetch Failed",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //WitcomLogoActivity.URL_BASE = firebaseRemoteConfig.getString("url_witcom");
        Toast.makeText(this, firebaseRemoteConfig.getString("url_witcom"), Toast.LENGTH_SHORT).show();

        /*database = FirebaseDatabase.getInstance();
        reference = database.getReference("url");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("URL CHANGE", dataSnapshot.getValue(String.class));
                WitcomLogoActivity.URL_BASE = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        database.getReference("version").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SQLiteDatabase bd = WitcomLogoActivity.wdb.getReadableDatabase();
                Cursor fila = bd.rawQuery("SELECT info_version FROM version", null);
                if (fila.moveToFirst()) {
                    if (!fila.getString(0).equals(dataSnapshot.getValue(String.class))) {
                        //Actualizar
                        Log.d("ACTUALIZAR", "POR FAVOR ACTUALIZA");
                        tvUpdate.setVisibility(View.VISIBLE);
                    }
                } else {
                    tvUpdate.setVisibility(View.VISIBLE);
                }
                WitcomLogoActivity.CONTENT_VERSION = dataSnapshot.getValue(String.class);
                fila.close();
                bd.close();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }
}
