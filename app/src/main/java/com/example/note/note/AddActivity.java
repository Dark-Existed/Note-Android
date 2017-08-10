package com.example.note.note;

import android.icu.text.SimpleDateFormat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.note.note.bean.Note;
import com.example.note.note.gson.Bold;
import com.example.note.note.gson.Format;
import com.example.note.note.gson.Italic;
import com.example.note.note.gson.Underline;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TooManyListenersException;

public class AddActivity extends AppCompatActivity {

    private String currTime;

    private TextView timeTextView;
    private EditText titleEditText;
    private EditText contentEditText;
    private List<StyleSpan> boldSpanList = new ArrayList<>();
    private List<StyleSpan> italicSpanList = new ArrayList<>();
    private List<UnderlineSpan> underlineSpanList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        timeTextView = (TextView) findViewById(R.id.time_TextView);
        titleEditText = (EditText) findViewById(R.id.title_EditView);
        contentEditText = (EditText) findViewById(R.id.content_EditView);

        currTime = getTime();
        timeTextView.setText(currTime);


        //设置toolbar上back图标
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
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
            note.setFormat(transform());
            note.setRecycled(0);
            note.save();
        }
    }

    //将格式转换成json
    private String transform() {
        Format format = new Format();
        for (int i = 0;i<boldSpanList.size();i++) {
            Bold bold = new Bold();
            bold.start = contentEditText.getEditableText().getSpanStart(boldSpanList.get(i));
            bold.end = contentEditText.getEditableText().getSpanEnd(boldSpanList.get(i));
            format.boldList.add(bold);
        }
        for (int i = 0;i<italicSpanList.size();i++) {
            Italic italic = new Italic();
            italic.start = contentEditText.getEditableText().getSpanStart(italicSpanList.get(i));
            italic.end = contentEditText.getEditableText().getSpanEnd(italicSpanList.get(i));
            format.italicList.add(italic);
        }
        for (int i = 0;i<underlineSpanList.size();i++) {
            Underline underline = new Underline();
            underline.start = contentEditText.getEditableText().getSpanStart(underlineSpanList.get(i));
            underline.end = contentEditText.getEditableText().getSpanEnd(underlineSpanList.get(i));
            format.underlineList.add(underline);
        }
        Gson gson = new Gson();
        return gson.toJson(format);
    }

}


