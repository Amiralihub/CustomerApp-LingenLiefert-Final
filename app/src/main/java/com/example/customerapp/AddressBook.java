package com.example.customerapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AddressBook {
    private static AddressBook instance;
    private static final String KEY_QR_CODE_COUNTER = "qr_code_counter";
    private List<Recipient> recipients;

    public AddressBook() {
        this.recipients = new ArrayList<>();
    }

    public static AddressBook getInstance() {
        if (instance == null) {
            instance = new AddressBook();
        }
        return instance;
    }

    public void addRecipient(Recipient recipient, Context context) {
        int qrCodeCounter = getQRCodeCounter(context);
        setQRCodeCounter(context, qrCodeCounter + 1);
        recipient.setQRCodeCounter(qrCodeCounter);
        recipients.add(recipient);
        System.out.println("Hinzufügen eines Recipients: " + recipient.getFirstName() + " " + recipient.getLastName());
        System.out.println("Recipient wurde zum AddressBook erfolgreich hinzugefügt");
        saveData(context);
    }



    public List<Recipient> getRecipients() {
        return recipients;
    }


    public void loadData(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = preferences.getString("addressBook", "");
        Type type = new TypeToken<List<Recipient>>() {}.getType();
        recipients = gson.fromJson(json, type);
        if (recipients == null) {
            recipients = new ArrayList<>();
        }
    }

    public void saveData(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(recipients);
        editor.putString("addressBook", json);
        editor.apply();
    }


    public void deleteRecipient(Recipient recipient, Context context) {
        if (recipients.contains(recipient)) {
            recipients.remove(recipient);
            System.out.println("Der Recipient wurde erfolgreich entfernt");
            saveData(context);
        } else {
            System.out.println("Der Recipient konnte nicht gefunden werden");
        }
    }

    public void deleteAllRecipients(Context context) {
        recipients.clear();
        setQRCodeCounter(context, 0);
        saveData(context);
        deleteAllSavedQRCodes(context);
        System.out.println("Alle Recipients und QR-Codes wurden gelöscht.");
    }

    private void deleteAllSavedQRCodes(Context context) {
        File directory = context.getDir("qr_codes", Context.MODE_PRIVATE);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    public static int getQRCodeCounter(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(KEY_QR_CODE_COUNTER, 1);
    }


    public static void setQRCodeCounter(Context context, int qrCodeCounter) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_QR_CODE_COUNTER, qrCodeCounter);
        editor.apply();
    }

    public Recipient getRecipientByQRCodeCounter(int qrCodeCounter) {
        if (qrCodeCounter >= 0 && qrCodeCounter < recipients.size()) {
            return recipients.get(qrCodeCounter);
        }
        return null;
    }

}

