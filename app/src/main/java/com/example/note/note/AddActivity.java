package com.example.note.note;

import android.icu.text.SimpleDateFormat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

import com.example.note.note.bean.Note;

import java.util.Date;
import java.util.TooManyListenersException;

public class AddActivity extends AppCompatActivity {

    private String currTime;

    private TextView timeTextView;
    private EditText titleEditText;
    private EditText contentEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        timeTextView = (TextView) findViewById(R.id.time_TextView);
        titleEditText = (EditText) findViewById(R.id.title_EditView);
        contentEditText = (EditText) findViewById(R.id.content_EditView);

        currTime = getTime();
        timeTextView.setText(currTime);


        //设置toolbar上back图标
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_add);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }

    }


    //获取时间
    public String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String cuTime = dateFormat.format(date);
        return cuTime;
    }


    //back时存储数据
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // TODO: 2017/8/1 需添检测数据是否发生改变
        saveNote(true);
    }

    //保存数据
    private void saveNote(boolean isSave) {
        if (isSave) {
            String title;
            String content;

            title = titleEditText.getText().toString();
            content = contentEditText.getText().toString();
            currTime = getTime();

            Note note = new Note();
            note.setTitle(title);
            note.setTime(currTime);
            note.setContent(content);
            note.save();
        }
    }


}


