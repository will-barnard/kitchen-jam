<template>
    <div>
        <TopBanner />
        <div class="main">
            <h3 class="child" @click="$router.push({ name: 'meal-log' })">Meal Log</h3>
            <h3 class="child" @click="$router.push({ name: 'cookbook' })">Cookbook</h3>
            <h3 class="child" @click="$router.push({ name: 'profile' , params: {userId: $store.state.user.id}})">Profile</h3>
            <h3 class="child" @click="$router.push({ name: 'about' })">About KJ</h3>
            <h3 class="child" @click="$router.push({ name: 'updates' })">Site Updates</h3>
        </div>
    </div>
</template>

<script>
import TopBanner from '../components/TopBanner.vue';

export default {
    components: {TopBanner}
}
</script>

<style scoped>
    .main {
        display: flex;
        justify-content: center;
        width: 100%;
        background-color: var(--light-2);
        border-radius: 15px;
        flex-wrap: wrap;
    }
    .child {
        text-align: center;
        border: 2px solid var(--dark-2);
        border-radius: 10px;
        padding: 15px;
        margin: 15px;
        width: 30vw
    }
</style>