package com.coretech.duck;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.coretech.duck.Chat.rimage;
import static com.coretech.duck.Chat.simage;

public class MessAdptr extends RecyclerView.Adapter {
    Context context;
    ArrayList<Messages> messagesArrayList;
    int ITEM_SEND=1;
    int ITEM_RECIVE=2;

    public MessAdptr(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==ITEM_SEND){
            View view= LayoutInflater.from(context).inflate(R.layout.sender,parent,false);
            return new SenderViewHolder(view);
        }
        else {
            View view= LayoutInflater.from(context).inflate(R.layout.reciver,parent,false);
            return new ReciverViewHolder(view);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages messages=messagesArrayList.get(position);


        if (holder.getClass()==SenderViewHolder.class){
            SenderViewHolder viewHolder=(SenderViewHolder) holder;

            viewHolder.txtMessage.setText(messages.getMessage());
            viewHolder.tdm.setText(messages.getTime());

            Glide.with(this.context).load(simage).into(viewHolder.circleImageView);


        }
        else {
            ReciverViewHolder viewHolder=(ReciverViewHolder) holder;

            viewHolder.txtMessage.setText(messages.getMessage());
            Glide.with(this.context).load(rimage).into(viewHolder.circleImageView);
            viewHolder.tdmr.setText(messages.getTime());


        }

    }

    @Override
    public int getItemCount() {

        return messagesArrayList.size();
    }


    @Override
    public int getItemViewType(int position) {
        Messages messages=messagesArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId())){
            return  ITEM_SEND;
        }
        else {
            return ITEM_RECIVE;
        }
    }

    class SenderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView txtMessage;
        TextView tdm;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            tdm=itemView.findViewById(R.id.tdm);

            circleImageView=itemView.findViewById(R.id.senderpro);
            txtMessage=itemView.findViewById(R.id.txtMessages);
        }
    }
    class ReciverViewHolder extends RecyclerView.ViewHolder {
        TextView tdmr;
        CircleImageView circleImageView;
        TextView txtMessage;
        public ReciverViewHolder(@NonNull View itemView) {
            super(itemView);
            tdmr=itemView.findViewById(R.id.tdmr);

            circleImageView=itemView.findViewById(R.id.renderpro);
            txtMessage=itemView.findViewById(R.id.txtrMessages);
        }
    }
}
