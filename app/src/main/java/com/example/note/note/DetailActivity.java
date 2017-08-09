package com.example.note.note;

import android.content.Intent;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.note.note.bean.Note;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
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
    private ImageButton boldButton;
    private ImageButton italicButton;
    private ImageButton underlineButton;
    private ImageButton clearButton;
    
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

        setupBold();
        setupItalic();
        setupUnderline();
        setupClear();

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

    //获取当前时间
    public String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    //初始化各个格式的按钮
    private void setupBold() {
        boldButton = (ImageButton) findViewById(R.id.bold);
        final List<StyleSpan> boldSpanList = new ArrayList<>();
        // TODO: 2017/8/9 待添加点击后的保存取消格式等操作
        boldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = contentEditView.getSelectionStart();
                int end = contentEditView.getSelectionEnd();
                StyleSpan span = new StyleSpan(Typeface.BOLD);
                contentEditView.getEditableText().setSpan(span,start,end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                boldSpanList.add(span);
            }
        });
        boldButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(DetailActivity.this,"加粗",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupItalic() {
        italicButton = (ImageButton) findViewById(R.id.italic);
        final List<StyleSpan> italicSpanList = new ArrayList<>();
        // TODO: 2017/8/9 待添加点击后的保存取消格式等操作
        italicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = contentEditView.getSelectionStart();
                int end = contentEditView.getSelectionEnd();
                StyleSpan span = new StyleSpan(Typeface.ITALIC);
                contentEditView.getEditableText().setSpan(span,start,end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                italicSpanList.add(span);
            }
        });
        italicButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(DetailActivity.this,"斜体",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupUnderline() {
        underlineButton = (ImageButton) findViewById(R.id.underline);
        final List<UnderlineSpan> underlineSpanList = new ArrayList<>();
        // TODO: 2017/8/9 待添加点击后的保存取消格式等操作
        underlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = contentEditView.getSelectionStart();
                int end = contentEditView.getSelectionEnd();
                UnderlineSpan underlineSpan = new UnderlineSpan();
                contentEditView.getEditableText().setSpan(underlineSpan,start,end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                underlineSpanList.add(underlineSpan);
            }
        });
        underlineButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(DetailActivity.this,"下划线",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupClear() {
        clearButton = (ImageButton) findViewById(R.id.clear);
        // TODO: 2017/8/9 待添加点击后的取消格式等操作
        clearButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(DetailActivity.this,"清除所有格式",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

}
