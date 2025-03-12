<template>
    <div>
        <TopBanner />
        <div class="controls">
            <div>
                <h3 :class="{ selected: control === 'recipes' }" @click="control = 'recipes'">Recipes</h3>
            </div>
            <div>
                <h3 :class="{ selected: control === 'tags' }" @click="control = 'tags'">Tags</h3>
            </div>
            <div>
                <h3 :class="{ selected: control === 'categories' }" @click="control = 'categories'">Categories</h3>
            </div>
        </div>
        <RecipeJam v-show="control === 'recipes'"/>
        <TagJam v-show="control === 'tags'"/>
        <CategoryJam v-show="control === 'categories'"/>
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
            control: "recipes"
        }
    }
}
</script>
<style>
    h3 {
        margin: 3px;
    }
    .block {
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