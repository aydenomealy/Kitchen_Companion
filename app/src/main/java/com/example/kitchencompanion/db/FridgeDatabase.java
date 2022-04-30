package com.example.kitchencompanion.db;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {Fridge.class}, version = 1, exportSchema = false)
public abstract class FridgeDatabase extends RoomDatabase {
    public interface FridgeListener {
        void onFridgeReturned(Fridge fridge);
    }

    public abstract FridgeDAO fridgeDAO();

    private static FridgeDatabase INSTANCE;

    public static FridgeDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FridgeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FridgeDatabase.class, "fridge_database")
                            .addCallback(createFridgeDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback createFridgeDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            createFridgeTable();
        }
    };

    private static void createFridgeTable() {
        for(int i = 0; i < DefaultContent.TITLE.length; i++) {
            insert(new Fridge(0, DefaultContent.TITLE[i], DefaultContent.TYPE[i], DefaultContent.QUANTITY[i]));
        }
    }

    public static void getFridge(int id, FridgeListener listener) {
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                listener.onFridgeReturned((Fridge) msg.obj);
            }
        };

        (new Thread(() -> {
            Message msg = handler.obtainMessage();
            msg.obj = INSTANCE.fridgeDAO().getById(id);
            handler.sendMessage(msg);
        })).start();
    }

    public static void insert(Fridge fridge) {
        (new Thread(() -> INSTANCE.fridgeDAO().insert(fridge))).start();
    }

    public static void delete(int fridgeId) {
        (new Thread(() -> INSTANCE.fridgeDAO().delete(fridgeId))).start();
    }
    public static void delete(Fridge fridge) {
        (new Thread(() -> INSTANCE.fridgeDAO().delete(fridge))).start();
    }

    public static void update(Fridge fridge) {
        (new Thread(() -> INSTANCE.fridgeDAO().update(fridge))).start();
    }
}
