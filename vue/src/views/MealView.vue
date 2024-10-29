<template>
        <div v-show="show">

            <MealMenu />

            <Transition name="quickFade">
                <body v-show="show">
                    <MealLog :mealList="$store.state.userMeals"/>
                </body>
            </Transition>

        </div>
</template>

<script>
import MealLog from '../components/MealLog.vue';
import MealMenu from '../components/MealMenu.vue';


export default {
    components: {MealLog, MealMenu},
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
    }
}
</script>

<style scoped>
    body {
        text-align: center;
    }
    nav {
        display: flex;
        flex-direction: row;
        justify-content: center;
    }
    div {
        margin: 0px;
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