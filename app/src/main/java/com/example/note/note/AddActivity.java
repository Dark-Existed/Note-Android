package com.example.note.note;

import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TooManyListenersException;

public class AddActivity extends AppCompatActivity {

    private String currTime;

    private TextView timeTextView;
    private EditText titleEditText;
    private EditText contentEditText;
    private ImageButton boldButton;
    private ImageButton italicButton;
    private ImageButton underlineButton;
    private ImageButton clearButton;

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

        setupBold();
        setupItalic();
        setupUnderline();
        setupClear();

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

    //初始化各个格式的按钮
    //加粗
    private void setupBold() {
        boldButton = (ImageButton) findViewById(R.id.bold);
        boldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = contentEditText.getSelectionStart();
                int end = contentEditText.getSelectionEnd();
                StyleSpan span = new StyleSpan(Typeface.BOLD);
                setFormat(start,end,boldSpanList,span);
                boldSpanList.add(span);
            }
        });
        boldButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(AddActivity.this,"加粗",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
    //斜体
    private void setupItalic() {
        italicButton = (ImageButton) findViewById(R.id.italic);
        italicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = contentEditText.getSelectionStart();
                int end = contentEditText.getSelectionEnd();
                StyleSpan span = new StyleSpan(Typeface.ITALIC);
                setFormat(start, end, italicSpanList, span);
                italicSpanList.add(span);
            }
        });
        italicButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(AddActivity.this,"斜体",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
    //下划线
    private void setupUnderline() {
        underlineButton = (ImageButton) findViewById(R.id.underline);
        underlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = contentEditText.getSelectionStart();
                int end = contentEditText.getSelectionEnd();
                UnderlineSpan underlineSpan = new UnderlineSpan();
                setFormat(start, end, underlineSpanList, underlineSpan);
                underlineSpanList.add(underlineSpan);
            }
        });
        underlineButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(AddActivity.this,"下划线",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
    //清除所有格式
    private void setupClear() {
        clearButton = (ImageButton) findViewById(R.id.clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0;i<boldSpanList.size();i++) {
                    contentEditText.getEditableText().removeSpan(boldSpanList.get(i));
                }
                for (int i = 0;i<italicSpanList.size();i++) {
                    contentEditText.getEditableText().removeSpan(italicSpanList.get(i));
                }
                for (int i = 0;i<underlineSpanList.size();i++) {
                    contentEditText.getEditableText().removeSpan(underlineSpanList.get(i));
                }
                boldSpanList.clear();
                italicSpanList.clear();
                underlineSpanList.clear();
            }
        });
        clearButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(AddActivity.this,"清除所有格式",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    //判断所添加的格式的位置并做出操作
    private <T> void setFormat(int start, int end, List<T> list,T span) {
        if (end < start) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            int mStart = contentEditText.getEditableText().getSpanStart(list.get(i));
            int mEnd = contentEditText.getEditableText().getSpanEnd(list.get(i));
            if (start == mStart && end == mEnd) {
                //相同位置的取消格式
                contentEditText.getEditableText().removeSpan(list.get(i));
                list.remove(i);
//                Toast.makeText(DetailActivity.this,"执行1",Toast.LENGTH_SHORT).show();
                return;
            } else if (start < mStart && end > mEnd) {
                //覆盖过去的
                contentEditText.getEditableText().setSpan(list.get(i), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                Toast.makeText(DetailActivity.this,"执行2",Toast.LENGTH_SHORT).show();
                return;
            } else if ((mStart < start && start < mEnd && mEnd < end) || (start < mStart && mStart < end && end < mEnd)) {
                //部分重叠的
                int minStart = start < mStart ? start : mStart;
                int maxEnd = end > mEnd ? end : mEnd;
                contentEditText.getEditableText().setSpan(list.get(i), minStart, maxEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                Toast.makeText(DetailActivity.this, "执行3", Toast.LENGTH_SHORT).show();
                return;
            } else if ((mStart < start && end < mEnd) || (mStart < start && end < mEnd)) {
                //在内部 分割成两个 // TODO: 2017/8/10 有问题待解决(不执行4 执行3)
                contentEditText.getEditableText().setSpan(list.get(i), mStart, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                contentEditText.getEditableText().setSpan(span, end, mEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                list.add(span);
//                Toast.makeText(DetailActivity.this, "执行4", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        contentEditText.getEditableText().setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        Toast.makeText(DetailActivity.this,"执行5",Toast.LENGTH_SHORT).show();
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


