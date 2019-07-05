package com.example.android.ad.DataBase;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.ad.model.adv_data;

public class MainViewModel extends AndroidViewModel {

    LiveData<adv_data[]> Ads;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Holder mDb = Holder.getInstance(this.getApplication());
        Ads = mDb.movieDao().loadAllAds();
    }

    public LiveData<adv_data[]> getAds() {
        return Ads;
    }
}
