package de.fhws.smartdisplay.view.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.fhws.smartdisplay.R;
import de.fhws.smartdisplay.server.ConnectionFactory;
import de.fhws.smartdisplay.server.ServerConnection;
import de.fhws.smartdisplay.view.popups.TodoPopup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodoFragment extends Fragment implements TodoPopup.DialogListener {

    private ServerConnection serverConnection;
    private ArrayAdapter<String> adapter;
    private Timer updateTimer;

    private ListView todoList;
    private List<String> todos;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_todo, container, false);

        serverConnection = new ConnectionFactory().buildConnection();

        todos = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity(), R.layout.list_todo, todos);

        setupTodoList(view);
        setupAddButton(view);
        setupRefreshButton(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateTodoList();

        updateTimer = new Timer();
        updateTimer.schedule(new UpdateTimer(), 30000, 30000);
    }

    @Override
    public void onStop() {
        super.onStop();
        updateTimer.cancel();
    }

    private void setupTodoList(View view) {
        todoList = view.findViewById(R.id.todoList);
        todoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        todoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String todo = (String) parent.getItemAtPosition(position);
                serverConnection.deleteTodo(todo).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()) {
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                public void run() {
                                    updateTodoList();
                                    Toast.makeText(getContext(), "ToDo wird gelöscht", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
                return true;
            }
        });

        todoList.setAdapter(adapter);
    }

    private void updateTodoList() {
        todos = new ArrayList<>();

        serverConnection.getTodoList().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, final Response<String> response) {
                if(response.isSuccessful() && response.body() != null  && response.body().length() > 0) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            todos = Arrays.asList(response.body().split(";"));
                            adapter = new ArrayAdapter<>(getActivity(), R.layout.list_todo, todos);
                            todoList.setAdapter(adapter);
                        }
                    });
                } else {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            adapter = new ArrayAdapter<>(getActivity(), R.layout.list_todo, todos);
                            todoList.setAdapter(adapter);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        adapter = new ArrayAdapter<>(getActivity(), R.layout.list_todo, todos);
                        todoList.setAdapter(adapter);
                    }
                });
            }
        });
    }

    private void setupAddButton(View view) {
        FloatingActionButton addTodo = view.findViewById(R.id.floatingActionButtonTodo);
        addTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTodoPopup();
            }
        });
    }

    private void setupRefreshButton(View view) {
        ImageButton refreshButton = view.findViewById(R.id.imageButtonRefreshTodos);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                updateTodoList();
            }
        });
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

    class UpdateTimer extends TimerTask {
        public void run() {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    updateTodoList();
                }
            });
        }
    }
}
