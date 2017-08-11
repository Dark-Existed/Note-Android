package com.example.note.note;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.note.note.bean.Note;
import com.example.note.note.gson.BackgroundColor;
import com.example.note.note.gson.Bold;
import com.example.note.note.gson.ForegroundColor;
import com.example.note.note.gson.Format;
import com.example.note.note.gson.Italic;
import com.example.note.note.gson.Strikethrough;
import com.example.note.note.gson.Subscript;
import com.example.note.note.gson.Superscript;
import com.example.note.note.gson.Underline;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
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
    private ImageButton strikethroughButton;
    private ImageButton foregroundcolorButton;
    private ImageButton backgroundcolorButton;
    private ImageButton superscriptButton;
    private ImageButton subscriptButton;


    private List<StyleSpan> boldSpanList = new ArrayList<>();
    private List<StyleSpan> italicSpanList = new ArrayList<>();
    private List<UnderlineSpan> underlineSpanList = new ArrayList<>();
    private List<StrikethroughSpan> strikethroughSpanList = new ArrayList<>();
    private List<ForegroundColorSpan> foregroundColorSpanList = new ArrayList<>();
    private List<BackgroundColorSpan> backgroundColorSpanList = new ArrayList<>();
    private List<SuperscriptSpan> superscriptSpanList = new ArrayList<>();
    private List<SubscriptSpan> subscriptSpanList = new ArrayList<>();


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
        setupStrikethrough();
        setupForegroundColor();
        setupBackgroundColor();
        setupSuperscript();
        setupSubscript();
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
    //删除线
    private void setupStrikethrough() {
        strikethroughButton = (ImageButton) findViewById(R.id.strikethrough);
        strikethroughButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = contentEditText.getSelectionStart();
                int end = contentEditText.getSelectionEnd();
                StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
                setFormat(start,end,strikethroughSpanList,strikethroughSpan);
                strikethroughSpanList.add(strikethroughSpan);
            }
        });
        strikethroughButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(AddActivity.this,"删除线",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
    //字体颜色（前景色）
    private void setupForegroundColor() {
        foregroundcolorButton = (ImageButton) findViewById(R.id.foregroundColor);
        foregroundcolorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = contentEditText.getSelectionStart();
                int end = contentEditText.getSelectionEnd();
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#0099EE"));
                setFormat(start, end, foregroundColorSpanList, foregroundColorSpan);
                foregroundColorSpanList.add(foregroundColorSpan);
            }
        });
        foregroundcolorButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(AddActivity.this,"字体颜色",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
    //背景色
    private void setupBackgroundColor() {
        backgroundcolorButton = (ImageButton) findViewById(R.id.backgroundColor);
        backgroundcolorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = contentEditText.getSelectionStart();
                int end = contentEditText.getSelectionEnd();
                BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.parseColor("#AC00FF30"));
                setFormat(start, end, backgroundColorSpanList, backgroundColorSpan);
                backgroundColorSpanList.add(backgroundColorSpan);
            }
        });
        backgroundcolorButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(AddActivity.this,"背景颜色",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
    //上标
    private void setupSuperscript() {
        superscriptButton = (ImageButton) findViewById(R.id.superscript);
        superscriptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = contentEditText.getSelectionStart();
                int end = contentEditText.getSelectionEnd();
                SuperscriptSpan superscriptSpan = new SuperscriptSpan();
                setFormat(start, end, superscriptSpanList, superscriptSpan);
                superscriptSpanList.add(superscriptSpan);
            }
        });
        superscriptButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(AddActivity.this,"上标",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
    //下标
    private void setupSubscript() {
        subscriptButton = (ImageButton) findViewById(R.id.subscript);
        subscriptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = contentEditText.getSelectionStart();
                int end = contentEditText.getSelectionEnd();
                SubscriptSpan subscriptSpan = new SubscriptSpan();
                setFormat(start, end, subscriptSpanList, subscriptSpan);
                subscriptSpanList.add(subscriptSpan);
            }
        });
        subscriptButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(AddActivity.this,"下标",Toast.LENGTH_SHORT).show();
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
                for (int i = 0; i < boldSpanList.size(); i++) {
                    contentEditText.getEditableText().removeSpan(boldSpanList.get(i));
                }
                for (int i = 0; i < italicSpanList.size(); i++) {
                    contentEditText.getEditableText().removeSpan(italicSpanList.get(i));
                }
                for (int i = 0; i < underlineSpanList.size(); i++) {
                    contentEditText.getEditableText().removeSpan(underlineSpanList.get(i));
                }
                for (int i = 0; i < strikethroughSpanList.size(); i++) {
                    contentEditText.getEditableText().removeSpan(strikethroughSpanList.get(i));
                }
                for (int i = 0; i < foregroundColorSpanList.size(); i++) {
                    contentEditText.getEditableText().removeSpan(foregroundColorSpanList.get(i));
                }
                for (int i = 0; i < backgroundColorSpanList.size(); i++) {
                    contentEditText.getEditableText().removeSpan(backgroundColorSpanList.get(i));
                }
                for (int i = 0; i < superscriptSpanList.size(); i++) {
                    contentEditText.getEditableText().removeSpan(superscriptSpanList.get(i));
                }
                for (int i = 0; i < subscriptSpanList.size(); i++) {
                    contentEditText.getEditableText().removeSpan(subscriptSpanList.get(i));
                }
                boldSpanList.clear();
                italicSpanList.clear();
                underlineSpanList.clear();
                strikethroughSpanList.clear();
                foregroundColorSpanList.clear();
                backgroundColorSpanList.clear();
                superscriptSpanList.clear();
                subscriptSpanList.clear();
            }
        });
        clearButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(AddActivity.this, "清除所有格式", Toast.LENGTH_SHORT).show();
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
                //在内部 分割成两个
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
            int start = contentEditText.getEditableText().getSpanStart(boldSpanList.get(i));
            int end =  contentEditText.getEditableText().getSpanEnd(boldSpanList.get(i));
            if (start < 0 || end < 0) { continue; } //以防数据中有 -1 出现
            bold.start = start;
            bold.end = end;
            format.boldList.add(bold);
        }
        for (int i = 0;i<italicSpanList.size();i++) {
            Italic italic = new Italic();
            int start = contentEditText.getEditableText().getSpanStart(italicSpanList.get(i));
            int end = contentEditText.getEditableText().getSpanEnd(italicSpanList.get(i));
            if (start < 0 || end < 0) { continue; } //以防数据中有 -1 出现
            italic.start = start;
            italic.end = end;
            format.italicList.add(italic);
        }
        for (int i = 0;i<underlineSpanList.size();i++) {
            Underline underline = new Underline();
            int start = contentEditText.getEditableText().getSpanStart(underlineSpanList.get(i));
            int end =  contentEditText.getEditableText().getSpanEnd(underlineSpanList.get(i));
            if (start < 0 || end < 0) { continue; }
            underline.start = start;
            underline.end = end;
            format.underlineList.add(underline);
        }
        for (int i = 0;i<strikethroughSpanList.size();i++) {
            Strikethrough strikethrough = new Strikethrough();
            int start = contentEditText.getEditableText().getSpanStart(strikethroughSpanList.get(i));
            int end =  contentEditText.getEditableText().getSpanEnd(strikethroughSpanList.get(i));
            if (start < 0 || end < 0) { continue; }
            strikethrough.start = start;
            strikethrough.end = end;
            format.strikethroughList.add(strikethrough);
        }
        for (int i = 0;i<foregroundColorSpanList.size();i++) {
            ForegroundColor foregroundColor = new ForegroundColor();
            int start = contentEditText.getEditableText().getSpanStart(foregroundColorSpanList.get(i));
            int end =  contentEditText.getEditableText().getSpanEnd(foregroundColorSpanList.get(i));
            if (start < 0 || end < 0) { continue; }
            foregroundColor.start = start;
            foregroundColor.end = end;
            format.foregroundColorList.add(foregroundColor);
        }
        for (int i = 0;i<backgroundColorSpanList.size();i++) {
            BackgroundColor backgroundColor = new BackgroundColor();
            int start = contentEditText.getEditableText().getSpanStart(backgroundColorSpanList.get(i));
            int end =  contentEditText.getEditableText().getSpanEnd(backgroundColorSpanList.get(i));
            if (start < 0 || end < 0) { continue; }
            backgroundColor.start = start;
            backgroundColor.end = end;
            format.backgroundColorList.add(backgroundColor);
        }
        for (int i = 0;i<superscriptSpanList.size();i++) {
            Superscript superscript = new Superscript();
            int start = contentEditText.getEditableText().getSpanStart(superscriptSpanList.get(i));
            int end =  contentEditText.getEditableText().getSpanEnd(superscriptSpanList.get(i));
            if (start < 0 || end < 0) { continue; }
            superscript.start = start;
            superscript.end = end;
            format.superscriptList.add(superscript);
        }
        for (int i = 0;i<subscriptSpanList.size();i++) {
            Subscript subscript = new Subscript();
            int start = contentEditText.getEditableText().getSpanStart(subscriptSpanList.get(i));
            int end =  contentEditText.getEditableText().getSpanEnd(subscriptSpanList.get(i));
            if (start < 0 || end < 0) { continue; }
            subscript.start = start;
            subscript.end = end;
            format.subscriptList.add(subscript);
        }
        Gson gson = new Gson();
        return gson.toJson(format);
    }

}


