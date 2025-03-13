<template>
    <div>
        <TopBanner />
        <Transition>
            <div v-if="loaded">
                <div class="tag-name">
                    <h1>{{ tag.tagName }}</h1>
                </div>
                <h3 class="used-in">Tag used in:</h3>
                <MealCard v-for="meal in mealList" :key="meal.mealId" :meal="meal" />
            </div>
        </Transition>
        <div v-if="notFound && loaded">
            <p>Tag not found</p>
        </div>
    </div>
</template>
<script>
import MealCard from '../components/MealCard.vue';
import TopBanner from '../components/TopBanner.vue';

export default {
    components: {TopBanner, MealCard},
    data() {
        return {
            tag: null,
            mealList: [],
            notFound: false,
            loaded: false
        }
    },
    created() {
        if (this.$store.state.loadedTags == false) {
            this.loadingTick();
        } else {
            this.tag = this.$store.state.userTags.find(tag => tag.tagId == this.$route.params.tagId);
            if (!this.tag) {
                this.notFound = true;
            } else {
                this.getMeals();
            }
            this.loaded = true;
        }
    },
    methods: {
        loadingTick() {
            setTimeout(() => {
                if (this.$store.state.loadedTags == false) {
                    this.loadingTick();
                } else {
                    this.tag = this.$store.state.userTags.find(tag => tag.tagId == this.$route.params.tagId);
                    if (!this.tag) {
                        this.notFound = true;
                    } else {
                        this.getMeals();
                    }
                    this.loaded = true;
                }
            }, 500);
        },
        getMeals() {
            this.mealList = this.$store.state.userMeals.filter(meal => meal.tags.some(t => t.tagId == this.tag.tagId));
        }
    }
}
</script>

<style scoped>
    .tag-name {
        display: flex;
        justify-content: center;
        margin-bottom: 10px;
    }
    h1 {
        margin: 0;
        background-color: var(--light-8);
        border-radius: 10px;
        padding: 10px;
        padding-left: 20px;
        padding-right: 20px;
        margin: 5px;
    }
    .used-in {
        margin-left: 10px;
    }
</style>