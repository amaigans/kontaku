package com.andi.kontaku;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listViewContacts;
    private ArrayList<KontakModel> contactList;
    private AdapterKontak adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_kontak);

        listViewContacts = findViewById(R.id.rvKontak);  // Perubahan ID menjadi rvContacts
        dbHelper = new DatabaseHelper(this);

        // Ambil data dari SQLite
        contactList = dbHelper.getAllContacts();

        // Set adapter ke ListView
        adapter = new AdapterKontak(this, contactList);
        listViewContacts.setAdapter(adapter);

        FloatingActionButton fabAddContact = findViewById(R.id.fabAddNote); // Referensi ke FAB

        fabAddContact.setOnClickListener(v -> {
            // Buat kontak baru dengan nama default
            String newName = "Nama Baru";
            String newEmail = "";
            String newAddress = "";
            String newGender = "";
            String newPhone = "";

            // Simpan kontak baru ke database
            dbHelper.insertContact(newName, newEmail, newAddress, newGender, newPhone);

            // Ambil kontak terakhir untuk mendapatkan ID-nya
            contactList = dbHelper.getAllContacts();
            KontakModel lastContact = contactList.get(contactList.size() - 1);

            // Perbarui adapter
            adapter.notifyDataSetChanged();

            // Buka halaman ContactDetailActivity dengan ID kontak baru
            Intent intent = new Intent(this, DetailKontak.class);
            intent.putExtra("contactId", lastContact.getId());
            startActivity(intent);

            Toast.makeText(this, "Kontak Baru Ditambahkan", Toast.LENGTH_SHORT).show();
        });

        // Set item click listener untuk membuka halaman detail kontak
        listViewContacts.setOnItemClickListener((parent, view, position, id) -> {
            KontakModel selectedContact = contactList.get(position);

            // Buka halaman detail kontak dengan ID yang dipilih
            Intent intent = new Intent(this, DetailKontak.class);
            intent.putExtra("contactId", selectedContact.getId());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Muat ulang data kontak dari database
        contactList = dbHelper.getAllContacts();

        // Perbarui adapter
        adapter.setContacts(contactList); // Pastikan ada metode setContacts di adapter
        adapter.notifyDataSetChanged();
    }
}
