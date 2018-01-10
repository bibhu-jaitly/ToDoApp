package com.example.quad2.todoapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by quad2 on 10/1/18.
 */

public class SystemUtils {

  public static boolean isStringValid(String input) {
    if (input != null && input.length() > 0) {
      return true;
    } else {
      return false;
    }
  }

  public static String getDate(long time) {
    //Log.d("timeChk", String.valueOf(time) + "***"+ System.currentTimeMillis());
    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
    Date resultDate = new Date(time);
    return sdf.format(resultDate);
  }

}
