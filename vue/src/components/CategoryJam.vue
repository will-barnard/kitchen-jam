<template>
    <div>
        <div class="jam-block" v-show="!compact || compactShow == 'unusedCategories'">
            <div class="single-row">
                <h3 class="jam-title">Categories not used recently:</h3>
                <div class="spacer"></div>
                <i class="fas fa-sync-alt refresh-icon" @click="shuffleUnusedCategories"></i>
            </div>
            <MiniCategoryCard v-for="category in unusedCategories" :key="category.categoryId" :category="category" :showLastUsed="true" :showGoDetail="true"/>
            <div v-if="compact">
                <h4 class="jam-link" @click="$router.push({name: 'jam'})">Jam Page <i class="fas fa-arrow-right"></i></h4>
            </div>
        </div>
        <div class="jam-block" v-show="!compact || compactShow == 'popularCategories'">
            <div class="single-row">
                <h3 class="jam-title">{{ popularCategoriesTitle }}:</h3>
                <div class="spacer"></div>
                <i class="fas fa-exchange-alt toggle-icon" @click="togglePopularCategories"></i>
                <i :class="!expandPopular ? 'fas fa-chevron-right showmore-icon' : 'fas fa-chevron-down showmore-icon'" @click="expandPopularCategories"></i>
            </div>
            <MiniCategoryCard v-for="category in popularCategories" :key="category.categoryId" :category="category" :showTimesUsed="true" :showLastUsed="!showMostUsed" :showGoDetail="true"/>
            <div v-if="compact">
                <h4 class="jam-link" @click="$router.push({name: 'jam'})">Jam Page <i class="fas fa-arrow-right"></i></h4>
            </div>
        </div>
        <div class="jam-block" v-show="!compact || compactShow == 'recipesCategories'">
            <div class="single-row">
                <h3 class="jam-title">{{ recipesCategoriesTitle }}:</h3>
                <div class="spacer"></div>
                <i class="fas fa-exchange-alt toggle-icon" @click="toggleRecipesCategories"></i>
                <i :class="!expandRecipes ? 'fas fa-chevron-right showmore-icon' : 'fas fa-chevron-down showmore-icon'" @click="expandRecipesCategories"></i>
            </div>
            <MiniCategoryCard v-for="category in recipesCategories" :key="category.categoryId" :category="category" :showRecipeCount="true" :showGoDetail="true" :showCountRecipes="true"/>
            <div v-if="compact">
                <h4 class="jam-link" @click="$router.push({name: 'jam'})">Jam Page <i class="fas fa-arrow-right"></i></h4>
            </div>
        </div>
        <div class="jam-block" v-show="!compact || compactShow == 'avgRatingCategories'">
            <div class="single-row">
                <h3 class="jam-title">{{ avgRatingCategoriesTitle }}:</h3>
                <div class="spacer"></div>
                <i class="fas fa-exchange-alt toggle-icon" @click="toggleAvgRatingCategories"></i>
                <i :class="!expandAvgRating ? 'fas fa-chevron-right showmore-icon' : 'fas fa-chevron-down showmore-icon'" @click="expandAvgRatingCategories"></i>
            </div>
            <MiniCategoryCard v-for="category in avgRatingCategories" :key="category.categoryId" :category="category" :showAvgRating="true" :showGoDetail="true" :showRating="true"/>
            <div v-if="compact">
                <h4 class="jam-link" @click="$router.push({name: 'jam'})">Jam Page <i class="fas fa-arrow-right"></i></h4>
            </div>
        </div>
    </div>
</template>

<script>
import MiniCategoryCard from './MiniCategoryCard.vue';

