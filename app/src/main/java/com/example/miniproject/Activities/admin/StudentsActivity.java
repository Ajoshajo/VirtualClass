package com.example.miniproject.Activities.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.miniproject.R;

public class StudentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_students);

        getSupportActionBar().setTitle("Students");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}