package com.app.cardfeature7;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// ChildAdapter.java
public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder> {

    private List<User.ChildInfo> childrenList;

    public ChildAdapter(List<User.ChildInfo> childrenList) {
        this.childrenList = childrenList;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the view for each item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_list_item, parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, int position) {
        // Bind child data to views
        User.ChildInfo childInfo = childrenList.get(position);
        Log.d("ChildAdapter", "Binding child at position " + position + ": " + childInfo.getChildUsername());
        holder.childUserName.setText(childInfo.getChildUsername());


        holder.childUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(childInfo.context, ReportGenerate.class);
                intent.putExtra("from","value");
                ExtraVat.snapshot=childInfo.snapshot;
                childInfo.context.startActivity(intent);
            }
        });


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childInfo.snapshot.getRef().child("socialAssistanceUID").removeValue();

            }
        });


        // Bind other child information if needed
    }

    @Override
    public int getItemCount() {
        return childrenList.size();
    }

    public static class ChildViewHolder extends RecyclerView.ViewHolder {
        TextView childUserName;
        TextView delete;

        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            childUserName = itemView.findViewById(R.id.childUserName);
            delete=itemView.findViewById(R.id.delete);
            // Initialize other TextViews if needed
        }
    }
}

