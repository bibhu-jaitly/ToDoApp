package com.example.quad2.todoapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import io.realm.Realm;
import io.realm.Realm.Transaction;
import io.realm.RealmResults;
import java.util.UUID;

public class TaskListActivity extends AppCompatActivity {

  @BindView(R.id.task_rv)
  RecyclerView taskRv;
  @BindView(R.id.no_task_text)
  TextView noTaskText;
  private Realm realm;
  private RealmResults<Task> tasks;
  private TaskAdapter adapter;
  private ProgressDialog progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task_list);
    ButterKnife.bind(this);
    progressDialog = new ProgressDialog(this);
    progressDialog.setMessage("Please Wait...");
    progressDialog.setCancelable(false);
    realm = Realm.getDefaultInstance();
    realm.setAutoRefresh(true);
    tasks = realm.where(Task.class).findAll().sort("createdTime");
    adapter = new TaskAdapter(tasks, this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    setTaskRv();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    realm.close();
  }

  @OnClick(R.id.fab)
  public void onViewClicked() {
    showAddTaskDialog();
  }

  private void setTaskRv() {
    printdata();
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    taskRv.setLayoutManager(linearLayoutManager);
    taskRv.setAdapter(adapter);
    adapter.notifyDataSetChanged();
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
    addBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        handleAddBtnClick(taskName, taskDescription, dialog);
      }
    });
    cancelBtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });
  }

  private void handleAddBtnClick(EditText name, EditText description, final AlertDialog dialog) {
    final String nameString = name.getText().toString();
    final String descString = description.getText().toString();
    if (SystemUtils.isStringValid(nameString) && SystemUtils.isStringValid(descString)) {
      progressDialog.show();
      realm.executeTransactionAsync(new Transaction() {
        @Override
        public void execute(Realm realm) {
          dialog.dismiss();
          Task task = realm.createObject(Task.class, UUID.randomUUID().toString());
          task.setAttribute(nameString, System.currentTimeMillis(), descString, false);
        }
      });
      realm.refresh();
      //Log.d("taskChk", tasks.);
      adapter.notifyDataSetChanged();
      printdata();
      progressDialog.dismiss();
    } else if (!SystemUtils.isStringValid(nameString)) {
      name.setError("Enter valid title for task!");
    } else if (!SystemUtils.isStringValid(descString)) {
      description.setError("Description can't be empty!");
    }
  }

  private void printdata() {
    RealmResults<Task> results = realm.where(Task.class).findAll().sort("createdTime");
    for (Task task : results) {
      Log.d("saveChk", task.toString());
    }
  }

}
