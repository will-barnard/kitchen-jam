<template>
    <div>
        <p>{{tag.tagName}}</p><p class="x" v-if="!edit">X</p>
    </div>
</template>

<script>
export default {
    props: ['tag', 'edit']
}
</script>

<style scoped>
    p {
        display: inline;
    }
    .x {
        margin-left: 5px;
        margin-right: 5px;
    }
</style>