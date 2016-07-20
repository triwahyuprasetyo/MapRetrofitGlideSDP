package com.triwayuprasetyo.mapretrofitglidesdp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.triwayuprasetyo.mapretrofitglidesdp.glide.CustomAdapter;
import com.triwayuprasetyo.mapretrofitglidesdp.retrofit.AnggotaService;
import com.triwayuprasetyo.mapretrofitglidesdp.retrofit.AnggotaWrapper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GlideActivity extends AppCompatActivity {
    private String[] daftarNama, daftarAlamat, daftarUrlFoto;
    private String urlImages;
    private ListView listViewGlide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide);
//        ImageView imageView = (ImageView) findViewById(R.id.imageview_glide);
//        Glide.with(getApplicationContext()).load("http://goo.gl/gEgYUd").into(imageView);
        setTitle("Glide & Retrofit");
        urlImages = "http://triwahyuprasetyo.xyz/images/";
        listViewGlide = (ListView) findViewById(R.id.listview_glide);


    }

    @Override
    protected void onResume() {
        super.onResume();
        retrofit2RetrieveAnggota();
    }

    private void setAdapter() {
        listViewGlide.setAdapter(new CustomAdapter(this, daftarNama, daftarAlamat, daftarUrlFoto));
    }

    private void retrofit2RetrieveAnggota() {
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
                daftarNama = new String[response.body().getAnggota().size()];
                daftarAlamat = new String[response.body().getAnggota().size()];
                daftarUrlFoto = new String[response.body().getAnggota().size()];
                int i = 0;
                for (AnggotaWrapper.Anggota anggota : response.body().getAnggota()) {
                    Log.d("SDP", "Anggota :: " + anggota.getId() + " : " + anggota.getFoto());
                    Log.d("SDP", "Anggota :: " + anggota.getNama() + " : " + anggota.getFoto());
                    Log.d("SDP", "Anggota :: " + anggota.getAlamat() + " : " + anggota.getFoto());
                    Log.d("SDP", "Anggota :: " + anggota.getUsername() + " : " + anggota.getFoto());
                    Log.d("SDP", "Anggota :: " + anggota.getPassword() + " : " + anggota.getFoto());
                    Log.d("SDP", "=======================================");
                    daftarNama[i] = anggota.getNama();
                    daftarAlamat[i] = anggota.getAlamat();
                    daftarUrlFoto[i] = urlImages + anggota.getFoto();
                    i++;
                }
                setAdapter();
            }

            @Override
            public void onFailure(Call<AnggotaWrapper> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Fail Retrieve", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
