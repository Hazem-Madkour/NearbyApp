package com.cognitev.nearbyapp.UI.MainActivity.ViewModels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.android.databinding.library.baseAdapters.BR;
import com.cognitev.nearbyapp.R;
import com.cognitev.nearbyapp.Utilities.Storage;

public class VMMainActivity extends BaseObservable {
    private String errorMessage;
    private boolean isLoading;
    private boolean isDataExist;
    private boolean isRealTime;

    public VMMainActivity(boolean realTime) {
        isLoading = true;
        this.isRealTime = realTime;
    }

    @BindingAdapter("errorString")
    public static void loadImage(ImageView view, String errorMessage) {
        if (errorMessage.equals(view.getContext().getString(R.string.warn_no_internet)))
            view.setImageResource(R.drawable.ic_network_off);
        else if (errorMessage.equals(view.getContext().getString(R.string.warn_no_data_found)))
            view.setImageResource(R.drawable.ic_error_outline);
        else
            view.setImageResource(R.drawable.ic_cloud_off);
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
        notifyChange();
    }

    public void setDataExist(boolean isDataExist) {
        this.isDataExist = isDataExist;
        notifyChange();
    }

    public void setMessage(String errorMessage) {
        isLoading = false;
        this.errorMessage = errorMessage;
        notifyChange();
    }

    public void setRealTime(boolean realTime) {
        isRealTime = realTime;
        notifyPropertyChanged(BR.appStatus);
    }

    public int getBigMessageVisibility() {
        return !isLoading && !isDataExist && errorMessage != null && !errorMessage.isEmpty() ? View.VISIBLE : View.GONE;
    }

    public int getSmallMessageVisibility() {
        return !isLoading && isDataExist && errorMessage != null && !errorMessage.isEmpty() ? View.VISIBLE : View.GONE;
    }

    public int getLoadingVisibility() {
        return isLoading && !isDataExist ? View.VISIBLE : View.GONE;
    }

    public int getRecyclerVisibility() {
        return isDataExist ? View.VISIBLE : View.GONE;
    }

    public String getErrorMessage() {
        return errorMessage == null ? "" : errorMessage;
    }

    @Bindable
    public String getAppStatus() {
        return isRealTime ? "Realtime" : "Single Update";
    }

    public void clickRealTime(View view) {
        this.setRealTime(Storage.getInstance(view.getContext()).changeAppStatus());
    }
}
