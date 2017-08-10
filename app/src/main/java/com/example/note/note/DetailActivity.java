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
import com.example.note.note.gson.Bold;
import com.example.note.note.gson.Format;
import com.example.note.note.gson.Italic;
import com.example.note.note.gson.Underline;
import com.google.gson.Gson;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String NOTE_TIME = "note_time";
    public static final String NOTE_TITLE = "note_title";
    public static final String NOTE_CONTENT = "note_content";
    public static final String NOTE_FORMAT = "note_format";

    private String title;
    private String time;
    private String content;
    private String format;
    private boolean isModify = false;
    private EditText titleEditView;
    private TextView timeTextView;
    private EditText contentEditView;
    private ImageButton boldButton;
    private ImageButton italicButton;
    private ImageButton underlineButton;
    private ImageButton clearButton;

    private List<StyleSpan> boldSpanList = new ArrayList<>();
    private List<StyleSpan> italicSpanList = new ArrayList<>();
    private List<UnderlineSpan> underlineSpanList = new ArrayList<>();

    // TODO: 2017/8/10 单个格式的取消 覆盖同一地方设置格式
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        title = intent.getStringExtra(NOTE_TITLE);
        time = intent.getStringExtra(NOTE_TIME);
        content = intent.getStringExtra(NOTE_CONTENT);
        format = intent.getStringExtra(NOTE_FORMAT);

        titleEditView = (EditText) findViewById(R.id.title_EditView);
        timeTextView = (TextView) findViewById(R.id.time_TextView);
        contentEditView = (EditText) findViewById(R.id.content_EditView);

        titleEditView.setText(title);
        titleEditView.setSelection(titleEditView.getText().length());
        timeTextView.setText(time);
        contentEditView.setText(content);
        restore(format);

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
        if (!newTitle.equals(title) || !newContent.equals(content) || isModify) {
            String newTime = getTime();
            List<Note> noteList = DataSupport.where("time = ?", time).find(Note.class);
            Note note = noteList.get(0);
            note.setTitle(newTitle);
            note.setTime(newTime);
            note.setContent(newContent);
            note.setFormat(transform());
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
        boldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = contentEditView.getSelectionStart();
                int end = contentEditView.getSelectionEnd();
                StyleSpan span = new StyleSpan(Typeface.BOLD);
                contentEditView.getEditableText().setSpan(span,start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                isModify = true;
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
        italicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = contentEditView.getSelectionStart();
                int end = contentEditView.getSelectionEnd();
                StyleSpan span = new StyleSpan(Typeface.ITALIC);
                contentEditView.getEditableText().setSpan(span,start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                isModify = true;
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
        underlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = contentEditView.getSelectionStart();
                int end = contentEditView.getSelectionEnd();
                UnderlineSpan underlineSpan = new UnderlineSpan();
                contentEditView.getEditableText().setSpan(underlineSpan,start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                isModify = true;
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
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0;i<boldSpanList.size();i++) {
                    contentEditView.getEditableText().removeSpan(boldSpanList.get(i));
                }
                for (int i = 0;i<italicSpanList.size();i++) {
                    contentEditView.getEditableText().removeSpan(italicSpanList.get(i));
                }
                for (int i = 0;i<underlineSpanList.size();i++) {
                    contentEditView.getEditableText().removeSpan(underlineSpanList.get(i));
                }
                boldSpanList.clear();
                italicSpanList.clear();
                underlineSpanList.clear();
                isModify = true;
            }
        });
        clearButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(DetailActivity.this,"清除所有格式",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    //将格式转换成json
    private String transform() {
        Format format = new Format();
        for (int i = 0;i<boldSpanList.size();i++) {
            Bold bold = new Bold();
            bold.start = contentEditView.getEditableText().getSpanStart(boldSpanList.get(i));
            bold.end = contentEditView.getEditableText().getSpanEnd(boldSpanList.get(i));
            format.boldList.add(bold);
        }
        for (int i = 0;i<italicSpanList.size();i++) {
            Italic italic = new Italic();
            italic.start = contentEditView.getEditableText().getSpanStart(italicSpanList.get(i));
            italic.end = contentEditView.getEditableText().getSpanEnd(italicSpanList.get(i));
            format.italicList.add(italic);
        }
        for (int i = 0;i<underlineSpanList.size();i++) {
            Underline underline = new Underline();
            underline.start = contentEditView.getEditableText().getSpanStart(underlineSpanList.get(i));
            underline.end = contentEditView.getEditableText().getSpanEnd(underlineSpanList.get(i));
            format.underlineList.add(underline);
        }
        Gson gson = new Gson();
        return gson.toJson(format);
    }

    //还原格式
    private void restore(String rFormat) {
        Gson gson = new Gson();
        Format format = gson.fromJson(rFormat, Format.class);
        for (int i = 0;i<format.boldList.size();i++) {
            int start = format.boldList.get(i).start;
            int end = format.boldList.get(i).end;
            StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
            contentEditView.getEditableText().setSpan(boldSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            boldSpanList.add(boldSpan);
        }
        for (int i = 0;i<format.italicList.size();i++) {
            int start = format.italicList.get(i).start;
            int end = format.italicList.get(i).end;
            StyleSpan italicSpan = new StyleSpan(Typeface.ITALIC);
            contentEditView.getEditableText().setSpan(italicSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            italicSpanList.add(italicSpan);
        }
        for (int i=0;i<format.underlineList.size();i++) {
            int start = format.underlineList.get(i).start;
            int end = format.underlineList.get(i).end;
            UnderlineSpan underlineSpan = new UnderlineSpan();
            contentEditView.getEditableText().setSpan(underlineSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            underlineSpanList.add(underlineSpan);
        }
    }

}
