<template>
    <header>
        <div>
            <h1 v-on:click="log()">
                <i class="fa fa-book"></i>&nbsp;Cookbook
            </h1>
            <div class="spacer"></div>
            <nav>
                <p v-on:click="newMeal()" v-show="!create"><i class="fa fa-plus"></i> New Recipe</p>
            </nav>
        </div>
    </header>
</template>
<script>
export default {
    props: ['create'],
    methods: {
        log() {
            this.$router.push({ name: 'cookbook' });
        },
        newMeal() {
            this.$router.push({ name: 'new-recipe' });
        }
    }
}
</script>
<style scoped>
header {
    text-align: center;
    margin-top: 20px;
    position: relative; /* allow positioning */
    top: 1px; /* Move the menu down by 1 pixel */
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
    background-color: var(--light-5);
    border-radius: 10px 10px 0px 0px;
    padding: 10px;
    padding-bottom: 5px;
    text-align: left;
    font-size: 1.5em;
}
.spacer {
    flex-grow: 1;
}
</style>