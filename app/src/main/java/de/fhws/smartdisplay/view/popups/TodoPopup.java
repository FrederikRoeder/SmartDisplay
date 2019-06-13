package de.fhws.smartdisplay.view.popups;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import de.fhws.smartdisplay.R;

public class TodoPopup extends DialogFragment {

    public static TodoPopup newInstance() {
        TodoPopup frag = new TodoPopup();
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_todo, null);

        final EditText textInput = view.findViewById(R.id.editTextTodo);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Speichern", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        final String inputText = getInputText(textInput);
                        //todo: Todo an Server schicken

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
