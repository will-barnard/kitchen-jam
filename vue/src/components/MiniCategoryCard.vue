<template>
    <div class="main-category-card">
        <h4 :class="showGoDetail ? 'go-detail' : ''" @click="showGoDetail ? $router.push({name: 'category-detail', params: {categoryId: category.categoryId}}) : ''">
            {{ category.categoryName }}
        </h4>
        <div class="spacer"></div>
        <div class="category-info">
            <p v-if="showTimesUsed">
                Times Used: {{ category.countMeals || 0 }}
            </p>
            <p v-if="showLastUsed" :class="category.lastCreated ? 'go-detail' : ''" @click="$router.push({name: 'meal-detail', params: {mealId: category.lastCreatedMeal}})">
                Last Used: {{ formatDate(category.lastCreated) }}
            </p>
            <p v-if="showCountRecipes">
                Recipe Count: {{ category.countRecipes || 0 }}
            </p>
            <div class="rating" v-if="showRating">
                <div class="stars">
                    <p class="rating-label">Avg Rating: </p>
                    <span v-for="n in 5" :key="n" class="star">
                        <i v-if="category.avgRating >= n * 2" class="fas fa-star"></i>
                        <i v-else-if="category.avgRating >= (n * 2) - 1" class="fas fa-star-half-alt"></i>
                        <i v-else class="far fa-star"></i>
                    </span>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
import UtilityService from '../services/UtilityService';

export default {
    props: ['category', 'showLastUsed', 'showTimesUsed', 'showGoDetail', 'showCountRecipes', 'showRating'],
    methods: {
        formatDate(date) {
            return date ? UtilityService.formatDate(date) : "Never";
        }
    }
}
</script>
<style scoped>
    p {
        margin: 0;
    }
    .main-category-card {
        display: flex;
        justify-content: center;
        align-items: center;
        background-color: var(--light-1);
        padding: 5px;
        padding-left: 10px;
        padding-right: 10px;
        border-radius: 10px;
        margin: 5px;
        gap: 10px;
        width: 100%;
    }
    .spacer {
        flex-grow: 1;
    }
    .go-detail {
        text-decoration: underline;
    }
    .category-info {
        display: flex;
        flex-direction: column;
        gap: 5px;
    }
    .stars {
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        background-color: var(--light-1);
        border-radius: 10px;
        padding: 5px;
    }
    .stars p {
        margin: 0px;
    }
    .star {
        color: var(--light-5);
        margin: 0 2px;
    }
    .rating-label {
        text-align: center;
        margin-top: 5px;
    }
</style>