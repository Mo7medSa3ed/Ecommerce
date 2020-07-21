package com.mohamedsaeed555.MyDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.lang.UScript;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    public static final String dbname = "Data";

    public Database(@Nullable Context context) {
        super(context, dbname, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table cosmatics ( id INTEGER PRIMARY KEY AUTOINCREMENT ,date TEXT , amount INTEGER , barcode TEXT ,name TEXT , price DOUBLE , brand TEXT , image TEXT )");
        db.execSQL("create table medical ( id INTEGER PRIMARY KEY AUTOINCREMENT ,date TEXT , amount INTEGER , barcode TEXT ,name TEXT , price DOUBLE , brand TEXT , image TEXT )");
        db.execSQL("create table makeup ( id INTEGER PRIMARY KEY AUTOINCREMENT ,date TEXT , amount INTEGER , barcode TEXT ,name TEXT , price DOUBLE , brand TEXT , image TEXT )");
        db.execSQL("create table papers ( id INTEGER PRIMARY KEY AUTOINCREMENT ,date TEXT , amount INTEGER , barcode TEXT ,name TEXT , price DOUBLE , brand TEXT , image TEXT )");
        db.execSQL("create table others ( id INTEGER PRIMARY KEY AUTOINCREMENT ,date TEXT , amount INTEGER , barcode TEXT ,name TEXT , price DOUBLE , brand TEXT , image TEXT )");
        db.execSQL("create table AllData ( id INTEGER PRIMARY KEY AUTOINCREMENT ,date TEXT , amount INTEGER , barcode TEXT ,name TEXT , price DOUBLE , brand TEXT , image TEXT , collection TEXT )");
        db.execSQL("create table Cart ( id INTEGER PRIMARY KEY AUTOINCREMENT ,date TEXT , amount INTEGER , barcode TEXT ,name TEXT , price DOUBLE , brand TEXT , image TEXT )");
        db.execSQL("create table Products ( id INTEGER PRIMARY KEY AUTOINCREMENT ,date TEXT , amount INTEGER , barcode TEXT ,name TEXT , price DOUBLE , brand TEXT , image TEXT , collection TEXT )");
        db.execSQL("create table Users (id INTEGER PRIMARY KEY AUTOINCREMENT , name TEXT , tel TEXT , address TEXT , image TEXT , email TEXT , password TEXT , city TEXT , fbid TEXT , goid TEXT ,admin TEXT , superAdmin TEXT , _id TEXT ,token TEXT)");
        db.execSQL("create table Favourite (id INTEGER PRIMARY KEY AUTOINCREMENT , Fav TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS cosmatics");
        db.execSQL("DROP TABLE IF EXISTS medical");
        db.execSQL("DROP TABLE IF EXISTS makeup");
        db.execSQL("DROP TABLE IF EXISTS papers");
        db.execSQL("DROP TABLE IF EXISTS others");
        db.execSQL("DROP TABLE IF EXISTS AllData");
        db.execSQL("DROP TABLE IF EXISTS Cart");
        db.execSQL("DROP TABLE IF EXISTS Products");
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Favourite");
        onCreate(db);
    }

    /*public  Boolean insert_product(String table_name , Product_class product_class){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date",product_class.getDate());
        contentValues.put("amount",product_class.getAmount());
        contentValues.put("barcode",product_class.getBarcode());
        contentValues.put("name",product_class.getName());
        contentValues.put("price",product_class.getPrice());
        contentValues.put("brand",product_class.getBrand());
        contentValues.put("image",product_class.getImage());
        long result = db.insert(table_name,null,contentValues);
        if (result== -1)
            return false;
        else
            return true;
    }*/

    public  Boolean insert_product_tocart (String table_name , Product_class product_class){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date",product_class.getDate());
        contentValues.put("amount",product_class.getAmount());
        contentValues.put("barcode",product_class.getBarcode());
        contentValues.put("name",product_class.getName());
        contentValues.put("price",product_class.getPrice());
        contentValues.put("brand",product_class.getBrand());
        contentValues.put("image",product_class.getImage());
        long result = db.insert(table_name,null,contentValues);
        if (result== -1)
            return false;
        else
            return true;
    }

    public  Boolean insert_product_toAlldata (String table_name , Product_class product_class){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date",product_class.getDate());
        contentValues.put("amount",product_class.getAmount());
        contentValues.put("barcode",product_class.getBarcode());
        contentValues.put("name",product_class.getName());
        contentValues.put("price",product_class.getPrice());
        contentValues.put("brand",product_class.getBrand());
        contentValues.put("image",product_class.getImage());
        contentValues.put("collection",product_class.getCollection());
        long result = db.insert(table_name,null,contentValues);
        if (result== -1)
            return false;
        else
            return true;
    }


/*
    public  Boolean update_product (String table_name , Product_class product_class,String barcode){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date",product_class.getDate());
        contentValues.put("amount",product_class.getAmount());
        contentValues.put("barcode",product_class.getBarcode());
        contentValues.put("name",product_class.getName());
        contentValues.put("price",product_class.getPrice());
        contentValues.put("brand",product_class.getBrand());
        contentValues.put("image",product_class.getImage());
        int result = db.update(table_name,contentValues,"barcode=?",new String[] {barcode});
        if (result== -1)
            return false;
        else
            return true;
    }
*/

    public  Boolean update_product3 (String table_name , int amount,String barcode){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("amount",amount);
        int result = db.update(table_name,contentValues,"barcode=?",new String[] {barcode});
        if (result== -1)
            return false;
        else
            return true;
    }


    public  Boolean update_product2 (String table_name , Product_class product_class,String barcode){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date",product_class.getDate());
        contentValues.put("amount",product_class.getAmount());
        contentValues.put("barcode",product_class.getBarcode());
        contentValues.put("name",product_class.getName());
        contentValues.put("price",product_class.getPrice());
        contentValues.put("brand",product_class.getBrand());
        contentValues.put("image",product_class.getImage());
        contentValues.put("collection",product_class.getCollection());
        int result = db.update(table_name,contentValues,"barcode=?",new String[] {barcode});
        if (result== -1)
            return false;
        else
            return true;
    }

    public ArrayList<Product_class> getAllProducts(String table_name){
        ArrayList<Product_class> arrayList = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+table_name,null);
        cursor.moveToFirst();
        while (cursor.isAfterLast()==false){
            String t1 = cursor.getString(0);
            String t2 = cursor.getString(1);
            int t3 = cursor.getInt(2);
            String t4 = cursor.getString(3);
            String t5 = cursor.getString(4);
            double t6 = cursor.getDouble(5);
            String t7 = cursor.getString(6);
            String t8 = cursor.getString(7);
            Product_class productClass = new Product_class(t2,t3,t4,t5,t6,t7,t8);
            arrayList.add(productClass);
            cursor.moveToNext();
        }
        return arrayList;
    }

    public ArrayList<Product_class> getAllProducts2(String table_name){
        ArrayList<Product_class> arrayList = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+table_name,null);
        cursor.moveToFirst();
        while (cursor.isAfterLast()==false){
            String t1 = cursor.getString(0);
            String t2 = cursor.getString(1);
            int t3 = cursor.getInt(2);
            String t4 = cursor.getString(3);
            String t5 = cursor.getString(4);
            double t6 = cursor.getDouble(5);
            String t7 = cursor.getString(6);
            String t8 = cursor.getString(7);
            String t9 = cursor.getString(8);
            Product_class productClass = new Product_class(t2,t3,t4,t5,t6,t7,t8,t9);
            arrayList.add(productClass);
            cursor.moveToNext();
        }
        return arrayList;
    }

    public ArrayList<Product_class> Search_product(String table_name , String barcode2){
        ArrayList<Product_class> arrayList = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+table_name + " WHERE barcode ='"+barcode2+"'",null);
        cursor.moveToFirst();
        while (cursor.isAfterLast()==false){
            String t4 = cursor.getString(3);
            String t5 = cursor.getString(4);
            double t6 = cursor.getDouble(5);
            String t8 = cursor.getString(7);
            Product_class productClass = new Product_class(t4,t5,t6,t8);
            arrayList.add(productClass);
            cursor.moveToNext();
        }
        return arrayList;
    }

    public ArrayList<Product_class> Search_product2(String table_name , String barcode2){
        ArrayList<Product_class> arrayList = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+table_name + " WHERE barcode ='"+barcode2+"'",null);
        cursor.moveToFirst();
        while (cursor.isAfterLast()==false){
            String t1 = cursor.getString(0);
            String t2 = cursor.getString(1);
            int t3 = cursor.getInt(2);
            String t4 = cursor.getString(3);
            String t5 = cursor.getString(4);
            double t6 = cursor.getDouble(5);
            String t7 = cursor.getString(6);
            String t8 = cursor.getString(7);
            String t9 = cursor.getString(8);
            Product_class productClass = new Product_class(t2,t3,t4,t5,t6,t7,t8,t9);
            arrayList.add(productClass);
            cursor.moveToNext();
        }
        return arrayList;
    }


    public void Delete_All(String table_name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table_name, null, null);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + table_name + "'");
    }

    public void Delete_product(String table_name,String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table_name, "id=?",new String[]{id} );
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +table_name+ "'");
    }

    public void Delete_product2(String table_name,String barcode){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table_name, "barcode=?",new String[]{barcode} );
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +table_name+ "'");
    }

    public Boolean isEmpty(String table_name){
        boolean empty = true;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM "+table_name, null);
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getInt (0) == 0);
        }
        cur.close();

        return empty;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public  Boolean insert_user ( Users users,String token){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",users.getName());
        contentValues.put("tel",users.getTel());
        contentValues.put("address",users.getAdress());
        contentValues.put("image",users.getImage());
        contentValues.put("email",users.getEmail());
        contentValues.put("password",users.getPassword());
        contentValues.put("city",users.getCity());
        contentValues.put("fbid",users.getFb_id());
        contentValues.put("goid",users.getGo_id());
        contentValues.put("superAdmin",users.getSuperAdmin().toString());
        contentValues.put("admin",users.getAdmin().toString());
        contentValues.put("_id",users.get_id());
        contentValues.put("token",token);
        long result = db.insert("Users",null,contentValues);
        if (result== -1)
            return false;
        else
            return true;
    }
    public ArrayList<Users> getAllusers(){
        ArrayList<Users> arrayList = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from  Users",null);
        cursor.moveToFirst();
        while (cursor.isAfterLast()==false){
            String t1 = cursor.getString(0);
            String t2 = cursor.getString(1);
            String t3 = cursor.getString(2);
            String t4 = cursor.getString(3);
            String t5 = cursor.getString(4);
            String t6 = cursor.getString(5);
            String t7 = cursor.getString(6);
            String t8 = cursor.getString(7);
            String t9 = cursor.getString(8);
            String t10 = cursor.getString(9);
            String t11 = cursor.getString(10);
            String t12 = cursor.getString(11);
            String t13 = cursor.getString(12);
            String t14 = cursor.getString(13);
            Users users = new Users(t2,t3,t4,t5,t6,t7,t8,t9,t10,Boolean.parseBoolean(t11),Boolean.parseBoolean(t12),t13,t14);
            arrayList.add(users);
            cursor.moveToNext();
        }
        return arrayList;
    }
    /*public ArrayList<Users> Search_user_fb( String fbid){
        ArrayList<Users> arrayList = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Users" + " WHERE fbid ='"+fbid+"'",null);
        cursor.moveToFirst();
        while (cursor.isAfterLast()==false){
            String t1 = cursor.getString(0);
            String t2 = cursor.getString(1);
            String t3 = cursor.getString(2);
            String t4 = cursor.getString(3);
            String t5 = cursor.getString(4);
            String t6 = cursor.getString(5);
            String t7 = cursor.getString(6);
            String t8 = cursor.getString(7);
            String t9 = cursor.getString(8);
            String t10 = cursor.getString(9);
            Users users = new Users(t2,t3,t4,t5,t6,t7,t8,t9,t10);
            arrayList.add(users);
            cursor.moveToNext();
        }
        return arrayList;
    }
    public ArrayList<Users> Search_user_go( String goid){
        ArrayList<Users> arrayList = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Users" + " WHERE goid ='"+goid+"'",null);
        cursor.moveToFirst();
        while (cursor.isAfterLast()==false){
            String t1 = cursor.getString(0);
            String t2 = cursor.getString(1);
            String t3 = cursor.getString(2);
            String t4 = cursor.getString(3);
            String t5 = cursor.getString(4);
            String t6 = cursor.getString(5);
            String t7 = cursor.getString(6);
            String t8 = cursor.getString(7);
            String t9 = cursor.getString(8);
            String t10 = cursor.getString(9);
            Users users = new Users(t2,t3,t4,t5,t6,t7,t8,t9,t10);
            arrayList.add(users);
            cursor.moveToNext();
        }
        return arrayList;
    }
    public ArrayList<Users> Search_user_email( String email){
        ArrayList<Users> arrayList = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Users" + " WHERE email ='"+email+"'",null);
        cursor.moveToFirst();
        while (cursor.isAfterLast()==false){
            String t1 = cursor.getString(0);
            String t2 = cursor.getString(1);
            String t3 = cursor.getString(2);
            String t4 = cursor.getString(3);
            String t5 = cursor.getString(4);
            String t6 = cursor.getString(5);
            String t7 = cursor.getString(6);
            String t8 = cursor.getString(7);
            String t9 = cursor.getString(8);
            String t10 = cursor.getString(9);
            Users users = new Users(t2,t3,t4,t5,t6,t7,t8,t9,t10);
            arrayList.add(users);
            cursor.moveToNext();
        }
        return arrayList;
    }*/


////////////////////////////////////////////////////////////


    public  Boolean insert_fav ( String fav){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Fav",fav);
        long result = db.insert("Favourite",null,contentValues);
        if (result== -1)
            return false;
        else
            return true;
    }

    public ArrayList<String> Search_fav( String Fav){
        ArrayList<String> arrayList = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Favourite WHERE Fav ='"+Fav+"'",null);
        cursor.moveToFirst();
        while (cursor.isAfterLast()==false){
            String id = cursor.getString(0);
            String fav = cursor.getString(1);
            arrayList.add(fav);
            cursor.moveToNext();
        }
        return arrayList;
    }

    public ArrayList<String> GETALLFAV(){
        ArrayList<String> arrayList = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Favourite",null);
        cursor.moveToFirst();
        while (cursor.isAfterLast()==false){
            String id = cursor.getString(0);
            String fav = cursor.getString(1);
            arrayList.add(fav);
            cursor.moveToNext();
        }
        return arrayList;
    }




    public void Delete_Fav(String Fav){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Favourite", "Fav=?",new String[]{Fav} );
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +"Favourite"+ "'");
    }









}
