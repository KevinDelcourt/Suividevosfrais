package fr.cned.emdsgil.suividevosfrais;
/**
 * Created by Belal on 11/5/2015.
 */
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface RegisterAPI {
    @FormUrlEncoded
    @POST("/includes/api.php?apicall=ok")
    public void insertUser(
            @Field("login") String login,
            @Field("mdp") String mdp,
            @Field("fraisF") String fraisF,
            @Field("fraisHF") String fraisHF,
            Callback<Response> callback);
}
