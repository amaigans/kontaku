package com.andi.kontaku;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nama database dan tabel
    private static final String DATABASE_NAME = "kontaku.db";  // Menggunakan nama database "kontaku"
    private static final int DATABASE_VERSION = 1;  // Versi database
    private static final String TABLE_NAME = "contacts";  // Nama tabel baru untuk kontak
    private static final String COLUMN_ID = "id";  // Kolom ID
    private static final String COLUMN_NAME = "nama";  // Kolom nama
    private static final String COLUMN_EMAIL = "email";  // Kolom email
    private static final String COLUMN_ADDRESS = "alamat";  // Kolom alamat
    private static final String COLUMN_GENDER = "jenis_kelamin";  // Kolom jenis kelamin
    private static final String COLUMN_PHONE = "nomor_telepon";  // Kolom nomor telepon

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Membuat tabel
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_ADDRESS + " TEXT, " +
                COLUMN_GENDER + " TEXT, " +
                COLUMN_PHONE + " TEXT)";  // Kolom-kolom kontak
        db.execSQL(createTable);
    }

    // Upgrade database (untuk menambah kolom baru jika diperlukan)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {  // Jika versi sebelumnya lebih rendah dari 2
            // Tidak ada perubahan di versi 2, hanya menambahkan kolom di versi lebih tinggi jika diperlukan
        }
    }

    // Menambahkan data kontak
    public void insertContact(String nama, String email, String alamat, String jenisKelamin, String nomorTelepon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, nama);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_ADDRESS, alamat);
        values.put(COLUMN_GENDER, jenisKelamin);
        values.put(COLUMN_PHONE, nomorTelepon);  // Menyimpan semua data kontak
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Mengambil semua data kontak
    public ArrayList<KontakModel> getAllContacts() {
        ArrayList<KontakModel> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Mengambil semua kolom
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_EMAIL, COLUMN_ADDRESS, COLUMN_GENDER, COLUMN_PHONE},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
                String address = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS));
                String gender = cursor.getString(cursor.getColumnIndex(COLUMN_GENDER));
                String phone = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE));

                // Membuat objek ContactModel untuk menyimpan data
                KontakModel contact = new KontakModel(id, name, email, address, gender, phone);
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return contactList;
    }

    // Mengambil data kontak berdasarkan ID
    public KontakModel getContactById(int contactId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_EMAIL, COLUMN_ADDRESS, COLUMN_GENDER, COLUMN_PHONE},
                COLUMN_ID + "=?", new String[]{String.valueOf(contactId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
            String address = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS));
            String gender = cursor.getString(cursor.getColumnIndex(COLUMN_GENDER));
            String phone = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE));
            cursor.close();
            db.close();
            return new KontakModel(id, name, email, address, gender, phone);  // Mengembalikan data dalam bentuk ContactModel
        }

        cursor.close();
        db.close();
        return null;
    }

    // Menghapus kontak berdasarkan ID
    public void deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Memperbarui data kontak berdasarkan ID
    public void updateContact(int id, String nama, String email, String alamat, String jenisKelamin, String nomorTelepon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, nama);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_ADDRESS, alamat);
        values.put(COLUMN_GENDER, jenisKelamin);
        values.put(COLUMN_PHONE, nomorTelepon);

        // Update berdasarkan ID
        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
