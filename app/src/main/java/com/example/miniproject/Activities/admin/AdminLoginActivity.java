package com.example.miniproject.Activities.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminLoginActivity extends AppCompatActivity {

    private Button login;
    private ProgressBar progressBar;
    private EditText username, password;
    private String msg = "";
    private TextView error;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        login = findViewById(R.id.btn);
        progressBar = findViewById(R.id.loader);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        error = findViewById(R.id.error);

        getSupportActionBar().setTitle("Admin Login");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                error.setVisibility(View.GONE);
                login(username.getText().toString(), password.getText().toString());
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
                        sql = "SELECT * FROM admin where email = '" + username + "'";
                        Log.d("admin", sql);
                        PreparedStatement prest = con.prepareStatement(sql);
                        ResultSet rs = prest.executeQuery();
                        if (rs.next() && rs.getString("password").equals(password)) {
                            startActivity(new Intent(AdminLoginActivity.this, AdminActivity.class));
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
}