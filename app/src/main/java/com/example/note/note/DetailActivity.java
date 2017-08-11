package com.example.note.note;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.note.note.bean.Note;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String NOTE_TIME = "note_time";
    public static final String NOTE_TITLE = "note_title";
    public static final String NOTE_CONTENT = "note_content";

    private String title;
    private String time;
    private String content;
    private EditText titleEditView;
    private TextView timeTextView;
    private EditText contentEditView;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        title = intent.getStringExtra(NOTE_TITLE);
        time = intent.getStringExtra(NOTE_TIME);
        content = intent.getStringExtra(NOTE_CONTENT);

        titleEditView = (EditText) findViewById(R.id.title_EditView);
        timeTextView = (TextView) findViewById(R.id.time_TextView);
        contentEditView = (EditText) findViewById(R.id.content_EditView);

        titleEditView.setText(title);
        titleEditView.setSelection(titleEditView.getText().length());
        timeTextView.setText(time);
        contentEditView.setText(content);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
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
                updateNote();
                finish();
                break;
            default:
                break;
        }
        return true;
    }


    //返回时更新数据
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateNote();
    }

    //更新笔记（如果没有变化则时间不修改）
    private void updateNote() {
        String newTitle = titleEditView.getText().toString();
        String newContent = contentEditView.getText().toString();
        if (!newTitle.equals(title) || !newContent.equals(content)) {
            String newTime = getTime();
            List<Note> noteList = DataSupport.where("time = ?", time).find(Note.class);
            Note note = noteList.get(0);
            note.setTitle(newTitle);
            note.setTime(newTime);
            note.setContent(newContent);
            note.save();
        }
    }

    public String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

}
