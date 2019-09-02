package de.fhws.smartdisplay.view.popups;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.fhws.smartdisplay.R;
import de.fhws.smartdisplay.server.ConnectionFactory;
import de.fhws.smartdisplay.server.ServerConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimerPopup extends DialogFragment {

    private ServerConnection serverConnection;

    public static TimerPopup newInstance() {
        TimerPopup frag = new TimerPopup();
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_timer, null);

        serverConnection = new ConnectionFactory().buildConnection();

        final EditText hoursInput = view.findViewById(R.id.editTextTimerH);
        final EditText minutesInput = view.findViewById(R.id.editTextTimerMin);
        final EditText secondsInput = view.findViewById(R.id.editTextTimerSec);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Speichern", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        final String inputHours = getInputText(hoursInput);
                        final String inputMinutes = getInputText(minutesInput);
                        final String inputSeconds = getInputText(secondsInput);
                        if(checkInputText(inputHours, inputMinutes, inputSeconds)) {
                            final String time = convertTime(inputHours, inputMinutes, inputSeconds);
                            addTimer(time);
                        }
                        TimerPopup.DialogListener listener = (TimerPopup.DialogListener) getTargetFragment();
                        listener.updateResult();
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TimerPopup.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private String getInputText(EditText textInput) {
        return textInput.getText().toString();
    }

    private boolean checkInputText(String h, String m, String s) {
        if((h.isEmpty() && m.isEmpty() && s.isEmpty()) ||
                ((h.equals("0") || h.equals("00")) && (m.equals("0") || m.equals("00")) && (s.equals("0") || s.equals("00")))) {
            return false;
        }
        if((h.isEmpty() || h.matches("([0-1]?[0-9])|(2[0-3])")) &&
                (m.isEmpty() || m.matches("([0-5]?[0-9])")) &&
                (s.isEmpty() || s.matches("([0-5]?[0-9])"))) {
            return true;
        }
        Toast.makeText(getContext(), "Ungültige Eingabe", Toast.LENGTH_LONG).show();
        return false;
    }

    private void addTimer(String time) {
        serverConnection.addTimer(time).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Keine Verbindung!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private String convertTime(String h, String m, String s) {
        int intTimerH = 0;
        int intTimerM = 0;
        int intTimerS = 0;
        if(!h.isEmpty()) {
            intTimerH = Integer.parseInt(h);
        }
        if(!m.isEmpty()) {
            intTimerM = Integer.parseInt(m);
        }
        if(!s.isEmpty()) {
            intTimerS = Integer.parseInt(s);
        }

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = dateFormat.format(date);

        String timeH = time.substring(0, 2);
        int intTimeH = Integer.parseInt(timeH);
        String timeM = time.substring(3, 5);
        int intTimeM = Integer.parseInt(timeM);
        String timeS = time.substring(6, 8);
        int intTimeS = Integer.parseInt(timeS);

        int tempM = 0;
        int tempH = 0;
        intTimeS += intTimerS;
        if(intTimeS > 59) {
            intTimeS = intTimeS % 60;
            tempM = 1;
        }
        intTimeM += (intTimerM + tempM);
        if(intTimeM > 59) {
            intTimeM = intTimeM % 60;
            tempH = 1;
        }
        intTimeH += (intTimerH + tempH);
        if(intTimeH > 23) {
            intTimeH = intTimeH % 24;
        }

        if(intTimeH < 10) {
            timeH = "0" + intTimeH;
        } else {
            timeH = "" + intTimeH;
        }
        if(intTimeM < 10) {
            timeM = "0" + intTimeM;
        } else {
            timeM = "" + intTimeM;
        }
        if(intTimeS < 10) {
            timeS = "0" + intTimeS;
        } else {
            timeS = "" + intTimeS;
        }
        String destinationTime = timeH + ":" + timeM + ":" + timeS;

        return destinationTime;
    }

    public interface DialogListener {
        void updateResult();
    }
}
