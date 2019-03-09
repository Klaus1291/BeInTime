package ch.uifz725.beintime.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Action.class}, version = 1, exportSchema = false)
public abstract class CreateDatabase extends RoomDatabase {

    public abstract ActionDao actionDao();
}