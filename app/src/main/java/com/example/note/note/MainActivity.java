package com.example.note.note;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.note.note.adapter.NoteAdapter;
import com.example.note.note.bean.Note;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    private RecyclerView recyclerView;

    private NavigationView navView;

    private List<Note> noteList = DataSupport.order("time desc").find(Note.class);

    private NoteAdapter adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.nav_view);

        //设置toolbar上home的图标
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        //侧滑菜单的点击事件
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_note:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_recycle:
                        Intent rintent = new Intent(MainActivity.this, RecycleBinActivity.class);
                        startActivity(rintent);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_setting:
                        Intent sintent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(sintent);
                        mDrawerLayout.closeDrawers();
                    default:
                }
                return true;
            }
        });

        //添加笔记
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NoteAdapter(noteList);
        recyclerView.setAdapter(adapter);

    }

    //resume时刷新数据
    @Override
    protected void onResume() {
        super.onResume();
        navView.setCheckedItem(R.id.nav_note);
        initNote();
        adapter = new NoteAdapter(noteList);
        recyclerView.setAdapter(adapter);

    }

    //toolbar的菜单
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.search:
                Toast.makeText(MainActivity.this,"click search",Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
//                startActivity(intent);
                break;
            default:
        }
        return true;
    }

    //初始化数据
    private void initNote() {
        noteList.clear();
        noteList = DataSupport.order("time desc").find(Note.class);
    }

}
