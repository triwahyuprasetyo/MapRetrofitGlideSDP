package com.triwayuprasetyo.mapretrofitglidesdp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.triwayuprasetyo.mapretrofitglidesdp.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VolleyActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonGetAnggota;
    private String[] daftarId, daftarNama, daftarAlamat, daftarUsername, daftarPassword, daftarLatitude, daftarLongitude, daftarFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);
        buttonGetAnggota = (Button) findViewById(R.id.button_getAnggota_volley);
        buttonGetAnggota.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == buttonGetAnggota.getId()) {
            String url = "http://triwahyuprasetyo.xyz/daftaranggota.php";
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("SDP Volley", "Response : SUCCESS");
                            try {
                                JSONArray menuitemArray = response.getJSONArray("data");
                                daftarId = new String[menuitemArray.length()];
                                daftarNama = new String[menuitemArray.length()];
                                daftarAlamat = new String[menuitemArray.length()];
                                daftarUsername = new String[menuitemArray.length()];
                                daftarPassword = new String[menuitemArray.length()];
                                daftarLatitude = new String[menuitemArray.length()];
                                daftarLongitude = new String[menuitemArray.length()];
                                daftarFoto = new String[menuitemArray.length()];
                                for (int i = 0; i < menuitemArray.length(); i++) {
                                    daftarId[i] = menuitemArray.getJSONObject(i).getString("id").toString();
                                    daftarNama[i] = menuitemArray.getJSONObject(i).getString("nama").toString();
                                    daftarAlamat[i] = menuitemArray.getJSONObject(i).getString("alamat").toString();
                                    daftarUsername[i] = menuitemArray.getJSONObject(i).getString("username").toString();
                                    daftarPassword[i] = menuitemArray.getJSONObject(i).getString("password").toString();
                                    daftarLatitude[i] = menuitemArray.getJSONObject(i).getString("latitude").toString();
                                    daftarLongitude[i] = menuitemArray.getJSONObject(i).getString("longitude").toString();
                                    daftarFoto[i] = menuitemArray.getJSONObject(i).getString("foto").toString();
                                    Log.i("SDP Volley", daftarNama[i] + " - " + daftarAlamat[i]);
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                            Log.i("SDP Volley", "Response : ERROR");
                        }
                    });
            // Access the RequestQueue through your singleton class.
            VolleySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
        }
    }
}
