package de.fhws.smartdisplay.server;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;


public interface ServerConnection {

    @GET("clockState")
    Call<String> getClockState();

    @PUT("clockSwitch")
    @FormUrlEncoded
    Call<String> switchClock(@Field("switchState") String switchState);

    @GET("todoState")
    Call<String> getTodoState();

    @PUT("todoSwitch")
    @FormUrlEncoded
    Call<String> switchTodo(@Field("switchState") String switchState);

    @GET("timerState")
    Call<String> getTimerState();

    @PUT("timerSwitch")
    @FormUrlEncoded
    Call<String> switchTimer(@Field("switchState") String switchState);

    @GET("effectState")
    Call<String> getEffectState();

    @PUT("effectSwitch")
    @FormUrlEncoded
    Call<String> switchEffect(@Field("switchState") String switchState);

    @GET("lightingState")
    Call<String> getLightingState();

    @PUT("lightingSwitch")
    @FormUrlEncoded
    Call<String> switchLighting(@Field("switchState") String switchState);

    @PUT("colorSwitch")
    Call<Void> switchColor();

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

    @GET("easteregg")
    Call<String> easteregg();

    @GET("startSnake")
    Call<String> startSnake();

    @GET("startPong")
    Call<String> startPong();
}
