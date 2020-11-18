package com.example.miniproject.adapter.admin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject.Activities.student.DetailActivity;
import com.example.miniproject.Activities.student.SemActivity;
import com.example.miniproject.Activities.teacher.RequestActivity;
import com.example.miniproject.Activities.teacher.SubjectActivity;
import com.example.miniproject.Db;
import com.example.miniproject.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.example.miniproject.models.Course;
import com.example.miniproject.models.Subject;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.viewholder> {
    private Context ctx;
    private List<Subject> sub;


    public SubjectAdapter(Context ctx, List<Subject> sub) {
        this.ctx = ctx;
        this.sub = sub;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_sub, viewGroup, false);
        return new viewholder(view, ctx, sub);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder productviewholder, final int i) {
        String s = sub.get(i).getName();
        productviewholder.tv_sub.setText(s);
        productviewholder.teacher.setText(sub.get(i).getTeacher());
        productviewholder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Connection con = null;
                        try {
                            Class.forName("com.mysql.jdbc.Driver");
                            try {
                                con = Db.getCon();
                                String sql;
                                sql = "update teachersub set active=1 where teacher=" + sub.get(i).getTeacherId() + " and sub=" + sub.get(i).getId();
                                Log.d("admin", sql);
                                PreparedStatement prest = con.prepareStatement(sql);
                                int rs = prest.executeUpdate();
                                Log.d("D", rs + "");
                                if (rs == 1) {
                                    sub.remove(i);
                                    ((AppCompatActivity) ctx).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            notifyDataSetChanged();
                                        }
                                    });

                                } else {
                                    ((AppCompatActivity) ctx).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(productviewholder.mcxs, "Error", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    //Toast.makeText(productviewholder.mcxs, "Error", Toast.LENGTH_LONG).show();
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
        });

    }

    @Override
    public int getItemCount() {
        return sub.size();
    }

    class viewholder extends RecyclerView.ViewHolder {


        Context mcxs;
        TextView tv_sub, teacher;
        Button approve;

        public viewholder(@NonNull final View itemView, Context ctx, final List<Subject> sub) {
            super(itemView);
            this.mcxs = ctx;
            tv_sub = itemView.findViewById(R.id.sub);
            teacher = itemView.findViewById(R.id.teacher);
            approve = itemView.findViewById(R.id.approve);
            final int pos = getAdapterPosition();

        }

    }
}
