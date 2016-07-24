package com.triwayuprasetyo.mapretrofitglidesdp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.triwayuprasetyo.mapretrofitglidesdp.retrofit.AnggotaService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

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
    private static final int REQUEST_TAKE_PHOTO = 1;
    private final int PICKFILE_RESULT_CODE = 42;
    private String imageFileName = "";
    private String mCurrentPhotoPath;
    private Button buttonUploadFile, buttonSelectFile, buttonCamera;
    private String pictureImagePath = "";
    private boolean upimg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        buttonUploadFile = (Button) findViewById(R.id.button_upload_upload_image);
        buttonUploadFile.setOnClickListener(this);
        buttonSelectFile = (Button) findViewById(R.id.button_select_upload_image);
        buttonSelectFile.setOnClickListener(this);
        buttonCamera = (Button) findViewById(R.id.button_camera_upload_image);
        buttonCamera.setOnClickListener(this);
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
        String descriptionString = "hello description";
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
            upimg = true;
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICKFILE_RESULT_CODE);
        } else if (v.getId() == buttonSelectFile.getId()) {
            //cara 1 --> file manager
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("file/*");
//            startActivityForResult(intent, PICKFILE_RESULT_CODE);

            //cara 2 --> gallery
//            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
//                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(pickPhoto, PICKFILE_RESULT_CODE);

            //cara 3 --> file manager & gallery
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICKFILE_RESULT_CODE);
        } else if (v.getId() == buttonCamera.getId()) {
            Log.i("SDP INTENT", "CAMERA");
            //cara 1
            //dispatchTakePictureIntent();

            //cara 2
            //takepic();

            //cara 3
            openBackCamera();
        }
    }

    private void takepic() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
    }

    private void SaveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
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
                        if (upimg == true) {
                            upimg = false;
                            uploadFile(path);
                        }
                    }
                }
                break;
            case REQUEST_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Log.i("SDP SUCCESSS", "SUCCESS TAKE POTO");
                    galleryAddPic();
                    Log.i("SDP Path", "/storage/emulated/0/Pictures/" + imageFileName);

//                    cara 2 --> simpan image tp dikompress
//                    Bundle extras = data.getExtras();
//                    Bitmap bitMap = (Bitmap) extras.get("data");
//                    SaveImage(bitMap);
                }
                break;
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.i("SDP ERROR", "" + ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.triwayuprasetyo.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
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

}
