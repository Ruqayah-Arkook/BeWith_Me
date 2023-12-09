package com.app.cardfeature7;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class NewsDisplayAdapter extends RecyclerView.Adapter<NewsDisplayAdapter.ViewHolder> {

    private ArrayList<NewsItem> newsList;
    private Context context;

    public NewsDisplayAdapter(ArrayList<NewsItem> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item_display_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsItem newsItem = newsList.get(position);
        holder.newsTextTextView.setText(newsItem.getText());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void clear() {
        newsList.clear();
    }

    public void addNewsItem(NewsItem newsItem) {
        newsList.add(newsItem);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView newsTextTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsTextTextView = itemView.findViewById(R.id.newsTextTextView);
        }
    }
}
