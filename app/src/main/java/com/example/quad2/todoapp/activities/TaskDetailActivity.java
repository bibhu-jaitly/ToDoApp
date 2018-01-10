package com.example.quad2.todoapp.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.quad2.todoapp.R;
import com.example.quad2.todoapp.models.Task;
import com.example.quad2.todoapp.utils.SystemUtils;
import io.realm.Realm;
import io.realm.Realm.Transaction;
import java.sql.Timestamp;
import java.util.Date;

public class TaskDetailActivity extends AppCompatActivity {

  @BindView(R.id.task_name)
  TextView taskName;
  @BindView(R.id.task_description)
  TextView taskDescription;
  @BindView(R.id.status)
  TextView status;
  @BindView(R.id.timeStamp)
  TextView timeCreated;
  @BindView(R.id.complete_btn)
  Button completeBtn;
  private ProgressDialog progressDialog;

  private String taskId, title, description;
  private boolean isCompleted;
  private long timeStamp;

  private Realm realm;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task_detail);
    ButterKnife.bind(this);

    realm = Realm.getDefaultInstance();

    progressDialog = new ProgressDialog(this);
    progressDialog.setMessage("Please Wait...");
    progressDialog.setCancelable(false);

    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {
      taskId = bundle.getString("taskId");
      title = bundle.getString("taskName");
      description = bundle.getString("description");
      timeStamp = bundle.getLong("timeStamp");
      isCompleted = bundle.getBoolean("taskStatus");
    }

    setViews();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.detail_activity_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_delete) {
      realm.executeTransactionAsync(new Transaction() {
        @Override
        public void execute(Realm realm) {
          realm.where(Task.class).equalTo("taskId", taskId)
              .findFirst()
              .deleteFromRealm();
        }
      });
      realm.refresh();
      finish();
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }

  private void setViews() {
    taskName.setText(title);
    taskDescription.setText(description);
    timeCreated.setText(SystemUtils.getDate(timeStamp));
    if (isCompleted) {
      status.setText("Completed");
    } else {
      status.setText("Pending");
    }
  }

  @OnClick(R.id.complete_btn)
  public void onCompleteClicked() {
    if (!isCompleted) {
      Builder builder = new Builder(this);
      builder.setMessage("Is this Task completed?")
          .setPositiveButton("Yes", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              progressDialog.show();
              realm.executeTransactionAsync(new Transaction() {
                @Override
                public void execute(Realm realm) {
                  Task task = realm.where(Task.class).equalTo("taskId", taskId).findFirst();
                  task.setCompleted(true);
                }
              });
              realm.refresh();
              isCompleted = true;
              progressDialog.dismiss();
              setViews();
            }
          })
          .setNegativeButton("No", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
            }
          });
      AlertDialog dialog = builder.create();
      dialog.show();
    } else {
      Toast.makeText(this, "Already Completed", Toast.LENGTH_SHORT).show();
    }
  }

  @OnClick(R.id.pending_btn)
  public void onPendingClicked() {
    if (isCompleted) {
      Builder builder = new Builder(this);
      builder.setMessage("Is this Task still pending?")
          .setPositiveButton("Yes", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              progressDialog.show();
              realm.executeTransactionAsync(new Transaction() {
                @Override
                public void execute(Realm realm) {
                  Task task = realm.where(Task.class).equalTo("taskId", taskId).findFirst();
                  task.setCompleted(false);
                }
              });
              realm.refresh();
              isCompleted = false;
              progressDialog.dismiss();
              setViews();
            }
          })
          .setNegativeButton("No", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
            }
          });
      AlertDialog dialog = builder.create();
      dialog.show();
    } else {
      Toast.makeText(this, "Already Pending", Toast.LENGTH_SHORT).show();
    }
  }

  @OnClick(R.id.edit_btn)
  public void onEditClicked() {
    showEditTaskDialog();
  }

  private void handleEditBtnClick(EditText name, EditText taskDescription,
      final AlertDialog dialog) {
    final String nameString = name.getText().toString();
    final String descString = taskDescription.getText().toString();
    if (SystemUtils.isStringValid(nameString) && SystemUtils.isStringValid(descString)) {
      title = nameString;
      description = descString;
      progressDialog.show();
      realm.executeTransactionAsync(new Transaction() {
        @Override
        public void execute(Realm realm) {
          dialog.dismiss();
          Task task = realm.where(Task.class).equalTo("taskId", taskId).findFirst();
          task.setTaskName(nameString);
          task.setTaskDescription(descString);
        }
      });
      realm.refresh();
      setViews();
      progressDialog.dismiss();
    } else if (!SystemUtils.isStringValid(nameString)) {
      name.setError("Enter valid title for task!");
    } else if (!SystemUtils.isStringValid(descString)) {
      taskDescription.setError("Description can't be empty!");
    }
  }

  private void showEditTaskDialog() {
    final AlertDialog dialog;
    Builder builder = new Builder(this).setView(R.layout.dialog_add_task);
    dialog = builder.create();
    dialog.show();
    TextView dialogTitle = dialog.findViewById(R.id.dialog_title);
    dialogTitle.setText("Edit Task");
    final EditText taskName = dialog.findViewById(R.id.task_name);
    final EditText taskDescription = dialog.findViewById(R.id.task_desc);
    Button addBtn = dialog.findViewById(R.id.add_btn);
    addBtn.setText("Save");
    Button cancelBtn = dialog.findViewById(R.id.cancel_btn);
    addBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        handleEditBtnClick(taskName, taskDescription, dialog);
      }
    });
    cancelBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });
  }

}
