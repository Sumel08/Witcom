package com.upiita.witcom.pager;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.upiita.witcom.R;
import com.upiita.witcom.WitcomLogoActivity;
import com.upiita.witcom.dataBaseHelper.Controller;
import com.viewpagerindicator.PageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.zip.Inflater;

import static com.upiita.witcom.WitcomLogoActivity.URL_BASE;
import static com.upiita.witcom.WitcomLogoActivity.wdb;

/**
 * Created by oscar on 26/09/16.
 */

public class WitcomBaseActivity extends AppCompatActivity {

    private static final Random RANDOM = new Random();
    public static String accent = "#FF9A22";
    public static String textWhite = "#EFEFEF";
    public static String blue = "#0F5DBE";
    public static String dark = "#1C1D26";
    private int requestPending;
    private ProgressDialog progressDia;
    private int total = 44;
    protected FirebaseRemoteConfig firebaseRemoteConfig;
    protected TextView tvUpdate;

    WitcomFragmentAdapter mAdapter;
    public static ViewPager mPager;
    PageIndicator mIndicator;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {
            new AlertDialog.Builder(this)
                    .setView(R.layout.about)
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .show();
        } else if (id == R.id.update) {


            /*if (checkInternetConnection()) {
                progressDia = new ProgressDialog(this);
                progressDia.setTitle(getString(R.string.updating));
                progressDia.setMessage(getString(R.string.wait));
                progressDia.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

                getTables();
            } else {
                Log.d("INTERNETCONNECTION", "No hay conexión");
                Toast.makeText(this, getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
            }*/

            Toast.makeText(this, firebaseRemoteConfig.getString("url_witcom"), Toast.LENGTH_SHORT).show();


        }

