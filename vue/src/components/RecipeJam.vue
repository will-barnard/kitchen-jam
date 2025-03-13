<template>
    <div>
        <div class="block" v-show="!compact || compactShow == 'oldRecipes'">
            <div class="single-row">
                <h3 class="jam-title">Recipes not made recently:</h3>
                <div class="spacer"></div>
                <i class="fas fa-sync-alt refresh-icon" @click="shuffleOldRecipes"></i>
            </div>
            
            <MiniRecipeCard v-for="recipe in oldRecipes" :key="recipe.recipeId" :recipe="recipe"/>
            <div v-if="compact">
                <h4 class="jam-link" @click="$router.push({name: 'jam'})">Jam Page <i class="fas fa-arrow-right"></i></h4>
            </div>
        </div>
        <div class="block " v-show="!compact || compactShow == 'improveRecipes'">
            <div class="single-row">
                <h3 class="jam-title">Recipes to try for improvement:</h3>
                <div class="spacer"></div>
                <i class="fas fa-sync-alt refresh-icon" @click="shuffleImproveRecipes"></i>
            </div>
            <MiniRecipeCard v-for="recipe in improveRecipes" :key="recipe.recipeId" :recipe="recipe"/>
            <div v-if="compact">
                <h4 class="jam-link" @click="$router.push({name: 'jam'})">Jam Page <i class="fas fa-arrow-right"></i></h4>
            </div>
        </div>
    </div>
</template>
<script>
import MiniRecipeCard from './MiniRecipeCard.vue';

export default {
    props: ['compact'],
    components: {MiniRecipeCard},
    data() {
        return {
            compactShow: "",
            oldRand: 0,
            improveRand: 0
        }
    },
    computed: {
        oldRecipes() {
            let sortedRecipes = this.$store.state.userRecipes.slice().sort((a, b) => new Date(a.lastCreated) - new Date(b.lastCreated));
            let firstHalfRecipes = sortedRecipes.slice(0, Math.ceil(sortedRecipes.length / 2));
            let shuffledRecipes = firstHalfRecipes.sort(() => this.oldRand);
            return shuffledRecipes.slice(0, 3).map(() => firstHalfRecipes.splice(Math.floor(Math.random() * firstHalfRecipes.length), 1)[0]);
        },
        improveRecipes() {
            let sortedRecipes = this.$store.state.userRecipes.slice().sort((a, b) => a.avgRating - b.avgRating);
            let topHalfRecipes = sortedRecipes.slice(0, Math.ceil(sortedRecipes.length / 2));
            let shuffledRecipes = topHalfRecipes.sort(() => this.improveRand);
            return shuffledRecipes.slice(0, 3).map(() => topHalfRecipes.splice(Math.floor(Math.random() * topHalfRecipes.length), 1)[0]);
        }
    },
    created() {
        this.oldRand = 0.5 - Math.random();
        this.improveRand = 0.5 - Math.random();

        if (this.compact) {
            let randomInt = Math.floor(Math.random() * 2);
            if (randomInt == 0) {
                this.compactShow = "oldRecipes";
            } else {
                this.compactShow = "improveRecipes";
            }
        }
    },
    methods: {
        shuffleOldRecipes() {
            this.oldRand = 0.5 - Math.random();
            this.$forceUpdate();
        },
        shuffleImproveRecipes() {
            this.improveRand = 0.5 - Math.random();
            this.$forceUpdate();
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
</style>