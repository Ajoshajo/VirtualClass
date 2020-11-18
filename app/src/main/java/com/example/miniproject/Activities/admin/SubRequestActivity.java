package com.example.miniproject.Activities.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.miniproject.Db;
import com.example.miniproject.R;
import com.example.miniproject.adapter.admin.SubjectAdapter;
import com.example.miniproject.models.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubRequestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SubjectAdapter subjectAdapter;
    private List<Subject> subjectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sub_request);

        getSupportActionBar().setTitle("Subjects");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView = findViewById(R.id.recyclerview);
        subjectList = new ArrayList<>();
        loadData();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        subjectAdapter = new SubjectAdapter(this, subjectList);
        recyclerView.setAdapter(subjectAdapter);
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
                        sql = "SELECT teachersub.sub as id,sub.name,teacher.id as teacher,teacher.name as teachername FROM teachersub join teacher JOIN sub where teachersub.active=0 and teacher.id=teachersub.teacher and sub.id=teachersub.sub";
                        PreparedStatement prest = con.prepareStatement(sql);
                        ResultSet rs = prest.executeQuery();
                        while (rs.next()) {
                            Subject s = new Subject(rs.getInt("id"), rs.getString("name"));
                            s.setTeacher(rs.getString("teachername"));
                            s.setTeacherId(rs.getInt("teacher"));
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