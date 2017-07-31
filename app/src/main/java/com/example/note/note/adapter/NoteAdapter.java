package com.example.note.note.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.note.note.R;
import com.example.note.note.bean.Note;

import java.util.List;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{

    private Context mContext;

    private List<Note> mNoteList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView title;
        TextView content;
        TextView time;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            title = (TextView) view.findViewById(R.id.noteTitle_TextView);
            content = (TextView) view.findViewById(R.id.noteContent_TextView);
            time = (TextView) view.findViewById(R.id.noteTime_TextView);
        }
    }

    public NoteAdapter(List<Note> noteList) {
        mNoteList = noteList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.note_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Note note = mNoteList.get(position);
        holder.title.setText(note.getTitle());
        holder.content.setText(note.getContent());
        holder.time.setText(note.getTime());
    }

    @Override
    public int getItemCount() {
        return mNoteList.size();
    }

}
