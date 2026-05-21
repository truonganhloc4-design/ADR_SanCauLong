package com.example.tlqlbadminton.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.tlqlbadminton.R;

public class MainActivity extends AppCompatActivity {

    // Bottom Nav views
    private LinearLayout navSoDo, navThongKe, navLichSu;
    private ImageView iconSoDo, iconThongKe, iconLichSu;
    private TextView labelSoDo, labelThongKe, labelLichSu;

    // Header
    private ImageView ivCauHinhSan;
    private com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton fabThemSan;

    // Fragment instances (giữ sống để không mất state khi switch)
    private SoDoFragment soDoFragment;
    private ThongKeFragment thongKeFragment;
    private LichSuFragment lichSuFragment;
    private ActivityResultLauncher<Intent> themSanLauncher;

    private int currentTab = 0; // 0=SoDo, 1=ThongKe, 2=LichSu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupActivityResults();
        bindViews();
        setupNavigation();

        // Load fragment đầu tiên
        if (savedInstanceState == null) {
            showFragment(0);
        }
    }

    private void setupActivityResults() {
        themSanLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (soDoFragment != null) {
                        soDoFragment.refreshAll();
                    }
                });
    }

    private void bindViews() {
        navSoDo    = findViewById(R.id.navSoDo);
        navThongKe = findViewById(R.id.navThongKe);
        navLichSu  = findViewById(R.id.navLichSu);

        iconSoDo    = findViewById(R.id.iconSoDo);
        iconThongKe = findViewById(R.id.iconThongKe);
        iconLichSu  = findViewById(R.id.iconLichSu);

        labelSoDo    = findViewById(R.id.labelSoDo);
        labelThongKe = findViewById(R.id.labelThongKe);
        labelLichSu  = findViewById(R.id.labelLichSu);

        ivCauHinhSan = findViewById(R.id.ivCauHinhSan);
        fabThemSan   = findViewById(R.id.fabThemSan);
    }

    private void setupNavigation() {
        navSoDo.setOnClickListener(v    -> showFragment(0));
        navThongKe.setOnClickListener(v -> showFragment(1));
        navLichSu.setOnClickListener(v  -> showFragment(2));

        ivCauHinhSan.setOnClickListener(v ->
            startActivity(new Intent(this, CauHinhSanActivity.class)));

        if (fabThemSan != null) {
            fabThemSan.setOnClickListener(v ->
                themSanLauncher.launch(new Intent(this, ThemSanActivity.class)));
        }
    }

    private void showFragment(int tabIndex) {
        if (currentTab == tabIndex && getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainer) != null) {
            return; // Không load lại nếu đang ở tab đó rồi
        }
        currentTab = tabIndex;

        // Lấy hoặc tạo fragment
        Fragment target;
        String tag;
        switch (tabIndex) {
            case 1:
                if (thongKeFragment == null) thongKeFragment = new ThongKeFragment();
                target = thongKeFragment;
                tag = "THONG_KE";
                break;
            case 2:
                if (lichSuFragment == null) lichSuFragment = new LichSuFragment();
                target = lichSuFragment;
                tag = "LICH_SU";
                break;
            default:
                if (soDoFragment == null) soDoFragment = new SoDoFragment();
                target = soDoFragment;
                tag = "SO_DO";
                break;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainer, target, tag);
        ft.commit();

        updateNavHighlight(tabIndex);
    }

    private void updateNavHighlight(int activeTab) {
        int selected   = ContextCompat.getColor(this, R.color.nav_selected);
        int unselected = ContextCompat.getColor(this, R.color.nav_unselected);

        // Reset all to unselected
        iconSoDo.setColorFilter(unselected);
        iconThongKe.setColorFilter(unselected);
        iconLichSu.setColorFilter(unselected);
        labelSoDo.setTextColor(unselected);    labelSoDo.setTypeface(null, android.graphics.Typeface.NORMAL);
        labelThongKe.setTextColor(unselected); labelThongKe.setTypeface(null, android.graphics.Typeface.NORMAL);
        labelLichSu.setTextColor(unselected);  labelLichSu.setTypeface(null, android.graphics.Typeface.NORMAL);

        // Highlight active
        switch (activeTab) {
            case 0:
                iconSoDo.setColorFilter(selected);
                labelSoDo.setTextColor(selected);
                labelSoDo.setTypeface(null, android.graphics.Typeface.BOLD);
                break;
            case 1:
                iconThongKe.setColorFilter(selected);
                labelThongKe.setTextColor(selected);
                labelThongKe.setTypeface(null, android.graphics.Typeface.BOLD);
                break;
            case 2:
                iconLichSu.setColorFilter(selected);
                labelLichSu.setTextColor(selected);
                labelLichSu.setTypeface(null, android.graphics.Typeface.BOLD);
                break;
        }
    }
}
