package com.miroslavgojic.flickrbrowser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 3/29/2016.
 */

// Adapter stores all data side, it's use to manipulate data
// He works in conjuction with View to display data on the screen
public class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrImageViewHolder>{
    private List<Photo> mPhotoList;
    /* Interface to global information about an application environment.
       This is an abstract class whose implementation is provided by the Android system.
       It allows access to application-specific resources and classes, as well as up-calls for
       application-level operations such as launching activities, broadcasting and receiving intents, etc.
    */
    private Context mContext;
    private String LOG_TAG = FlickrRecyclerViewAdapter.class.getSimpleName();

    public FlickrRecyclerViewAdapter(Context context, List<Photo> photoList) {
        this.mContext = context;
        this.mPhotoList = photoList;
    }

    // podizemo(inflate) view holder(browse)
    @Override
    public FlickrImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.browse, null);
        FlickrImageViewHolder flickrImageViewHolder = new FlickrImageViewHolder(view);
        return flickrImageViewHolder;
    }

    /*
        onBindViewHolder(VH holder, int position)
        Called by RecyclerView to display the data at the specified position.
     */
    // any time there is view that is on screen that needs to be updated,
    // this method is called automatically
    @Override
    public void onBindViewHolder(FlickrImageViewHolder flickrImageViewHolder, int position) {
        Photo photoItem = mPhotoList.get(position);
        Log.d(LOG_TAG, " Proccessing photo item: " + photoItem.getmTitle() + " --> " + Integer.toString(position));

        // building thumbanil
        // kako se koja slika skine(download) tako je Picasso prikazuje
        Picasso.with(mContext)
                .load(photoItem.getmImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(flickrImageViewHolder.thumbnail);

        flickrImageViewHolder.title.setText(photoItem.getmTitle());
    }

    @Override
    public int getItemCount() {
        return (null != mPhotoList ? mPhotoList.size() : 0);
    }
}
