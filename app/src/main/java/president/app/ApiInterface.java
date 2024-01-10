package president.app;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("signup.php")
    Call<GetSignupData> getSignupData(
            @Field("name") String name,
            @Field("contact") String contact,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<GetLoginData> getLoginData(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("updateProfile.php")
    Call<GetSignupData> updateProfileData(
            @Field("id") String id,
            @Field("name") String name,
            @Field("contact") String contact,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("deleteProfile.php")
    Call<GetSignupData> deleteProfileData(
            @Field("id") String id
    );

    @Multipart
    @POST("updateProfileImage.php")
    Call<GetProfileImageData> updateProfileImageData(
            @Part("id") RequestBody id,
            @Part("name") RequestBody name,
            @Part("contact") RequestBody contact,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part MultipartBody.Part image
    );

    /*@Multipart
    @POST("addOrganization/")
    Call<AddJobSeekerData> addOrganizationData(
            @Header("token") String token,
            @Part("user_longitude") RequestBody user_longitude,
            @Part MultipartBody.Part imagePassport
    );*/

}