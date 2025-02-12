<template>
    <div>
        <TopBanner />
        <Transition name="fade">
            <DashboardComp v-if="show"/>
        </Transition>
    </div>
</template>

<script>
import TopBanner from '../components/TopBanner.vue';
import DashboardComp from '../components/DashboardComp.vue';

export default {
    components: {TopBanner, DashboardComp},
    data() {
        return {
            show: false
        }
    },
    created() {
        setTimeout(() => {
            this.show = true;
        }, 10);
    }
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
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    text-align: center;
    border: 3px solid var(--dark-2);
    border-radius: 10px;
    padding: 5px 10px; /* Adjusted padding */
    margin: 10px; /* Adjusted margin */
    width: 25vw; /* Adjusted width */
    height: 120px; /* Adjusted height */
    transition: transform 0.3s, background-color 0.3s, color 0.3s;
    cursor: pointer;
}

.child.large {
    width: 40vw; /* Larger width for the first two elements */
    height: 40vw; /* Make the first two elements square */
}

.child i {
    font-size: 1.8em; /* Adjusted font size */
    margin-bottom: 0;
    display: block;
}

.child span {
    margin-top: 5px;
    font-size: 1.2em;
    font-weight: bold;
    color: var(--primary);
}

.child:hover {
    background-color: var(--light-1);
    color: var(--dark-1);
}

.child:active {
    background-color: var(--dark-1);
    color: var(--light-1);
}

@media (max-width: 600px) {
    .child {
        width: 30vw; /* Adjusted width for mobile screens */
        height: 100px; /* Adjusted height for mobile screens */
    }
    .child.large {
        width: 45vw; /* Adjusted larger width for mobile screens */
        height: 45vw; /* Make the first two elements square on mobile screens */
    }
}
</style>