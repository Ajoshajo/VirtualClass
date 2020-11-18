package com.example.miniproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject.Activities.student.SemActivity;
import com.example.miniproject.R;

import java.util.List;

import com.example.miniproject.models.Course;
import com.example.miniproject.models.Student;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.viewholder> {
    private Context ctx;
    private List<Student> sub;


    public StudentAdapter(Context ctx, List<Student> sub) {
        this.ctx = ctx;
        this.sub = sub;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_student, viewGroup, false);
        return new viewholder(view, ctx, sub);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder productviewholder, int i) {
        String s = sub.get(i).getName();
        productviewholder.tv_sub.setText(s);
        productviewholder.email.setText(sub.get(i).getEmail());

    }

    @Override
    public int getItemCount() {
        return sub.size();
    }

    class viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {


        Context mcxs;
        TextView tv_sub, email;

        public viewholder(@NonNull View itemView, Context ctx, List<Student> sub) {
            super(itemView);
            this.mcxs = ctx;
            tv_sub = itemView.findViewById(R.id.sub);
            email = itemView.findViewById(R.id.email);
            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            int s = sub.get(pos).getId();
            Intent intent = new Intent(this.mcxs, SemActivity.class);
            ;
            intent.putExtra("sub", s);
            intent.putExtra("name", sub.get(pos).getName());
            view.getContext().startActivity(intent);
        }
    }
}