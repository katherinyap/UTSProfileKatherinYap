package com.example.utsprofile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Kriteria: DashboardActivity - Kode Program Lengkap
 */
public class DashboardActivity extends AppCompatActivity {

    private ImageView imgProfile;
    private TextView tvWelcome, tvFullName, tvUsername, tvBirthPlace, tvBirthDate, tvHobby, tvBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setTitle("Dashboard");

        // Inisialisasi View
        imgProfile = findViewById(R.id.imgProfile);
        tvWelcome = findViewById(R.id.tvWelcome);
        tvFullName = findViewById(R.id.tvFullName);
        tvUsername = findViewById(R.id.tvUsername);
        tvBirthPlace = findViewById(R.id.tvBirthPlace);
        tvBirthDate = findViewById(R.id.tvBirthDate);
        tvHobby = findViewById(R.id.tvHobby);
        tvBio = findViewById(R.id.tvBio);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        SharedPreferences sp = getSharedPreferences("PROFILE", MODE_PRIVATE);

        // Load Foto
        String imageUriString = sp.getString("IMAGE_URI", "");
        if (!imageUriString.isEmpty()) {
            try {
                imgProfile.setImageURI(Uri.parse(imageUriString));
            } catch (Exception e) {
                imgProfile.setImageResource(R.mipmap.ic_launcher_round);
            }
        }

        // Load Data Text
        String fullName = sp.getString("FULL_NAME", "Belum diisi");
        tvWelcome.setText("Welcome, " + fullName + "!");
        tvFullName.setText(fullName);
        tvUsername.setText(sp.getString("USERNAME", "NIM Tidak Ada"));
        tvBirthPlace.setText(sp.getString("BIRTH_PLACE", "-"));
        tvBirthDate.setText(sp.getString("BIRTH_DATE", "-"));
        tvHobby.setText(sp.getString("HOBBY", "-"));
        tvBio.setText(sp.getString("BIO", "-"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_edit) {
            // Navigasi ke ProfilActivity sesuai rubrik
            startActivity(new Intent(this, ProfilActivity.class));
            return true;
        } else if (id == R.id.menu_logout) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
