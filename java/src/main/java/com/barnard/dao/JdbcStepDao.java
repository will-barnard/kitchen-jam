package com.barnard.dao;

import com.barnard.exception.DaoException;
import com.barnard.model.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcStepDao implements StepDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Step getStepById(int stepId) {
        Step step = null;
        String sql = "SELECT * FROM step " +
                "WHERE step_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, stepId);
            if (rowSet.next()) {
                step = mapRowToStep(rowSet);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return step;
    }

    @Override
    public List<Step> getStepsByRecipe(int recipeId) {
        List<Step> stepList = new ArrayList<>();
        String sql  = "SELECT * FROM step " +
                "WHERE recipe_id = ?";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, recipeId);
            while (rowSet.next()) {
                stepList.add(mapRowToStep(rowSet));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return stepList;
    }

    @Override
    public Step createStep(Step step) {
        Step newStep = null;
        String sql = "INSERT INTO step (user_id, recipe_id, step_description, step_order) " +
                "VALUES (?, ?, ?, ?) " +
                "RETURNING step_id;";

        try {
            int stepId = jdbcTemplate.queryForObject(sql, int.class, step.getUserId(), step.getRecipeId(), step.getStepDescription(), step.getStepOrder());
            newStep = getStepById(stepId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newStep;
    }

    @Override
    public Step updateStep(Step step) {
        Step updatedStep = null;
        String sql = "UPDATE step " +
                "SET step_description = ?, step_order = ? " +
                "WHERE step_id = ?;";

        try {
            int rowsAffected = 0;
            rowsAffected = jdbcTemplate.update(sql, step.getStepDescription(), step.getStepOrder(), step.getStepId());
            if (rowsAffected == 0) {
                throw new DaoException("Something went wrong, no rows affected");
            }
            updatedStep = getStepById(step.getStepId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedStep;
    }

    @Override
    public void deleteStep(int stepId) {
        String sql = "DELETE FROM step " +
                "WHERE step_id = ?;";

        try {
            int rowsAffected = 0;
            rowsAffected = jdbcTemplate.update(sql, stepId);
            if (rowsAffected == 0) {
                throw new DaoException("Something went wrong, no rows affected");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    private Step mapRowToStep(SqlRowSet rs) {
        Step step = new Step();

        step.setStepId(rs.getInt("step_id"));
        step.setUserId(rs.getInt("user_id"));
        step.setRecipeId(rs.getInt("recipe_id"));
        step.setStepDescription(rs.getString("step_description"));
        step.setStepOrder(rs.getInt("step_order"));

        return step;
    }
}
