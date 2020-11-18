package com.example.miniproject.Activities.teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.miniproject.Db;
import com.example.miniproject.R;
import com.example.miniproject.adapter.teacher.SubjectAdapter;
import com.example.miniproject.models.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubjectActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SubjectAdapter subjectAdapter;
    private List<Subject> subjectList;
    private SharedPreferences sharedPreferences;
    private int teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_subject);
        getSupportActionBar().setTitle("Subjects");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sharedPreferences = getApplicationContext().getSharedPreferences("teacher", 0);

        teacher = sharedPreferences.getInt("id", 0);

        recyclerView = findViewById(R.id.recyclerview);
        subjectList = new ArrayList<>();
        loadData();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        subjectAdapter = new SubjectAdapter(this, subjectList);
        recyclerView.setAdapter(subjectAdapter);


    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, TeacherMainActivity.class));
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
                        sql = "SELECT sub.id,sub.name,teachersub.active FROM teachersub join sub WHERE teachersub.teacher =" + teacher + " and sub.id=teachersub.sub";
                        PreparedStatement prest = con.prepareStatement(sql);
                        ResultSet rs = prest.executeQuery();
                        while (rs.next()) {
                            Subject s = new Subject(rs.getInt("id"), rs.getString("name"));
                            if (rs.getInt("active") == 1) {
                                s.setActive(true);
                            }
                            subjectList.add(s);
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
