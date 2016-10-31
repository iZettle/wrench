package se.warting.sampleapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import se.eelde.localconfiguration.library.LocalConfiguration;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    private LocalConfiguration localConfiguration;

    private void log(String text) {
        textView.setText((textView.getText() + "\n" + text).trim());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.log);

        localConfiguration = new LocalConfiguration(this);

        // text that defaults to something
        log("WELCOME_TITLE:");
        log(localConfiguration.getString("WELCOME_TITLE", "welcome!"));

        // welcome text that can be null
        log("WELCOME_TEXT:");
        log(localConfiguration.getString("WELCOME_TEXT", null));

        // check if strict mode is enabled, default to false/DISABLED
        log("STRICT_MODE:");
        log(localConfiguration.getBoolean("STRICT_MODE", false) ? "ENABLED" : "DISABLED");

        // get num columns to show, defaults to 2
        log("NUM_COLUMNS:");
        log(String.valueOf(localConfiguration.getInt("NUM_COLUMNS", 2)));

    }
}
