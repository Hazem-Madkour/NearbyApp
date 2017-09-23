package com.cognitev.nearbyapp.UI.MainActivity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.cognitev.nearbyapp.Interfaces.IChangeLocationProviderChange;
import com.cognitev.nearbyapp.R;
import com.cognitev.nearbyapp.UI.MainActivity.ViewModels.VMMainActivity;
import com.cognitev.nearbyapp.Utilities.LocationListenerHelper;
import com.cognitev.nearbyapp.Utilities.Storage;
import com.cognitev.nearbyapp.Utilities.UtilityDatabase;
import com.cognitev.nearbyapp.Utilities.UtilityGeneral;
import com.cognitev.nearbyapp.databinding.ActivityMainBinding;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        IChangeLocationProviderChange, SharedPreferences.OnSharedPreferenceChangeListener {

    //region Member variables
    private Observable<String> serverDownloadObservable;
    private RecyclerView mRecyclerView;
    private AdapterRecycleVenues adapter;
    private VMMainActivity viewModel;
    private CompositeDisposable mCompositeDisposable;
    private LocationListenerHelper mLocationListenerHelper;
    private BroadcastReceiver mReceiver;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new VMMainActivity(Storage.getInstance(getBaseContext()).isRealTime());
        binding.setViewModel(viewModel);
        initViews();
        registerListeners();
        connectLocationListeners();
        checkGPS();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.num_of_grid_columns));
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
    }

    private void registerListeners() {
        mCompositeDisposable = new CompositeDisposable();
        serverDownloadObservable = UtilityGeneral.downloadVenuesObservable(getBaseContext());
        getSupportLoaderManager().initLoader(0, null, this);
        mLocationListenerHelper = new LocationListenerHelper(getBaseContext(), UtilityGeneral.LOCATION_LISTENER_INTERVAL, UtilityGeneral.LOCATION_LISTENER_MIN_DISTANCE, this);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (UtilityGeneral.isOnline(getBaseContext()))
                    downloadVenues();
                else if (Storage.getInstance(context).isRealTime())
                    viewModel.setMessage(getString(R.string.warn_no_internet));
            }
        };
        registerReceiver(mReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    public void unregisterListeners() {
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        if (mLocationListenerHelper != null)
            mLocationListenerHelper.disconnect();
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed())
            mCompositeDisposable.dispose();
        if (mReceiver != null)
            unregisterReceiver(mReceiver);
    }

    private void connectLocationListeners() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
        mLocationListenerHelper.connect();
    }

    private void downloadVenues() {
        checkGPS();
        Disposable subscribe = serverDownloadObservable.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                viewModel.setLoading(false);
                viewModel.setMessage(s);
            }
        });
        mCompositeDisposable.add(subscribe);
    }

    private void checkGPS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        UtilityGeneral.showLocationSettingsRequest(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(Storage.LAST_LOCATION_KEY)) {
            downloadVenues();
            if (!Storage.getInstance(getBaseContext()).isRealTime())
                if (mLocationListenerHelper != null)
                    mLocationListenerHelper.disconnect();
        } else if (key.equals(Storage.APP_STATUS_KEY)) {
            if (Storage.getInstance(getBaseContext()).isRealTime()) {
                if (mLocationListenerHelper != null) {
                    mLocationListenerHelper.disconnect();
                    mLocationListenerHelper.connect();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            connectLocationListeners();
        } else {
            viewModel.setMessage(getString(R.string.warn_something_went_wrong));
        }
    }

    @Override
    public void onDestroy() {
        unregisterListeners();
        super.onDestroy();
    }

    //region Location methods
    @Override
    public void onProvidersDisabled() {
        viewModel.setMessage(getString(R.string.warn_something_went_wrong));
    }

    @Override
    public void onProviderEnabled() {
        downloadVenues();
    }
    //endregion

    //region Loader methods
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return UtilityDatabase.getVenuesCursorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter = new AdapterRecycleVenues(this, data);
        mRecyclerView.setAdapter(adapter);
        viewModel.setDataExist(adapter.mCursorAdapter.getCount() > 0);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.mCursorAdapter.swapCursor(null);
    }
    //endregion
}
