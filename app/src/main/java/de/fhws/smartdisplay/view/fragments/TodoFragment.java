package de.fhws.smartdisplay.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.fhws.smartdisplay.R;
import de.fhws.smartdisplay.server.ServerConnection;
import de.fhws.smartdisplay.view.popups.TodoPopup;

public class TodoFragment extends Fragment implements TodoPopup.DialogListener {

    private ArrayAdapter<String> adapter;
    private ServerConnection serverConnection;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_todo, container, false);

        serverConnection = new ServerConnection();

        setupTodoList(view);

        FloatingActionButton addTodo = view.findViewById(R.id.floatingActionButtonTodo);
        addTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTodoPopup();
            }
        });

        ImageButton refreshButton = view.findViewById(R.id.imageButtonRefreshTodos);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                updateTodoList();
            }
        });
        return view;
    }

    private void setupTodoList(View view) {
        //todo: ToDos vom Server ziehen und in "todos" (next line) speichern
        List<String> todos = new ArrayList<>();
        ListView todoList = view.findViewById(R.id.todoList);

        adapter = new ArrayAdapter<>(getActivity(), R.layout.list_todo, todos);
        todoList.setAdapter(adapter);
        todoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        todoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String todo = (String) parent.getItemAtPosition(position);
                //todo: Server das zu löschende "todo" schicken
                updateTodoList();
                Toast.makeText(getContext(), "ToDo gelöscht", Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }

    private void updateTodoList() {
        //todo: aktualisierte ToDos vom Server ziehen und in "todos" (next line) speichern
        List<String> todos = new ArrayList<>();
        adapter.clear();
        adapter = new ArrayAdapter<>(getActivity(), R.layout.list_todo, todos);
    }

    private void openTodoPopup() {
        TodoPopup todoPopup = TodoPopup.newInstance();
        todoPopup.setTargetFragment(this, 0);
        todoPopup.show(getActivity().getSupportFragmentManager(), "TodoPopup");
    }

    @Override
    public void updateResult() {
        updateTodoList();
    }
}
