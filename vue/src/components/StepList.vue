<template>
    <div class="block">
        <div>
            <h3>Steps</h3>
            <div v-for="step in stepList" class="step-item">
                <p>{{ step.stepOrder }}. {{ step.stepDescription }}</p>
            </div>
        </div>
    </div>
</template>
<script>
export default {
    props: ['stepList']
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
    p {
        margin: 3px;
        padding: 5px;
    }
    h3 {
        margin: 5px;
        margin-left: 10px;
        text-align: left;
    }
</style>