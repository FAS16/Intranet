package com.fahadali.intranet.clients;

import com.fahadali.intranet.model.Attendance;
import com.fahadali.intranet.model.Credentials;
import com.fahadali.intranet.model.Student;
import com.fahadali.intranet.model.Token;

import java.util.Date;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserClient {

    @POST("token")
    Call<Token> login(@Body Credentials credentials);

    @FormUrlEncoded
    @POST("token")
    Call<Token> login(@Field("username") String username, @Field("password") String password, @Field("grant_type") String grantType);

    @GET("api/students/{email}/")
    Call<Student> getStudent(@Path("email") String email, @Header("Authorization") String authHeader);

    @FormUrlEncoded
    @POST("api/attendances")
    Call<Attendance> checkIn(@Field("lessonId") int lessonId,@Field("studentId") int studentId, @Field("timestamp") String timestamp, @Field("checkType") int checkType,  @Field("tagId") String tagId,  @Field("deviceId") String deviceId, @Field("possibleFraud") boolean possibleFraud, @Header("Authorization") String authHeader);


}
