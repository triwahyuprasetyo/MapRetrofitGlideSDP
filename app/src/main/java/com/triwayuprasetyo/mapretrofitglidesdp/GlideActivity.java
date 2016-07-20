package com.triwayuprasetyo.mapretrofitglidesdp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.triwayuprasetyo.mapretrofitglidesdp.glide.CustomAdapter;

import java.util.ArrayList;

public class GlideActivity extends AppCompatActivity {
    public static int[] prgmImages = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    public static String[] prgmNameList = {"Let Us C", "c++", "JAVA", "Jsp", "Microsoft .Net", "Android", "PHP", "Jquery", "JavaScript"};
    ArrayList prgmName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide);
        ImageView imageView = (ImageView) findViewById(R.id.imageView_listView_glide);
        Glide.with(GlideActivity.this).load("http://goo.gl/gEgYUd").into(imageView);


        ListView lv = (ListView) findViewById(R.id.listview_glide);
        lv.setAdapter(new CustomAdapter(this, prgmNameList, prgmImages));

    }
}
