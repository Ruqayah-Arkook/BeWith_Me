package com.app.cardfeature7;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class EmotionAdapter extends ArrayAdapter<Emotion> {

    public EmotionAdapter(Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        Emotion emotion = getItem(position);

        if (emotion != null) {
            TextView nameTextView = convertView.findViewById(android.R.id.text1);
            TextView descriptionTextView = convertView.findViewById(android.R.id.text2);

            nameTextView.setText(emotion.getName());
            descriptionTextView.setText(emotion.getDescription());
        }

        return convertView;
    }
}

 class Emotion {
    private String date;
    private String emotion;

    public Emotion() {
        // Default constructor required for DataSnapshot.getValue(Emotion.class)
    }

    public Emotion(String date, String emotion) {
        this.date = date;
        this.emotion = emotion;
    }

    public String getName() {
        return emotion;
    }

    public String getDescription() {
        return date;
    }
}
