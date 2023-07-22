package com.example.customerapp;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.AppCompatImageButton;


public class CodeFragment extends Fragment {

    private EditText lastNameEditText, firstNameEditText, streetEditText, streetNrEditText;
    private TextView lastNameErrorTextView, firstNameErrorTextView, streetErrorTextView, streetNrErrorTextView;
    private ImageView qrCodeImageView;

    private Toolbar toolbar;

    private AppCompatImageButton backButton;

    private static final int WIDTH_HEIGHT_NR = 400;

    private AddressBook addressBook= new AddressBook();

    @SuppressLint("StaticFieldLeak")
    public static CodeFragment instance;

    public static CodeFragment getInstance() {
        if (instance == null) {
            instance = new CodeFragment();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_code, container, false);

        lastNameEditText = rootView.findViewById(R.id.lastNameEditText);
        firstNameEditText = rootView.findViewById(R.id.firstNameEditText);
        streetEditText = rootView.findViewById(R.id.streetEditText);
        streetNrEditText = rootView.findViewById(R.id.streetNrEditText);

        lastNameErrorTextView = rootView.findViewById(R.id.lastNameErrorTextView);
        firstNameErrorTextView = rootView.findViewById(R.id.firstNameErrorTextView);
        streetErrorTextView = rootView.findViewById(R.id.streetErrorTextView);
        streetNrErrorTextView = rootView.findViewById(R.id.streetNrErrorTextView);

        qrCodeImageView = rootView.findViewById(R.id.qrCodeImageView);

        Button generateQRCodeButton = rootView.findViewById(R.id.buttonGenerate);
        generateQRCodeButton.setOnClickListener(v -> {
            generateQRCode();
        });

        backButton = rootView.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            replaceFragment(new QRCodeListFragment());
        });

        return rootView;
    }

    private void generateQRCode() {
        String lastName = lastNameEditText.getText().toString().trim();
        String firstName = firstNameEditText.getText().toString().trim();
        String street = streetEditText.getText().toString().trim();
        String streetNr = streetNrEditText.getText().toString().trim();

        boolean isValidInput = true;
        if (!isValidLastName(lastName)) {
            lastNameErrorTextView.setVisibility(View.VISIBLE);
            isValidInput = false;
        } else {
            lastNameErrorTextView.setVisibility(View.GONE);
        }

        if (!isValidFirstName(firstName)) {
            firstNameErrorTextView.setVisibility(View.VISIBLE);
            isValidInput = false;
        } else {
            firstNameErrorTextView.setVisibility(View.GONE);
        }

        if (!isValidStreet(street)) {
            streetErrorTextView.setVisibility(View.VISIBLE);
            isValidInput = false;
        } else {
            streetErrorTextView.setVisibility(View.GONE);
        }

        if (!isValidStreetNr(streetNr)) {
            streetNrErrorTextView.setVisibility(View.VISIBLE);
            isValidInput = false;
        } else {
            streetNrErrorTextView.setVisibility(View.GONE);
        }

        if (isValidInput) {
            Recipient recipient = new Recipient(firstName, lastName);
            Address address = new Address(street, streetNr);
            address.setPlz("49808");
            recipient.addAddress(address);

            // Counter erhöhen
            int qrCodeCounter = AddressBook.getQRCodeCounter(getContext());
            recipient.setQrCodeCounter(qrCodeCounter);

            Bitmap qrCodeBitmap = recipient.generateQRCode();
            qrCodeImageView.setImageBitmap(qrCodeBitmap);

            if (recipient.saveQRCodeToInternalStorage(getContext())) {
                // Erfolgreich gespeichert, Counter erhöhen
                qrCodeCounter++;
                AddressBook.setQRCodeCounter(getContext(), qrCodeCounter);
            } else {
                // Fehler beim Speichern
                Toast.makeText(getContext(), "Fehler beim Speichern des QR-Codes", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isValidLastName(@NonNull String lastName) {
        return !lastName.isEmpty();
    }

    private boolean isValidFirstName(@NonNull String firstName) {
        return !firstName.isEmpty();
    }

    private boolean isValidStreet(@NonNull String street) {
        return !street.isEmpty();
    }

    private boolean isValidStreetNr(@NonNull String streetNr) {
        return !streetNr.isEmpty();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            requireActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}