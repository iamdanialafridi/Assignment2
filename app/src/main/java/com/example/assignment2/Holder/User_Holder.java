package com.example.assignment2.Holder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment2.Classess.User_Model;
import com.example.assignment2.MainActivity;
import com.example.assignment2.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class User_Holder  extends RecyclerView.Adapter<User_Holder.View_Holder>{
Context context;
List<User_Model> user_modelList;

    public User_Holder(@NonNull Context context, List<User_Model> user_modelList) {
        this.context = context;
        this.user_modelList = user_modelList;
    }

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       context = parent.getContext();
       View view = LayoutInflater.from(context).inflate(R.layout.user_row,parent,false);

        return new View_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, int position) {
        User_Model userModel = user_modelList.get(position);
        Picasso.get().load(userModel.getUser_image()).fit()
                .placeholder(R.drawable.placeholder)
                .centerCrop().into(holder.USERIMG);
        holder.Fullname.setText(userModel.getName());
    }

    @Override
    public int getItemCount() {
        return user_modelList.size();
    }

    class View_Holder extends RecyclerView.ViewHolder {
        ImageView USERIMG;
        TextView Fullname;
        public View_Holder(@NonNull View itemView) {
            super(itemView);
            USERIMG = itemView.findViewById(R.id.USERIMG);
            Fullname = itemView.findViewById(R.id.Fullname);

            USERIMG.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    String UID = user_modelList.get(pos).getUser_id();
                    Intent i = new Intent(context, MainActivity.class);
                    i.putExtra("USERID", UID);
                    context.startActivity(i);
                    ((Activity) context).finish();
                }
            });
        }
    }
}