        return super.onOptionsItemSelected(item);
    }

    public void getTables() {
        final HashMap<String, ArrayList<String>> dataBase = new HashMap<>();
        ArrayList<String> columns;
        requestPending = 0;

        /**
         * Activities
         */
        columns = new ArrayList<>();
        columns.add("id");
        columns.add("activity");
        columns.add("id_image");
        columns.add("details");
        dataBase.put("activities", columns);

        /**
         * Conferences
         */
        columns = new ArrayList<>();
        columns.add("id");
        columns.add("title");
        columns.add("speaker");
        columns.add("come");
        columns.add("details");
        columns.add("notes");
        columns.add("id_image");
        dataBase.put("conferences", columns);

        /**
         * Other
         */
        columns = new ArrayList<>();
        columns.add("id");
        columns.add("title");
        columns.add("second");
        columns.add("third");
        columns.add("id_image");
        dataBase.put("other", columns);

        /**
         * Papers
         */
        columns = new ArrayList<>();
        columns.add("id");
        columns.add("title");
        columns.add("author");
        columns.add("details");
        columns.add("notes");
        columns.add("Field6");
        dataBase.put("papers", columns);

        /**
         * Schedule
         */
        columns = new ArrayList<>();
        columns.add("id");
        columns.add("id_activity");
        columns.add("date");
        columns.add("time");
        columns.add("id_details");
        dataBase.put("schedule", columns);

        /**
         * Workshops
         */
        columns = new ArrayList<>();
        columns.add("id");
        columns.add("title");
        columns.add("monitor");
        columns.add("detail");
        columns.add("notes");
        columns.add("id_image");
        columns.add("place");
        columns.add("date");
        columns.add("time");
        dataBase.put("workshops", columns);

        /**
         * Cities
         */
        columns = new ArrayList<>();
        columns.add("id");
        columns.add("city");
        columns.add("id_image");
        dataBase.put("cities", columns);

        /**
         * Places
         */
        columns = new ArrayList<>();
        columns.add("id");
        columns.add("id_city");
        columns.add("place");
        columns.add("description");
        columns.add("address");
        columns.add("schedule");
        columns.add("extra");
        columns.add("latitude");
        columns.add("longitude");
        columns.add("id_image");
        columns.add("cost");
        columns.add("telephone");
        columns.add("webpage");
        dataBase.put("places", columns);

        tvUpdate.setVisibility(View.INVISIBLE);

        SQLiteDatabase bd = WitcomLogoActivity.wdb.getReadableDatabase();
        bd.execSQL("delete from version");
        bd = WitcomLogoActivity.wdb.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("info_version", WitcomLogoActivity.CONTENT_VERSION);
        bd.insert("version", null, values);
        bd.close();

        progressDia.setMax(dataBase.size()+1);
        progressDia.setProgress(0);
        progressDia.show();

        getImages();

        for (final String table: dataBase.keySet()) {
            JsonArrayRequest request = new JsonArrayRequest(URL_BASE + "/objeto/" + table + ".json", new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        SQLiteDatabase db = wdb.getWritableDatabase();
                        db.execSQL("delete from " + table);
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject object = response.getJSONObject(i);
                            ContentValues values = new ContentValues();

                            for (String column: dataBase.get(table)) {
                                values.put(column, object.getString(column));
                            }

                            db.insert(table, null, values);
                        }

                    } catch (JSONException e) {
                        Log.d("FATALERROR1", e.toString());
                    } finally {
                        wdb.close();
                        progressDia.setProgress(progressDia.getProgress()+1);
                        if (progressDia.getProgress() == progressDia.getMax())
                            progressDia.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("FATALERROR", error.toString());
                    progressDia.setProgress(progressDia.getProgress()+1);
                    if (progressDia.getProgress() == progressDia.getMax())
                        progressDia.dismiss();
                }
            });

            Controller.getInstance().addToRequestQueue(request);
        }
    }

    private void getImages () {
        JsonArrayRequest request = new JsonArrayRequest(URL_BASE + "/objeto/images.json", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    SQLiteDatabase db = wdb.getWritableDatabase();
                    db.execSQL("delete from images");
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);
                        progressDia.setMax(progressDia.getMax()+1);
                        getImage(object.getString("id"), URL_BASE + "/images/" + object.getString("image"));
                    }

                } catch (JSONException e) {
                    Log.d("FATALERROR1", e.toString());
                } finally {
                    wdb.close();
                    progressDia.setProgress(progressDia.getProgress()+1);
                    if (progressDia.getProgress() == progressDia.getMax())
                        progressDia.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("FATALERROR", error.toString());
                progressDia.setProgress(progressDia.getProgress()+1);
                if (progressDia.getProgress() == progressDia.getMax())
                    progressDia.dismiss();
            }
        });

        Controller.getInstance().addToRequestQueue(request);
    }

    private void getImage (final String id, String url) {
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
                        byte[] b = baos.toByteArray();

                        SQLiteDatabase db = wdb.getWritableDatabase();

                        ContentValues values = new ContentValues();
                        values.put("id", id);
                        values.put("image", b);
                        db.insert("images", null, values);
                        db.close();
                        progressDia.setProgress(progressDia.getProgress()+1);
                        if (progressDia.getProgress() == progressDia.getMax())
                            progressDia.dismiss();
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Log.d("IMAGEERROR", "Algo salió mal");
                        progressDia.setProgress(progressDia.getProgress()+1);
                        if (progressDia.getProgress() == progressDia.getMax())
                            progressDia.dismiss();
                    }
                });
        Controller.getInstance().addToRequestQueue(request);
    }

    private boolean checkInternetConnection() {
        boolean mobileNwInfo = false;
        ConnectivityManager conxMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        try { mobileNwInfo = conxMgr.getActiveNetworkInfo().isConnected(); }
        catch (NullPointerException e) { mobileNwInfo = false; }

        return mobileNwInfo;
    }

    public void sendMail(View v) {
        TextView t = (TextView)v;

        Intent emailIntent = null;
        String email = "";

        if(t.getText().toString().compareTo(">_ Dr. Miguel Félix Mata Rivera") == 0) {
            email = "mmatar@ipn.mx";
        }

        else if(t.getText().toString().compareTo(">_ M. en C. Carlos Hernández Nava") == 0) {
            email = "hernandeznc@ipn.mx";
        }

        else if(t.getText().toString().compareTo(">_ M. en C. Miguel Alejandro Martínez Rosales") == 0) {
            email = "mamartinezr@ipn.mx";
        }

        else if(t.getText().toString().compareTo(">_ Edgar Hernández Solís") == 0) {
            email = "edgarhzs.93@gmail.com";
        }

        else if(t.getText().toString().compareTo(">_ Oscar Alejandro Lemus Pichardo") == 0) {
            email = "oscarl.ocho@gmail.com";
        }

        else if(t.getText().toString().compareTo(">_ Miguel Armando Maldonado Vázquez") == 0) {
            email = "miguel.maldonadov@gmail.com";
        }

        emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "WITCOM 2016");

        Toast.makeText(getApplicationContext(), "Sending mail to "+t.getText(), Toast.LENGTH_LONG).show();
        startActivity(Intent.createChooser(emailIntent, "Send Mail"));

    }
}
