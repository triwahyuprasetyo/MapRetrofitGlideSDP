package com.triwayuprasetyo.mapretrofitglidesdp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.triwayuprasetyo.mapretrofitglidesdp.retrofit.AnggotaService;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadImageActivity extends AppCompatActivity implements View.OnClickListener {
    private final int PICKFILE_RESULT_CODE = 42;
    private final int SELECT_IMAGE_CODE = 43;
    private final int EXTERNAL_CODE = 44;
    private Button buttonUploadFile, buttonSelectFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        //uploadFile();

        buttonUploadFile = (Button) findViewById(R.id.button_upload_upload_image);
        buttonUploadFile.setOnClickListener(this);
        buttonSelectFile = (Button) findViewById(R.id.button_select_upload_image);
        buttonSelectFile.setOnClickListener(this);
    }

    private void uploadFile(String uri) {
        // create upload service client
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://triwahyuprasetyo.xyz/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AnggotaService service = retrofit.create(AnggotaService.class);

        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
//        File file = FileUtils.getFile(this, fileUri);
        //String filename = "image.png";
        //String path = "/mnt/sdcard/" + filename;

        //Uri imageUri = Uri.parse("file:///mnt/sdcard/ayana_shahab.jpg");
        //File file = new File("mnt/sdcard/ayana_shahab.jpg");
        File file = new File(uri);
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("fileToUpload", file.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);

        // finally, execute the request
        Call<ResponseBody> call = service.upload(description, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == buttonUploadFile.getId()) {
            //uploadFile();
        } else if (v.getId() == buttonSelectFile.getId()) {
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("file/*");
//            startActivityForResult(intent, PICKFILE_RESULT_CODE);

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE_CODE);
            startActivityForResult(intent, 1);

//            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
//                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(pickPhoto, EXTERNAL_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
// TODO Auto-generated method stub
        Log.i("SDP UPLOAD : ", requestCode + " , " + resultCode);
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    String filePath = data.getData().getPath();


                    Uri selectedImageURI = data.getData();
                    File imageFile = new File(getRealPathFromURI(selectedImageURI));
                    Log.i("SDP UPLOAD p: ", imageFile.getPath());
                }
                //intent galery atau camera
                break;
            case SELECT_IMAGE_CODE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImageURI = data.getData();
                    File imageFile = new File(getRealPathFromURI(selectedImageURI));
                    Log.i("SDP UPLOAD s: ", imageFile.getPath());
                    //Toast.makeText(getApplicationContext(), "SDP UPLOAD : " + filePath, Toast.LENGTH_SHORT).show();
                    //uploadFile(filePath);
                }
                //intent galery atau camera
                break;
            case EXTERNAL_CODE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImageURI = data.getData();
                    File imageFile = new File(getRealPathFromURI(selectedImageURI));
                    Log.i("SDP UPLOAD e: ", imageFile.getPath());
                }
                //intent galery atau camera
                break;
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

}
