package com.example.kitchencompanion.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FridgeDAO {
    @Query("SELECT * FROM fridge WHERE category = :foodType ORDER BY category COLLATE NOCASE, rowid")
    LiveData<List<Fridge>> getType(String foodType);

    @Query("SELECT * FROM fridge ORDER BY category COLLATE NOCASE, rowid")
    LiveData<List<Fridge>> getAll();

    @Query("SELECT * FROM fridge WHERE rowid = :foodId")
    Fridge getById(int foodId);

    @Insert
    void insert(Fridge... fridges);

    @Update
    void update(Fridge... food);

    @Delete
    void delete(Fridge... item);

    @Query("DELETE FROM fridge WHERE rowid = :fridgeId")
    void delete(int fridgeId);
}
