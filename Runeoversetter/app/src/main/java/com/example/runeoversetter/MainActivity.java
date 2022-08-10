package com.example.runeoversetter;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final HashMap<String, String> runeFiles = new HashMap<String, String>() {{
        put("middelalderruner", "middelalderruner.txt");
        put("yngre futhark", "yngreFuthark.txt");
    }};
    private String chosenRuneType = "middelalderruner";
    private boolean skipNextLetter = false;
    public static final String ORIGINAL_MESSAGE = "com.example.runeoversetter.ORIGINAL_MESSAGE";
    public static final String TRANSLATED_MESSAGE = "com.example.runeoversetter.TRANSLATED_MESSAGE";
    public static final String CHOSEN_RUNE_TYPE = "com.example.runeoversetter.CHOSEN_RUNE_TYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        if (view.getId() == R.id.middelalderruner && checked) {
            chosenRuneType = "middelalderruner";
        } else if (view.getId() == R.id.yngreFuthark && checked) {
            chosenRuneType = "yngre futhark";
        }
    }

    private String stringToRune(String thisLetter, String nextLetter, List<String> runeList) {
        String rune = "";

        // Going through all possible runes
        for (int i = 0; i < runeList.size(); i++) {
            // Split into letter and rune
            String[] temp = runeList.get(i).split(" ");
            String possibleLetter = temp[0];
            String possibleRune = temp[1];

            // Check if the letters match
            if (thisLetter.equals(possibleLetter)) {
                temp = runeList.get(i+1).split(" ");
                String nextPossibleLetter = temp[0];
                String nextPossibleRune = temp[1];
                if ((thisLetter + nextLetter).equals(nextPossibleLetter)) {
                    rune = nextPossibleRune;
                    skipNextLetter = true;
                } else {
                    rune = possibleRune;
                }
            }
        }

        return rune.trim();
    }

    public static boolean isNumber(String i) {
        try {
            Integer.parseInt(i);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private List<String> getRuneList() {
        List<String> lines = new ArrayList<>();

        try {
            InputStream is = getAssets().open(runeFiles.get(chosenRuneType));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = reader.readLine()) != null)
                lines.add(line);

        } catch (Exception ignored) {}

        return lines;
    }

    private String toRunes(String userInput) {
        // Getting a list of all possible runes
        List<String> runeList = getRuneList();
        if (runeList.size() == 0) return "Error: Rune library not found";

        // Making the user input into a list
        String input = userInput.toUpperCase();
        char[] letters = input.toCharArray();

        StringBuilder runes = new StringBuilder();

        // Converting from letters to runes
        for (int i = 0; i < letters.length; i++) {
            String thisLetter = Character.toString(letters[i]);
            String nextLetter = "";
            if ((i+1) < letters.length) {
                nextLetter = Character.toString(letters[i+1]);
            }

            if (isNumber(thisLetter) || thisLetter.equals(" ")) {
                // Do not convert numbers and spaces
                runes.append(thisLetter);
            } else {
                // Convert letter to rune and add to the result
                if (!skipNextLetter) {
                    String nyRune = stringToRune(thisLetter, nextLetter, runeList);
                    runes.append(nyRune);
                } else {
                    skipNextLetter = false;
                }
            }
        }

        return runes.toString();
    }


    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.textUserInput);
        String userInput = editText.getText().toString();
        String runes = toRunes(userInput);

        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra(ORIGINAL_MESSAGE, userInput);
        intent.putExtra(TRANSLATED_MESSAGE, runes);
        intent.putExtra(CHOSEN_RUNE_TYPE, chosenRuneType);
        startActivity(intent);
    }
}