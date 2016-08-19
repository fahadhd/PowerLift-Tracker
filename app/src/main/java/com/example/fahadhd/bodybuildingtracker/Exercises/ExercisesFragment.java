package com.example.fahadhd.bodybuildingtracker.exercises;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.fahadhd.bodybuildingtracker.MainActivity;
import com.example.fahadhd.bodybuildingtracker.R;
import com.example.fahadhd.bodybuildingtracker.sessions.SessionsFragment;
import com.example.fahadhd.bodybuildingtracker.sessions.Session;
import com.example.fahadhd.bodybuildingtracker.TrackerApplication;
import com.example.fahadhd.bodybuildingtracker.data.TrackerDAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;


public class ExercisesFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Workout>>{
    TrackerDAO dao;
    ExerciseAdapter adapter;
    Session currentSession;
    long sessionID;
    int position;
    ArrayList<Session> sessions;
    ArrayList<Workout> workouts = new ArrayList<>();
    ListView exerciseListView;
    Menu exerciseMenu;
    FloatingActionButton fabExercise;
    View buttonView;
    boolean showFab;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TrackerApplication application  = (TrackerApplication)getActivity().getApplication();
        dao = application.getDatabase();
        sessions = application.getSessions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.exercises_list_fragment, container, false);
        this.buttonView =  inflater.inflate(R.layout.exercise_list_add_btn, container, false);
        adapter = new ExerciseAdapter(getActivity(),workouts,dao);
        fabExercise = (FloatingActionButton) rootView.findViewById(R.id.add_exercise);

        Intent sessionIntent = getActivity().getIntent();

        //Get session information from main activity
        if(sessionIntent != null && sessionIntent.hasExtra(SessionsFragment.INTENT_KEY)) {

            currentSession = (Session) sessionIntent.getSerializableExtra
                    (SessionsFragment.INTENT_KEY);
            position = sessionIntent.getIntExtra(SessionsFragment.POSITION_KEY,0);
            setExistingWorkout(currentSession);
        }
        else if (sessionIntent != null && sessionIntent.hasExtra(MainActivity.ADD_TASK)){
            currentSession = (Session) sessionIntent.getSerializableExtra
                    (MainActivity.ADD_TASK);
            //Add new session to cached data-+3*9
            sessions.add(0,currentSession);
            position = 0;
            getActivity().setTitle("Today's Workout");
        }
        sessionID = currentSession.getSessionId();

        exerciseListView = (ListView)rootView.findViewById(R.id.exercises_list_main);
        exerciseListView.setAdapter(adapter);
        exerciseListView.addFooterView(buttonView);



        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(fabExercise != null && adapter.getCount() > 0) {
            fabExercise.setVisibility(View.VISIBLE);
        }
    }

    public void setExistingWorkout(Session session){
        String title;
        SimpleDateFormat fmt = new SimpleDateFormat("MMM dd");
        GregorianCalendar calendar = new GregorianCalendar();
        fmt.setCalendar(calendar);
        String todayDate = fmt.format(calendar.getTime());

        if(session.getDate().equals(todayDate)){
            title = "Today's Workout";
        }
        else {
            title = session.getDate() + "   Session  #" + session.getSessionId();
        }

        getActivity().setTitle(title);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.exerciseMenu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void refreshSessionData(){
        Session updatedSession = dao.getSession(sessionID);
        sessions.set(position,updatedSession);
    }


    /*************** ASYNC LOADER FOR ADAPTER********************/
    //Loads all workouts for current session in workouts list.
    @Override
    public Loader<List<Workout>> onCreateLoader(int id, Bundle args) {
        return new ExerciseLoader(getActivity().getApplicationContext(),dao,sessionID);
    }

    @Override
    public void onLoadFinished(Loader<List<Workout>> loader, List<Workout> data) {
        this.showFab = true;
        if(workouts.size() == 0) workouts.addAll(data);
        else{
            workouts.add(data.get(data.size()-1));
        }
        if(workouts.size() > 4 && fabExercise != null){
            MenuItem addExercise = exerciseMenu.findItem(R.id.add_exercise);
            addExercise.setVisible(true);
            exerciseListView.removeFooterView(buttonView);
            this.showFab = true;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Workout>> loader) {
        workouts.clear();
    }
    /************************************************************/

}
