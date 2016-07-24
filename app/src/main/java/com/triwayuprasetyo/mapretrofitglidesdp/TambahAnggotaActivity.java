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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TambahAnggotaActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_TAKE_PHOTO = 41;
    private final int PICKFILE_RESULT_CODE = 42;
    private EditText editTextNama, editTextAlamat, editTextUsername, editTextPassword, editTextPath;
    private Button buttonSave, buttonCamera, buttonBrowse;
    private String imageFileName = "";
    private String pictureImagePath = "";

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

    private void openBackCamera() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = timeStamp + ".jpg";
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
        File f = new File("/storage/emulated/0/Pictures/" + imageFileName);
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
                    }
                }
                break;
            case REQUEST_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Log.i("SDP SUCCESSS", "SUCCESS TAKE POTO");
                    Log.i("SDP Path", "/storage/emulated/0/Pictures/" + imageFileName);
                    editTextPath.setText("/storage/emulated/0/Pictures/" + imageFileName);
                    galleryAddPic();
                }
                break;
        }
    }
}
