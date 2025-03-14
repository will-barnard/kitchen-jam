<template>
    <div>
        <TopBanner />
        <div class="controls">
            <div>
                <h3 :class="{ selected: control === 'recipes' }" @click="updateControl('recipes')">Recipes</h3>
            </div>
            <div>
                <h3 :class="{ selected: control === 'tags' }" @click="updateControl('tags')">Tags</h3>
            </div>
            <div>
                <h3 :class="{ selected: control === 'categories' }" @click="updateControl('categories')">Categories</h3>
            </div>
        </div>
        <div v-show="control === 'recipes'">
            <RecipeJam v-if="validRecipes"/>
            <h3 v-if="!validRecipes && $store.state.loadedRecipes">Log more recipes to generate insights</h3>
        </div>
        <div v-show="control === 'tags'">
            <TagJam v-show="validTags"/>
            <h3 v-if="!validTags && $store.state.loadedTags">Create more tags to generate insights</h3>
        </div>
        <div v-show="control === 'categories'">
            <CategoryJam v-show="validCategories"/>
            <h3 v-if="!validCategories && $store.state.loadedCategories">Create more categories to generate insights</h3>
        </div>
    </div>
</template>

<script>
import TopBanner from '../components/TopBanner.vue';
import MiniRecipeCard from '../components/MiniRecipeCard.vue';
import RecipeJam from '../components/RecipeJam.vue';
import TagJam from '../components/TagJam.vue';
import CategoryJam from '../components/CategoryJam.vue';

export default {
    components: {TopBanner, MiniRecipeCard, RecipeJam, TagJam, CategoryJam},
    data() {
        return {
            control: this.$route.query.tab || "recipes",
        }
    },
    watch: {
        '$route.query.tab'(newControl) {
            this.control = newControl || 'recipes';
        }
    },
    methods: {
        updateControl(newControl) {
            this.control = newControl;
            this.$router.push({ query: { tab: newControl } });
        }
    },
    computed: {
        validRecipes() {
            return this.$store.state.userRecipes.length > 4 ? true : false;
        },
        validTags() {
            return this.$store.state.userTags.length > 10 ? true : false;
        },
        validCategories() {
            return this.$store.state.userCategories.length > 2 ? true : false;
        }
    }
}
</script>
<style>
    h3 {
        margin: 3px;
    }
    .jam-block {
        display: flex;
        justify-content: center;
        width: 100%;
        background-color: var(--light-2);
        border-radius: 15px;
        flex-wrap: wrap;
        padding-top: 10px;
        padding-bottom: 10px;
        margin-top: 10px;
    }
    .controls {
        display: flex;
        justify-content: space-between;
        margin: 10px;
    }
    .controls h3 {
        cursor: pointer;
        border: 1px;
        border-radius: 10px;
        padding: 10px;
        background-color: var(--light-2);
    }
    .controls h3.selected {
        font-weight: bold;
        background-color: var(--light-5);
    }
</style>