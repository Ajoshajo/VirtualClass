package com.example.miniproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject.Activities.student.SemActivity;
import com.example.miniproject.R;

import java.io.File;
import java.util.List;

import com.example.miniproject.models.Chat;
import com.example.miniproject.models.Course;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.viewholder> {
    private Context ctx;
    private List<Chat> sub;


    public ChatAdapter(Context ctx, List<Chat> sub) {
        this.ctx = ctx;
        this.sub = sub;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat, viewGroup, false);
        return new viewholder(view, ctx, sub);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder productviewholder, int i) {

        if (sub.get(i).getType() == Chat.MSG) {
            productviewholder.msg.setText(sub.get(i).getMsg());
        } else {
            productviewholder.msg.setText(sub.get(i).getType());
            productviewholder.fname.setVisibility(View.VISIBLE);
            String filename = sub.get(i).getMsg().substring(sub.get(i).getMsg().lastIndexOf("/") + 1);
            if (!sub.get(i).isActive()) {
                filename += " (Waiting For Approval)";
            }

            productviewholder.fname.setText(filename);
        }

        if (sub.get(i).getIsteacher()) {
            productviewholder.tv_sub.setText(sub.get(i).getTeacher());
            productviewholder.tv_sub.setGravity(Gravity.RIGHT);
            productviewholder.msg.setGravity(Gravity.RIGHT);
            productviewholder.fname.setGravity(Gravity.RIGHT);
        } else {
            productviewholder.tv_sub.setText(sub.get(i).getStudent());
        }

    }

    @Override
    public int getItemCount() {
        return sub.size();
    }

    class viewholder extends RecyclerView.ViewHolder {


        Context mcxs;
        TextView tv_sub, msg, fname;

        public viewholder(@NonNull View itemView, Context ctx, List<Chat> sub) {
            super(itemView);
            this.mcxs = ctx;
            tv_sub = itemView.findViewById(R.id.name);
            msg = itemView.findViewById(R.id.msg);
            fname = itemView.findViewById(R.id.fname);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (sub.get(pos).getType() == Chat.PDF) {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW);
                        myIntent.setData(Uri.fromFile(new File(sub.get(pos).getMsg())));
                        Intent j = Intent.createChooser(myIntent, "Choose an application to open with:");
                        mcxs.startActivity(j);
                    }
                }
            });
        }


    }
}
