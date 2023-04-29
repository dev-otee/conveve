package com.test.coneve;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.Collection;
import java.util.Objects;

public class EventsRVAdapter extends RecyclerView.Adapter<EventsRVAdapter.ViewHolder> {

    EventFragment caller;
    int size;
    int windowheight;
    int windowwidth;

    EventsDataModel[] eventSet;



    public void setEventSet(Collection<EventsDataModel> eventSet) {
        if(eventSet == null)
            return;
        size = eventSet.size();
        this.eventSet = eventSet.toArray(this.eventSet);
        notifyDataSetChanged();
    }

    public EventsRVAdapter(int widthPixels, int heightPixels, EventFragment eventFragment) {
        caller = eventFragment;
        windowwidth = widthPixels;
        windowheight = heightPixels;
        size = 0;
        eventSet = new EventsDataModel[0];
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.parentEventContainer);
        }

        public CardView getCardView(){
            return cardView;
        }
    }

    @NonNull
    @Override
    public EventsRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card_view,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventsRVAdapter.ViewHolder holder, int position) {

        EventsDataModel event = eventSet[position];
        ((TextView) holder.getCardView().findViewById(R.id.eventname)).setText(event.getName());
        ((TextView) holder.getCardView().findViewById(R.id.eventdescription)).setText(event.getDescription());
        


        event.eventPoster.observe(caller.getActivity(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                ((ImageView) holder.getCardView().findViewById(R.id.eventphoto)).setImageBitmap(bitmap);
            }
        });

    }

    @Override
    public int getItemCount() {

        return size;
    }


}
