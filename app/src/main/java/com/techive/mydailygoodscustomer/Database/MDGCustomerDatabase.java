package com.techive.mydailygoodscustomer.Database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.techive.mydailygoodscustomer.Database.Dao.CashFreeOrderDao;
import com.techive.mydailygoodscustomer.Database.Entities.CashFreeOrderEntity;

@Database(entities = {CashFreeOrderEntity.class}, version = 1)
public abstract class MDGCustomerDatabase extends RoomDatabase {

    private static MDGCustomerDatabase mdgCustomerDatabaseInstance;

    /*DAO*/
    public abstract CashFreeOrderDao cashFreeOrderDao();

    public static synchronized MDGCustomerDatabase getInstance(Context context) {
        if (mdgCustomerDatabaseInstance == null) {
            mdgCustomerDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(), MDGCustomerDatabase.class, "mdg_customer_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return mdgCustomerDatabaseInstance;
    }
}
