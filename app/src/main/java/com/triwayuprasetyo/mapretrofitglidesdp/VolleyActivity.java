package com.triwayuprasetyo.mapretrofitglidesdp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.triwayuprasetyo.mapretrofitglidesdp.process.GlobalConst;
import com.triwayuprasetyo.mapretrofitglidesdp.process.ServiceSample;

public class VolleyActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int ID_NOTIFICATION = 1;
    private Button buttonGetAnggota;
    private String[] daftarId, daftarNama, daftarAlamat, daftarUsername, daftarPassword, daftarLatitude, daftarLongitude, daftarFoto;
    static VolleyActivity va;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);
        buttonGetAnggota = (Button) findViewById(R.id.button_getAnggota_volley);
        buttonGetAnggota.setOnClickListener(this);

        va=this;
        registerReceiver(getProcessStatus, new IntentFilter(GlobalConst.ID_BROADCAST_OTHER_PROCESS));

    }

    private static BroadcastReceiver getProcessStatus = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                final String message = extras.getString(GlobalConst.ID_RESULT_FROM_OTHER_PROCESS);
                Log.i("SDP NOTIFICATION","");
                va.generateNotification(message);
            }
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == buttonGetAnggota.getId()) {
//            String url = "http://triwahyuprasetyo.xyz/daftaranggota.php";
//            JsonObjectRequest jsObjRequest = new JsonObjectRequest
//                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            Log.i("SDP Volley", "Response : SUCCESS");
//                            try {
//                                JSONArray menuitemArray = response.getJSONArray("data");
//                                daftarId = new String[menuitemArray.length()];
//                                daftarNama = new String[menuitemArray.length()];
//                                daftarAlamat = new String[menuitemArray.length()];
//                                daftarUsername = new String[menuitemArray.length()];
//                                daftarPassword = new String[menuitemArray.length()];
//                                daftarLatitude = new String[menuitemArray.length()];
//                                daftarLongitude = new String[menuitemArray.length()];
//                                daftarFoto = new String[menuitemArray.length()];
//                                for (int i = 0; i < menuitemArray.length(); i++) {
//                                    daftarId[i] = menuitemArray.getJSONObject(i).getString("id").toString();
//                                    daftarNama[i] = menuitemArray.getJSONObject(i).getString("nama").toString();
//                                    daftarAlamat[i] = menuitemArray.getJSONObject(i).getString("alamat").toString();
//                                    daftarUsername[i] = menuitemArray.getJSONObject(i).getString("username").toString();
//                                    daftarPassword[i] = menuitemArray.getJSONObject(i).getString("password").toString();
//                                    daftarLatitude[i] = menuitemArray.getJSONObject(i).getString("latitude").toString();
//                                    daftarLongitude[i] = menuitemArray.getJSONObject(i).getString("longitude").toString();
//                                    daftarFoto[i] = menuitemArray.getJSONObject(i).getString("foto").toString();
//                                    Log.i("SDP Volley", daftarNama[i] + " - " + daftarAlamat[i]);
//                                }
//                            } catch (JSONException e) {
//
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            // TODO Auto-generated method stub
//                            Log.i("SDP Volley", "Response : ERROR");
//                        }
//                    });
//            // Access the RequestQueue through your singleton class.
//            VolleySingleton.getInstance(this).addToRequestQueue(jsObjRequest);

            //generateNotification("Hello Notification");

            Intent intent = new Intent(getApplicationContext(), ServiceSample.class);
            getApplicationContext().startService(intent);

        }
    }

    public void generateNotification(String message) {
        Context context = getApplicationContext();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(ID_NOTIFICATION);//Untuk menutup notifikasi yang ada (dengan id yang disebutkan), ditutup dulu.

        long when = System.currentTimeMillis();

        String contentTitle = "Notification";
        boolean is_notif_sound = true;

        Bitmap icon_big = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);

//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
//        {
//
//            Notification notification = new Notification(R.mipmap.ic_launcher, message, when); // deprecated in API level 11
//            Intent toLaunch = new Intent(context,LauncherActivity.class);
//            toLaunch.putExtra("NOTIFICATION",true);
//
//            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, toLaunch, PendingIntent.FLAG_UPDATE_CURRENT);
//            notification.setLatestEventInfo(context, contentTitle, message, contentIntent); // deprecated in API level 11
//            //Setting Notification Flags
//            notification.flags |= Notification.FLAG_AUTO_CANCEL;
//            if (is_notif_sound) {
//                notification.sound = Uri.parse("android.resource://com.yandi.mp3player/" + R.raw.pop);
//            }
//            //vibrate permission harus diregister dimanifest
//            //notification.vibrate = new long[]{200, 200, 200, 200, 200};
//            notificationManager.notify(1, notification);
//        }
//
//        else{

        Intent notificationIntent = new Intent(context, TambahAnggotaActivity.class);
        notificationIntent.putExtra("NOTIFICATION", true);

        PendingIntent intent = PendingIntent.getActivity(context, 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.addLine(message);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(contentTitle)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(icon_big)
                .setSubText("Sub Text here")
                .setContentInfo("")
                .setWhen(when)
                .setAutoCancel(true)
                .setContentIntent(intent)
                .setStyle(inboxStyle)
                //vibrate permission harus diregister dimanifest
                //.setVibrate(new long[]{200,200,200,200,200})
                .setTicker(message);

        // if (is_notif_sound)
        //mBuilder.setSound(Uri.parse("android.resource://com.yandi.mp3player/" + R.raw.pop));

        notificationManager.notify(1, mBuilder.build());
//        }
    }
}
