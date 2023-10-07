package customerapp.adapters.cutsomerapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.customerapp.R;
import customerapp.fragments.customerapp.StoreDetailsFragment;
import customerapp.models.customerapp.FragmentManagerHelper;
import customerapp.models.customerapp.StoreDetails;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Represents an adapter for the RecyclerView in the ExploreFragment, responsible for displaying a list of store details.
 * When a store details item is clicked, it navigates to the StoreDetailsFragment to show more information about the selected store.
 */
public class StoreDetailsAdapter extends RecyclerView.Adapter<StoreDetailsAdapter.StoreViewHolder>
{
    private ArrayList<StoreDetails> storeList;
    private Context context;

    public StoreDetailsAdapter(Context context, ArrayList<StoreDetails> storeList)
    {
        this.context = context;
        this.storeList = storeList;
    }

    /**
     * Creates a new ViewHolder instance for a given view type.
     *
     * @param parent The parent ViewGroup.
     * @param viewType The type of view for this ViewHolder.
     * @return A new QRCodeViewHolder instance.
     */
    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_details, parent, false);
        return new StoreViewHolder(itemView);
    }

    /**
     * Binds data to the provided ViewHolder, setting the store's name, logo,
     * and click listener for navigation to the `StoreDetailsFragment`.
     *
     * @param holder   The ViewHolder instance to which the data should be bound.
     * @param position The position of the item within the dataset.
     */
    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position)
    {
        StoreDetails store = storeList.get(position);
        holder.storeNameTextView.setText(store.getStoreName());

        String logoUrl = store.getLogo();
        loadImage(holder.storeLogoImageView, logoUrl);

        holder.openDetails.setOnClickListener(view -> goToStoreDetailsFragment(store));
    }

    /**
     * Returns the total number of store items in the dataset.
     *
     * @return The item count.
     */
    @Override
    public int getItemCount()
    {
        return storeList.size();
    }

    /**
     * ViewHolder class that represents an individual store item view.
     */
    static class StoreViewHolder extends RecyclerView.ViewHolder
    {
        ImageView storeLogoImageView;
        TextView storeNameTextView;
        ImageView openStoreDetailsImageView;
        LinearLayout openDetails;


        StoreViewHolder(@NonNull View itemView)
        {
            super(itemView);
            storeLogoImageView = itemView.findViewById(R.id.storeLogoImageView);
            storeNameTextView = itemView.findViewById(R.id.storeNameTextView);
            openStoreDetailsImageView = itemView.findViewById(R.id.openStoreDetailsImageView);
            openDetails = itemView.findViewById(R.id.openDetails);
        }
    }

    /**
     * Handles the navigation to the StoreDetailsFragment when a store item is clicked.
     *
     * @param store The selected store.
     */
    private void goToStoreDetailsFragment(StoreDetails store)
    {
        FragmentManagerHelper.goToFragment(
                ((AppCompatActivity) context).getSupportFragmentManager(),
                R.id.frame_layout,
                StoreDetailsFragment.newInstance(store),
                R.anim.slide_in_right,
                R.anim.slide_out,
                true
        );
    }

    /**
     * Loads and displays the store's logo in the specified ImageView using the Picasso library.
     * If the image fails to load, an error placeholder is displayed.
     *
     * @param imageView The ImageView where the logo should be displayed.
     * @param imageUrl  The URL from which the image should be loaded.
     */
    private void loadImage(ImageView imageView, String imageUrl)
        {
        try
            {
            int targetWidth = (int) context.getResources().getDimension(
                    R.dimen.store_logo_adapter_width);
            int targetHeight = (int) context.getResources().getDimension(
                    R.dimen.store_logo_adapter_height);

            Picasso.get().load(imageUrl).resize(targetWidth, targetHeight).centerInside()
                   .placeholder(R.drawable.baseline_loading_animation).error(
                           R.drawable.baseline_error_image).into(imageView, new Callback()
                       {
                       @Override
                       public void onSuccess()
                           {
                           Log.d("Picasso", "Logo-Bild erfolgreich im Adapter geladen");
                           }

                       @Override
                       public void onError(Exception e)
                           {
                           Log.e("Picasso", "Fehler beim Laden des Logo-Bildes im Adapter", e);
                           }
                       });
            } catch (Exception e)
            {
            Log.e("StoreDetailsAdapter", "Fehler beim Laden des Bildes mit Picasso", e);
            }
        }

}
