package com.example.adventureawaits.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adventureawaits.AddEditTripActivity;
import com.example.adventureawaits.MyDatabase;
import com.example.adventureawaits.R;
import com.example.adventureawaits.retrofit.ReadOnlyActivity;
import com.example.adventureawaits.RecyclerViewInterface;
import com.example.adventureawaits.TripDao;
import com.example.adventureawaits.TripEntity;
import com.example.adventureawaits.TripsAdapter;
//import com.example.adventureawaits.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements RecyclerViewInterface {

    //    private FragmentHomeBinding binding;
    private List<TripEntity> trips;
    private RecyclerView recyclerViewTrips;

    private TripsAdapter tripsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);

//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerViewTrips = view.findViewById(R.id.recyclerViewTrips);
        populateTrips();
        setupRecyclerView();
//        return root;
        return view;
    }

    private void setupRecyclerView() {
        recyclerViewTrips.setLayoutManager(new LinearLayoutManager(getContext()));
        tripsAdapter = new TripsAdapter(trips, this);
        recyclerViewTrips.setAdapter(tripsAdapter);
    }

    private void populateTrips() {
        trips = new ArrayList<>();
        MyDatabase tripsDatabase = MyDatabase.getTripDatabase(getActivity());
        TripDao tripDao = tripsDatabase.tripDao();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<TripEntity> tripEntities = tripDao.allTrips();
                for (TripEntity tripEntity : tripEntities) {
                    trips.add(tripEntity);
                }
            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), ReadOnlyActivity.class);
        MyDatabase tripsDatabase = MyDatabase.getTripDatabase(getActivity());
        TripDao tripDao = tripsDatabase.tripDao();
        new Thread(new Runnable() {
            @Override
            public void run() {
                TripEntity tripEntity = tripDao.findTripByName(trips.get(position).getName());
                intent.putExtra("ID", tripEntity.getId());
                startActivity(intent);
            }
        }).start();
    }

    @Override
    public void onItemLongClick(int position) {
        Intent intent = new Intent(getActivity(), AddEditTripActivity.class);
        MyDatabase tripsDatabase = MyDatabase.getTripDatabase(getActivity());
        TripDao tripDao = tripsDatabase.tripDao();
        new Thread(new Runnable() {
            @Override
            public void run() {
                TripEntity tripEntity = tripDao.findTripByName(trips.get(position).getName());
                intent.putExtra("ID", tripEntity.getId());
                startActivity(intent);
            }
        }).start();
    }

    @Override
    public void onBookmarkClick(int position) {
        Log.e("BOOKMARK", "Is clicked");
        MyDatabase tripsDatabase = MyDatabase.getTripDatabase(getActivity());
        TripDao tripDao = tripsDatabase.tripDao();
        new Thread(new Runnable() {
            @Override
            public void run() {
                TripEntity tripEntity = tripDao.findTripByName(trips.get(position).getName());
                boolean isFavourite = tripEntity.getFavourite();
                tripEntity.setFavourite(!isFavourite);
                tripDao.updateTrip(tripEntity);
                trips.get(position).setFavourite(!isFavourite);
                Log.e("FAVOURITE", tripEntity.getFavourite().toString());
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tripsAdapter.updateItem(position, tripEntity);
                    }
                });
            }
        }).start();
    }
}