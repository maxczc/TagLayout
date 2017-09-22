package com.czc.max.taglayout.sample.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.czc.max.taglayout.sample.R;
import com.czc.max.taglayout.sample.bean.User;
import com.czc.max.taglayout.interfaces.ITag;

/**
 * @author maxczc
 * @since 22/9/17
 */

public class CustomTagView extends LinearLayout implements ITag<User> {

  private TextView textView;

  public CustomTagView(Context context) {
    this(context, null);
  }

  public CustomTagView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    LayoutInflater.from(context).inflate(R.layout.view_custom_tag, this, true);
    textView = (TextView) findViewById(R.id.valueTv);
    this.setBackgroundResource(R.drawable.shape_label);
  }

  @Override public void setValue(User user) {
    textView.setText(user.name);
  }
}
