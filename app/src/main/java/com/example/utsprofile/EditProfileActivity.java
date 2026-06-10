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

public class EditProfileActivity extends AppCompatActivity {

    ImageView imgProfile;
    Button btnChangeImage, btnSave;
    EditText etFullName, etBirthPlace, etBirthDate, etHobby, etBio;

    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setTitle("Edit Profile");

        // Bind Views
        imgProfile = findViewById(R.id.imgProfile);
        btnChangeImage = findViewById(R.id.btnChangeImage);
        btnSave = findViewById(R.id.btnSave);
        etFullName = findViewById(R.id.etFullName);
        etBirthPlace = findViewById(R.id.etBirthPlace);
        etBirthDate = findViewById(R.id.etBirthDate);
        etHobby = findViewById(R.id.etHobby);
        etBio = findViewById(R.id.etBio);

        SharedPreferences sp = getSharedPreferences("PROFILE", MODE_PRIVATE);

        // 1. Load existing data into form
        String savedImageUri = sp.getString("IMAGE_URI", "");
        if (!savedImageUri.isEmpty()) {
            try {
                imgProfile.setImageURI(Uri.parse(savedImageUri));
            } catch (Exception e) {
                imgProfile.setImageResource(R.mipmap.ic_launcher_round);
            }
        }

        etFullName.setText(sp.getString("FULL_NAME", "Kelvin Chen"));
        etBirthPlace.setText(sp.getString("BIRTH_PLACE", "Medan"));
        etBirthDate.setText(sp.getString("BIRTH_DATE", "8/6/1996"));
        etHobby.setText(sp.getString("HOBBY", "Programming, Desain Web"));
        etBio.setText(sp.getString("BIO", "Full Stack Developer"));

        // 2. Setup Image Picker with Persistent Permission
        ActivityResultLauncher<String> pickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedImageUri = uri;
                        try {
                            // Important: Request persistent permission so URI works after reboot/restart
                            getContentResolver().takePersistableUriPermission(uri,
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } catch (Exception e) {
                            // Some providers don't support persistable permissions
                        }
                        imgProfile.setImageURI(uri);
                    }
                }
        );

        btnChangeImage.setOnClickListener(v -> pickerLauncher.launch("image/*"));

        // 3. Save Logic
        btnSave.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sp.edit();
            
            if (selectedImageUri != null) {
                editor.putString("IMAGE_URI", selectedImageUri.toString());
            }

            editor.putString("FULL_NAME", etFullName.getText().toString().trim());
            editor.putString("BIRTH_PLACE", etBirthPlace.getText().toString().trim());
            editor.putString("BIRTH_DATE", etBirthDate.getText().toString().trim());
            editor.putString("HOBBY", etHobby.getText().toString().trim());
            editor.putString("BIO", etBio.getText().toString().trim());
            
            if (editor.commit()) { // Use commit to ensure it's written before finish
                Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
