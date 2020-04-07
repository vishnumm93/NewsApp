package com.vishnu.newsapp.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vishnu.newsapp.Model.Articles;
import com.vishnu.newsapp.Model.Source;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "NewsApp.db";
    public static final String WISHLIST_TABLE_NAME = "wishlist";
    public static final String WISHLIST_COLUMN_ID = "id";
    public static final String WISHLIST_COLUMN_NAME = "name";
    public static final String WISHLIST_COLUMN_TITLE = "title";
    public static final String WISHLIST_COLUMN_DESCRIPTION = "description";
    public static final String WISHLIST_COLUMN_CONTENT = "content";
    public static final String WISHLIST_COLUMN_URL = "url";
    public static final String WISHLIST_COLUMN_URLTOIMAGE = "urlToImage";
    public static final String WISHLIST_COLUMN_AUTHOR = "author";
    public static final String WISHLIST_COLUMN_PUBLISHEDAT = "publishedAt";
    public SQLiteDatabase db;


    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String sql = "CREATE TABLE IF NOT EXISTS " +WISHLIST_TABLE_NAME+
                " ("+WISHLIST_COLUMN_ID +" text, "+WISHLIST_COLUMN_NAME+" text, "+WISHLIST_COLUMN_TITLE+" text primary key, " +
                WISHLIST_COLUMN_DESCRIPTION+" text, "+WISHLIST_COLUMN_CONTENT+" text, "+WISHLIST_COLUMN_URL+" text, "+WISHLIST_COLUMN_URLTOIMAGE+" text, "+WISHLIST_COLUMN_AUTHOR+" text, "+WISHLIST_COLUMN_PUBLISHEDAT+" text )";


        Log.d("dblog",sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS wishlist");
        onCreate(db);
    }

    public boolean insertWishlist (Source source, String title, String description, String author, String publishedAt,String content, String url, String urlToImage) {
        SQLiteDatabase db = this.getWritableDatabase();

        String id = source.getSourceId();
        String name = source.getSourceName();

        ContentValues contentValues = new ContentValues();
        contentValues.put(WISHLIST_COLUMN_ID, id);
        contentValues.put(WISHLIST_COLUMN_NAME, name);
        contentValues.put(WISHLIST_COLUMN_TITLE , title);
        contentValues.put(WISHLIST_COLUMN_DESCRIPTION, description);
        contentValues.put(WISHLIST_COLUMN_AUTHOR, author);
        contentValues.put(WISHLIST_COLUMN_CONTENT, content);
        contentValues.put(WISHLIST_COLUMN_PUBLISHEDAT, publishedAt);
        contentValues.put(WISHLIST_COLUMN_URL, url);
        contentValues.put(WISHLIST_COLUMN_URLTOIMAGE, urlToImage);

        long val = db.insert(WISHLIST_TABLE_NAME, null, contentValues);
        Log.d("dbcheck", "-- "+val);
        return true;
    }


    public ArrayList<Articles> getWishList() {
        ArrayList<Articles> wishList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+WISHLIST_TABLE_NAME, null );
        res.moveToFirst();

        if(res.getCount() > 0) {
            while (res.isAfterLast() == false) {

                String id = res.getString(res.getColumnIndex(WISHLIST_COLUMN_ID));

                Source source = new Source(res.getString(res.getColumnIndex(WISHLIST_COLUMN_ID)),res.getString(res.getColumnIndex(WISHLIST_COLUMN_NAME)));

                Articles articles = new Articles();

                articles.setTitle(res.getString(res.getColumnIndex(WISHLIST_COLUMN_TITLE)));
                articles.setDescription(res.getString(res.getColumnIndex(WISHLIST_COLUMN_DESCRIPTION)));
                articles.setAuthor(res.getString(res.getColumnIndex(WISHLIST_COLUMN_AUTHOR)));
                articles.setContent(res.getString(res.getColumnIndex(WISHLIST_COLUMN_CONTENT)));
                articles.setSource(source);
                articles.setUrl(res.getString(res.getColumnIndex(WISHLIST_COLUMN_URL)));
                articles.setUrlToImage(res.getString(res.getColumnIndex(WISHLIST_COLUMN_URLTOIMAGE)));
                articles.setPublishedAt(res.getString(res.getColumnIndex(WISHLIST_COLUMN_PUBLISHEDAT)));
                res.moveToNext();
            }
        }
        return wishList;
    }
}