package ch.uifz725.beintime.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Location {

    @PrimaryKey (autoGenerate = true)
    private int id;

    private String name;
    private Float radius;
    private String information;
    private String position;
}
