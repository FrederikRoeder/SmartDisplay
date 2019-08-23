package de.fhws.smartdisplay.server;

import com.google.gson.JsonArray;

import javax.xml.transform.Result;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;


public interface ServerConnection {

    @GET("retrospect")
    Call<String> getRetrospect();

    @GET("clockState")
    Call<String> getClockState();

    @PUT("clockSwitch")
    @FormUrlEncoded
    Call<Void> switchClock(@Field("switchState") String switchState);

    @GET("todoState")
    Call<String> getTodoState();

    @PUT("todoSwitch")
    @FormUrlEncoded
    Call<Void> switchTodo(@Field("switchState") String switchState);

    @GET("timerState")
    Call<String> getTimerState();

    @PUT("timerSwitch")
    @FormUrlEncoded
    Call<Void> switchTimer(@Field("switchState") String switchState);

    @GET("effectState")
    Call<String> getEffectState();

    @PUT("effectSwitch")
    @FormUrlEncoded
    Call<Void> switchEffect(@Field("switchState") String switchState);

    @GET("todoGet")
    Call<String> getTodoList();

    @POST("todoAdd")
    @FormUrlEncoded
    Call<Void> addTodo(@Field("todo") String todo);

    @PUT("todoDel")
    @FormUrlEncoded
    Call<Void> deleteTodo(@Field("todo") String todo);

    @GET("timerGet")
    Call<String> getTimerList();

    @POST("timerAdd")
    @FormUrlEncoded
    Call<Void> addTimer(@Field("timer") String timer);

    @PUT("timerDel")
    @FormUrlEncoded
    Call<Void> deleteTimer(@Field("timer") String timer);

    @POST("notification")
    @FormUrlEncoded
    Call<Void> sendNotification(@Field("app") String app,
                                @Field("user") String user);

}
