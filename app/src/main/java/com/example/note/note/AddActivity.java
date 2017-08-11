package com.example.note.note;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.note.note.bean.Note;

import java.text.SimpleDateFormat;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveNote();
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    //获取时间
    public String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return simpleDateFormat.format(date);
    }


    //back时存储数据
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveNote();
    }

    //保存数据（数据为空时不保存）
    private void saveNote() {
        String title;
        String content;
        title = titleEditText.getText().toString();
        content = contentEditText.getText().toString();
        if (!title.trim().isEmpty() || !content.trim().isEmpty()) {
            currTime = getTime();
            Note note = new Note();
            note.setTitle(title);
            note.setTime(currTime);
            note.setContent(content);
            note.save();
        }
    }

}


