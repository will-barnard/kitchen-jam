<template>
    <div>
        Steplist
        <ol>
            <div v-for="step in stepList">
            <p>{{ step.stepDescription }}</p>
        </div>
        </ol>
    </div>
</template>
<script>
export default {
    props: ['stepList']
}
</script>
<style>
    
</style>

<!-- private int stepId;
    private int userId;
    private int recipeId;
    private String stepDescription;
    private int stepOrder; -->