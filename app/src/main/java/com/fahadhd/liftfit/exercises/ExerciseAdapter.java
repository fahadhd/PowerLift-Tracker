package com.fahadhd.liftfit.exercises;


import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fahadhd.liftfit.R;
import com.fahadhd.liftfit.utilities.Utility;
import com.fahadhd.liftfit.data.TrackerDAO;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;

//Populates the list_view of the exercises fragment.
public class ExerciseAdapter extends BaseAdapter{
    ExerciseActivity activity;
    ArrayList<Workout> workouts = new ArrayList<>();
    TrackerDAO dao;
    Typeface tekton;


    public ExerciseAdapter(ExerciseActivity activity, ArrayList<Workout> data, TrackerDAO dao, Typeface tekton){
        this.activity = activity;
        this.workouts = data;
        this.dao = dao;
        this.tekton =  tekton;
    }

    @Override
    public int getCount() {
        return workouts.size();
    }

    @Override
    public Object getItem(int position) {
        return workouts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    //Set to false if you want to remove listview divider
    public boolean isEnabled(int position) {
        return true;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        WorkoutViewHolder viewHolder;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) activity.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.exercises_list_item, parent, false);
            viewHolder = new WorkoutViewHolder(row);
            row.setTag(viewHolder);
        } else {
            viewHolder = (WorkoutViewHolder) row.getTag();
        }
        Workout workout = workouts.get(position);

        viewHolder.workoutInfo.setText(spanWorkoutInfo(workout));
        viewHolder.workoutInfo.setTypeface(tekton);
        activateButtons(viewHolder, workout);


        //TODO: App is a little slow when running this code, optimize later possibly put in thread.
        //In charge of putting congrats/next time text after workout is done
//        boolean sets_started = Utility.allSetsStarted(workout);
//        boolean sets_finished = (sets_started && Utility.allSetsFinished(workout));
//        if(sets_started && !sets_finished ){
//            //viewHolder.completed_dialog.setText("You'll get in next time!");
//        }
//        else if(sets_finished){
//            //viewHolder.completed_dialog.setText("Congrats +5 lb next time!");
//        }


        return row;
    }
    public void activateButtons(WorkoutViewHolder viewHolder, final Workout workout){
        ArrayList<Set> setList = workout.getSets();
        TextView currButton;
        Set currSet;
        for(int i = 7; i >= 0; i -- ){
            switch (i){
                case 0: currButton = viewHolder.buttonOne; break;
                case 1: currButton = viewHolder.buttonTwo; break;
                case 2: currButton = viewHolder.buttonThree; break;
                case 3: currButton = viewHolder.buttonFour; break;
                case 4: currButton = viewHolder.buttonFive; break;
                case 5: currButton = viewHolder.buttonSix; break;
                case 6: currButton = viewHolder.buttonSeven; break;
                case 7:currButton = viewHolder.buttonEight; break;
                default: currButton = viewHolder.buttonOne;
            }
            currButton.setVisibility(View.INVISIBLE);
            currButton.setText(null);
        }
        for(int i = 0; i < setList.size(); i++){
            currSet = setList.get(i);

            switch (i){
                case 0: currButton = viewHolder.buttonOne; break;
                case 1: currButton = viewHolder.buttonTwo; break;
                case 2: currButton = viewHolder.buttonThree; break;
                case 3: currButton = viewHolder.buttonFour; break;
                case 4: currButton = viewHolder.buttonFive; break;
                case 5: currButton = viewHolder.buttonSix; break;
                case 6: currButton = viewHolder.buttonSeven; break;
                case 7:currButton = viewHolder.buttonEight; break;
                default: currSet = setList.get(0); currButton = viewHolder.buttonOne;
            }
            currButton.setVisibility(View.VISIBLE);
            currButton.setOnClickListener(new
                    SetListener(currButton,workout,currSet,viewHolder,activity));
            currButton.setTypeface(tekton);
        }
        viewHolder.editWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 8/29/2016 Communicate dialog with fragment instead of activity
                WorkoutDialog dialog = WorkoutDialog.newInstance(workout);
                dialog.show(activity.getFragmentManager(), "WorkoutDialog");
            }
        });
    }


    //TODO: Put in utility class and use it for session list item as well.
    //Creates a customizable string for workout info
    public SpannableStringBuilder spanWorkoutInfo(Workout workout){
        int start = 0, end;
        String buffer = WordUtils.capitalizeFully(workout.getName().replaceAll("\\s+","\n"));
        end = buffer.length();
        /******************Workout Title********************/
        SpannableStringBuilder spanBuilder = new SpannableStringBuilder(buffer);
        spanBuilder.setSpan(new RelativeSizeSpan(1.2f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //spanBuilder.setSpan(new ForegroundColorSpan(Color.BLACK),start,end,0);
        /******************Workout Weight********************/
        String unit = Utility.getUnit(activity);
        Double weightUnit = (unit.equals("LB")) ? workout.getWeight(): workout.getWeight()*0.45359237;
        buffer = "\n\n"+" "+Integer.toString(weightUnit.intValue());
        spanBuilder.append(buffer);
        int weightColor = ContextCompat.getColor(activity,R.color.orange_a400);
        start = end;
        end = spanBuilder.length();
        spanBuilder.setSpan(new ForegroundColorSpan(weightColor),start,end,0);
        /******************Workout Unit*******************/
        spanBuilder.append(unit);
        start = end;
        end = spanBuilder.length();
        /******************Workout Sets x Reps*******************/
        buffer = '\n'+Integer.toString(workout.getMaxSets());
        spanBuilder.append(buffer);
        start = end;
        end = spanBuilder.length();
        spanBuilder.setSpan(new ForegroundColorSpan(weightColor),start,end,0);

        spanBuilder.append(" X ");
        start = end;
        end = spanBuilder.length();
        spanBuilder.setSpan(new RelativeSizeSpan(0.7f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        buffer = Integer.toString(workout.getMaxReps());
        spanBuilder.append(buffer);
        start = end;
        end = spanBuilder.length();
        spanBuilder.setSpan(new ForegroundColorSpan(weightColor),start,end,0);

        return spanBuilder;
    }


}
