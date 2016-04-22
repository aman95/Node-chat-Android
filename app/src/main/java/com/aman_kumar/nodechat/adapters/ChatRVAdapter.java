package com.aman_kumar.nodechat.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aman_kumar.nodechat.R;
import com.aman_kumar.nodechat.models.Chat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aman on 29/03/16.
 */
public class ChatRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Chat> dataSet = new ArrayList<>();


    public ChatRVAdapter(List<Chat> chatList) {
        this.dataSet.addAll(chatList);
    }

    public void setData(List<Chat> chatList) {
        this.dataSet.clear();
        this.dataSet.addAll(chatList);
        notifyDataSetChanged();
    }
    public void addData(Chat chat, RecyclerView recyclerView) {
        dataSet.add(chat);
        notifyItemInserted(dataSet.size());
        recyclerView.smoothScrollToPosition(dataSet.size()-1);
    }

    public class ViewHolderCust extends RecyclerView.ViewHolder {
        TextView tvMsgCust;
        public ViewHolderCust(View itemView) {
            super(itemView);
            tvMsgCust = (TextView) itemView.findViewById(R.id.tv_cust_msg);

        }
    }
    public class ViewHolderAdmin extends RecyclerView.ViewHolder {
        TextView tvMsgAdmin;
        public ViewHolderAdmin(View itemView) {
            super(itemView);
            tvMsgAdmin = (TextView) itemView.findViewById(R.id.tv_admin_msg);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        if( viewType == 0) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_chat_row,parent,false);
            viewHolder = new ViewHolderAdmin(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_chat_row_cust,parent,false);
            viewHolder = new ViewHolderCust(v);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder.getItemViewType() == 0) {
            ViewHolderAdmin viewHolderAdmin = (ViewHolderAdmin) holder;
            viewHolderAdmin.tvMsgAdmin.setText(dataSet.get(position).getMsg());
        } else {
            ViewHolderCust viewHolderCust = (ViewHolderCust) holder;
            viewHolderCust.tvMsgCust.setText(dataSet.get(position).getMsg());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position).getSentBy();
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
