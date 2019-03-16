package ch.uifz725.beintime.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface LocationDao {

    @Insert
    void insertLocation (Location location);

    @Query("Select * FROM `Location`")
    List<Location> getAllLocation();

}
