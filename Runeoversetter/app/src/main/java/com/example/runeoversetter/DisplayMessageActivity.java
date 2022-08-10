package com.example.runeoversetter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        String originalMessage = intent.getStringExtra(MainActivity.ORIGINAL_MESSAGE);
        String translatedMessage = intent.getStringExtra(MainActivity.TRANSLATED_MESSAGE);
        String runeType = intent.getStringExtra(MainActivity.CHOSEN_RUNE_TYPE);

        TextView textViewOriginal = findViewById(R.id.textOriginal);
        textViewOriginal.setText(originalMessage);

        TextView textViewTranslated = findViewById(R.id.textTranslated);
        textViewTranslated.setText(translatedMessage);

        TextView textViewResultInfo = findViewById(R.id.infoText2);
        textViewResultInfo.setText(getString(R.string.infoText2,runeType));

    }

    public void onCopyText(View view) {
        TextView textViewTranslated = findViewById(R.id.textTranslated);

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("translated text", textViewTranslated.getText());
        clipboard.setPrimaryClip(clip);
    }
}