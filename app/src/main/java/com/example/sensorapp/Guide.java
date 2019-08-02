package com.example.sensorapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import us.feras.mdv.MarkdownView;
import android.view.Window;


/**
 * Displays markdown file README
 */
public class Guide extends AppCompatActivity {

    /**
     * Creates object and opens file to display
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MarkdownView webView = (MarkdownView)findViewById(R.id.markdown);
        //setContentView(webView);
        webView.loadMarkdownFile("file:///android_asset/README.md");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Open contacts", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
