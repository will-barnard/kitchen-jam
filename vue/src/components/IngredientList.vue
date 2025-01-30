<template>
    <div class="block">
        <div>
            <h3>Ingredients</h3>
            <div v-for="ingredient in ingredientList" class="step-item">
                <ul>&#8226; {{ ingredient.quantity }} {{ ingredient.ingredientName }}</ul>
            </div>
        </div>
    </div>
</template>
<script>
export default {
    props: ['ingredientList']
}
</script>
<style scoped>
    .block {
        margin-left: 0px;
        margin-top: 10px;
        padding: 10px;
        background-color: var(--light-1);
        border-radius: 10px;
        flex-grow: 1;
    }
    .step-item {
        background-color: var(--light-2);
        border-radius: 5px;
        border: 1px;
    }
    ul {
        margin: 3px;
        padding: 5px;
    }
    h3 {
        margin: 5px;
        text-align: right;
    }
</style>