<template>
    <div>
        Steplist
        <div>
            <ol>
                <div v-for="step in stepList">
                    <li>{{ step.stepDescription }}</li>
                </div>
            </ol>
        </div>
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