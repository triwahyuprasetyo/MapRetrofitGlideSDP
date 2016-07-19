package com.triwayuprasetyo.mapretrofitglidesdp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.triwayuprasetyo.mapretrofitglidesdp.httpRequest.HttpRequest;

import java.util.HashMap;

public class AddAnggotaActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog pd;
    private EditText editTextNama, editTextAlamat, editTextUsername, editTextPassword;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_anggota);
        setTitle("Add Anggota");

        editTextNama = (EditText) findViewById(R.id.edittext_nama_addanggota);
        editTextAlamat = (EditText) findViewById(R.id.edittext_alamat_addanggota);
        editTextUsername = (EditText) findViewById(R.id.edittext_username_addanggota);
        editTextPassword = (EditText) findViewById(R.id.edittext_password_addanggota);

        buttonSave = (Button) findViewById(R.id.button_save_addanggota);
        buttonSave.setOnClickListener(this);
    }

    public void httpRequestSaveAnggota(final String nama,
                                       final String alamat,
                                       final String username,
                                       final String password) {
        Thread background = new Thread(new Runnable() {
            // membuat Handler untuk menerima pesan ketika selesai
            private final Handler handler = new Handler() {
                public void handleMessage(Message msg) {
                    String aResponse = msg.getData().getString("message");
                    // String cek = "Login Sukses";
                    Toast.makeText(
                            getBaseContext(),
                            "Data Anggota Tersimpan " + aResponse,
                            Toast.LENGTH_SHORT).show();

                    pd.dismiss();
                    //getdata();
                    finish();
                    // btnloginlogin.setEnabled(true);
                }
            };

            // program yang dijalankan ketika thread background berjalan
            public void run() {
                String SetServerString = "";

                try {
                    HttpRequest req = new HttpRequest("http://triwahyuprasetyo.xyz/addanggota.php");
                    //GET
                    //req.prepare().sendAndReadString();
                    //POST
                    //req.preparePost().withData("name=Bubu&age=29").send();
                    //POST
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("nama", nama);
                    params.put("alamat", alamat);
                    params.put("username", username);
                    params.put("password", password);
                    //edcari.getText().toString());
                    //req.preparePost().withData(params).sendAndReadJSON();
                    //req.preparePost().withData(params).sendAndReadString();
                    //SetServerString = req.preparePost().withData("name=123&age=29").sendAndReadString(); //req.preparePost().withData(params).sendAndReadString();
                    SetServerString = req.preparePost().withData(params).sendAndReadString();

                } catch (Exception e) {

                }

                Message msgObj = handler.obtainMessage();
                Bundle b = new Bundle();
                b.putString("message", SetServerString);
                msgObj.setData(b);
                handler.sendMessage(msgObj);
            }

        });
        // Start Thread
        pd = ProgressDialog.show(AddAnggotaActivity.this, "Please Wait",
                "Connecting..", true);
        // btnloginlogin.setEnabled(false);
        background.start(); // memanggil thread background agar start
    }

    private void retrofit1SaveAnggota() {
        /*
        //Retrofit 1.9
        RestAdapter restAdapter = new RestAdapter.Builder()
                //.setEndpoint("http://www.cheesejedi.com")
                .setEndpoint("http://triwahyuprasetyo.xyz")
                .build();

        AnggotaInterface anggotaService = restAdapter.create(AnggotaInterface.class);
        AnggotaWrapper.Anggota a = new AnggotaWrapper.Anggota();
        a.setNama(editTextNama.getText().toString());
        a.setAlamat(editTextAlamat.getText().toString());
        a.setUsername(editTextUsername.getText().toString());
        a.setPassword(editTextPassword.getText().toString());

        Log.d("SDP", "Anggota :: " + a.getNama());
        Log.d("SDP", "Anggota :: " + a.getAlamat());
        Log.d("SDP", "Anggota :: " + a.getUsername());
        Log.d("SDP", "Anggota :: " + a.getPassword());

        anggotaService.tambahPostAnggota(a.getNama(),
                a.getUsername(),
                a.getPassword(),
                a.getAlamat(),
                a.getLatitude(),
                a.getLongitude(),
                a.getFoto(), new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        Toast.makeText(getApplicationContext(), "Add Success : " + response2.getStatus(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getApplicationContext(), "Add Error : " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        */
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == buttonSave.getId()) {
            //httpRequestSaveAnggota(editTextNama.getText().toString(), editTextAlamat.getText().toString(), editTextUsername.getText().toString(), editTextPassword.getText().toString());
            retrofit1SaveAnggota();
            finish();
        }
    }
}
