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

import java.io.IOException;

import de.fhws.smartdisplay.R;
import de.fhws.smartdisplay.server.ConnectionFactory;
import de.fhws.smartdisplay.server.ServerConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodoPopup extends DialogFragment {

    private ServerConnection serverConnection;

    public static TodoPopup newInstance() {
        TodoPopup frag = new TodoPopup();
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_todo, null);

        serverConnection = new ConnectionFactory().buildConnection();

        final EditText textInput = view.findViewById(R.id.editTextTodo);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Speichern", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        final String inputText = getInputText(textInput);
                        if(!inputText.isEmpty()) {
                            if(inputText.contains(";")) {
                                Toast.makeText(getContext(), "Unerlaubtes Zeichen!", Toast.LENGTH_LONG).show();
                            } else {
                                serverConnection.addTodo(inputText).enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {

                                    }
                                });
                            }
                        }
                        DialogListener listener = (DialogListener) getTargetFragment();
                        listener.updateResult();
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TodoPopup.this.getDialog().cancel();
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
