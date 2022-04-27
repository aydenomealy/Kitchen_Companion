package com.example.kitchencompanion.db;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class FridgeDAO_Impl implements FridgeDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Fridge> __insertionAdapterOfFridge;

  private final EntityDeletionOrUpdateAdapter<Fridge> __deletionAdapterOfFridge;

  private final EntityDeletionOrUpdateAdapter<Fridge> __updateAdapterOfFridge;

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  public FridgeDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFridge = new EntityInsertionAdapter<Fridge>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `fridge` (`rowid`,`item`,`category`,`quantity`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Fridge value) {
        stmt.bindLong(1, value.id);
        if (value.item == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.item);
        }
        if (value.category == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.category);
        }
        stmt.bindLong(4, value.quantity);
      }
    };
    this.__deletionAdapterOfFridge = new EntityDeletionOrUpdateAdapter<Fridge>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `fridge` WHERE `rowid` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Fridge value) {
        stmt.bindLong(1, value.id);
      }
    };
    this.__updateAdapterOfFridge = new EntityDeletionOrUpdateAdapter<Fridge>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `fridge` SET `rowid` = ?,`item` = ?,`category` = ?,`quantity` = ? WHERE `rowid` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Fridge value) {
        stmt.bindLong(1, value.id);
        if (value.item == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.item);
        }
        if (value.category == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.category);
        }
        stmt.bindLong(4, value.quantity);
        stmt.bindLong(5, value.id);
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM fridge WHERE rowid = ?";
        return _query;
      }
    };
  }

  @Override
  public void insert(final Fridge... fridges) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFridge.insert(fridges);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Fridge... item) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfFridge.handleMultiple(item);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Fridge... food) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfFridge.handleMultiple(food);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final int fridgeId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDelete.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, fridgeId);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDelete.release(_stmt);
    }
  }

  @Override
  public LiveData<List<Fridge>> getType(final String foodType) {
    final String _sql = "SELECT * FROM fridge WHERE category = ? ORDER BY category COLLATE NOCASE, rowid";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (foodType == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, foodType);
    }
    return __db.getInvalidationTracker().createLiveData(new String[]{"fridge"}, false, new Callable<List<Fridge>>() {
      @Override
      public List<Fridge> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "rowid");
          final int _cursorIndexOfItem = CursorUtil.getColumnIndexOrThrow(_cursor, "item");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final List<Fridge> _result = new ArrayList<Fridge>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Fridge _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpItem;
            _tmpItem = _cursor.getString(_cursorIndexOfItem);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            _item = new Fridge(_tmpId,_tmpItem,_tmpCategory,_tmpQuantity);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<Fridge>> getAll() {
    final String _sql = "SELECT * FROM fridge ORDER BY category COLLATE NOCASE, rowid";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"fridge"}, false, new Callable<List<Fridge>>() {
      @Override
      public List<Fridge> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "rowid");
          final int _cursorIndexOfItem = CursorUtil.getColumnIndexOrThrow(_cursor, "item");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final List<Fridge> _result = new ArrayList<Fridge>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Fridge _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpItem;
            _tmpItem = _cursor.getString(_cursorIndexOfItem);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            _item = new Fridge(_tmpId,_tmpItem,_tmpCategory,_tmpQuantity);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Fridge getById(final int foodId) {
    final String _sql = "SELECT * FROM fridge WHERE rowid = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, foodId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "rowid");
      final int _cursorIndexOfItem = CursorUtil.getColumnIndexOrThrow(_cursor, "item");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
      final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
      final Fridge _result;
      if(_cursor.moveToFirst()) {
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        final String _tmpItem;
        _tmpItem = _cursor.getString(_cursorIndexOfItem);
        final String _tmpCategory;
        _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        final int _tmpQuantity;
        _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
        _result = new Fridge(_tmpId,_tmpItem,_tmpCategory,_tmpQuantity);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
