package com.example.quad2.todoapp.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by quad2 on 9/1/18.
 */

public class Task extends RealmObject {

  @PrimaryKey
  private String taskId;
  @Required
  private String taskName;
  private long createdTime;
  private String taskDescription;
  private boolean completed;

  public Task() {
  }

  public void setAttribute( String taskName, long createdTime,
      String taskDescription,
      boolean completed) {
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
