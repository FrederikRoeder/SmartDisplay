package de.fhws.smartdisplay.view.popups;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;

import de.fhws.smartdisplay.R;
import de.fhws.smartdisplay.server.ConnectionFactory;
import de.fhws.smartdisplay.server.ServerConnection;

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

        final EditText hourInput = view.findViewById(R.id.editTextTimerH);
        final EditText minutesInput = view.findViewById(R.id.editTextTimerMin);
        final EditText secondsInput = view.findViewById(R.id.editTextTimerSec);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Speichern", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        final String inputHours = getInputText(hourInput);
                        final String inputMinutes = getInputText(minutesInput);
                        final String inputSeconds = getInputText(secondsInput);
                        //todo: Eingabe in Ablauf-Uhrzeit umwandeln
                        final String time = "";
                        //todo: Time an Server schicken
                        if(!inputHours.isEmpty() ) {
                            try {
                                serverConnection.addTimer(time).execute();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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

    public interface DialogListener {
        void updateResult();
    }
}
