package com.test.coneve;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collection;

public class TagsRVAdapter extends RecyclerView.Adapter<TagsRVAdapter.ViewHolder> {
    int size;
    EventFragment parent;
    static TagWord currentWord;
    TextView tview;
    @NonNull
    @Override
    public TagsRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_item_view,parent,false);
        return new ViewHolder(view);
    }
    Tag[] tags;
    public TagWord GetTagWord()
    {
        return currentWord;
    }
    @Override
    public void onBindViewHolder(@NonNull TagsRVAdapter.ViewHolder holder, int position) {
        TextView view = holder.getTextView();

        view.setText(tags[position].getName());
        view.setOnClickListener(new View.OnClickListener() {
            boolean enabled = false;

            @Override
            public void onClick(View view) {
                tview = (TextView) view;
                if(!enabled)
                {
                    enabled = true;
                    tview.setBackgroundColor(parent.getResources().getColor(R.color.Red));
                    tview.setTextColor(parent.getResources().getColor(R.color.white));
                    currentWord.addTag(tags[holder.getAdapterPosition()]);
                    Collection<EventsDataModel> temp = parent.filterEvents(parent.eventSet,currentWord);
                    temp = parent.sortEvents(temp,new EventsDataModel.ComparatorList.TimeCmp());
                    parent.adapter.setEventSet(temp);

                }
                else
                {
                    enabled = false;
                    tview.setBackgroundColor(parent.getResources().getColor(R.color.white));
                    tview.setTextColor(parent.getResources().getColor(R.color.black));
                    currentWord.removeTag(tags[holder.getAdapterPosition()]);
                    Collection<EventsDataModel> temp = parent.filterEvents(parent.eventSet,currentWord);
                    temp = parent.sortEvents(temp,new EventsDataModel.ComparatorList.TimeCmp());
                    parent.adapter.setEventSet(temp);
                }
            }
        });
        holder.setTextView(view);

    }

    @Override
    public int getItemCount() {

        return size;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.hor_text);
            imageView = (ImageView) itemView.findViewById(R.id.hor_img);
        }
        public TextView getTextView()
        {
            return textView;
        }
        public void setTextView(TextView txview)
        {
            textView = txview;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(int res) {
            imageView.setImageResource(res);
        }
    }
    public TagsRVAdapter(EventFragment parent)
    {
        this.tags = new Tag[1];
        size = 0;
        currentWord = new TagWord();
        this.parent = parent;
    }
    void setData(Collection<Tag> tags)
    {
        if(tags == null)
            return;
        this.tags = tags.toArray(this.tags);
        size = tags.size();
        notifyDataSetChanged();
    }
}
