package com.example.miniproject.Activities.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.miniproject.Db;
import com.example.miniproject.R;
import com.example.miniproject.adapter.CourseAdapter;
import com.example.miniproject.adapter.StudentAdapter;
import com.example.miniproject.models.Course;
import com.example.miniproject.models.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Student> studentList;
    private StudentAdapter studentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_students);

        getSupportActionBar().setTitle("Students");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.recyclerview);
        studentList = new ArrayList<>();
        loadData();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentAdapter = new StudentAdapter(this, studentList);
        recyclerView.setAdapter(studentAdapter);
        Log.d("l", studentAdapter.getItemCount() + "");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadData() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Connection con = null;
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    try {
                        con = Db.getCon();
                        String sql;
                        sql = "SELECT * FROM student where id > 0";
                        PreparedStatement prest = con.prepareStatement(sql);
                        ResultSet rs = prest.executeQuery();
                        while (rs.next()) {
                            studentList.add(new Student(rs.getInt("id"), rs.getString("name"), rs.getString("email")));
                        }
                        prest.close();
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}