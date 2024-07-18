package com.barnard.controller;

import com.barnard.dao.StepDao;
import com.barnard.model.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping(path = "/step")
public class StepController {

    @Autowired
    private StepDao stepDao;

    @PostMapping
    public List<Step> updateSteps(@RequestBody List<Step> steps) {
        List<Step> newSteps = null;
        int recipeId = steps.get(0).getRecipeId();
        try {
            stepDao.deleteAllStepsFromRecipe(recipeId);
            for (Step step : steps) {
                stepDao.createStep(step);
            }
            newSteps = stepDao.getStepsByRecipe(recipeId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return newSteps;
    }
}
