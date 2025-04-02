<template>
   <header>
        <div class="main">
            <h1 v-on:click="log()">Meal Log</h1>
            <div class="spacer"></div>
            <nav>
                <p v-on:click="newMeal()" v-show="!create">
                    <i class="fa fa-plus"></i> Log Meal
                </p>
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
    .main {
        margin-top: 20px;
    }
    header {
        text-align: center;
    }
    nav {
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        margin-right: 20px;
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
        background-color: var(--light-6);
        margin: 5px;
        border-radius: 10px;
        padding: 8px;
        font-weight: bold;
    }
    p:hover {
        cursor: pointer;
    }
    h1 {
        margin-left: 25px;
        margin-bottom: 0px;
        margin-top: 0px;
        background-color: var(--light-8);
        border-radius: 10px 10px 0px 0px;
        padding: 10px;
        padding-bottom: 5px;
        text-align: left;
    }
    .spacer {
        flex-grow: 1;
    }
</style>