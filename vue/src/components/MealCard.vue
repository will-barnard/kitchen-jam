<template>

    <body>

        <div class="meal-img">
            <img src="src/img/placeholder.jpeg" />
            <!-- img goes here -->
        </div>

        <div class="content">
            <div class="title">
                <h2 class="name">{{ meal.mealName }}</h2>
                <h3>{{ meal.date }}</h3>
            </div>
            <div class="info">
                <p>{{ meal.mealComment }}</p>
                <p>{{ meal.cookTime }} min</p>
                <p>{{ meal.rating }} / 10 Rating</p>
                <p>{{ meal.ingredients }}</p>
                <p>{{ meal.notes }}</p>
            </div>
        </div>

    </body>

</template>
<script>
export default {
    props: ['meal']
}
</script>

<style scoped>
    body {
        display: flex;
        flex-direction: row;
        padding: 5px;
        border: 1px solid black;
        height: 25vh;
    }
    div {
        display: flex;
        flex-direction: column; 
    }
    h2, h3 {
        display: inline;
        margin: 5px;
    }
    p {
        margin: 5px;
        text-align: left;
    }
    .content {
        display: flex;
        flex-direction: column;        
    }
    .meal-img {
        width: 40%;
        border: 1px solid black;
    }
    .meal-img img {
        object-fit: cover;
        height: 25vh;
    }
    .content {
        flex-grow: 1;
        margin-left: 10px;
    }
    .title {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        align-items: end;
        border: 1px solid black;
    }
    .info {
        border: 1px solid black;
        flex-grow: 1;
    }
</style>