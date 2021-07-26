package com.coretech.duck;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Viewholder> {
    Context  home;
    ArrayList<User> userArrayList;
    public UserAdapter(Home home, ArrayList<User> userArrayList) {
        this.home=home;
        this.userArrayList=userArrayList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(home).inflate(R.layout.items,parent,false);
        return new Viewholder(view);
    }
    public  void updatedata(ArrayList<User>userArrayList){
        userArrayList.clear();
        userArrayList.addAll(userArrayList);
        notifyDataSetChanged();
    }
    public void addItem(int position,User user){

        userArrayList.add(position, user);
        notifyItemInserted(position);
    }
    public void removeItem(int position){
        userArrayList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        User user=userArrayList.get(position);
        holder.username.setText(user.name);
        holder.userstatus.setText(user.getStat());
        Glide.with(this.home).load(user.image).into(holder.userprofile);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(home,Chat.class);
                intent.putExtra("name",user.getName());
                intent.putExtra("uid",user.getUid());
                intent .putExtra("image",user.getImage());
                intent.putExtra("stat",user.getStat());

                home.startActivity(intent);

            }
        });


    }



    @Override
    public int getItemCount() {
        return userArrayList.size();
    }
    static class Viewholder extends RecyclerView.ViewHolder {
        CircleImageView userprofile;
        TextView username;
        TextView userstatus;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            userprofile=itemView.findViewById(R.id.userprofile);
            username=itemView.findViewById(R.id.username);
            userstatus=itemView.findViewById(R.id.userstatus);
        }
    }


}
