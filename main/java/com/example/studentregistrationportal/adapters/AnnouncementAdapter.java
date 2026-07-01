package com.example.studentregistrationportal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentregistrationportal.R;
import com.example.studentregistrationportal.models.Announcement;

import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {
    private Context context;
    private List<Announcement> announcements;

    public AnnouncementAdapter(Context context, List<Announcement> announcements) {
        this.context = context;
        this.announcements = announcements;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_announcement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Announcement announcement = announcements.get(position);
        if (holder.tvTitle != null) holder.tvTitle.setText(announcement.getTitle());
        if (holder.tvDate != null) holder.tvDate.setText(announcement.getDate());
        if (holder.tvContent != null) holder.tvContent.setText(announcement.getContent());
        
        if (holder.tvAuthor != null) {
            holder.tvAuthor.setText("By: " + announcement.getAuthor());
        }

        if (announcement.isImportant()) {
            holder.itemView.setBackgroundColor(
                    context.getResources().getColor(android.R.color.holo_orange_light));
            if (holder.tvAuthor != null) holder.tvAuthor.setTextColor(context.getResources().getColor(android.R.color.black));
            if (holder.tvDate != null) holder.tvDate.setTextColor(context.getResources().getColor(android.R.color.black));
            if (holder.tvCategory != null) holder.tvCategory.setTextColor(context.getResources().getColor(android.R.color.holo_blue_dark));
        } else {
            holder.itemView.setBackgroundColor(
                    context.getResources().getColor(android.R.color.white));
            if (holder.tvAuthor != null) holder.tvAuthor.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            if (holder.tvDate != null) holder.tvDate.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            if (holder.tvCategory != null) holder.tvCategory.setTextColor(context.getResources().getColor(android.R.color.holo_blue_dark));
        }

        if (holder.tvCategory != null) {
            if (announcement.getCategory() != null) {
                holder.tvCategory.setText(announcement.getCategory());
                holder.tvCategory.setVisibility(View.VISIBLE);
            } else {
                holder.tvCategory.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDate, tvContent, tvAuthor, tvCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvCategory = itemView.findViewById(R.id.tvCategory);
        }
    }
}