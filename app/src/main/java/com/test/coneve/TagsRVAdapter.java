package com.test.coneve;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collection;

public class TagsRVAdapter extends RecyclerView.Adapter<TagsRVAdapter.ViewHolder> {
    int size;
    Fragment parent;
    static TagWord currentWord;
    TextView tview;

    Callback<Tag> setTag;
    Callback<Tag> resetTag;
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
        CardView currentCard = holder.getCadview();
        TextView view = ((TextView)currentCard.findViewById(R.id.hor_text));
        view.setText(tags[position].getName());
        tags[position].getIcon().observe(parent, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                ((ImageView)currentCard.findViewById(R.id.hor_img)).setImageBitmap(bitmap);
            }
        });

        currentCard.setOnClickListener(new View.OnClickListener() {
            boolean enabled = false;
            CardView cview;
            @Override
            public void onClick(View view) {
                cview = (CardView) view;
                if(!enabled)
                {
                    enabled = true;
                    cview.setCardBackgroundColor(parent.getResources().getColor(R.color.selected_green));
                    setTag.callback(tags[holder.getAdapterPosition()]);
                }
                else
                {
                    enabled = false;
                    cview.setCardBackgroundColor(parent.getResources().getColor(R.color.unselected_grey));
                    resetTag.callback(tags[holder.getAdapterPosition()]);
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
        CardView crdview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            crdview = (CardView) itemView.findViewById(R.id.cardView);
            setIsRecyclable(false);
        }
        public TextView getTextView()
        {
            return textView;
        }
        public void setTextView(TextView txview)
        {
            textView = txview;
        }
        public CardView getCadview()
        {
            return crdview;
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

    public TagsRVAdapter(Fragment parent,Callback<Tag> setTag,Callback<Tag> resetTag)
    {
        this.tags = new Tag[1];
        size = 0;
        currentWord = new TagWord();
        this.parent = parent;
        this.setTag = setTag;
        this.resetTag = resetTag;
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