export default {
    props: ['compact'],
    components: { MiniCategoryCard },
    data() {
        return {
            compactShow: "",
            unusedRand: 0,
            unusedCategoriesList: [],
            popularCategoriesList: [],
            recipesCategoriesList: [],
            avgRatingCategoriesList: [],
            expandPopular: false,
            expandRecipes: false,
            expandAvgRating: false,
            showMostUsed: false,
            showMostRecipes: false,
            showHighestAvgRating: false
        }
    },
    computed: {
        popularCategoriesTitle() {
            return this.showMostUsed ? 'Popular Categories' : 'Least Used Categories';
        },
        recipesCategoriesTitle() {
            return this.showMostRecipes ? 'Categories with most recipes' : 'Categories with least recipes';
        },
        avgRatingCategoriesTitle() {
            return this.showHighestAvgRating ? 'Categories with highest average rating' : 'Categories with lowest average rating';
        },
        unusedCategories() {
            if (this.unusedCategoriesList.length === 0) {
                let sortedCategories = this.$store.state.userCategories.sort((a, b) => {
                    if (!a.lastCreated) return 1;
                    if (!b.lastCreated) return -1;
                    return new Date(a.lastCreated) - new Date(b.lastCreated);
                });
                let firstHalfCategories = sortedCategories.slice(0, Math.ceil(sortedCategories.length / 2));
                let shuffledCategories = firstHalfCategories.sort(() => this.unusedRand);
                this.unusedCategoriesList = shuffledCategories.slice(0, 2).map(() => firstHalfCategories.splice(Math.floor(Math.random() * firstHalfCategories.length), 1)[0]);
            }
            return this.unusedCategoriesList;
        },
        popularCategories() {
            return this.getSortedCategories(this.showMostUsed ? 'countMeals' : 'countMeals', this.showMostUsed ? 'desc' : 'asc', this.expandPopular);
        },
        recipesCategories() {
            return this.getSortedCategories(this.showMostRecipes ? 'countRecipes' : 'countRecipes', this.showMostRecipes ? 'desc' : 'asc', this.expandRecipes);
        },
        avgRatingCategories() {
            return this.getSortedCategories(this.showHighestAvgRating ? 'avgRating' : 'avgRating', this.showHighestAvgRating ? 'desc' : 'asc', this.expandAvgRating);
        }
    },
    created() {
        if (this.compact) {
            let randomInt = Math.floor(Math.random() * 4);
            if (randomInt == 0) {
                this.compactShow = "unusedCategories";
            } else if (randomInt == 1) {
                this.compactShow = "popularCategories";
            } else if (randomInt == 2) {
                this.compactShow = "recipesCategories";
            } else {
                this.compactShow = "avgRatingCategories";
            }
        }
    },
    methods: {
        shuffleUnusedCategories() {
            this.unusedRand = 0.5 - Math.random();
            this.unusedCategoriesList = [];
        },
        expandPopularCategories() {
            this.expandPopular = !this.expandPopular;
        },
        togglePopularCategories() {
            this.showMostUsed = !this.showMostUsed;
            this.popularCategoriesList = [];
        },
        expandRecipesCategories() {
            this.expandRecipes = !this.expandRecipes;
        },
        toggleRecipesCategories() {
            this.showMostRecipes = !this.showMostRecipes;
            this.recipesCategoriesList = [];
        },
        expandAvgRatingCategories() {
            this.expandAvgRating = !this.expandAvgRating;
        },
        toggleAvgRatingCategories() {
            this.showHighestAvgRating = !this.showHighestAvgRating;
            this.avgRatingCategoriesList = [];
        },
        getSortedCategories(key, order, expand) {
            let sortedCategories = [...this.$store.state.userCategories].sort((a, b) => {
                if (order === 'asc') {
                    return a[key] - b[key];
                } else {
                    return b[key] - a[key];
                }
            });
            return sortedCategories.slice(0, expand ? sortedCategories.length : 2);
        }
    }
}
</script>

<style scoped>
    .jam-link {
        cursor: pointer;
        padding: 5px;
        margin: 2px;
        background-color: var(--light-5);
        border-radius: 5px;
    }
    .refresh-icon {
        cursor: pointer;
        margin-left: 10px;
        background-color: var(--light-4);
        padding: 5px;
        border-radius: 5px;
        margin-right: 20px;
    }
    .refresh-icon:hover {
        background-color: var(--light-6);
    }
    .showmore-icon {
        cursor: pointer;
        margin-left: 10px;
        background-color: var(--light-5);
        padding: 5px;
        border-radius: 5px;
        margin-right: 20px;
    }
    .showmore-icon:hover {
        background-color: var(--light-5);
    }
    .single-row {
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: space-between;
        width: 100%;
    }
    .spacer {
        flex-grow: 1;
    }
    .jam-title {
        margin-left: 20px;
    }
    .toggle-icon {
        cursor: pointer;
        margin-left: 10px;
        background-color: var(--light-5);
        padding: 5px;
        border-radius: 5px;
        margin-right: 20px;
    }
    .toggle-icon:hover {
        background-color: var(--light-6);
    }
</style>