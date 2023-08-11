package com.example.customerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.MapView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Represents an adapter for the RecyclerView in the ExploreFragment, responsible for displaying a list of store details.
 * When a store details item is clicked, it navigates to the StoreDetailsFragment to show more information about the selected store.
 */
public class StoreDetailsAdapter extends RecyclerView.Adapter<StoreDetailsAdapter.StoreViewHolder> {
    private ArrayList<StoreDetails> storeList;
    private Context context;

    public StoreDetailsAdapter(Context context, ArrayList<StoreDetails> storeList) {
        this.context = context;
        this.storeList = storeList;
    }


    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_details, parent, false);
        return new StoreViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        StoreDetails store = storeList.get(position);
        holder.storeNameTextView.setText(store.getName());

        String logoUrl = store.getLogo();
        Picasso.get().load(logoUrl).into(holder.storeLogoImageView);

        Picasso.get().load(logoUrl).into(holder.storeLogoImageView, new Callback() {
            @Override
            public void onSuccess() {
                // Optional: Zusätzlicher Code für Erfolgsfall
            }

            @Override
            public void onError(Exception e) {
                // Optional: Zusätzlicher Code für Fehlerfall
            }
        });

        holder.openDetails.setOnClickListener(view -> goToStoreDetailsFragment(store));
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    static class StoreViewHolder extends RecyclerView.ViewHolder {
        ImageView storeLogoImageView;
        TextView storeNameTextView;
        ImageView openStoreDetails;
        LinearLayout openDetails;



        StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            storeLogoImageView = itemView.findViewById(R.id.storeLogoImageView);
            storeNameTextView = itemView.findViewById(R.id.storeNameTextView);
            openStoreDetails = itemView.findViewById(R.id.openStoreDetails);
            openDetails = itemView.findViewById(R.id.openDetails);


        }
    }

    private void goToStoreDetailsFragment(StoreDetails store) {
        StoreDetailsFragment storeDetailsFragment = StoreDetailsFragment.newInstance(store);
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, storeDetailsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}