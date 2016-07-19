package com.triwayuprasetyo.mapretrofitglidesdp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class Retrofit1Activity extends AppCompatActivity implements View.OnClickListener {

    private ListView listViewRetrofit;
    private Button buttonAddRetrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit1);

        setTitle("Retrofit 1.9");

        listViewRetrofit = (ListView) findViewById(R.id.listview_retrofit);
        buttonAddRetrofit = (Button) findViewById(R.id.button_add_retrofit);
        buttonAddRetrofit.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveData();
        Log.i("SDP Retrofit", "On Resume Method");
    }

    private void retrieveData() {
        /*
        //Retrofit 1.9
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://triwahyuprasetyo.xyz")
                .build();
        AnggotaInterface anggotaService = restAdapter.create(AnggotaInterface.class);
        anggotaService.getDataAnggota(new Callback<AnggotaWrapper>() {
            @Override
            public void success(AnggotaWrapper anggotaWrapper, Response response) {
                Toast.makeText(getApplicationContext(), "Retrieve Success", Toast.LENGTH_SHORT).show();
                String[] daftarNama = new String[anggotaWrapper.getAnggota().size()];
                int i = 0;
                for (AnggotaWrapper.Anggota anggota : anggotaWrapper.getAnggota()) {
                    Log.d("SDP", "Anggota :: " + anggota.getId());
                    Log.d("SDP", "Anggota :: " + anggota.getNama());
                    Log.d("SDP", "Anggota :: " + anggota.getAlamat());
                    Log.d("SDP", "Anggota :: " + anggota.getUsername());
                    Log.d("SDP", "Anggota :: " + anggota.getPassword());
                    Log.d("SDP", "=======================================");
                    daftarNama[i] = anggota.getNama();
                    i++;
                }
                listViewRetrofit.setAdapter(new ArrayAdapter(Retrofit1Activity.this, android.R.layout.simple_list_item_1, daftarNama));
                listViewRetrofit.invalidate();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Retrieve Error", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("SDP", "Error :: " + error.getMessage());
            }

        });
        */
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == buttonAddRetrofit.getId()) {
            Intent i = new Intent(getApplicationContext(), AddAnggotaActivity.class);
            startActivity(i);
        }
    }
}
