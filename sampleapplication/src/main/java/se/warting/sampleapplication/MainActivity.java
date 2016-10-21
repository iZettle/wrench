package se.warting.sampleapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import se.eelde.localconfiguration.library.LocalConfiguration;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    private void log(String text) {
        textView.setText((textView.getText() + "\n" + text).trim());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.log);

        // text that defaults to something
        log("WELCOME_TITLE:");
        log(LocalConfiguration.getString(getContentResolver(), "WELCOME_TITLE", "welcome!"));

        // welcome text that can be null
        log("WELCOME_TEXT:");
        log(LocalConfiguration.getString(getContentResolver(), "WELCOME_TEXT", null));

        // check if strict mode is enabled, default to false/DISABLED
        log("STRICT_MODE:");
        log(LocalConfiguration.getBoolean(getContentResolver(), "STRICT_MODE", false) ? "ENABLED" : "DISABLED");

        // get num columns to show, defaults to 2
        log("NUM_COLUMNS:");
        log(String.valueOf(LocalConfiguration.getInt(getContentResolver(), "NUM_COLUMNS", 2)));


    }
}
