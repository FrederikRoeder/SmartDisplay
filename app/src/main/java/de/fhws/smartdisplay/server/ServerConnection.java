package de.fhws.smartdisplay.server;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServerConnection {

    @GET("clockState")
    Call<String> getClockState();

    @GET("clockSwitchOn")
    Call<Void> switchClockOn();

    @GET("clockSwitchOff")
    Call<Void> switchClockOff();

    @PUT("clockSwitch")
    Call<Void> switchClock(@Body String switchState);

    @GET("todoState")
    Call<String> getTodoState();

    @GET("todoSwitchOn")
    Call<Void> switchTodoOn();

    @GET("todoSwitchOff")
    Call<Void> switchTodoOff();

    @PUT("todoSwitch")
    Call<Void> switchTodo(@Body String switchState);

    @GET("timerState")
    Call<String> getTimerState();

    @GET("timerSwitchOn")
    Call<Void> switchTimerOn();

    @GET("timerSwitchOff")
    Call<Void> switchTimerOff();

    @PUT("timerSwitch")
    Call<Void> switchTimer(@Body String switchState);

    @GET("temperatureState")
    Call<String> getTemperatureState();

    @GET("temperatureSwitchOn")
    Call<Void> switchTemperatureOn();

    @GET("temperatureSwitchOff")
    Call<Void> switchTemperatureOff();

    @PUT("temperatureSwitch")
    Call<Void> switchTemperature(@Body String switchState);

    @GET("effectState")
    Call<String> getEffectState();

    @GET("effectSwitchOn")
    Call<Void> switchEffectOn();

    @GET("effectSwitchOff")
    Call<Void> switchEffectOff();

    @PUT("effectSwitch")
    Call<Void> switchEffect(@Body String switchState);

    @GET("todoGet")
    Call<List<String>> getTodoList();

    @POST("todoAdd")
    Call<Void> addTodo(@Body String todo);

    @PUT("todoDel")
    Call<Void> deleteTodo(@Body String todo);

    @GET("timerGet")
    Call<List<String>> getTimerList();

    @POST("timerAdd")
    Call<Void> addTimer(@Body String timer);

    @PUT("timerDel")
    Call<Void> deleteTimer(@Body String timer);

    @POST("notification")
    Call<Void> sendNotification(@Body String user,
                                @Body String app,
                                @Body String notification);

}
