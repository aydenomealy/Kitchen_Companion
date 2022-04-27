package com.example.kitchencompanion.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="fridge")
public class Fridge {

    public Fridge(int id, String item, String category, int quantity) {
        this.id = id;
        this.item = item;
        this.category = category;
        this.quantity = quantity;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid")
    public int id;

    @ColumnInfo(name = "item")
    public String item;

    @ColumnInfo(name = "category")
    public String category;

    @ColumnInfo(name = "quantity")
    public int quantity;
}
