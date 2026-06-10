package com.example.utsprofile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Kriteria: ProfilActivity - Kode Program Lengkap
 */
public class ProfilActivity extends AppCompatActivity {

    private ImageView imgProfile;
    private Button btnChangeImage, btnSave;
    private EditText etFullName, etBirthPlace, etBirthDate, etHobby, etBio;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        setTitle("Profil Pengguna");

        // Bind Views menggunakan ID baru
        imgProfile = findViewById(R.id.id_imgProfile);
        btnChangeImage = findViewById(R.id.id_btnChangeImage);
        btnSave = findViewById(R.id.id_btnSave);
        etFullName = findViewById(R.id.id_etFullName);
        etBirthPlace = findViewById(R.id.id_etBirthPlace);
        etBirthDate = findViewById(R.id.id_etBirthDate);
        etHobby = findViewById(R.id.id_etHobby);
        etBio = findViewById(R.id.id_etBio);

        SharedPreferences sp = getSharedPreferences("PROFILE", MODE_PRIVATE);

        // Load existing data
        loadProfileData(sp);

        // Image Picker Setup
        ActivityResultLauncher<String> pickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedImageUri = uri;
                        try {
                            getContentResolver().takePersistableUriPermission(uri,
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } catch (Exception ignored) {}
                        imgProfile.setImageURI(uri);
                    }
                }
        );

        btnChangeImage.setOnClickListener(v -> pickerLauncher.launch("image/*"));

        btnSave.setOnClickListener(v -> saveProfileData(sp));
    }

    private void loadProfileData(SharedPreferences sp) {
        String savedImageUri = sp.getString("IMAGE_URI", "");
        if (!savedImageUri.isEmpty()) {
            try {
                imgProfile.setImageURI(Uri.parse(savedImageUri));
            } catch (Exception e) {
                imgProfile.setImageResource(R.mipmap.ic_launcher_round);
            }
        }

        etFullName.setText(sp.getString("FULL_NAME", ""));
        etBirthPlace.setText(sp.getString("BIRTH_PLACE", ""));
        etBirthDate.setText(sp.getString("BIRTH_DATE", ""));
        etHobby.setText(sp.getString("HOBBY", ""));
        etBio.setText(sp.getString("BIO", ""));
    }

    private void saveProfileData(SharedPreferences sp) {
        String fullName = etFullName.getText().toString().trim();
        String birthPlace = etBirthPlace.getText().toString().trim();
        String birthDate = etBirthDate.getText().toString().trim();
        String hobby = etHobby.getText().toString().trim();
        String bio = etBio.getText().toString().trim();

        // Validasi: Cek jika ada field yang kosong
        if (fullName.isEmpty() || birthPlace.isEmpty() || birthDate.isEmpty() || hobby.isEmpty() || bio.isEmpty()) {
            Toast.makeText(this, "Silakan isi semua field!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = sp.edit();
        
        if (selectedImageUri != null) {
            editor.putString("IMAGE_URI", selectedImageUri.toString());
        }

        editor.putString("FULL_NAME", fullName);
        editor.putString("BIRTH_PLACE", birthPlace);
        editor.putString("BIRTH_DATE", birthDate);
        editor.putString("HOBBY", hobby);
        editor.putString("BIO", bio);
        
        if (editor.commit()) {
            Toast.makeText(this, "Profil Berhasil Diperbarui!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
