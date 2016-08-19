package com.example.fahadhd.bodybuildingtracker.exercises;


import java.io.Serializable;
import java.util.ArrayList;

public class Workout implements Serializable {
    long session_id, workout_id;
    int workout_num, weight, max_sets, max_reps;
    String name;
    ArrayList<Set> sets;
    public Workout(long session_id, long workout_id, int workout_num,
                   String name, int weight, int max_sets, int max_reps, ArrayList<Set> sets){
        this.session_id = session_id;
        this.workout_id  = workout_id;
        this.workout_num = workout_num;
        this.name = name;
        this.weight = weight;
        this.max_sets = max_sets;
        this.max_reps = max_reps;
        this.sets = sets;
    }
    public Workout(Workout workout){
        this.session_id = workout.session_id;
        this.workout_id  = workout.workout_id;
        this.workout_num = workout.workout_num;
        this.name = workout.name;
        this.weight = workout.weight;
        this.max_sets = workout.max_sets;
        this.max_reps = workout.max_reps;
        this.sets = workout.sets;

    }

    public Workout(String buttonName){
        this.name = buttonName;
    }


    public String getName(){
        return this.name;
    }

    public long getWorkoutID(){
        return workout_id;
    }

    public int getOrderNum(){
        return this.workout_num;
    }
    public int getWeight(){
        return this.weight;
    }
    public int getMaxSets(){
        return this.max_sets;
    }
    public int getMaxReps(){
        return this.max_reps;
    }

    public ArrayList<Set> getSets() {
        return sets;
    }
}