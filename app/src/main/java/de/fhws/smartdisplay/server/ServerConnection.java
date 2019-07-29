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

    @GET("clockSwitchOn")
    Call<String> switchClockOn();

    @GET("clockSwitchOff")
    Call<String> switchClockOff();

    @GET("todoState")
    Call<String> getTodoState();

    @GET("todoSwitchOn")
    Call<String> switchTodoOn();

    @GET("todoSwitchOff")
    Call<String> switchTodoOff();

    @GET("timerState")
    Call<String> getTimerState();

    @GET("timerSwitchOn")
    Call<String> switchTimerOn();

    @GET("timerSwitchOff")
    Call<String> switchTimerOff();

    @GET("temperatureState")
    Call<String> getTemperatureState();

    @GET("temperatureSwitchOn")
    Call<String> switchTemperatureOn();

    @GET("temperatureSwitchOff")
    Call<String> switchTemperatureOff();

    @GET("effectState")
    Call<String> getEffectState();

    @GET("effectSwitchOn")
    Call<String> switchEffectOn();

    @GET("effectSwitchOff")
    Call<String> switchEffectOff();

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

    @POST("notificationApp")
    Call<Void> sendNotificationApp(@Body String notificationApp);

    @POST("name")
    Call<Void> sendName(@Body String name);

}
