package com.example.miniproject.Activities.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.miniproject.R;
import com.example.miniproject.adapter.SemAdapter;

import java.util.ArrayList;
import java.util.List;

public class SemActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SemAdapter semAdapter;
    private List<Integer> semList;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sem);
        Intent intent = getIntent();
        id = intent.getIntExtra("sub", 0);
        getSupportActionBar().setTitle(intent.getStringExtra("name"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView = findViewById(R.id.recyclerview);
        semList = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            semList.add(i);
        }
        semAdapter = new SemAdapter(this, semList, id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(semAdapter);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}