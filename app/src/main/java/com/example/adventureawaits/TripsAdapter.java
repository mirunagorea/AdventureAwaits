package com.example.adventureawaits;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TripsAdapter extends RecyclerView.Adapter<TripViewHolder> {
    private List<TripEntity> trips;
    private final RecyclerViewInterface recyclerViewInterface;

    public TripsAdapter(List<TripEntity> trips, RecyclerViewInterface recyclerViewInterface) {
        this.trips = trips;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item, parent, false);
        return new TripViewHolder(itemView, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        TripEntity currentTrip = trips.get(position);
        holder.getTextViewTripName().setText(currentTrip.getName());
        holder.getTextViewDestination().setText(currentTrip.getDestination());
        if (currentTrip.getPhotoData() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(currentTrip.getPhotoData(), 0, currentTrip.getPhotoData().length);
            holder.getImageViewTrip().setImageBitmap(bitmap);
        }
        holder.getSeekBarPrice().setProgress(currentTrip.getPrice());
        holder.getRatingBar().setRating(currentTrip.getRating());
//        holder.mTextView.setText(mData.get(position));
        if (currentTrip.getFavourite())
            holder.getBookmarkImageView().setImageResource(R.drawable.ic_bookmark_full);
        else holder.getBookmarkImageView().setImageResource(R.drawable.ic_bookmark_empty);
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public void updateItem(int position, TripEntity tripEntity) {
        trips.set(position, tripEntity);
        notifyItemChanged(position);
    }
}
