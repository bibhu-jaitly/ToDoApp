package com.example.quad2.todoapp.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import com.example.quad2.todoapp.data.TaskDao;
import com.example.quad2.todoapp.data.TaskDatabase;
import com.example.quad2.todoapp.models.Task;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TaskDetailViewModel extends AndroidViewModel {

  private TaskDatabase taskDatabase;
  private TaskDao taskDao;
  private LiveData<Task> taskLiveData;
  private CompositeDisposable compositeDisposable;

  public TaskDetailViewModel(@NonNull Application application) {
    super(application);
    compositeDisposable = new CompositeDisposable();
    taskDatabase = TaskDatabase.getDatabase(application);
    taskDao = taskDatabase.taskDao();
  }

  public LiveData<Task> getTask(String id) {
    return taskDao.getTask(id);
  }

  public void updateTask(final Task task) {
    compositeDisposable.add(Single.create(emitter -> taskDao.updateTask(task))
        .subscribeOn(Schedulers.io()).subscribe());
  }

  public void deleteTask(final Task task) {
    compositeDisposable.add(Single.create(emitter -> taskDao.deleteTask(task))
        .subscribeOn(Schedulers.io()).subscribe());
  }

  public void clearCompositeDisposable(){
    compositeDisposable.clear();
  }
}
