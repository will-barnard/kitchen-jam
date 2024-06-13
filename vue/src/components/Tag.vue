<template>
    <div>
        <p>#{{tag.tagName}}</p><p class="x" v-if="!edit">X</p>
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
        white-space: nowrap;
        padding: 0px;
    }
    .x {
        margin-left: 5px;
        margin-right: 5px;
    }
    div {
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;
    }
</style>