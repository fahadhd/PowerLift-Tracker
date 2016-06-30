package com.example.fahadhd.bodybuildingtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.fahadhd.bodybuildingtracker.Exercises.ExerciseActivity;
import com.example.fahadhd.bodybuildingtracker.Sessions.ViewSessionsFragment;

public class MainActivity extends AppCompatActivity {

   ViewSessionsFragment sessionsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sessionsFragment =  ((ViewSessionsFragment)getSupportFragmentManager()
                .findFragmentById(R.id.sessions_fragment));
        FloatingActionButton addTask = (FloatingActionButton) findViewById(R.id.session_button);
        assert addTask != null;
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionsFragment.startSessionTask();

                Intent exercise = new Intent(MainActivity.this, ExerciseActivity.class);
                startActivity(exercise);
            }
        });





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
