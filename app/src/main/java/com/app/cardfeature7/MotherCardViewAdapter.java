package com.app.cardfeature7;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.graphics.Color;


public class MotherCardViewAdapter extends RecyclerView.Adapter<MotherCardViewAdapter.ViewHolder> {

    private List<Task> taskList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(Task task);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public MotherCardViewAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    //  this method is to update the strike-through effect
    public void updateTaskStrikeThrough(Task updatedTask) {
        int position = taskList.indexOf(updatedTask);
        if (position != -1) {
            // Update the task in the list
            taskList.set(position, updatedTask);

            // Notify the adapter that the data at the specific position has changed
            notifyItemChanged(position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView taskNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskNameTextView = itemView.findViewById(R.id.textTaskName);

            // Handle item click events
            itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(taskList.get(position));
                    }
                }
            });
        }

        public void bind(Task task) {
            taskNameTextView.setText(task.getName());

            // Apply strike-through effect for complete tasks
            if (task.isCompleted()) {
                taskNameTextView.setPaintFlags(taskNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                // Remove strike-through effect for incomplete tasks
                taskNameTextView.setPaintFlags(taskNameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }

            // Bind other views with data if needed
        }
    }


}

