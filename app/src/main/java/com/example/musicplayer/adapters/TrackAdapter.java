package com.example.musicplayer.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.activities.MusicPlayerActivity;
import com.example.musicplayer.models.TrackModel;

import java.io.Serializable;
import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    Context context;
    List<TrackModel> list;

    public TrackAdapter(Context context, List<TrackModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TrackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_all_track_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrackAdapter.ViewHolder holder, int position) {

        holder.textViewTitle.setText(list.get(position).getTitle());

        holder.textViewArtist.setText(list.get(position).getArtist());

        holder.itemView.setOnClickListener(view -> {
            //send list track to music player activity
            Intent intent = new Intent(context, MusicPlayerActivity.class);
            intent.putExtra("TrackModelList", (Serializable) list);
            intent.putExtra("TrackPosition", position);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewTitle, textViewArtist;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewTrackItem);
            textViewTitle = itemView.findViewById(R.id.textViewTitleTrackItem);
            textViewArtist = itemView.findViewById(R.id.textViewArtistTrackItem);
        }
    }
}
