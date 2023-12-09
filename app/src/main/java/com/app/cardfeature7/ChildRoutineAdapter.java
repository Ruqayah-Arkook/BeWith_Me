package com.app.cardfeature7;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class ChildRoutineAdapter extends RecyclerView.Adapter<ChildRoutineAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Task> taskList;

    private OnCompleteListener mListener; // for complete button
    private String motherId;
    public interface OnCompleteListener {
        void onCompleteButtonClick(String taskId);
    }

    public ChildRoutineAdapter(Context context, ArrayList<Task> taskList, OnCompleteListener listener,
                               String motherId) {
        this.context = context;
        this.taskList = taskList;
        this.mListener = listener;
        this.motherId = motherId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.child_task_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task currentTask = taskList.get(position);

        // Update the ViewHolder components with task information
        holder.nameTextView.setText(currentTask.getName());
        holder.descriptionTextView.setText(currentTask.getDescription());
        holder.timeTextView.setText(currentTask.getTime());

        // Check if there is a voice recording and update UI accordingly
        if (currentTask.getVoiceRecordePath() != null && !currentTask.getVoiceRecordePath().isEmpty()) {
            holder.playVoiceButton.setVisibility(View.VISIBLE);
            holder.playVoiceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle playing the voice recording here...
                    String voicePath = currentTask.getVoiceRecordePath();
                    // Implement the logic to play the voice recording...

                    // For example, you can use MediaPlayer to play the voice recording
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(voicePath);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            holder.playVoiceButton.setVisibility(View.GONE);
        }

        // Check if selectedImageUrl is not empty or null before loading the image
        if (currentTask.getSelectedImageUrl() != null && !currentTask.getSelectedImageUrl().isEmpty()) {
            // Display the resized image using Picasso
            Picasso.get()
                    .load(currentTask.getSelectedImageUrl())
                    .resize(350, 270)
                    .centerCrop()
                    .into(holder.taskImageView);
            holder.taskImageView.setVisibility(View.VISIBLE); // Make sure the ImageView is visible
        } else {
            // If selectedImageUrl is empty or null, hide the ImageView
            holder.taskImageView.setVisibility(View.GONE);
        }

// Set the initial state of the completeButton
        if (currentTask.isCompleted()) {
            holder.completeButton.setBackgroundResource(R.drawable.ic_empty_tick);
        } else {
            holder.completeButton.setBackgroundResource(R.drawable.ic_empty_square);
        }

        // Set click listener for "Complete" button
        holder.completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notify the listener that "Complete" button is clicked
                if (mListener != null) {
                    mListener.onCompleteButtonClick(currentTask.getId());
                }

                // Update the completion status persistently
                currentTask.setCompleted(!currentTask.isCompleted());

                // Change the background drawable based on the completion status
                if (currentTask.isCompleted()) {
                    holder.completeButton.setBackgroundResource(R.drawable.ic_empty_tick);
                } else {
                    holder.completeButton.setBackgroundResource(R.drawable.ic_empty_square);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;
        TextView timeTextView;
        ImageView taskImageView;
        Button playVoiceButton;
        AppCompatImageButton completeButton; // Change to AppCompatImageButton

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.taskNameTextView);
            descriptionTextView = itemView.findViewById(R.id.taskDescriptionTextView);
            timeTextView = itemView.findViewById(R.id.taskTimeTextView);
            taskImageView = itemView.findViewById(R.id.taskImageView);
            playVoiceButton = itemView.findViewById(R.id.playVoiceButton);
            completeButton = itemView.findViewById(R.id.completeButton); // Change to AppCompatImageButton
        }
    }

}