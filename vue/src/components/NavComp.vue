<template>
    <div class="global">
        <div :class="hamburger">
            <div class="container" :class="toggle?'change':''" @click="toggle = !toggle;">
                <div class="bar1"></div>
                <div class="bar2"></div>
                <div class="bar3"></div>
            </div>
        </div>
            <div :class="menu">
                <div class="spacer"></div>
                <p @click="push({ name: 'home' })" >Home</p>
                <p @click="push({ name: 'meal-log' })" v-if="$store.state.token != ''">Meal Log</p>
                <p @click="push({ name: 'cookbook' })" v-if="$store.state.token != ''">Cookbook</p>
                <p @click="push({ name: 'logout' })" v-if="$store.state.token != ''">Logout</p>
            <div :class="white"></div>
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
        },
        hamburger() {
            if (!this.toggle) {
                return "hamburger hamburger-closed";
            } else {
                return "hamburger hamburger-open";
            }
        },
        white() {
            if (!this.toggle) {
                return "white white-closed";
            } else {
                return "white white-open";
            }
        }
    },
    created() {
        document.body.addEventListener('click', (e) => {
            if (!this.$el.contains(e.target)) {
                this.toggle = false;
            }
        });
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
        bottom: -10px;
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
        bottom: -40vh;
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
        width: 100%;
        height: 30px;
        background-color: var(--dark-1);
        z-index: -1;
    }
    .white-closed {
        bottom: -40px;
        transition: bottom .5s ease;
    }
    .white-open {
        bottom: 0px;
        transition: bottom .5s ease;
    }
    .hamburger {
        background-color: var(--dark-2);
        border-radius: 100%;
        right: 30px;
        position: fixed;
        user-select: none;
    }
    .hamburger-closed {
        bottom: 30px;
        transition: bottom .5s ease;

    }
    .hamburger-open {
        bottom: 250px;
        transition: bottom .5s ease;
        z-index: 1;

    }
    .spacer {
        height: 50px;
    }
    .container {
        padding: 18px;
    }

    .bar1, .bar2, .bar3 {
    width: 42px;
    height: 6px;
    background-color: var(--text-color);
    margin: 6px 0;
    transition: 0.4s;
    border-radius: 3px;
  }
    /* Rotate first bar */
  .change .bar1 {
    transform: translate(0, 11px) rotate(-90deg);
  }
  /* Fade out the second bar */
  .change .bar2 {
      opacity: 0;
      transition: 0.1s;
  }
  /* Rotate last bar */
  .change .bar3 {
    transform: translate(0, -11px) rotate(90deg);
  }
</style>