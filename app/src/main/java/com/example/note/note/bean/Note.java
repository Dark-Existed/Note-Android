package com.example.note.note.bean;


import android.text.SpannableStringBuilder;

import org.litepal.crud.DataSupport;

public class Note extends DataSupport{

    private int id;

    private String title;

    private String content;

    private String time;

    private int recycled;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getRecycled() {
        return recycled;
    }

    public void setRecycled(int recycled) {
        this.recycled = recycled;
    }

    public Note() {
    }

    public Note(String title, String content, String time,int recycled) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.recycled = recycled;
    }

}
