package com.example.quad2.todoapp.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import com.example.quad2.todoapp.data.TaskDao;
import com.example.quad2.todoapp.data.TaskDatabase;
import com.example.quad2.todoapp.models.Task;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class TaskListViewModel extends AndroidViewModel {

  private TaskDao taskDao;
  private TaskDatabase taskDatabase;
  private LiveData<List<Task>> listLiveData;

  public TaskListViewModel(@NonNull Application application) {
    super(application);
    taskDatabase = TaskDatabase.getDatabase(application);
    taskDao = taskDatabase.taskDao();
    listLiveData = taskDao.getTaskList();
  }

  public LiveData<List<Task>> getAllTasks(){
    return listLiveData;
  }

  public void addTask(final Task task){

    Completable.fromAction(new Action() {
      @Override public void run() throws Exception {
        taskDao.addTask(task);
      }
    }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new CompletableObserver() {
          @Override public void onSubscribe(Disposable d) {

          }

          @Override public void onComplete() {

          }

          @Override public void onError(Throwable e) {
            Log.d("eroo****", e.getMessage());
          }
        });
  }

}
