package com.test.coneve;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

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
        ((TextView)holder.getcrdview().findViewById(R.id.feedbackresponse)).setText(data[position].getMessage());
        ((TextView)holder.getcrdview().findViewById(R.id.feedbackUser)).setText(data[position].getUid());
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
        CardView crdview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txview = itemView.findViewById(R.id.feedbackresponse);
            crdview = itemView.findViewById(R.id.parentFeedbackContainer);
        }

        public TextView gettextview()
        {
            return txview;
        }
        public CardView getcrdview(){return crdview;}
    }
}
