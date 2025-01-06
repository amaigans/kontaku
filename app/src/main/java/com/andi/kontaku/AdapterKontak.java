package com.andi.kontaku;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class AdapterKontak extends BaseAdapter {

    private Context context;
    private ArrayList<KontakModel> contacts;
    private DatabaseHelper dbHelper;  // DatabaseHelper untuk akses database

    // Konstruktor adapter
    public AdapterKontak(Context context, ArrayList<KontakModel> contacts) {
        this.context = context;
        this.contacts = contacts;
        this.dbHelper = new DatabaseHelper(context);  // Inisialisasi DatabaseHelper
    }

    @Override
    public int getCount() {
        return contacts.size();  // Mengembalikan jumlah kontak
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);  // Mengembalikan objek ContactModel pada posisi tertentu
    }

    @Override
    public long getItemId(int position) {
        return contacts.get(position).getId();  // Mengembalikan ID kontak
    }

    /**
     * Memperbarui data kontak pada adapter.
     */
    public void setContacts(ArrayList<KontakModel> newContacts) {
        this.contacts = newContacts; // Perbarui data kontak
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_kontak, parent, false);
        }

        // Ambil data dari ContactModel
        KontakModel contact = contacts.get(position);

        // Mengambil referensi untuk CardView
        CardView cardView = convertView.findViewById(R.id.card_view);

        // Array warna
        int[] colors = {
                ContextCompat.getColor(context, R.color.color1),
                ContextCompat.getColor(context, R.color.color2),
                ContextCompat.getColor(context, R.color.color3),
                ContextCompat.getColor(context, R.color.color4),
                ContextCompat.getColor(context, R.color.color5)
        };

        // Tetapkan warna berdasarkan posisi
        cardView.setCardBackgroundColor(colors[position % colors.length]);

        // Set data ke TextView (Nama Kontak)
        TextView tvName = convertView.findViewById(R.id.tvNama);
        tvName.setText(contact.getName());

        // Set data ke TextView (Email Kontak)
        TextView tvEmail = convertView.findViewById(R.id.tvNumber);
        tvEmail.setText(contact.getPhone());

        // Tombol delete

        // Set click listener pada item untuk membuka detail
        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailKontak.class);
            intent.putExtra("contactId", contact.getId());
            context.startActivity(intent);  // Membuka halaman detail kontak
        });

        return convertView;  // Mengembalikan tampilan untuk item
    }
}
