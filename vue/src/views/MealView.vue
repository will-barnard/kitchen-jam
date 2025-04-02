<template>
        <div v-show="show">

            <MealMenu />
            <LoadingWidget v-if="!showLoading"/>  
            <Transition name="quickFade">
                <body v-show="show">
                    <MealLog :mealList="$store.state.userMeals"/>
                </body>
            </Transition>
            <div v-show="$store.state.loadedMeals && $store.state.userMeals.length < 1">
                <h3>You haven't logged any meals yet</h3>
            </div>
        </div>
</template>

<script>
import LoadingWidget from '../components/LoadingWidget.vue';
import MealLog from '../components/MealLog.vue';
import MealMenu from '../components/MealMenu.vue';


export default {
    components: {MealLog, MealMenu, LoadingWidget},
    data() {
        return {
            mealList: [],
            show: false,
        }
    },
    methods: {
        log() {
            this.tab = 'log';
        },
        newMeal() {
            this.tab = 'new';
        }
    },
    created() {
        if (!this.$store.state.userMeals.length < 1) {
            this.mealList = this.$store.state.userMeals;
        }
        setTimeout(
        () => {this.show = true}
        , 10)
    },
    computed: {
        showLoading() {
            if (!this.$store.state.loadedMeals) {
                return false;
            }
            else {
                return true;
            }
        }
    }
}
</script>

<style scoped>
    body {
        text-align: center;
        margin-top: 0;
        padding-top: 0;
    }
    nav {
        display: flex;
        flex-direction: row;
        justify-content: center;
    }
    div {
        margin: 0;
        padding: 0; 
    }
    p {
        margin: 0px;
        padding: 5px;
        display: inline;
        border: 1px solid black;
    }
    p:hover {
        cursor: pointer;
    }
</style>