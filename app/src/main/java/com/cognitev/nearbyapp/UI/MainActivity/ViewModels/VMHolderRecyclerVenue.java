package com.cognitev.nearbyapp.UI.MainActivity.ViewModels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cognitev.nearbyapp.BR;
import com.cognitev.nearbyapp.Models.Venue;
import com.cognitev.nearbyapp.Utilities.UtilityGeneral;

public class VMHolderRecyclerVenue extends BaseObservable {
    private String name;
    private String formattedAddress;
    private String imageUrl;

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext()).load(url).apply(UtilityGeneral.GetGlideOptions()).into(view);
    }

    public VMHolderRecyclerVenue changeVenue(Venue v) {
        this.name = v.name;
        this.formattedAddress = v.formattedAddress;
        this.imageUrl = v.getImageUrl();
        notifyChange();
        return this;
    }

    @Bindable
    public String getName() {
        return name == null ? "" : name;
    }

    public VMHolderRecyclerVenue setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
        return this;
    }

    @Bindable
    public String getFormattedAddress() {
        return formattedAddress == null ? "" : formattedAddress;
    }

    public VMHolderRecyclerVenue setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
        notifyPropertyChanged(BR.formattedAddress);
        return this;
    }

    @Bindable
    public String getImageUrl() {
        return imageUrl == null ? "" : imageUrl;
    }

    public VMHolderRecyclerVenue setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        notifyPropertyChanged(BR.imageUrl);
        return this;
    }
}
