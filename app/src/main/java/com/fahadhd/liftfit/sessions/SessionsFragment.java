package com.fahadhd.liftfit.sessions;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fahadhd.liftfit.MainActivity;
import com.fahadhd.liftfit.exercises.ExerciseActivity;
import com.fahadhd.liftfit.R;
import com.fahadhd.liftfit.TrackerApplication;
import com.fahadhd.liftfit.data.TrackerDAO;

import java.util.ArrayList;

/**
 * First view a user see's when starting the app. Displays all of the user's
 * past workout sessions and allows the ability to add a new session which will then start another
 * activity.
 */
public class SessionsFragment extends Fragment{

    public SessionAdapter adapter;
    private ArrayList<Session> sessions;
    ListView sessionsListView;
    TrackerApplication application;
    TrackerDAO dao;
    Typeface tekton;
    int recentPosition = -1;

    public static final String POSITION_KEY = "position";
    public static final String INTENT_KEY = "session_ID";
    public static final int RECENT_POSITION = 7;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application  = (TrackerApplication)getActivity().getApplication();
        dao = application.getDatabase();
        sessions = application.getSessions();
        if(sessions.isEmpty() || sessions.size() == 1){
            new GetSessions().execute();
        }
        adapter = new SessionAdapter((MainActivity) getActivity(),sessions);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sessions_list_fragment, container, false);
        tekton = Typeface.createFromAsset(getActivity().getAssets(),"TektonPro-Bold.otf");
        sessionsListView = (ListView) rootView.findViewById(R.id.session_list_main);

        if(dao.isSessionsEmpty()){
            rootView =  inflater.inflate(R.layout.empty_sessions, container, false);
            TextView userMsg = (TextView) rootView.findViewById(R.id.user_info);
            userMsg.setTypeface(tekton);
            return rootView;
        }


        sessionsListView.setAdapter(adapter);
        sessionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(),ExerciseActivity.class).
                        putExtra(INTENT_KEY,sessions.get(position)).putExtra(POSITION_KEY,position);
                startActivityForResult(intent,RECENT_POSITION);
            }
        });

        return rootView;
    }

    public void restartAdapter(){
        if(sessionsListView != null) {
            adapter = new SessionAdapter((MainActivity) getActivity(), sessions);
            adapter.setRecentPosition(recentPosition);
            sessionsListView.setAdapter(adapter);
        }
    }


    public class GetSessions extends AsyncTask<Void,Void,ArrayList<Session>> {
        @Override
        protected ArrayList<Session> doInBackground(Void... params) {
            return dao.getSessions();
        }

        @Override
        protected void onPostExecute(ArrayList<Session> result) {
            if(sessions.isEmpty()){
                sessions.addAll(result);
            }
            else{
                sessions.clear();
                sessions.addAll(result);
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECENT_POSITION && data != null && data.hasExtra(ExerciseActivity.RECENT_POSITION_KEY)) {
            recentPosition = data.getIntExtra(ExerciseActivity.RECENT_POSITION_KEY, -1);
        }
    }

}