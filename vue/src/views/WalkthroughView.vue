<template>
    <div>
        <TopBanner />
        <h2 class="title">Site Walkthrough</h2>
        <div class="main-area" v-if="showMain">
            <DashboardComp :admin="false" :clickable="$store.state.token ? true : false" />
            <div class="walkthrough-container">
                <div v-for="item of info" :key="item.title" class="child" @click="switchShowWalkthrough(item.title)">
                    <h3>{{ item.title }}</h3>
                    <p>{{ item.copy }}</p>
                    <div class="spacer"></div>
                    <i class="fas fa-long-arrow-alt-right" v-if="item.title !== 'About'"></i>
                </div>
            </div>
        </div>
        <div v-if="!showMain">
            <h2>{{ showWalkthrough }}</h2>
            <p>Here you can find information about {{ showWalkthrough }}.</p>
            <button @click="showMain = true">Back to Main</button>
        </div>
    </div>
</template>

<script>
import TopBanner from '../components/TopBanner.vue';
import DashboardComp from '../components/DashboardComp.vue';

export default {
    components: {
        TopBanner, DashboardComp
    },
    data() {
        return {
            showMain: true,
            showWalkthrough: "",
            info: [
                { 
                    title: "Meals", icon: "fa-solid fa-utensils",
                    copy: "Meals are the heart of Kitchen Jam. They are the main way you log your cooking."
                },
                { 
                    title: "Cookbook", icon: "fa-solid fa-book",
                    copy: "View all the times you've cooked a recipe and hone the ingredients and steps."
                },
                { 
                    title: "Jam Page", icon: "fa-solid fa-guitar",
                    copy: "Jam out! See insights into your cooking habits and how you can improve."
                },
                { 
                    title: "Friends", icon: "fa-solid fa-user-friends",
                    copy: "Find you friends - see what they are cooking and share your own recipes." 
                },
                { 
                    title: "Notifications", icon: "fa-solid fa-bell",
                    copy: "Stay up to date. See new comments on your posts and new friend requests."
                },
                { 
                    title: "Profile", icon: "fa-solid fa-user",
                    copy: "Show off your cooking personality! Share your profile with friends."
                },
                { 
                    title: "Settings", icon: "fa-solid fa-cog",
                    copy: "Change your account and privacy settings. You can also change your password here."
                },
                { 
                    title: "About", icon: "fa-solid fa-info-circle",
                    copy: "Find out more about the app. You probably navigated here from this page."
                },
                { 
                    title: "Updates", icon: "fa-solid fa-bell",
                    copy: "Changelog. Find out about the latest features and bug fixes."
                },
            ]
        }
    },
    methods: {
        switchShowWalkthrough(title) {
            if (title !== 'About') {
                this.showWalkthrough = title;
                this.showMain = false;
                document.title = `Walkthrough - Kitchen Jam`; // Set dynamic title
                this.$router.push({ name: 'walkthrough', query: { title: title } });
            }
        }
    },
}
</script>

<style scoped>
    .main-area {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
    }
    .child {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: flex-start;
        width: 85%;
        height: 120px;
        margin: 10px;
        margin-bottom: 20px;
        text-align: left;
        border: 3px solid var(--dark-2);
        border-radius: 10px;
        padding: 5px;
    }
    p, h3 {
        margin: 0;
        padding: 0;
        text-align: left;
    }
    .walkthrough-container {
        margin-top: 10px;
    }
    .spacer {
        flex-grow: 1;
    }
    .title {
        text-align: center;
        margin-top: 20px;
        font-size: 2em;
    }
</style>