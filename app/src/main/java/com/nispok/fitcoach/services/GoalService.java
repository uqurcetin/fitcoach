package com.nispok.fitcoach.services;

import com.nispok.fitcoach.models.Goal;

import java.util.ArrayList;
import java.util.List;

public class GoalService {

    /**
     * Saves this {@link com.nispok.fitcoach.models.Goal} in local persistence
     * @param goal the {@link com.nispok.fitcoach.models.Goal} to save
     */
    public static void save(Goal goal) {
        //TODO: Implement me!
    }

    public static List<Goal> get() {
        // TODO: Get data from DB
        List<Goal> goals = new ArrayList<>();
        goals.add(new Goal());
        return goals;
    }

}
