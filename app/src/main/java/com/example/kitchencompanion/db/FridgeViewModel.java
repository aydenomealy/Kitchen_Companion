package com.example.kitchencompanion.db;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FridgeViewModel extends AndroidViewModel {
    private LiveData<List<Fridge>> fridges;

    public FridgeViewModel(@NonNull Application application) {
        super(application);
        fridges = FridgeDatabase.getDatabase(getApplication()).fridgeDAO().getAll();
    }

    public void filterFridge(String category) {
        fridges = FridgeDatabase.getDatabase(getApplication()).fridgeDAO().getType(category);
    }

    public LiveData<List<Fridge>> getAllFridge() {
        return fridges;
    }
}
