<template>
   <header>
        <h1>Meal Log</h1>
        <nav>
            <p v-on:click="log()">My Log</p>
            <p v-on:click="newMeal()">Log New Meal</p>
        </nav>
    </header>
</template>
<script>

export default {
    methods: {
        log() {
            this.$router.push({ name: 'meal-log'});
        },
        newMeal() {
            this.$router.push({ name: 'new-meal'})
        }
    }
}
</script>


<style scoped>
    header {
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