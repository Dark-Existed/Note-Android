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

import com.example.note.note.R;
import com.example.note.note.RecycleBinActivity;
import com.example.note.note.bean.Note;

import org.litepal.crud.DataSupport;

import java.util.List;

public class RecycledAdapter extends RecyclerView.Adapter<RecycledAdapter.ViewHolder> {

    private Context mContext;

    private List<Note> mRecycledList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title;
        TextView content;
        TextView time;
        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            title = (TextView) view.findViewById(R.id.noteTitle_TextView);
            content = (TextView) view.findViewById(R.id.noteContent_TextView);
            time = (TextView) view.findViewById(R.id.noteTime_TextView);
        }
    }

    public RecycledAdapter(List<Note> recycledList) {
        mRecycledList = recycledList;
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("是否将笔记彻底删除？");
                dialog.setMessage("将笔记彻底删除?\n或者移动到笔记?");
                dialog.setPositiveButton("彻底删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int position = holder.getAdapterPosition();
                        Intent intent = new Intent(mContext, RecycleBinActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        Note recycled = mRecycledList.get(position);
                        DataSupport.delete(Note.class, recycled.getId());
                        mContext.startActivity(intent);
                    }
                });
                dialog.setNeutralButton("移动到笔记",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int position = holder.getAdapterPosition();
                        Intent intent = new Intent(mContext, RecycleBinActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        Note recycled = mRecycledList.get(position);
                        recycled.setRecycled(0);
                        recycled.save();
//                        Note note = new Note();
//                        note.setTitle(recycled.getTitle());
//                        note.setTime(recycled.getTime());
//                        note.setContent(recycled.getContent());
//                        note.save();
//                        DataSupport.delete(Note.class, recycled.getId());
                        mContext.startActivity(intent);
                    }
                });
                dialog.show();
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Note recycled = mRecycledList.get(position);
        holder.title.setText(recycled.getTitle());
        holder.content.setText(recycled.getContent());
        holder.time.setText(recycled.getTime());
    }

    @Override
    public int getItemCount() {
        return mRecycledList.size();
    }
}
