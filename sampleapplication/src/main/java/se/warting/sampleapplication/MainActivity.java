package se.warting.sampleapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.izettle.localconfiguration.LocalConfiguration;

import java.util.Map;


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

        LocalConfiguration localConfiguration = new LocalConfiguration(this);

        if (!BuildConfig.DEBUG || localConfiguration.exists()) {
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


            log("All keys and values:");
            for (Map.Entry<String, ?> entry : localConfiguration.getAll().entrySet()) {
                if (entry.getValue() != null) {
                    log(entry.getKey() + ": (" + entry.getValue().getClass().getSimpleName() + ")" + entry.getValue());
                } else {
                    log(entry.getKey() + ": (?) NULL ");
                }
            }


        } else {
            log("Local configuration application not installed");
        }

    }
}
