package com.triwayuprasetyo.mapretrofitglidesdp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.android.volley.toolbox.Volley;

public class GabungActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bRetrofit, bGlide, bVolley, bSer, bGps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gabung);
        bRetrofit = (Button) findViewById(R.id.buttonG_retrofit);
        bRetrofit.setOnClickListener(this);

        bGlide = (Button) findViewById(R.id.buttonG_glide);
        bGlide.setOnClickListener(this);

        bVolley = (Button) findViewById(R.id.buttonG_volley);
        bVolley.setOnClickListener(this);

        bSer = (Button) findViewById(R.id.buttonG_serv);
        bSer.setOnClickListener(this);

        bGps = (Button) findViewById(R.id.buttonG_gmap);
        bGps.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == bRetrofit.getId()) {
            Intent i = new Intent(getApplicationContext(), Retrofit2Activity.class);
            startActivity(i);
        } else if (v.getId() == bGlide.getId()) {
            Intent i = new Intent(getApplicationContext(), GlideActivity.class);
            startActivity(i);
        } else if (v.getId() == bVolley.getId()) {
            Intent i = new Intent(getApplicationContext(), Volley.class);
            startActivity(i);
        } else if (v.getId() == bSer.getId()) {
            Intent i = new Intent(getApplicationContext(), ServiceActivity.class);
            startActivity(i);
        } else if (v.getId() == bGps.getId()) {
            Intent i = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(i);
        }
    }
}
