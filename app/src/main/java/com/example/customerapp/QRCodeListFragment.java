package com.example.customerapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


//TODO: fix the problem with addressBook after closing the app
//TODO: Clear Information in CodeFragment after adding a new Recipient
//TODO: func delete QRCodeCode should set the counter to 0 after deleting all QRCodes
//TODO: func delete QRCodeCode should be combined with addressBook


public class QRCodeListFragment extends Fragment {

    private List<String> qrCodeFilePaths;
    private QRCodeAdapter qrCodeAdapter;
    private AddressBook addressBook;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qrCodeFilePaths = new ArrayList<>();
        addressBook = AddressBook.getInstance();
        addressBook.loadData(getContext());
        qrCodeAdapter = new QRCodeAdapter(getContext(), qrCodeFilePaths, addressBook);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr_code_list, container, false);

        addressBook = AddressBook.getInstance();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewQRCodeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(qrCodeAdapter);

        loadQRCodeFilePaths();

        view.findViewById(R.id.fabAddQRCode).setOnClickListener(v -> {
            goToCodeFragment();
        });

        return view;
    }

    private void goToCodeFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, CodeFragment.getInstance());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    private void loadQRCodeFilePaths() {
        qrCodeFilePaths.clear();
        File directory = getContext().getDir("qr_codes", Context.MODE_PRIVATE);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    qrCodeFilePaths.add(file.getAbsolutePath());
                }
            }
        }
        //aktulalisierung
        addressBook.loadData(getContext());
        qrCodeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_qr_code_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete_all_recipients) {
            deleteAllRecipients();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllRecipients() {
        addressBook.deleteAllRecipients(getContext());
        qrCodeFilePaths.clear();
        qrCodeAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Alle QR-Codes wurden gelöscht.", Toast.LENGTH_SHORT).show();
    }

}

