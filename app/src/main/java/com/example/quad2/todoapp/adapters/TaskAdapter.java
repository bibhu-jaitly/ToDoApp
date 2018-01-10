package com.example.quad2.todoapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.quad2.todoapp.R;
import com.example.quad2.todoapp.activities.TaskDetailActivity;
import com.example.quad2.todoapp.adapters.TaskAdapter.ViewHolder;
import com.example.quad2.todoapp.models.Task;
import com.example.quad2.todoapp.utils.SystemUtils;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by quad2 on 9/1/18.
 */

public class TaskAdapter extends RealmRecyclerViewAdapter<Task, ViewHolder> {


  private Context context;
  private OrderedRealmCollection<Task> tasks;

  public TaskAdapter(
      @Nullable OrderedRealmCollection<Task> data, Context context) {
    super(data, true, true);
    this.tasks = data;
    this.context = context;
  }


  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.adapter_task, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final Task task = tasks.get(position);
    if (task != null) {
      holder.taskName.setText(task.getTaskName());
      holder.taskDescription.setText(task.getTaskDescription());
      holder.timeStamp.setText(SystemUtils.getDate(task.getCreatedTime()));
      if (task.isCompleted()) {
        holder.status.setText("Completed");
      } else {
        holder.status.setText("Pending");
      }

    }
    holder.taskCard.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(context, TaskDetailActivity.class);
        intent.putExtra("taskId", task.getTaskId());
        intent.putExtra("taskName", task.getTaskName());
        intent.putExtra("description", task.getTaskDescription());
        intent.putExtra("taskStatus", task.isCompleted());
        intent.putExtra("timeStamp", task.getCreatedTime());
        context.startActivity(intent);
      }
    });

  }

  class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.task_name)
    TextView taskName;
    @BindView(R.id.task_description)
    TextView taskDescription;
    @BindView(R.id.timeStamp)
    TextView timeStamp;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.task_card)
    CardView taskCard;

    ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

}
