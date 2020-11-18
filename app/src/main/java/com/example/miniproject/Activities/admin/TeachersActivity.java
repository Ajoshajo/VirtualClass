package com.example.miniproject.Activities.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.miniproject.Db;
import com.example.miniproject.R;
import com.example.miniproject.adapter.StudentAdapter;
import com.example.miniproject.adapter.TeacherAdapter;
import com.example.miniproject.models.Student;
import com.example.miniproject.models.Teacher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeachersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Teacher> teacherList;
    private TeacherAdapter teacherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_teachers);

        getSupportActionBar().setTitle("Teachers");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.recyclerview);
        teacherList = new ArrayList<>();
        loadData();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        teacherAdapter = new TeacherAdapter(this, teacherList);
        recyclerView.setAdapter(teacherAdapter);
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
                        sql = "SELECT teacher.id,teacher.name,teacher.email,dept.name as dept FROM teacher join dept where dept.id = teacher.dept";
                        PreparedStatement prest = con.prepareStatement(sql);
                        ResultSet rs = prest.executeQuery();
                        while (rs.next()) {
                            teacherList.add(new Teacher(rs.getInt("id"), rs.getString("name"),rs.getString("dept"), rs.getString("email")));
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