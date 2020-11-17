package com.example.miniproject.Activities.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.miniproject.Activities.admin.AdminActivity;
import com.example.miniproject.Activities.admin.AdminLoginActivity;
import com.example.miniproject.Db;
import com.example.miniproject.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentLoginActivity extends AppCompatActivity {

    //components and variables
    private Button login, register;
    private ProgressBar progressBar;
    private EditText username, password;
    private String msg = "";
    private TextView error;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    //main method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        //component binding
        login = findViewById(R.id.btn);
        register = findViewById(R.id.reg);
        progressBar = findViewById(R.id.loader);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        error = findViewById(R.id.error);

        //App bar
        getSupportActionBar().setTitle("Student Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //save login
        sharedPreferences = getApplicationContext().getSharedPreferences("student", 0);
        editor = sharedPreferences.edit();

        //button click
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                error.setVisibility(View.GONE);
                login(username.getText().toString(), password.getText().toString());
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StudentLoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void login(final String username, final String password) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Connection con = null;
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    try {
                        con = Db.getCon();
                        String sql;
                        sql = "SELECT * FROM student where email = '" + username + "'";
                        Log.d("admin", sql);
                        PreparedStatement prest = con.prepareStatement(sql);
                        ResultSet rs = prest.executeQuery();
                        if (rs.next() && rs.getString("password").equals(password)) {
                            editor.putInt("id", rs.getInt("id"));
                            editor.putString("name", rs.getString("name"));
                            editor.commit();
                            startActivity(new Intent(StudentLoginActivity.this, StudentActivity.class));
                        } else {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                    login.setVisibility(View.VISIBLE);
                                    error.setText("Invalid Login Details!\nTry Again");
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}