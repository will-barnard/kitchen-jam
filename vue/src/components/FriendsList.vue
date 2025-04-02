<template>
    <div class="friend-area">
        <input 
            type="text" 
            v-model="searchQuery" 
            placeholder="Search friends..." 
            class="search-input"
        />
        <div v-if="$store.state.loadedFriends && filteredUsers.length === 0">
            <p>No friends found.</p>
        </div>
        <div v-for="user in filteredUsers" :key="user.id" class="feed-results">
            <p class="user-name">{{ user.username }}</p>
            <div class="spacer"></div>
            <div class="user-button view-profile" @click="$router.push({name: 'profile', params: {userId: user.friendId}})">
                <p><i class="fa fa-user"></i> Profile</p>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            searchQuery: ''
        };
    },
    computed: {
        users() {
            return this.$store.state.userFriends;
        },
        filteredUsers() {
            return this.users.filter(user => 
                user.username.toLowerCase().includes(this.searchQuery.toLowerCase())
            );
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
        border-radius: 10px;
        background-color: var(--light-1);
        font-size: .8em;
    }
    .spacer {
        flex-grow: 1;
    }
    .user-name {
        font-weight: bold;
        margin-left: 10px;
    }
    .view-profile {
        background-color: var(--light-8);
    }
    .search-input {
        margin-bottom: 10px;
        padding: 8px;
        border-radius: 5px;
        border: 1px;
        width: 100%;
        box-sizing: border-box;
    }
</style>