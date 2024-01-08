package president.app;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetLoginData {

    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("UserData")
    @Expose
    public List<GetLoginUserData> userData;


    public class GetLoginUserData {
        @SerializedName("userId")
        @Expose
        public String userId;
        @SerializedName("userName")
        @Expose
        public String userName;
        @SerializedName("userEmail")
        @Expose
        public String userEmail;
        @SerializedName("userContact")
        @Expose
        public String userContact;
    }
}
