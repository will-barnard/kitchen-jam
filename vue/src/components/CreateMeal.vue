<template>

    <body>
        <form v-on:submit.prevent="createMeal()">
            <div>
                <h2>name</h2>
                <h3>date</h3>
            </div>
            <div>
                <p>comment</p>
                <p>cook time</p>
                <p>Rating</p>
                <p>ingredients</p>
                <p>notes</p>
            </div>
            <div>
                <!-- img goes here -->
            </div>
            <submit />
        </form> 
    </body>

</template>
<script>
export default {
}
</script>

<style scoped>
    body {
        display: flex;
        flex-direction: row;
        padding: 5px;
        border: 1px solid black;
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
</style>