package com.cognitev.nearbyapp.UI.MainActivity;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cognitev.nearbyapp.UI.MainActivity.ViewModels.VMHolderRecyclerVenue;
import com.cognitev.nearbyapp.databinding.ItemRecyclerVenuesBinding;

public class HolderRecycleVenue extends RecyclerView.ViewHolder {

    public ItemRecyclerVenuesBinding binding;

    public HolderRecycleVenue(View itemView, VMHolderRecyclerVenue viewModel) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
        binding.setVenue(viewModel);
    }
}
