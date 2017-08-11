package com.example.note.note;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
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

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.transform.Templates;

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
        setupStrikethrough();
        setupForegroundColor();
        setupBackgroundColor();
        setupSuperscript();
        setupSubscript();
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
    //加粗
    private void setupBold() {
        boldButton = (ImageButton) findViewById(R.id.bold);
        boldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = contentEditView.getSelectionStart();
                int end = contentEditView.getSelectionEnd();
                StyleSpan span = new StyleSpan(Typeface.BOLD);
                setFormat(start,end,boldSpanList,span);
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
    //斜体
    private void setupItalic() {
        italicButton = (ImageButton) findViewById(R.id.italic);
        italicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = contentEditView.getSelectionStart();
                int end = contentEditView.getSelectionEnd();
                StyleSpan span = new StyleSpan(Typeface.ITALIC);
                setFormat(start, end, italicSpanList, span);
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
    //下划线
    private void setupUnderline() {
        underlineButton = (ImageButton) findViewById(R.id.underline);
        underlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = contentEditView.getSelectionStart();
                int end = contentEditView.getSelectionEnd();
                UnderlineSpan underlineSpan = new UnderlineSpan();
                setFormat(start, end, underlineSpanList, underlineSpan);
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
    //删除线
    private void setupStrikethrough() {
        strikethroughButton = (ImageButton) findViewById(R.id.strikethrough);
        strikethroughButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start = contentEditView.getSelectionStart();
                int end = contentEditView.getSelectionEnd();
                StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
                setFormat(start,end,strikethroughSpanList,strikethroughSpan);
                isModify = true;
                strikethroughSpanList.add(strikethroughSpan);
            }
        });
        strikethroughButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(DetailActivity.this,"删除线",Toast.LENGTH_SHORT).show();
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
                int start = contentEditView.getSelectionStart();
                int end = contentEditView.getSelectionEnd();
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#0099EE"));
                setFormat(start, end, foregroundColorSpanList, foregroundColorSpan);
                isModify = true;
                foregroundColorSpanList.add(foregroundColorSpan);
            }
        });
        foregroundcolorButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(DetailActivity.this,"字体颜色",Toast.LENGTH_SHORT).show();
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
                int start = contentEditView.getSelectionStart();
                int end = contentEditView.getSelectionEnd();
                BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.parseColor("#AC00FF30"));
                setFormat(start, end, backgroundColorSpanList, backgroundColorSpan);
                isModify = true;
                backgroundColorSpanList.add(backgroundColorSpan);
            }
        });
        backgroundcolorButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(DetailActivity.this,"背景颜色",Toast.LENGTH_SHORT).show();
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
                int start = contentEditView.getSelectionStart();
                int end = contentEditView.getSelectionEnd();
                SuperscriptSpan superscriptSpan = new SuperscriptSpan();
                setFormat(start, end, superscriptSpanList, superscriptSpan);
                isModify = true;
                superscriptSpanList.add(superscriptSpan);
            }
        });
        superscriptButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(DetailActivity.this,"上标",Toast.LENGTH_SHORT).show();
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
                int start = contentEditView.getSelectionStart();
                int end = contentEditView.getSelectionEnd();
                SubscriptSpan subscriptSpan = new SubscriptSpan();
                setFormat(start, end, subscriptSpanList, subscriptSpan);
                isModify = true;
                subscriptSpanList.add(subscriptSpan);
            }
        });
        subscriptButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(DetailActivity.this,"下标",Toast.LENGTH_SHORT).show();
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
                    contentEditView.getEditableText().removeSpan(boldSpanList.get(i));
                }
                for (int i = 0; i < italicSpanList.size(); i++) {
                    contentEditView.getEditableText().removeSpan(italicSpanList.get(i));
                }
                for (int i = 0; i < underlineSpanList.size(); i++) {
                    contentEditView.getEditableText().removeSpan(underlineSpanList.get(i));
                }
                for (int i = 0; i < strikethroughSpanList.size(); i++) {
                    contentEditView.getEditableText().removeSpan(strikethroughSpanList.get(i));
                }
                for (int i = 0; i < foregroundColorSpanList.size(); i++) {
                    contentEditView.getEditableText().removeSpan(foregroundColorSpanList.get(i));
                }
                for (int i = 0; i < backgroundColorSpanList.size(); i++) {
                    contentEditView.getEditableText().removeSpan(backgroundColorSpanList.get(i));
                }
                for (int i = 0; i < superscriptSpanList.size(); i++) {
                    contentEditView.getEditableText().removeSpan(superscriptSpanList.get(i));
                }
                for (int i = 0; i < subscriptSpanList.size(); i++) {
                    contentEditView.getEditableText().removeSpan(subscriptSpanList.get(i));
                }
                boldSpanList.clear();
                italicSpanList.clear();
                underlineSpanList.clear();
                strikethroughSpanList.clear();
                foregroundColorSpanList.clear();
                backgroundColorSpanList.clear();
                superscriptSpanList.clear();
                subscriptSpanList.clear();
                isModify = true;
            }
        });
        clearButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(DetailActivity.this, "清除所有格式", Toast.LENGTH_SHORT).show();
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
            int mStart = contentEditView.getEditableText().getSpanStart(list.get(i));
            int mEnd = contentEditView.getEditableText().getSpanEnd(list.get(i));
            if (start == mStart && end == mEnd) {
                //相同位置的取消格式
                contentEditView.getEditableText().removeSpan(list.get(i));
                list.remove(i);
//                Toast.makeText(DetailActivity.this,"执行1",Toast.LENGTH_SHORT).show();
                return;
            } else if (start < mStart && end > mEnd) {
                //覆盖过去的
                contentEditView.getEditableText().setSpan(list.get(i), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                Toast.makeText(DetailActivity.this,"执行2",Toast.LENGTH_SHORT).show();
                return;
            } else if ((mStart < start && start < mEnd && mEnd < end) || (start < mStart && mStart < end && end < mEnd)) {
                //部分重叠的
                int minStart = start < mStart ? start : mStart;
                int maxEnd = end > mEnd ? end : mEnd;
                contentEditView.getEditableText().setSpan(list.get(i), minStart, maxEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                Toast.makeText(DetailActivity.this, "执行3", Toast.LENGTH_SHORT).show();
                return;
            } else if ((mStart < start && end < mEnd) || (mStart < start && end < mEnd)) {
                //在内部 分割成两个
                contentEditView.getEditableText().setSpan(list.get(i), mStart, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                contentEditView.getEditableText().setSpan(span, end, mEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                list.add(span);
//                Toast.makeText(DetailActivity.this, "执行4", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        contentEditView.getEditableText().setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        Toast.makeText(DetailActivity.this,"执行5",Toast.LENGTH_SHORT).show();
    }

    //将格式转换成json
    private String transform() {
        Format format = new Format();
        for (int i = 0;i<boldSpanList.size();i++) {
            Bold bold = new Bold();
            int start = contentEditView.getEditableText().getSpanStart(boldSpanList.get(i));
            int end = contentEditView.getEditableText().getSpanEnd(boldSpanList.get(i));
            if (start < 0 || end < 0) { continue; }  //以防数据中有 -1 出现
            bold.start = start;
            bold.end = end;
            format.boldList.add(bold);
        }
        for (int i = 0;i<italicSpanList.size();i++) {
            Italic italic = new Italic();
            int start = contentEditView.getEditableText().getSpanStart(italicSpanList.get(i));
            int end = contentEditView.getEditableText().getSpanEnd(italicSpanList.get(i));
            if (start < 0 || end < 0) { continue; }
            italic.start = start;
            italic.end = end;
            format.italicList.add(italic);
        }
        for (int i = 0;i<underlineSpanList.size();i++) {
            Underline underline = new Underline();
            int start = contentEditView.getEditableText().getSpanStart(underlineSpanList.get(i));
            int end =  contentEditView.getEditableText().getSpanEnd(underlineSpanList.get(i));
            if (start < 0 || end < 0) { continue; }
            underline.start = start;
            underline.end = end;
            format.underlineList.add(underline);
        }
        for (int i = 0;i<strikethroughSpanList.size();i++) {
            Strikethrough strikethrough = new Strikethrough();
            int start = contentEditView.getEditableText().getSpanStart(strikethroughSpanList.get(i));
            int end =  contentEditView.getEditableText().getSpanEnd(strikethroughSpanList.get(i));
            if (start < 0 || end < 0) { continue; }
            strikethrough.start = start;
            strikethrough.end = end;
            format.strikethroughList.add(strikethrough);
        }
        for (int i = 0;i<foregroundColorSpanList.size();i++) {
            ForegroundColor foregroundColor = new ForegroundColor();
            int start = contentEditView.getEditableText().getSpanStart(foregroundColorSpanList.get(i));
            int end =  contentEditView.getEditableText().getSpanEnd(foregroundColorSpanList.get(i));
            if (start < 0 || end < 0) { continue; }
            foregroundColor.start = start;
            foregroundColor.end = end;
            format.foregroundColorList.add(foregroundColor);
        }
        for (int i = 0;i<backgroundColorSpanList.size();i++) {
            BackgroundColor backgroundColor = new BackgroundColor();
            int start = contentEditView.getEditableText().getSpanStart(backgroundColorSpanList.get(i));
            int end =  contentEditView.getEditableText().getSpanEnd(backgroundColorSpanList.get(i));
            if (start < 0 || end < 0) { continue; }
            backgroundColor.start = start;
            backgroundColor.end = end;
            format.backgroundColorList.add(backgroundColor);
        }
        for (int i = 0;i<superscriptSpanList.size();i++) {
            Superscript superscript = new Superscript();
            int start = contentEditView.getEditableText().getSpanStart(superscriptSpanList.get(i));
            int end =  contentEditView.getEditableText().getSpanEnd(superscriptSpanList.get(i));
            if (start < 0 || end < 0) { continue; }
            superscript.start = start;
            superscript.end = end;
            format.superscriptList.add(superscript);
        }
        for (int i = 0;i<subscriptSpanList.size();i++) {
            Subscript subscript = new Subscript();
            int start = contentEditView.getEditableText().getSpanStart(subscriptSpanList.get(i));
            int end =  contentEditView.getEditableText().getSpanEnd(subscriptSpanList.get(i));
            if (start < 0 || end < 0) { continue; }
            subscript.start = start;
            subscript.end = end;
            format.subscriptList.add(subscript);
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
            if (start < 0 || end < 0) { continue; } //以防数据中有 -1 出现
            StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
            contentEditView.getEditableText().setSpan(boldSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            boldSpanList.add(boldSpan);
        }
        for (int i = 0;i<format.italicList.size();i++) {
            int start = format.italicList.get(i).start;
            int end = format.italicList.get(i).end;
            if (start < 0 || end < 0) { continue; }
            StyleSpan italicSpan = new StyleSpan(Typeface.ITALIC);
            contentEditView.getEditableText().setSpan(italicSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            italicSpanList.add(italicSpan);
        }
        for (int i=0;i<format.underlineList.size();i++) {
            int start = format.underlineList.get(i).start;
            int end = format.underlineList.get(i).end;
            if (start < 0 || end < 0) { continue; }
            UnderlineSpan underlineSpan = new UnderlineSpan();
            contentEditView.getEditableText().setSpan(underlineSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            underlineSpanList.add(underlineSpan);
        }
        for (int i = 0;i<format.strikethroughList.size();i++) {
            int start = format.strikethroughList.get(i).start;
            int end = format.strikethroughList.get(i).end;
            if (start < 0 || end < 0) { continue; }
            StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
            contentEditView.getEditableText().setSpan(strikethroughSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            strikethroughSpanList.add(strikethroughSpan);
        }
        for (int i = 0;i<format.foregroundColorList.size();i++) {
            int start = format.foregroundColorList.get(i).start;
            int end = format.foregroundColorList.get(i).end;
            if (start < 0 || end < 0) { continue; }
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#0099EE"));
            contentEditView.getEditableText().setSpan(foregroundColorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            foregroundColorSpanList.add(foregroundColorSpan);
        }
        for (int i = 0;i<format.backgroundColorList.size();i++) {
            int start = format.backgroundColorList.get(i).start;
            int end = format.backgroundColorList.get(i).end;
            if (start < 0 || end < 0) { continue; }
            BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.parseColor("#AC00FF30"));
            contentEditView.getEditableText().setSpan(backgroundColorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            backgroundColorSpanList.add(backgroundColorSpan);
        }
        for (int i = 0;i<format.superscriptList.size();i++) {
            int start = format.superscriptList.get(i).start;
            int end = format.superscriptList.get(i).end;
            if (start < 0 || end < 0) { continue; }
            SuperscriptSpan superscriptSpan = new SuperscriptSpan();
            contentEditView.getEditableText().setSpan(superscriptSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            superscriptSpanList.add(superscriptSpan);
        }
        for (int i = 0;i<format.subscriptList.size();i++) {
            int start = format.subscriptList.get(i).start;
            int end = format.subscriptList.get(i).end;
            if (start < 0 || end < 0) { continue; }
            SubscriptSpan subscriptSpan = new SubscriptSpan();
            contentEditView.getEditableText().setSpan(subscriptSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            subscriptSpanList.add(subscriptSpan);
        }
    }


}
