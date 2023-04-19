package com.test.coneve;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collection;

public class EventsRVAdapter extends RecyclerView.Adapter<EventsRVAdapter.ViewHolder> {

    EventFragment caller;
    int windowheight;
    int windowwidth;

    Collection<EventsDataModel> eventSet;

    public Collection<EventsDataModel> getEventSet() {
        return eventSet;
    }

    public void setEventSet(Collection<EventsDataModel> eventSet) {
        this.eventSet = eventSet;
        notifyDataSetChanged();
    }

    public EventsRVAdapter(int widthPixels, int heightPixels, EventFragment eventFragment) {
        caller = eventFragment;
        windowwidth = widthPixels;
        windowheight = heightPixels;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public EventsRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull EventsRVAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
