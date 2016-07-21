package com.triwayuprasetyo.mapretrofitglidesdp.retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by why on 7/19/16.
 */

public interface AnggotaService {
    @GET("daftaranggota.php")
    Call<AnggotaWrapper> listAnggota();

    @FormUrlEncoded
    @POST("addanggota.php")
    Call<Void> tambahPostAnggota(@Field("nama") String nama,
                                 @Field("username") String username,
                                 @Field("password") String password,
                                 @Field("alamat") String alamat,
                                 @Field("latitude") String latitude,
                                 @Field("longitude") String longitude,
                                 @Field("foto") String foto);
    @Multipart
    @POST("uploadImage.php")
    Call<ResponseBody> upload(@Part("description") RequestBody description,
                              @Part MultipartBody.Part file);
}
