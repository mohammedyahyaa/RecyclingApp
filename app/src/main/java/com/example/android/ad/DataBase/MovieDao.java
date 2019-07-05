package com.example.android.ad.DataBase;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.graphics.Movie;

import com.example.android.ad.model.adv_data;


@Dao
public interface MovieDao {
    @Query("SELECT * FROM ad ")
    LiveData<adv_data[]> loadAllAds();


    @Query("select * from ad where `key` LIKE :key")
    adv_data selectAdByKey(String key);

    @Insert
    void insertAd(adv_data mAd);


    @Delete
    void deleteAd(adv_data mAd);


}
