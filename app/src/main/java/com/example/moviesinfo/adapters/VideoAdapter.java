package com.example.moviesinfo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesinfo.R;
import com.example.moviesinfo.data.Video;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private onPlayClickListener onPlayClickListener;
    private ArrayList<Video> videos;

    public void setVideos(ArrayList<Video> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }

    public void setOnPlayClickListener(VideoAdapter.onPlayClickListener onPlayClickListener) {
        this.onPlayClickListener = onPlayClickListener;
    }

    //Adapter
    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = videos.get(position);
        holder.textViewVideoName.setText(video.getVideo_name());
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public interface onPlayClickListener {
        void onPlayClick(String videoURL);
    }

    //ViewHolder
    class VideoViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewVideoName;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewVideoName = itemView.findViewById(R.id.textViewVideoName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPlayClickListener.onPlayClick(videos.get(getAdapterPosition()).getVideo_key());
                }
            });
        }
    }
}
