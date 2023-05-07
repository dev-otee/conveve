package com.test.coneve;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.Collection;

public class feedbackRVA extends RecyclerView.Adapter<feedbackRVA.ViewHolder> {

    feedback[] data;
    public void setData(Collection<feedback> data)
    {
        this.data = data.toArray(this.data);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public feedbackRVA.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feedback_card_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull feedbackRVA.ViewHolder holder, int position) {
        holder.gettextview().setText(data[position].message);
    }

    @Override
    public int getItemCount() {
        return Array.getLength(data);
    }
    public feedbackRVA()
    {
        data = new feedback[0];
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txview = itemView.findViewById(R.id.feedbackresponse);
        }

        public TextView gettextview()
        {
            return txview;
        }
    }
}
