package com.example.miniproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniproject.Activities.student.SubActivity;
import com.example.miniproject.R;
import java.util.List;

public class SemAdapter extends RecyclerView.Adapter<SemAdapter.viewholder> {
    private Context ctx;
    private List<Integer> sub;
    private int sid;


    public SemAdapter(Context ctx , List<Integer> sub,int sid) {
        this.ctx = ctx;
        this.sub = sub;
        this.sid = sid;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup , int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_course,viewGroup,false);
        return new viewholder(view,ctx,sub,sid);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder productviewholder , int i) {
        String s =sub.get(i).toString();
        productviewholder.tv_sub.setText("Semester "+s);

    }

    @Override
    public int getItemCount() {
        return sub.size();
    }

    class viewholder extends RecyclerView.ViewHolder implements View.OnClickListener{


        Context mcxs;
        TextView tv_sub,tv_code;
        public viewholder(@NonNull View itemView,Context ctx,List<Integer> sub,int sid) {
            super(itemView);
            this.mcxs=ctx;
            tv_sub=itemView.findViewById(R.id.sub);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos=getAdapterPosition();
            int s = sub.get(pos);
            Intent intent = new Intent(this.mcxs, SubActivity.class);;
            intent.putExtra("sem",s);
            intent.putExtra("course",sid);
            view.getContext().startActivity(intent);
        }
    }
}
