package com.example.adventureawaits;

import android.media.Image;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TripViewHolder extends RecyclerView.ViewHolder {
    private final TextView textViewTripName;
    private final TextView textViewDestination;
    private final ImageView imageViewTrip;
    private final SeekBar seekBarPrice;
    private final RatingBar ratingBar;

    private final ImageView bookmarkImageView;

    public TripViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
        super(itemView);
        textViewTripName = itemView.findViewById(R.id.textViewTripName);
        textViewDestination = itemView.findViewById(R.id.textViewDestination);
        imageViewTrip = itemView.findViewById(R.id.imageViewTrip);
        seekBarPrice = itemView.findViewById(R.id.seekBarPrice);
        ratingBar = itemView.findViewById(R.id.ratingBar);
        bookmarkImageView = itemView.findViewById(R.id.bookmarkImageView);

        bookmarkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerViewInterface != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onBookmarkClick(position);
                    }
                }
            }
        });

        seekBarPrice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        seekBarPrice.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerViewInterface != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onItemClick(position);
                    }
                }
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (recyclerViewInterface != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onItemLongClick(position);
                    }
                }
                return true;
            }
        });
    }

    public void bind(TripEntity tripEntity) {
        // Set the bookmark image based on the bookmark state
        if (tripEntity.getFavourite()) {
            bookmarkImageView.setImageResource(R.drawable.ic_bookmark_full);
        } else {
            bookmarkImageView.setImageResource(R.drawable.ic_bookmark_empty);
        }
    }

    public TextView getTextViewTripName() {
        return textViewTripName;
    }

    public TextView getTextViewDestination() {
        return textViewDestination;
    }

    public ImageView getImageViewTrip() {
        return imageViewTrip;
    }

    public SeekBar getSeekBarPrice() {
        return seekBarPrice;
    }

    public RatingBar getRatingBar() {
        return ratingBar;
    }

    public ImageView getBookmarkImageView() {
        return bookmarkImageView;
    }
}
