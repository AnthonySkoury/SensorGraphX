package com.example.sensorapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        View aboutpage = new AboutPage(this)
                .isRTL(false)
                .setDescription("About Page")
                .addItem(new Element().setTitle("Version 1.0"))
                .addGroup("Contacts")
                .addEmail("askoury@uci.edu")
                .addWebsite("http://engineering.uci.edu/dept/mae/research")
                .addGitHub("AnthonySkoury")
                .addItem(Copyright())
                .create()
                ;

        setContentView(aboutpage);
    }

    private Element Copyright(){
        final Element copyright = new Element();
        final String text = String.format("Copyright %d by MAE Group at UCI", Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(text);
        copyright.setIconDrawable(R.mipmap.ic_launcher);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(About.this, text,Toast.LENGTH_LONG).show();
            }
        });
        return copyright;
    }
}
