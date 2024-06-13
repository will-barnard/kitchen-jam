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
        border: 1px solid var(--border-color);
        background-color: var(--light-1);
        margin: 5px;
        border-radius: 10px;
        padding: 6px;
        margin-bottom: 10px;
        margin-top: 0px;    
    }
    p:hover {
        cursor: pointer;
    }
    h1 {
        margin: 10px;
    }
</style>