package com.techive.mydailygoodscustomer.Database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.techive.mydailygoodscustomer.Database.Entities.CashFreeOrderEntity;

@Dao
public interface CashFreeOrderDao {

    @Insert
    void insertCashFreeOrder(CashFreeOrderEntity cashFreeOrderEntity);

    @Query("DELETE FROM cashfree_table WHERE order_id = :orderId")
    void removeCashFreeOrder(String orderId);

}
