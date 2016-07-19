package com.triwayuprasetyo.mapretrofitglidesdp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.triwayuprasetyo.mapretrofitglidesdp.retrofit.AnggotaService;
import com.triwayuprasetyo.mapretrofitglidesdp.retrofit.AnggotaWrapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by why on 7/19/16.
 */

public class Retrofit2Activity extends AppCompatActivity implements View.OnClickListener {

    private ListView listViewRetrofit;
    private Button buttonAddRetrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit1);

        setTitle("Retrofit 2.1");

        listViewRetrofit = (ListView) findViewById(R.id.listview_retrofit);
        buttonAddRetrofit = (Button) findViewById(R.id.button_add_retrofit);
        buttonAddRetrofit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == buttonAddRetrofit.getId()) {
            Intent i = new Intent(getApplicationContext(), AddAnggotaActivity.class);
            startActivity(i);
        }
    }

    private void retrieveAnggota() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://triwahyuprasetyo.xyz/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AnggotaService service = retrofit.create(AnggotaService.class);

        Call<AnggotaWrapper> call = service.listAnggota();
        call.enqueue(new Callback<AnggotaWrapper>() {
            @Override
            public void onResponse(Call<AnggotaWrapper> call, Response<AnggotaWrapper> response) {
                Toast.makeText(getApplicationContext(), "Success Retrieve: " + response.code() + "/" + response.message(), Toast.LENGTH_SHORT).show();
                String[] daftarNama = new String[response.body().getAnggota().size()];
                int i = 0;
                for (AnggotaWrapper.Anggota anggota : response.body().getAnggota()) {
                    Log.d("SDP", "Anggota :: " + anggota.getId());
                    Log.d("SDP", "Anggota :: " + anggota.getNama());
                    Log.d("SDP", "Anggota :: " + anggota.getAlamat());
                    Log.d("SDP", "Anggota :: " + anggota.getUsername());
                    Log.d("SDP", "Anggota :: " + anggota.getPassword());
                    Log.d("SDP", "=======================================");
                    daftarNama[i] = anggota.getNama();
                    i++;
                }
                listViewRetrofit.setAdapter(new ArrayAdapter(Retrofit2Activity.this, android.R.layout.simple_list_item_1, daftarNama));
            }

            @Override
            public void onFailure(Call<AnggotaWrapper> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Fail Retrieve", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
