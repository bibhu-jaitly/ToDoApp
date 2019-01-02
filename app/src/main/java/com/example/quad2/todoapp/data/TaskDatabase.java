package com.example.quad2.todoapp.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import com.example.quad2.todoapp.models.Task;

@Database(entities = { Task.class }, version = 1, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {

  private static volatile TaskDatabase INSTANCE;

  public abstract TaskDao taskDao();

  public static TaskDatabase getDatabase(Context context) {
    if (INSTANCE == null) {
      synchronized (TaskDatabase.class) {
        if (INSTANCE == null) {
          INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TaskDatabase.class,
              "task_table").build();
        }
      }
    }
    return INSTANCE;
  }
}
