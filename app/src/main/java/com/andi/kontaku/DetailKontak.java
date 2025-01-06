package com.andi.kontaku;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetailKontak extends AppCompatActivity {

    private EditText etName, etEmail, etAddress, etGender, etPhone;
    private int contactId;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_kontak);  // Ganti dengan layout yang sesuai

        // Inisialisasi EditText
        etName = findViewById(R.id.editTextNama);
        etEmail = findViewById(R.id.editTextEmail);
        etAddress = findViewById(R.id.editTextAlamat);
        etGender = findViewById(R.id.editTextJenisKelamin);
        etPhone = findViewById(R.id.editTextNomorTelepon);
        Button fabSaveContact = findViewById(R.id.buttonSimpan);
        Button DeleteKontak = findViewById(R.id.buttonHapus);


        dbHelper = new DatabaseHelper(this);

        // Ambil ID kontak dari Intent
        contactId = getIntent().getIntExtra("contactId", -1);
        if (contactId != -1) {
            KontakModel contact = dbHelper.getContactById(contactId);
            if (contact != null) {
                // Tampilkan data kontak yang ada
                etName.setText(contact.getName());
                etEmail.setText(contact.getEmail());
                etAddress.setText(contact.getAddress());
                etGender.setText(contact.getGender());
                etPhone.setText(contact.getPhone());
            }
        }

        // Simpan perubahan kontak ketika tombol save di klik
        fabSaveContact.setOnClickListener(v -> {
            String newName = etName.getText().toString().trim();
            String newEmail = etEmail.getText().toString().trim();
            String newAddress = etAddress.getText().toString().trim();
            String newGender = etGender.getText().toString().trim();
            String newPhone = etPhone.getText().toString().trim();

            if (!newName.isEmpty() && !newEmail.isEmpty() && !newAddress.isEmpty() &&
                    !newGender.isEmpty() && !newPhone.isEmpty()) {

                // Perbarui kontak dalam database
                dbHelper.updateContact(contactId, newName, newEmail, newAddress, newGender, newPhone);

                // Kirim hasil kembali ke MainActivity atau activity sebelumnya
                Intent resultIntent = new Intent();
                resultIntent.putExtra("contact_updated", true);
                setResult(RESULT_OK, resultIntent);

                Toast.makeText(this, "Kontak berhasil diperbarui!", Toast.LENGTH_SHORT).show();
                finish(); // Kembali ke activity sebelumnya
            } else {
                Toast.makeText(this, "Semua kolom harus diisi!", Toast.LENGTH_SHORT).show();
            }
        });

        DeleteKontak.setOnClickListener(v -> {
            dbHelper.deleteContact(contactId);  // Menghapus kontak dari database
            Toast.makeText(this, "Kontak berhasil Dihapus!", Toast.LENGTH_SHORT).show();
            finish(); // Kembali ke activity sebelumnya
        });
    }
}
