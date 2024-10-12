package com.barnard.dao;

import com.barnard.model.Recipe;
import com.barnard.model.Step;

import java.util.List;

public interface StepDao {

    Step getStepById(int stepId);
    List<Step> getStepsByRecipe(int recipeId);
    List<Recipe> getSteplist(List<Recipe> recipes);
    Step createStep(Step step);
    Step updateStep(Step step);
    void deleteStep(int stepId);
    void deleteAllStepsFromRecipe(int recipeId);
}
