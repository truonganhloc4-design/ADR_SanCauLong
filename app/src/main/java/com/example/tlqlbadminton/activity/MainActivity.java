package com.example.tlqlbadminton.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    private TextView tvFacilityName;
    private ImageView ivCauHinhSan;
    private com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton fabThemSan;

    // Fragment instances (giữ sống để không mất state khi switch)
    private SoDoFragment soDoFragment;
    private ThongKeFragment thongKeFragment;
    private LichSuFragment lichSuFragment;
    private ActivityResultLauncher<Intent> themSanLauncher;

    private int currentTab = 0; // 0=SoDo, 1=ThongKe, 2=LichSu

    // Màn hình chính sau đăng nhập: chứa bottom navigation và Fragment.
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

    // Nhận kết quả sau khi thêm sân để refresh lại sơ đồ sân.
    private void setupActivityResults() {
        themSanLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (soDoFragment != null) {
                        soDoFragment.refreshAll();
                    }
                });
    }

    // Ánh xạ các view trong activity_main.xml.
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

        tvFacilityName = findViewById(R.id.tvFacilityName);
        ivCauHinhSan = findViewById(R.id.ivCauHinhSan);
        fabThemSan   = findViewById(R.id.fabThemSan);
    }

    // Gắn sự kiện cho bottom nav, nút cấu hình sân và nút thêm sân.
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

    // Đổi Fragment theo tab được chọn: Sơ đồ, Thống kê, Lịch sử.
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

        updateHeaderForTab(tabIndex);
        updateNavHighlight(tabIndex);
    }

    // Cập nhật màu/icon chữ đậm cho tab đang chọn.
    // Đổi tiêu đề header và các nút thao tác theo tab hiện tại.
    private void updateHeaderForTab(int activeTab) {
        switch (activeTab) {
            case 1:
                tvFacilityName.setText("Thống kê");
                setCourtActionsVisible(false);
                break;
            case 2:
                tvFacilityName.setText("Lịch sử");
                setCourtActionsVisible(false);
                break;
            default:
                tvFacilityName.setText("Sơ đồ sân");
                setCourtActionsVisible(true);
                break;
        }
    }

    // Nút cấu hình sân và thêm sân chỉ hợp với tab Sơ đồ.
    private void setCourtActionsVisible(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        ivCauHinhSan.setVisibility(visibility);
        if (fabThemSan != null) {
            fabThemSan.setVisibility(visibility);
        }
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
