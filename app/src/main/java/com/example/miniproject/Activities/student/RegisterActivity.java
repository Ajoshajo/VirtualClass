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

import com.example.miniproject.Db;
import com.example.miniproject.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterActivity extends AppCompatActivity {

    private Button login, register;
    private ProgressBar progressBar;
    private EditText name, username, password, cpassword;
    private String msg = "";
    private TextView error;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);
        getSupportActionBar().setTitle("Student Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        login = findViewById(R.id.login);
        register = findViewById(R.id.reg);
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        cpassword = findViewById(R.id.cpassword);
        error = findViewById(R.id.error);

        sharedPreferences = getApplicationContext().getSharedPreferences("student", 0);
        editor = sharedPreferences.edit();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty() || username.getText().toString().isEmpty() || password.getText().toString().isEmpty() || cpassword.getText().toString().isEmpty()) {
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
                        sql = "insert into student values (id,'" + name + "','" + email + "',null,'" + password + "')";
                        Log.d("admin", sql);
                        PreparedStatement prest = con.prepareStatement(sql);
                        int rs = prest.executeUpdate();
                        if (rs == 1) {
                            startActivity(new Intent(RegisterActivity.this, StudentLoginActivity.class));
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}