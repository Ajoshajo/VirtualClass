package com.example.miniproject.Activities.teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.miniproject.Activities.student.CourseActivity;
import com.example.miniproject.Activities.student.StudentActivity;
import com.example.miniproject.MainActivity;
import com.example.miniproject.R;

public class TeacherMainActivity extends AppCompatActivity {

    private CardView subjects, logout, reqsub;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);

        getSupportActionBar().setTitle("Teacher Home");
        subjects = findViewById(R.id.course);
        logout = findViewById(R.id.logout);
        reqsub = findViewById(R.id.regsub);

        sharedPreferences = getApplicationContext().getSharedPreferences("student", 0);
        editor = sharedPreferences.edit();

        subjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TeacherMainActivity.this, SubjectActivity.class);
                startActivity(i);
            }
        });
        reqsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherMainActivity.this, RequestActivity.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.commit();
                Intent i = new Intent(TeacherMainActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        return;
    }
}