package com.example.utsprofile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Kriteria: Validasi 3 Field & Navigasi ke Dashboard
 */
public class LoginActivity extends AppCompatActivity {

    private EditText etNama, etNIM, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi View
        etNama = findViewById(R.id.etNama);
        etNIM = findViewById(R.id.etNIM);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String nama = etNama.getText().toString().trim();
            String nim = etNIM.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            // Validasi: 3 Field tidak boleh kosong
            if (nama.isEmpty() || nim.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Harap lengkapi semua field!", Toast.LENGTH_SHORT).show();
            } else if (nim.equals("03081240047") && pass.equals("admin")) {
                // NIM harus "03081240047" dan Password harus "admin"
                
                // Simpan data login ke SharedPreferences agar bisa tampil di Dashboard
                getSharedPreferences("PROFILE", MODE_PRIVATE).edit()
                        .putString("FULL_NAME", nama)
                        .putString("USERNAME", nim)
                        .apply();

                Intent intent = new Intent(this, DashboardActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "NIM atau Password salah! *nim: 03081240047 dan pw: admin", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
