package com.example.note.note;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.note.note.adapter.RecycledAdapter;
import com.example.note.note.bean.Note;

import org.litepal.crud.DataSupport;

import java.util.List;

public class RecycleBinActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    private RecyclerView recyclerView;

    private NavigationView navView;

    private List<Note> recycledList = DataSupport.order("time desc").where("recycled = ?","1").find(Note.class);
    private RecycledAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_bin);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_recycleBin);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.nav_view);

        //设置toolbar上home的图标
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecycledAdapter(recycledList);
        recyclerView.setAdapter(adapter);


        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_note:
                        Intent mintent = new Intent(RecycleBinActivity.this, MainActivity.class);
                        startActivity(mintent);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_recycle:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_setting:
                        Intent sintent = new Intent(RecycleBinActivity.this, SettingActivity.class);
                        startActivity(sintent);
                        mDrawerLayout.closeDrawers();
                        break;
                    default:
                }
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    //onResume时刷新数据
    @Override
    protected void onResume() {
        super.onResume();
        initRecycled();
        navView.setCheckedItem(R.id.nav_recycle);
        adapter = new RecycledAdapter(recycledList);
        recyclerView.setAdapter(adapter);
    }

    private void initRecycled() {
        recycledList.clear();
        recycledList = DataSupport.where("recycled = ?","1").order("time desc").find(Note.class);
    }

}
