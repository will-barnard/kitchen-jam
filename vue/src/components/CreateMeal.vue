<template>

    <body>
        <form v-on:submit.prevent="createMeal()">
            <div>
                <label>Name</label><input type="text" v-model="meal.mealName">
                <label>Date cooked</label><input type="date" v-model="meal.date"/>
                <label>Comment</label><input type="text" v-model="meal.mealComment"/>
                <label>Cook time (min)</label><input type="number" v-model="meal.cookTime"/>
                <label>Rating</label><select v-model="meal.rating">
                    <option disabled="true" >----</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                </select>
                <label>Ingredient list</label><input type="text" v-model="meal.ingredients"/>
                <label>Notes</label><textarea v-model="meal.notes"></textarea>
            </div>

            <div>
                <!-- img goes here -->
            </div>

            <input type="submit" value="Submit" />
        </form> 
    </body>

</template>
<script>
import MealService from '../services/MealService.js'

export default {
    data() {
        return {
            meal: {}
        }
    },
    methods: {
        createMeal() {
            MealService.createMeal(this.meal).then(
                () => {
                    this.$router.push({ name: 'meal-log' });
                }
            )
        }
    }
}
</script>

<style scoped>
    body {
        display: flex;
        flex-direction: row;
        padding: 5px;
        border: 1px solid black;
    }
    div {
        display: flex;
        flex-direction: column; 
    }
    h2, h3 {
        display: inline;
        margin: 5px;
    }
    p {
        margin: 5px;
        text-align: left;
    }
    
</style>