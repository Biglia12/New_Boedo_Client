package com.example.androidfood.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.androidfood.Model.Order;
import com.google.firestore.v1beta1.StructuredQuery;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "AndroidFoodDB.db";
    private static final int DB_VER = 2;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }




    public List<Order> getCarts() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ID","ProductoNombre", "ProductoId", "Cantidad", "Precio", "Descuento","Imagen"};
        String sqlTable = "OrderDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null,null, null, null, null);

        final List<Order> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                result.add(new Order(
                        c.getInt(c.getColumnIndex("ID")),
                        c.getString(c.getColumnIndex("ProductoId")),
                        c.getString(c.getColumnIndex("ProductoNombre")),
                        c.getString(c.getColumnIndex("Cantidad")),
                        c.getString(c.getColumnIndex("Precio")),
                        c.getString(c.getColumnIndex("Descuento")),
                        c.getString(c.getColumnIndex("Imagen"))
                ));
            } while (c.moveToNext());
        }
        return result;
    }


    public void addToCart(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail (ProductoId,ProductoNombre,Cantidad,Precio,Descuento,Imagen)VALUES('%s','%s','%s','%s','%s','%s');",
                order.getProductoId(),
                order.getProductoNombre(),
                order.getCantidad(),
                order.getPrecio(),
                order.getDescuento(),
                order.getImagen());
        db.execSQL(query);

    }

    public void cleanCart()
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        db.execSQL(query);

    }

    public int getCountCart() {
        int count=0;

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT COUNT(*) FROM OrderDetail");
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst())
        {
            do {
                count=cursor.getInt(0);
            }while (cursor.moveToNext());
        }
        return count;
    }

    public void updateCart(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("UPDATE OrderDetail SET Cantidad= %s WHERE ID = %d",order.getCantidad(),order.getID());
        db.execSQL(query);
    }

    public void remoFormCart(String productId, String phone) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        db.execSQL(query);

    }
}


