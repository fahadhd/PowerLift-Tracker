package com.example.fahadhd.bodybuildingtracker.Exercises;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.fahadhd.bodybuildingtracker.MainActivity;
import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.Sessions.Session;
import com.example.fahadhd.bodybuildingtracker.Utility;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;


public class ExerciseActivity extends AppCompatActivity implements WorkoutDialog.Communicator {
    ExercisesFragment exercisesFragment;
    TrackerDAO dao;
    long sessionID;
    ArrayList<Workout> workouts;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);
        Toolbar toolbar = (Toolbar) findViewById(R.id.exercise_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        exercisesFragment = (ExercisesFragment) getSupportFragmentManager().
                findFragmentById(R.id.exercises_fragment);

        //Creates a new loader if one doesn't exist or refreshes the data if one does exist.
        if(this.getSupportLoaderManager().getLoader(R.id.exercise_loader_id) == null) {
            this.getSupportLoaderManager().initLoader(R.id.exercise_loader_id, null, exercisesFragment);
        }
        else{
            this.getSupportLoaderManager().restartLoader(R.id.exercise_loader_id,null,exercisesFragment);
        }

        FloatingActionButton addExercise = (FloatingActionButton)findViewById(R.id.add_exercise);
        if (addExercise != null) {
            addExercise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WorkoutDialog dialog = new WorkoutDialog();
                    dialog.show(getFragmentManager(),"WorkoutDialog");
                }
            });
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void getWorkoutInfo(String name, int weight, int max_sets, int max_reps) {

        dao  = exercisesFragment.dao;
        sessionID = exercisesFragment.sessionID;
        workouts = exercisesFragment.workouts;
        this.name = name;
        new AddWorkoutTask().execute(weight,max_sets,max_reps);


    }

    public class  AddWorkoutTask extends AsyncTask<Integer,Void,Void>{

        @Override
        protected Void doInBackground(Integer... params) {
            int weight =   params[0];
            int max_sets =  params[1];
            int max_reps =  params[2];
            addWorkoutTask(name,weight,max_sets,max_reps);
            ExerciseActivity.this.getSupportLoaderManager().restartLoader(R.id.exercise_loader_id,null,exercisesFragment);
            return null;
        }
        public void addWorkoutTask(String name, int weight, int max_sets, int max_reps){
            long workoutID = dao.addWorkout(sessionID,workouts.size()+1,name,weight,max_sets,max_reps);


                dao.addSets(workoutID,max_sets);

        }

    }



}
