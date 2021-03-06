package com.example.miniproject.Activities.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.miniproject.MainActivity;
import com.example.miniproject.R;

public class AdminActivity extends AppCompatActivity {

    private CardView students, teachers, subreq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getSupportActionBar().setTitle("Admin Home");

        students = findViewById(R.id.students);
        teachers = findViewById(R.id.teachers);
        subreq = findViewById(R.id.subreq);

        students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, StudentsActivity.class));
            }
        });

        teachers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, TeachersActivity.class));
            }
        });

        subreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, SubRequestActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        return;
    }
}