<template>
    <div class="global">
            <div :class="menu">
                <img src="/img/burger-bar-tinted.png" @click="toggle = !toggle"/>
                <p @click="push({ name: 'home' })" >Home</p>
                <p @click="push({ name: 'meal-log' })" v-if="$store.state.token != ''">Log</p>
                <p @click="push({ name: 'cookbook' })" v-if="$store.state.token != ''">Cookbook</p>
                <p @click="push({ name: 'logout' })" v-if="$store.state.token != ''">Logout</p>
            </div>
            <div class="white">
            </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            toggle: false
        }
    },
    methods: {
        push(route) {
            this.$router.push(route);
            this.toggle = false;
        }
    },
    computed: {
        menu() {
            if (!this.toggle) {
                return "nav-open";
            } else {
                return "nav";
            }
        }
    }
}
</script>

<style scoped>
    .global {
        display: flex;
        justify-content: center;
    }
    .global p {
        width: 75vw;
        background-color: var(--light-1);
        text-align: center;
        font-style: bold;
        border-radius: 10px;
        padding: 5px;
        margin: 12px;
        user-select: none;
    }
    .global p:hover {
        background-color: var(--light-orange);
    }
    .nav {

        display: flex;
        flex-direction: column;
        justify-content: start;
        align-items: center;

        background-color: var(--dark-1);

        position: fixed;
        left: 0px;
        right: 0px;
        width: 100vw;
        border-radius: 20px;

        height: 300px;
        bottom: 0px;
        transition: bottom .5s ease;
    }
    .nav-open {
        display: flex;
        flex-direction: column;
        justify-content: start;
        align-items: center;

        background-color: var(--dark-1);

        position: fixed;
        left: 0px;
        right: 0px;
        width: 100vw;
        border-radius: 20px;

        height: 300px;
        bottom: -215px;
        transition: bottom .5s ease;
    }
    .nav img {
        height: 8vh;
        margin-top: 10px;

    }
    .nav-open img {
        height: 8vh;
        margin-top: 10px;
    }
    .white {
        position: fixed;
        bottom: 0px;
        height: 30px;
        width: 100vw;
        left: 0px;
        right: 0px;
        background-color: var(--dark-1);
        z-index: -1;
    }
</style>