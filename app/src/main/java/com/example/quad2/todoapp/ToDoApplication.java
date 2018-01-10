package com.example.quad2.todoapp;

import android.app.Application;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmSchema;

/**
 * Created by quad2 on 9/1/18.
 */

public class ToDoApplication extends Application {


  @Override
  public void onCreate() {
    super.onCreate();
    Realm.init(this);
    RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().name("toDoApp.realm")
        .schemaVersion(0)
        .build();

    try {
      Realm.setDefaultConfiguration(realmConfiguration);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
