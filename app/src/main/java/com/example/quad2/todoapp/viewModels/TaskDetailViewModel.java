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

public class TaskDetailViewModel extends AndroidViewModel {

  private TaskDatabase taskDatabase;
  private TaskDao taskDao;
  private LiveData<Task> taskLiveData;

  public TaskDetailViewModel(@NonNull Application application) {
    super(application);
    taskDatabase  = TaskDatabase.getDatabase(application);
    taskDao = taskDatabase.taskDao();
  }

  public LiveData<Task> getTask(String id){
    return taskDao.getTask(id);
  }

  public void updateTask(final Task task){
    /*Observable.just(true).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Boolean>() {
          @Override public void onSubscribe(Disposable d) {
            taskDao.updateTask(task);
          }

          @Override public void onNext(Boolean aBoolean) {

          }

          @Override public void onError(Throwable e) {

          }

          @Override public void onComplete() {

          }
        });*/

    Completable.fromAction(new Action() {
      @Override public void run() {
        taskDao.updateTask(task);
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

  public void deleteTask(final Task task){
    /*Observable.just(true).observeOn(Schedulers.io())
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Boolean>() {
          @Override public void onSubscribe(Disposable d) {
            taskDao.deleteTask(task);
          }

          @Override public void onNext(Boolean aBoolean) {

          }

          @Override public void onError(Throwable e) {

          }

          @Override public void onComplete() {

          }
        });
*/
    Completable.fromAction(new Action() {
      @Override public void run() throws Exception {
        taskDao.deleteTask(task);
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
