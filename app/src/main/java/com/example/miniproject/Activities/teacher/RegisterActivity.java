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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniproject.Activities.student.StudentLoginActivity;
import com.example.miniproject.Db;
import com.example.miniproject.R;
import com.example.miniproject.models.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private Button login, register;
    private AppCompatSpinner dept;
    private ProgressBar progressBar;
    private EditText name, username, password, cpassword;
    private String msg = "";
    private TextView error;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ArrayList<Course> deptList;
    private ArrayAdapter<Course> adapter;
    private int deptId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_register);

        getSupportActionBar().setTitle("Teacher Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        login = findViewById(R.id.login);
        register = findViewById(R.id.reg);
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        cpassword = findViewById(R.id.cpassword);
        error = findViewById(R.id.error);
        dept = findViewById(R.id.dept);
        sharedPreferences = getApplicationContext().getSharedPreferences("teacher", 0);
        editor = sharedPreferences.edit();
        deptList = new ArrayList<>();
        deptList.add(new Course(0, "Select Dept"));
        getData();
        adapter = new ArrayAdapter<Course>(RegisterActivity.this, R.layout.support_simple_spinner_dropdown_item, deptList);
        dept.setAdapter(adapter);

        dept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3) {
                if (position > 0) {
                    deptId = adapter.getItem(position).getId();
                } else {
                    deptId = 0;
                }


            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty() || username.getText().toString().isEmpty() || password.getText().toString().isEmpty() || cpassword.getText().toString().isEmpty() || deptId == 0) {
                    msg = "Please Enter Details";
                    error.setText(msg);
                    error.setVisibility(View.VISIBLE);
                } else if (!password.getText().toString().equals(cpassword.getText().toString())) {
                    msg = "Password Donot Match !";
                    error.setText(msg);
                    error.setVisibility(View.VISIBLE);
                } else {
                    error.setVisibility(View.GONE);
                    login(name.getText().toString(), username.getText().toString(), password.getText().toString());
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void login(final String name, final String email, final String password) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Connection con = null;
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    try {
                        con = Db.getCon();
                        String sql;
                        sql = "insert into teacher values (id,'" + name + "','" + email + "',null,'" + password + "'," + deptId + ")";
                        Log.d("admin", sql);
                        PreparedStatement prest = con.prepareStatement(sql);
                        int rs = prest.executeUpdate();
                        if (rs == 1) {
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        } else {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                    login.setVisibility(View.VISIBLE);
                                    error.setText("Registration Failed !\nTry Again");
                                    error.setVisibility(View.VISIBLE);
                                }
                            });
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
                        sql = "select * from dept";
                        PreparedStatement prest = con.prepareStatement(sql);
                        ResultSet rs = prest.executeQuery();
                        while (rs.next()) {
                            deptList.add(new Course(rs.getInt("id"), rs.getString("name")));
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}