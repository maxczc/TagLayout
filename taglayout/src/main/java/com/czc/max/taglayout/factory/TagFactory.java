package com.czc.max.taglayout.factory;

import android.content.Context;
import android.view.View;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author maxczc
 * @since 21/9/17
 */

public class TagFactory {

  @SuppressWarnings({ "unchecked", "TryWithIdenticalCatches" })
  public static <T extends View> T create(Context context, Class<? extends View> cls) {
    T t = null;
    Constructor constructor;
    try {
      constructor = Class.forName(cls.getName()).getConstructor(Context.class);
      t = (T) constructor.newInstance(context);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return t;
  }
}
