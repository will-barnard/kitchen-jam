import axios from 'axios';

export default {

    getMealFeed() {
        return axios.get('/profile/meal');
    },
    getMoreMeals(timesLoaded) {
        return axios.get(`/profile/meal/more/${timesLoaded}`);
    },
    getRecipeFeed() {
        return axios.get('/profile/recipe');
    },
    getMoreRecipes(timesLoaded) {
        return axios.get(`/profile/recipe/more/${timesLoaded}`);
    }

}