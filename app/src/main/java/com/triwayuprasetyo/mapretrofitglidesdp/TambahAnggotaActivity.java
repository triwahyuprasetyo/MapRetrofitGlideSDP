package com.triwayuprasetyo.mapretrofitglidesdp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.triwayuprasetyo.mapretrofitglidesdp.retrofit.AnggotaService;
import com.triwayuprasetyo.mapretrofitglidesdp.retrofit.AnggotaWrapper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TambahAnggotaActivity extends AppCompatActivity implements View.OnClickListener {
    private final int REQUEST_TAKE_PHOTO = 41;
    private final int PICKFILE_RESULT_CODE = 42;
    private EditText editTextNama, editTextAlamat, editTextUsername, editTextPassword, editTextPath;
    private Button buttonSave, buttonCamera, buttonBrowse;
    private String pictureImagePath;
    private boolean uploadDone = false;
    private boolean postDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_anggota);

        editTextNama = (EditText) findViewById(R.id.editText_nama_tambahAnggota);
        editTextAlamat = (EditText) findViewById(R.id.editText_alamat_tambahAnggota);
        editTextUsername = (EditText) findViewById(R.id.editText_username_tambahAnggota);
        editTextPassword = (EditText) findViewById(R.id.editText_password_tambahAnggota);
        editTextPath = (EditText) findViewById(R.id.editText_pathImage_tambahAnggota);

        buttonSave = (Button) findViewById(R.id.button_save_tambahAnggota);
        buttonSave.setOnClickListener(this);
        buttonCamera = (Button) findViewById(R.id.button_camera_tambahAnggota);
        buttonCamera.setOnClickListener(this);
        buttonBrowse = (Button) findViewById(R.id.button_browse_tambahAnggota);
        buttonBrowse.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == buttonSave.getId()) {
            Log.i("TAMBAH ANGGOTA", "SAVE");
            String nama = editTextNama.getText().toString().trim();
            String alamat = editTextAlamat.getText().toString().trim();
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String path = editTextPath.getText().toString().trim();
            if (!nama.equals("") && !alamat.equals("") && !username.equals("") && !password.equals("") && !path.equals("")) {
                if (path.endsWith(".jpg") || path.endsWith(".png")) {
                    File imageFile = new File(path);
                    Log.i("SDP Name", imageFile.getName());
                    //retrofit2SaveAnggota(nama, alamat, username, password, imageFile.getName());
                    uploadFile(path, nama, alamat, username, password, imageFile.getName());

                }
            }
        } else if (v.getId() == buttonCamera.getId()) {
            Log.i("TAMBAH ANGGOTA", "CAMERA");
            openBackCamera();
        } else if (v.getId() == buttonBrowse.getId()) {
            Log.i("TAMBAH ANGGOTA", "BROWSE");
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICKFILE_RESULT_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImageURI = data.getData();
                    File imageFile = new File(getRealPathFromURI(selectedImageURI));
                    String path = imageFile.getPath();
                    if (path.contains("Exception")) {
                        Log.i("SDP UPLOAD", "Gagal Ambil Path");
                    } else {
                        Log.i("SDP UPLOAD", path);
                        editTextPath.setText(path);
                        Log.i("SDP Name", imageFile.getName());
                    }
                }
                break;
            case REQUEST_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Log.i("SDP SUCCESSS", "SUCCESS TAKE POTO");
                    Log.i("SDP Path", pictureImagePath);
                    editTextPath.setText(pictureImagePath);
                    galleryAddPic();
                    File imageFile = new File(pictureImagePath);
                    Log.i("SDP Name", imageFile.getName());
                }
                break;
        }
    }

    private void openBackCamera() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        Uri outputFileUri = Uri.fromFile(file);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(pictureImagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result = "";
        try {
            Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
            if (cursor == null) { // Source is Dropbox or other similar local file path
                result = contentURI.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
                cursor.close();
            }
        } catch (IllegalStateException e) {
            result = "Exception " + e.getMessage();
        } catch (RuntimeException e) {
            result = "Exception " + e.getMessage();
        } catch (Exception e) {
            result = "Exception " + e.getMessage();
        }
        return result;
    }

    private void callFinish() {
        finish();
        uploadDone = false;
        postDone = false;
    }

    private void uploadFile(String uri, String nama, String alamat, String username, String password, String foto) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://triwahyuprasetyo.xyz/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AnggotaService service = retrofit.create(AnggotaService.class);
        File file = new File(uri);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("fileToUpload", file.getName(), requestFile);
        String descriptionString = nama+":"+username+":"+password+":"+alamat+":"+""+":"+""+":"+foto;
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);
        Call<ResponseBody> call = service.upload2(description, body);
        call.enqueue(new Callback<ResponseBody>() {
                         @Override
                         public void onResponse(Call<ResponseBody> call,
                                                Response<ResponseBody> response) {
                             Log.v("Upload", "success");
                             uploadDone = true;
                             //if (uploadDone == true && postDone == true) {
                                 callFinish();
                             //}
                         }

                         @Override
                         public void onFailure(Call<ResponseBody> call, Throwable t) {
                             Log.e("Upload error:", t.getMessage());
                             uploadDone = true;
                             if (uploadDone == true && postDone == true) {
                                 callFinish();
                             }
                         }
                     }
        );
    }

    private void retrofit2SaveAnggota(String nama, String alamat, String username, String password, String foto) {
        AnggotaWrapper.Anggota a = new AnggotaWrapper.Anggota();
        a.setNama(nama);
        a.setAlamat(alamat);
        a.setUsername(username);
        a.setPassword(password);
        a.setFoto(foto);

        Log.d("SDP", "Anggota :: " + a.getNama());
        Log.d("SDP", "Anggota :: " + a.getAlamat());
        Log.d("SDP", "Anggota :: " + a.getUsername());
        Log.d("SDP", "Anggota :: " + a.getPassword());
        Log.d("SDP", "Anggota :: " + a.getFoto());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://triwahyuprasetyo.xyz/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AnggotaService service = retrofit.create(AnggotaService.class);

        Call<Void> call = service.tambahPostAnggota(a.getNama(),
                a.getUsername(),
                a.getPassword(),
                a.getAlamat(),
                a.getLatitude(),
                a.getLongitude(),
                a.getFoto());
        call.enqueue(new Callback<Void>() {
                         @Override
                         public void onResponse(Call<Void> call, Response<Void> response) {
                             Toast.makeText(getApplicationContext(), "Add Success : " + response.message(), Toast.LENGTH_SHORT).show();
                             postDone = true;
                             if (uploadDone == true && postDone == true) {
                                 callFinish();
                             }
                         }

                         @Override
                         public void onFailure(Call<Void> call, Throwable t) {
                             Toast.makeText(getApplicationContext(), "Add Fail", Toast.LENGTH_SHORT).show();
                             Log.i("SDP ERROR", t.getMessage());
                             postDone = true;
                             if (uploadDone == true && postDone == true) {
                                 callFinish();
                             }
                         }
                     }
        );
    }
}
