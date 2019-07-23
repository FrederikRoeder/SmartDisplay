package de.fhws.smartdisplay.server;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface ServerConnection {

    @GET("clockState")
    Call<String> getClockState();

    @GET("clockSwitch")
    Call<String> switchClock();

    @GET("todoState")
    Call<String> getTodoState();

    @GET("todoSwitch")
    Call<String> switchTodo();

    @GET("timerState")
    Call<String> getTimerState();

    @GET("timerSwitch")
    Call<String> switchTimer();

    @GET("temperatureState")
    Call<String> getTemperatureState();

    @GET("temperatureSwitch")
    Call<String> switchTemperature();

    @GET("effectState")
    Call<String> getEffectState();

    @GET("effectSwitch")
    Call<String> switchEffect();

    @GET("todoGet")
    Call<List<String>> getTodoList();

    @POST("todoAdd")
    Call<Void> addTodo(@Body String todo);

    @DELETE("todoDel/{todo}")
    Call<Void> deleteTodo(@Path("todo") String todo);

    @GET("timerGet")
    Call<List<String>> getTimerList();

    @POST("timerAdd")
    Call<Void> addTimer(@Body String timer);

    @DELETE("timerDel/{timer}")
    Call<Void> deleteTimer(@Path("timer") String timer);

    @POST("notification")
    Call<Void> sendNotification(@Body String notification);

    @POST("name")
    Call<Void> sendName(@Body String name);

}
