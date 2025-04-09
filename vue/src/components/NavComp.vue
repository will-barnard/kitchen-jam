<template>
    <div class="global">
        <div :class="!toggle ? 'hamburger-container hc-closed' : 'hamburger-container hc-open'">
            <div class="notification" @click="$router.push({ name: 'home' }); toggle = false;">
                <i class="fas fa-home"></i>
            </div>
            <div class="notification" @click="$router.push({ name: 'notifications' }); toggle = false;" v-if="$store.state.token != ''">
                <i class="fas fa-bell"></i>
                <span v-if="$store.state.loadedNotifications && $store.state.userNotifications.length > 0" class="notification-badge">
                    {{ $store.state.userNotifications.length }}
                </span>
            </div>
            <div :class="hamburger" v-if="$store.state.token != ''">
                <div class="container" :class="toggle?'change':''" @click="toggle = !toggle;">
                    <div class="bar1"></div>
                    <div class="bar2"></div>
                    <div class="bar3"></div>
                </div>
            </div>
        </div>
        <div :class="menu">
            <div class="spacer"></div>
            <div class="single-row">
                <p @click="push({ name: 'meal-log' })" v-if="$store.state.token != ''">Meal Log</p>
                <p @click="push({ name: 'cookbook' })" v-if="$store.state.token != ''">Cookbook</p>
            </div>
            <p @click="push({ name: 'dashboard' })" v-if="$store.state.token != ''">Dashboard</p>
            <p @click="push({ name: 'logout' })" v-if="$store.state.token != ''">Logout</p>
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
                return "nav nav-open";
            } else {
                return "nav nav-closed";
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
.global p {
    width: 75vw;
    background-color: var(--light-1);
    text-align: center;
    font-weight: bold;
    border-radius: 10px;
    padding: 10px;
    margin: 12px;
    user-select: none;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    transition: transform 0.2s, background-color 0.2s;
}
.global p:hover {
    background-color: var(--light-orange);
    transform: scale(1.05);
}
.single-row p {
    display: flex;
    justify-content: space-around;
    width: 33vw;
    margin: 8px;
}
.nav {
    display: flex;
    flex-direction: column;
    justify-content: start;
    align-items: center;
    position: fixed;
    background: linear-gradient(135deg, var(--dark-1), var(--light-orange));
    left: 0px;
    right: 0px;
    width: 100vw;
    border-radius: 0px 0px 0px 0px;
    height: 300px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
}
.nav-open {
    bottom: -250px;
    transition: bottom .5s ease;
}
.nav-closed {
    bottom: -10px;
    transition: bottom .5s ease;
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
.spacer {
    height: 50px;
}

/* HAMBURGER */
.hamburger {
    background-color: var(--dark-1);
    width: 50px; /* Match the notification size */
    height: 50px; /* Match the notification size */
    display: flex; /* Center content inside */
    justify-content: center; /* Center horizontally */
    align-items: center; /* Center vertically */
    user-select: none; /* Prevent text selection */
    z-index: 11; /* Ensure it appears above other elements */
}
.hamburger-closed {
    bottom: -5px;
    transition: bottom .485s ease;
}
.hamburger-open {
    bottom: 265px;
    transition: bottom .5s ease;
}
.container {
    padding: 10px;
}
.bar1, .bar2, .bar3 {
    width: 30px;
    height: 5px;
    background-color: var(--text-color);
    margin: 4px 0;
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
.hamburger-container {
    display: flex;
    flex-direction: row;
    align-items: center; /* Align items vertically */
    position: fixed;
    right: 0px; /* Add spacing from the right edge */
    z-index: 10; /* Ensure it appears above other elements */
}
.notification {
    position: relative; /* Make the badge position relative to the notification */
    background-color: var(--dark-1); /* Match the hamburger background */
    width: 50px; /* Set width to match the hamburger size */
    height: 50px; /* Set height to match the hamburger size */
    display: flex; /* Center content inside */
    justify-content: center; /* Center horizontally */
    align-items: center; /* Center vertically */
    user-select: none; /* Prevent text selection */
    z-index: 11; /* Ensure it appears above other elements */
}
.notification i {
    color: var(--text-color); /* Match the text color of the hamburger */
    font-size: 20px; /* Adjust icon size */
}
.notification-badge {
    position: absolute;
    top: 5px; /* Position above the bell */
    right: 5px; /* Position to the top-right corner */
    background-color: red;
    color: white;
    border-radius: 50%;
    padding: 2px 6px;
    font-size: 0.8em;
    font-weight: bold;
    z-index: 12; /* Ensure it appears above the notification icon */
}
.hc-closed {
    bottom: 0px;
    transition: bottom .485s ease;
}
.hc-open {
    bottom: 265px;
    transition: bottom .5s ease;
}
</style>