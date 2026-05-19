package com.example.tlqlbadminton.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tlqlbadminton.R;
import com.example.tlqlbadminton.model.AppState;

public class LichSuFragment extends Fragment implements AppState.OnStateChangedListener {

    private TextView tvEmpty;
    private RecyclerView rvLichDat;
    private AppState appState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lich_su, container, false);
        appState = AppState.getInstance();

        tvEmpty  = root.findViewById(R.id.tvEmptyLichSu);
        rvLichDat = root.findViewById(R.id.rvLichDat);
        rvLichDat.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshList();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        appState.addListener(this);
        refreshList();
    }

    @Override
    public void onPause() {
        super.onPause();
        appState.removeListener(this);
    }

    private void refreshList() {
        if (tvEmpty == null) return;
        boolean isEmpty = appState.getBookingList().isEmpty();
        tvEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        // TODO: Bind RecyclerView adapter với appState.getBookingList()
    }

    // Observer callbacks
    @Override
    public void onCourtStatusChanged(int courtIndex, int newStatus, String info) {}

    @Override
    public void onInvoiceAdded(AppState.InvoiceEntry entry, long tongDoanhThu) {}

    @Override
    public void onBookingAdded(AppState.BookingEntry entry) {
        if (getView() == null) return;
        refreshList();
    }
}
