package com.example.note.note.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.note.note.DetailActivity;
import com.example.note.note.MainActivity;
import com.example.note.note.R;
import com.example.note.note.bean.Note;

import org.litepal.crud.DataSupport;

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
        final View view = LayoutInflater.from(mContext).inflate(R.layout.note_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        //单击
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Note note = mNoteList.get(position);
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(DetailActivity.NOTE_TITLE, note.getTitle());
                intent.putExtra(DetailActivity.NOTE_TIME, note.getTime());
                intent.putExtra(DetailActivity.NOTE_CONTENT, note.getContent());
                mContext.startActivity(intent);
            }
        });
        //长按
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("移动到回收站");
                dialog.setMessage("确定将这条笔记移动到回收站?");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int position = holder.getAdapterPosition();
                        Intent intent = new Intent(mContext, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        Note note = mNoteList.get(position);
                        note.setRecycled(1);
                        note.save();
//                        Recycled recycled = new Recycled();
//                        recycled.setTitle(note.getTitle());
//                        recycled.setTime(note.getTime());
//                        recycled.setContent(note.getContent());
//                        recycled.save();
//                        DataSupport.delete(Note.class, note.getId());
                        mContext.startActivity(intent);
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
                return true;
            }
        });
        return holder;
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
