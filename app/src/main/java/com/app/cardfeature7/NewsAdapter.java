
package com.app.cardfeature7;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.firebase.database.DatabaseReference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;


// this task take the information of the news ( text ) and then displays it
public class NewsAdapter extends BaseAdapter {
    private ArrayList<NewsItem> newsList;
    private Context context;
    private DatabaseReference newsItemReference;

    public NewsAdapter(ArrayList<NewsItem> newsList, Context context, DatabaseReference newsItemReference) {
        this.newsList = newsList;
        this.context = context;
        this.newsItemReference = newsItemReference;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.news_item_card, parent, false);
        }

        TextView newsTextTextView = convertView.findViewById(R.id.newsTextTextView);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);
        Button modifyButton = convertView.findViewById(R.id.modifyButton);

        NewsItem newsItem = newsList.get(position);
        newsTextTextView.setText(newsItem.getText());

        // modify button
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a dialog for editing the news text
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View editDialogView = LayoutInflater.from(context).inflate(R.layout.edit_news_dialog, null);
                builder.setView(editDialogView);

                final EditText editNewsEditText = editDialogView.findViewById(R.id.editNewsEditText);
                Button saveButton = editDialogView.findViewById(R.id.saveButton);

                // Populate the edit text with the current news text
                editNewsEditText.setText(newsItem.getText());

                final AlertDialog editDialog = builder.create();

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Get the edited news text
                        String editedNewsText = editNewsEditText.getText().toString();

                        // Update the news item and notify the adapter of the data change
                        newsList.get(position).setText(editedNewsText);
                        notifyDataSetChanged();

                        // Update the item in Firebase Realtime Database
                        if (newsList.get(position).getId() != null) {
                            DatabaseReference itemRef = newsItemReference.child(newsList.get(position).getId());
                            itemRef.child("text").setValue(editedNewsText); // Corrected the path to "text"
                        }

                        // Close the edit dialog
                        editDialog.dismiss();
                    }
                });

                editDialog.show();
            }
        });

// Set a click listener for the delete button
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position >= 0 && position < newsList.size()) {
                    // Get the news item to be deleted
                    NewsItem deletedNews = newsList.get(position);

                    // Check if the ID is null
                    if (deletedNews.getId() != null) {
                        // Retrieve the unique ID (key) of the news item
                        String newsItemId = deletedNews.getId();

                        // Remove the item from the local list
                        newsList.remove(position);
                        notifyDataSetChanged();

                        // Delete the corresponding news item from Firebase Realtime Database
                        DatabaseReference itemRef = newsItemReference.child(newsItemId);
                        itemRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Deletion was successful
                                    Toast.makeText(context, "Removed successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Deletion encountered an error
                                    Toast.makeText(context, "Deletion failed", Toast.LENGTH_SHORT).show();
                                    // You might want to add additional error handling here
                                }
                            }
                        });
                    }
                }
            }
        });

        return convertView;
    }

    public void addNewsItem(NewsItem newsItem) {
        // Add the NewsItem to the database
        DatabaseReference newNewsItemRef = newsItemReference.push();
        String key = newNewsItemRef.getKey();
        if (key != null) {
            newsItem.setId(key);
            newNewsItemRef.setValue(newsItem);
        }
    }
}
