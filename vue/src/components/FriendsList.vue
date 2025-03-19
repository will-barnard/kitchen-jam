<template>
    <div class="friend-area">
        <div v-if="$store.state.loadedFriends && $store.state.userFriends.length === 0">
            <p>No friends yet. Search for users to add as friends.</p>
        </div>
        <div v-for="user in users" :key="user.id" class="feed-results">
            <p class="user-name">{{ user.username }}</p>
            <div class="spacer"></div>
            <div class="user-button view-profile" @click="$router.push({name: 'profile', params: {userId: user.friendId}})">
                <p>View Profile</p>
            </div>
        </div>

    </div>
</template>

<script>
export default {
    computed: {
        users() {
            return this.$store.state.userFriends;
        }
    }
}
</script>

<style scoped>
    p {
        margin: 0px;
    }
    .friend-area {
        background-color: var(--light-2);
        border-radius: 10px;
        padding: 15px;
        margin: 5px;
        display: flex;
        flex-direction: column;
    }
    .feed-results {
        padding: 10px;
        margin-bottom: 5px;
        margin-top: 5px;
        background-color: var(--light-1);
        border-radius: 10px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    .user-button {
        cursor: pointer;
        padding: 5px;
        margin: 0 5px;
        border: 2px solid var(--light-2);
        border-radius: 10px;
        background-color: var(--light-1);
        font-size: .8em;
    }
    .spacer {
        flex-grow: 1;
    }
    .user-name {
        font-weight: bold;
    }
    .view-profile {
        background-color: var(--light-8);
    }
</style>