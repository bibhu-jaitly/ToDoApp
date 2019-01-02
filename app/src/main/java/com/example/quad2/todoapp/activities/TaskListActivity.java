package com.example.quad2.todoapp.activities;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.quad2.todoapp.R;
import com.example.quad2.todoapp.adapters.TaskAdapter;
import com.example.quad2.todoapp.models.Task;
import com.example.quad2.todoapp.utils.SystemUtils;
import com.example.quad2.todoapp.viewModels.TaskListViewModel;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.UUID;

public class TaskListActivity extends AppCompatActivity {

  @BindView(R.id.task_rv)
  RecyclerView taskRv;
  @BindView(R.id.no_task_text)
  TextView noTaskText;
  private TaskAdapter adapter;
  private ProgressDialog progressDialog;
  private TaskListViewModel viewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task_list);
    viewModel = ViewModelProviders.of(this).get(TaskListViewModel.class);
    ButterKnife.bind(this);
    progressDialog = new ProgressDialog(this);
    progressDialog.setMessage("Please Wait...");
    progressDialog.setCancelable(false);
    adapter = new TaskAdapter(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    setTaskRv();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    viewModel.clearCompositeDisposable();
  }

  @OnClick(R.id.fab)
  public void onViewClicked() {
    showAddTaskDialog();
  }

  private void setTaskRv() {
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    taskRv.setLayoutManager(linearLayoutManager);
    taskRv.setAdapter(adapter);
    viewModel.getAllTasks().observe(this, tasks -> adapter.setTasks(tasks));
  }

  private void showAddTaskDialog() {
    final AlertDialog dialog;
    AlertDialog.Builder builder = new Builder(this).setView(R.layout.dialog_add_task);
    dialog = builder.create();
    dialog.show();
    final EditText taskName = dialog.findViewById(R.id.task_name);
    final EditText taskDescription = dialog.findViewById(R.id.task_desc);
    Button addBtn = dialog.findViewById(R.id.add_btn);
    Button cancelBtn = dialog.findViewById(R.id.cancel_btn);
    addBtn.setOnClickListener(v -> handleAddBtnClick(taskName, taskDescription, dialog));
    cancelBtn.setOnClickListener(v -> dialog.dismiss());
  }

  private void handleAddBtnClick(EditText name, EditText description, final AlertDialog dialog) {
    final String nameString = name.getText().toString();
    final String descString = description.getText().toString();
    if (SystemUtils.isStringValid(nameString) && SystemUtils.isStringValid(descString)) {
      Task task = new Task();
      task.setAttribute(UUID.randomUUID().toString(), nameString, System.currentTimeMillis(),
          descString, false);
      viewModel.addTask(task);
      dialog.dismiss();
    } else if (!SystemUtils.isStringValid(nameString)) {
      name.setError("Enter valid title for task!");
    } else if (!SystemUtils.isStringValid(descString)) {
      description.setError("Description can't be empty!");
    }
  }
}
