package com.triwayuprasetyo.mapretrofitglidesdp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.triwayuprasetyo.mapretrofitglidesdp.glide.CustomAdapter;

import java.util.ArrayList;

public class GlideActivity extends AppCompatActivity {
    public static int[] daftarImages = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    public static String[] daftarNama = {"nabila", "sendi", "naomi", "sinka", "melody", "dike"};
    public static String[] daftarAlamat = {"UK", "INA", "USA", "CHINA", "CANADA", "RUSIA"};
    ArrayList prgmName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide);
        ImageView imageView = (ImageView) findViewById(R.id.imageview_glide);
        Glide.with(getApplicationContext()).load("http://goo.gl/gEgYUd").into(imageView);


        ListView lv = (ListView) findViewById(R.id.listview_glide);
        lv.setAdapter(new CustomAdapter(this, daftarNama, daftarAlamat, daftarImages));

    }
}
