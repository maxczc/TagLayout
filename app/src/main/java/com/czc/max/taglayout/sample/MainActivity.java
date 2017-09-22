package com.czc.max.taglayout.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;
import com.czc.max.taglayout.TagLayout;
import com.czc.max.taglayout.interfaces.OnTagItemClickListener;
import com.czc.max.taglayout.sample.bean.User;
import com.czc.max.taglayout.sample.view.CustomTagView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


    TagLayout tagLayout1 = (TagLayout) findViewById(R.id.tagLayout1);
    TagLayout tagLayout2 = (TagLayout) findViewById(R.id.tagLayout2);
    TagLayout tagLayout3 = (TagLayout) findViewById(R.id.tagLayout3);

    tagLayout1.setTagSpacing(40);
    tagLayout1.addAllTags(getTags1());
    tagLayout1.setOnTagItemClickListener(new OnTagItemClickListener<String>() {
      @Override public void onItemClick(String value) {
        Toast.makeText(MainActivity.this, value, Toast.LENGTH_SHORT).show();
      }
    });
    tagLayout2.setCustomTag(CustomTagView.class);
    tagLayout2.addAllTags(getTags2());
    tagLayout2.setOnTagItemClickListener(new OnTagItemClickListener<User>() {
      @Override public void onItemClick(User user) {
        Toast.makeText(MainActivity.this, user.name, Toast.LENGTH_SHORT).show();
      }
    });

    tagLayout3.setCustomTag(CustomTagView.class);
    tagLayout3.addAllTags(getTags2());
    tagLayout3.setOnTagItemClickListener(new OnTagItemClickListener<User>() {
      @Override public void onItemClick(User user) {
        Toast.makeText(MainActivity.this, user.name, Toast.LENGTH_SHORT).show();
      }
    });
  }

  private List<String> getTags1() {
    List<String> tags = new ArrayList<>();
    for (int i = 0; i < 9; i++) {
      tags.add("tag " + (i + 1) + ((i % 2 == 0) ? " tag tag" : ""));
    }
    return tags;
  }

  private List<User> getTags2() {
    List<User> tags = new ArrayList<>();
    User user;
    for (int i = 0; i < 10; i++) {
      user = new User("user" + i + ((i % 4 == 0) ? " user user" : ""));
      tags.add(user);
    }
    return tags;
  }
}
