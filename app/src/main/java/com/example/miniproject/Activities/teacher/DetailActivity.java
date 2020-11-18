package com.example.miniproject.Activities.teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.miniproject.Db;
import com.example.miniproject.R;
import com.example.miniproject.adapter.ChatAdapter;
import com.example.miniproject.adapter.teacher.SubjectAdapter;
import com.example.miniproject.models.Chat;
import com.example.miniproject.models.Course;
import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private int id, teacher;
    private String name;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private RecyclerView recyclerView;
    private List<Chat> chatList;
    private ChatAdapter chatAdapter;
    private Button send, files;
    private EditText msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_detail);
        Intent intent = getIntent();
        id = intent.getIntExtra("sub", 0);
        name = intent.getStringExtra("name");
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        recyclerView = findViewById(R.id.recyclerview);
        send = findViewById(R.id.send);
        msg = findViewById(R.id.msg);
        files = findViewById(R.id.files);
        sharedPreferences = getApplicationContext().getSharedPreferences("teacher", 0);
        teacher = sharedPreferences.getInt("id", 0);

        recyclerView = findViewById(R.id.recyclerview);
        chatList = new ArrayList<>();
        loadData();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send(msg.getText().toString(), 1, 1);
                msg.setText("");
            }
        });

        files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ChooserDialog(DetailActivity.this)
                        //.withStartFile(path)
                        .withChosenListener(new ChooserDialog.Result() {
                            @Override
                            public void onChoosePath(String path, File pathFile) {
                                Toast.makeText(DetailActivity.this, "FILE: " + path, Toast.LENGTH_SHORT).show();
                                send(path, 1, 0);
                            }
                        })
                        // to handle the back key pressed or clicked outside the dialog:
                        .withOnCancelListener(new DialogInterface.OnCancelListener() {
                            public void onCancel(DialogInterface dialog) {
                                Log.d("CANCEL", "CANCEL");
                                dialog.cancel(); // MUST have
                            }
                        })
                        .build()
                        .show();
            }
        });
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
                        sql = "SELECT chat.id,chat.active,chat.msg,chat.type,teacher.id as teacherid,teacher.name as teacher,student.id as studentid,student.name as student FROM `chat` join teachersub join teacher join student WHERE (teachersub.id=chat.teachersub AND teacher.id=teachersub.teacher) and student.id=chat.student and teachersub.sub=" + id;
                        PreparedStatement prest = con.prepareStatement(sql);
                        ResultSet rs = prest.executeQuery();
                        Log.d("q", sql);
                        chatList.clear();
                        while (rs.next()) {
                            Chat c = new Chat(1, rs.getString("msg"), rs.getInt("teacherid"), rs.getString("teacher"), rs.getInt("studentid"), rs.getString("student"), rs.getInt("type"), rs.getInt("active") == 1);
                            if (rs.getString("student") == null) {
                                c.setIsteacher(true);
                            }
                            chatList.add(c);
                        }
                        chatAdapter = new ChatAdapter(DetailActivity.this, chatList);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setAdapter(chatAdapter);
                            }
                        });

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

    private void send(final String msg, final int type, int active) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Connection con = null;
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    try {
                        con = Db.getCon();
                        String sql;
                        sql = "insert into chat values (null,0,(select id from teachersub where teacher=" + teacher + " and sub=" + id + ")," + type + ",'" + msg + "'," + active + ")";
                        Log.d("v", sql);
                        PreparedStatement prest = con.prepareStatement(sql);
                        int rs = prest.executeUpdate();

                        if (rs == 1) {
                            loadData();
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
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