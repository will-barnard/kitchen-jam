<template>
   <header>
        <div>
            <h1 v-on:click="log()">Meal Log</h1>
            <div class="spacer">

            </div>
            <nav>
                <p v-on:click="newMeal()" v-show="!create">+ Log New Meal</p>
            </nav>
        </div> 
    </header>
</template>
<script>

export default {
    props: ['create'],
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
        align-items: center;
    }
    div {
        margin: 0px;
        display: flex;
        justify-content: center;
        align-items: center;
    }
    p {
        margin: 0px;
        display: inline;
        background-color: var(--light-1);
        margin: 5px;
        border-radius: 10px;
        padding: 8px;
        font-weight: bold;
    }
    p:hover {
        cursor: pointer;
    }
    h1 {
        margin: 10px;
        text-align: left;
    }
    .spacer {
        flex-grow: 1;
    }
</style>