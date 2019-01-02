package com.example.quad2.todoapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.example.quad2.todoapp.models.Task;
import java.util.List;

@Dao
public interface TaskDao {

  @Insert
  void addTask(Task task);

  @Query("SELECT * FROM task")
  LiveData<List<Task>> getTaskList();

  @Query("SELECT * FROM task WHERE taskId=:taskId")
  LiveData<Task> getTask(String taskId);

  @Delete
  void deleteTask(Task task);

  @Update
  void updateTask(Task task);
}
