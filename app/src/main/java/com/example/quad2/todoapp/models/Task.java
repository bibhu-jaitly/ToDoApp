package com.example.quad2.todoapp.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by quad2 on 9/1/18.
 */

@Entity(tableName = "task")
public class Task {

  @PrimaryKey
  @NonNull
  private String taskId;

  @ColumnInfo(name = "taskName")
  private String taskName;

  @ColumnInfo
  private long createdTime;
  private String taskDescription;
  private boolean completed;

  public Task() {
  }

  public void setAttribute(String taskId, String taskName, long createdTime,
      String taskDescription,
      boolean completed) {
    this.taskId = taskId;
    this.taskName = taskName;
    this.createdTime = createdTime;
    this.taskDescription = taskDescription;
    this.completed = completed;
  }

  public long getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(long createdTime) {
    this.createdTime = createdTime;
  }

  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  public String getTaskName() {
    return taskName;
  }

  public void setTaskName(String taskName) {
    this.taskName = taskName;
  }

  public String getTaskDescription() {
    return taskDescription;
  }

  public void setTaskDescription(String taskDescription) {
    this.taskDescription = taskDescription;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean taskStatus) {
    this.completed = taskStatus;
  }

  @Override
  public String toString() {
    return "Task{" +
        "taskId='" + taskId + '\'' +
        ", taskName='" + taskName + '\'' +
        ", createdTime='" + createdTime + '\'' +
        ", taskDescription='" + taskDescription + '\'' +
        ", taskStatus=" + completed +
        '}';
  }
}
