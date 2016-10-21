package se.warting.sampleapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import se.eelde.localconfiguration.library.LocalConfiguration;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.tv);
        textView.setText("Logging enabled: " + (LocalConfiguration.getBoolean(getContentResolver(), "logging_enabled") ? " true" : "false"));
    }
}
