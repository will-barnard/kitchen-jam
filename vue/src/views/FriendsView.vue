<template>
    <div>
        <TopBanner />
        <h1>Friends</h1>
        <div class="toggle-buttons">
            <h3 :class="{ active: currentView === views.FEED }" @click="toggleView(views.FEED)">Feed</h3>
            <h3 :class="{ active: currentView === views.FRIENDS }" @click="toggleView(views.FRIENDS)">Friends</h3>
            <h3 :class="{ active: currentView === views.SEARCH }" @click="toggleView(views.SEARCH)">Search</h3>
            <h3 :class="{ active: currentView === views.REQUESTS }" @click="toggleView(views.REQUESTS)">Requests</h3>
        </div>
        <div v-if="currentView === views.FEED">
            <FriendFeed />
        </div>
        <div v-else-if="currentView === views.FRIENDS">
            <FriendsList />
        </div>
        <div v-else-if="currentView === views.SEARCH">
            <SearchUsers @change-view="toggleView" />
        </div>
        <div v-else>
            <FriendRequests />
        </div>
    </div>
</template>

<script>
import TopBanner from '../components/TopBanner.vue';
import FriendFeed from '../components/FriendFeed.vue';
import FriendsList from '../components/FriendsList.vue';
import SearchUsers from '../components/SearchUsers.vue';
import FriendRequests from '../components/FriendRequests.vue';

export default {
    components: {TopBanner, FriendFeed, FriendsList, SearchUsers, FriendRequests},
    data() {
        return {
            views: {
                FEED: 'feed',
                FRIENDS: 'friends',
                SEARCH: 'search',
                REQUESTS: 'requests'
            },
            currentView: 'feed'
        }
    },
    created() {
        const query = this.$route.query;
        if (query.view && Object.values(this.views).includes(query.view)) {
            this.currentView = query.view;
        }
    },
    methods: {
        toggleView(view) {
            this.currentView = view;
            this.$router.push({ query: { view: view } });
        }
    }
}
</script>
<style scoped>
.toggle-buttons {
    display: flex;
    justify-content: center;
    margin: 10px;
}
.toggle-buttons h3 {
    cursor: pointer;
    padding: 10px;
    margin: 0 5px;
    border: 2px solid var(--light-1);
    border-radius: 10px;
    background-color: var(--light-1);
}
.toggle-buttons .active {
    background-color: var(--light-2);
}
.toggle-buttons h3 {
    font-size: 1em;
}
h1 {
    margin: 0px;
}
</style>