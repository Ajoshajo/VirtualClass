package com.example.miniproject.Activities.teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.miniproject.Db;
import com.example.miniproject.R;
import com.example.miniproject.models.Course;
import com.example.miniproject.models.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RequestActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private AppCompatSpinner subject;
    private ArrayList<Subject> subtList;
    private ArrayAdapter<Subject> adapter;
    private int subId, teacher;
    private Button request;
    private ProgressBar progressBar;
    private String msg = "";
    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_request);
        getSupportActionBar().setTitle("Request Subject");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        subject = findViewById(R.id.sub);
        request = findViewById(R.id.reg);
        error = findViewById(R.id.error);
        progressBar = findViewById(R.id.loader);

        sharedPreferences = getApplicationContext().getSharedPreferences("teacher", 0);

        teacher = sharedPreferences.getInt("id", 0);

        subtList = new ArrayList<>();
        subtList.add(new Subject(0, "Select Subject"));
        getData();
        adapter = new ArrayAdapter<Subject>(RequestActivity.this, R.layout.support_simple_spinner_dropdown_item, subtList);
        subject.setAdapter(adapter);

        subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3) {
                if (position > 0) {
                    subId = adapter.getItem(position).getId();
                } else {
                    subId = 0;
                }


            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subId == 0) {
                    msg = "Please Select Subject";
                    error.setText(msg);
                    error.setVisibility(View.VISIBLE);
                } else {
                    error.setVisibility(View.GONE);
                    reg();
                }
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getData() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Connection con = null;
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    try {
                        con = Db.getCon();
                        String sql;
                        sql = "SELECT * FROM `sub` where id not in (SELECT sub from teachersub WHERE teacher = " + teacher + ")";
                        PreparedStatement prest = con.prepareStatement(sql);
                        ResultSet rs = prest.executeQuery();
                        while (rs.next()) {
                            subtList.add(new Subject(rs.getInt("id"), rs.getString("name")));
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

    private void reg() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Connection con = null;
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    try {
                        con = Db.getCon();
                        String sql;
                        sql = "insert into teachersub values (id,'" + teacher + "','" + subId + "',0)";
                        Log.d("admin", sql);
                        PreparedStatement prest = con.prepareStatement(sql);
                        int rs = prest.executeUpdate();
                        if (rs == 1) {
                            startActivity(new Intent(RequestActivity.this, SubjectActivity.class));
                        } else {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                    request.setVisibility(View.VISIBLE);
                                    error.setText("Request Failed !\nTry Again");
                                    error.setVisibility(View.VISIBLE);
                                }
                            });
                        }


                        prest.close();
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                request.setVisibility(View.VISIBLE);
                                error.setText("Request Failed !\nTry Again");
                                error.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}