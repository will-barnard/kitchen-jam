<template>
    <div>
        <Transition name="fade">
            <div class="search-results">
                <div class="search-area">
                    <input v-model="query" class="search-input" @input="searchUsers" placeholder="Search users..." />
                </div>
                <LoadingWidget v-if="loading" />
                <div v-if="!loading && users.length === 0 && query.length > 0">
                    <p>No users found.</p>
                </div>
                <div v-for="user in users" :key="user.id" class="search-result">
                    <div class="user-name">
                        <p>{{ user.displayName }}</p>
                    </div>
                    <div class="spacer"></div>
                    <div class="user-button">
                        <div class="add-friend" v-if="!isPending(user.userId) && !isFriend(user.userId) && !isRequest(user.userId)" @click="addFriend(user)">
                            <p><i class="fa fa-plus"></i> Add Friend</p>
                        </div>
                        <div class="pending" v-if="isRequest(user.userId)" @click="respondToRequest">
                            <p>Respond to Request</p>
                        </div>
                        <div class="pending" v-if="isPending(user.userId)" @click="cancelRequest(user)">
                            <p><i class="fa fa-times"></i> Requested</p>
                        </div>
                        <div class="friends" v-if="isFriend(user.userId)">
                            <p><i class="fa fa-check"></i> Friends</p>
                        </div>
                    </div>
                    <div class="user-button view-profile" @click="$router.push({name: 'profile', params: {userId: user.userId}})">
                        <p><i class="fa fa-user"></i> Profile</p>
                    </div>
                </div>
            </div>
        </Transition>
    </div>
</template>

<script>
import ProfileService from '@/services/ProfileService';
import LoadingWidget from './LoadingWidget.vue';
import FriendshipService from '../services/FriendshipService';

export default {
    components: {LoadingWidget},
    data() {
        return {
            query: '',
            users: [],
            loading: false
        };
    },
    computed: {
        userPending() {
            return this.$store.state.userPending;
        },
        userFriends() {
            return this.$store.state.userFriends;
        },
        userRequests() {
            return this.$store.state.userRequests;
        }
    },
    methods: {
        isPending(userId) {
            return this.userPending.some(request => request.friendId === userId);
        },
        isFriend(userId) {
            return this.userFriends.some(friend => friend.friendId === userId);
        },
        isRequest(userId) {
            return this.userRequests.some(request => request.friendId === userId);
        },
        async searchUsers() {
            if (this.query.length > 0) {
                try {
                    this.loading = true;
                    ProfileService.searchUsers(this.query).then(
                        (response) => {
                            this.users = response.data;
                            this.loading = false;
                        }
                    );
                } catch (error) {
                    console.error('Error searching users:', error);
                }
            } else {
                this.users = [];
            }
        },
        async addFriend(user) {
            try {
                await FriendshipService.createFriendRequest(user.userId);
                this.$store.commit('ADD_FRIEND_TO_PENDING', user);
            } catch (error) {
                console.error('Error sending friend request:', error);
            }
        },
        async cancelRequest(user) {
            try {
                await FriendshipService.cancelFriendRequest(user.userId);
                this.$store.commit('CANCEL_FRIEND_REQUEST', user.userId);
            } catch (error) {
                console.error('Error cancelling friend request:', error);
            }
        },
        respondToRequest() {
            this.$emit('change-view', 'requests');
        }
    }
};
</script>
<style scoped>
    .search-results {
        background-color: var(--light-2);
        border-radius: 10px;
        padding: 5px;
        margin: 5px;
        display: flex;
        flex-direction: column;
    }
    .search-result {
        padding: 5px;
        margin-bottom: 5px;
        margin-left: 10px;
        margin-right: 10px;
        background-color: var(--light-1);
        border-radius: 10px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    .search-result p {
        margin: 0px;
    }
    .search-area {
        display: flex;
        justify-content: center;
        margin: 10px;
    }
    .user-name {
        font-weight: bold;
        margin-left: 10px;

    }
    .user-button {
        cursor: pointer;
        margin: 0 5px;
        border-radius: 10px;
        background-color: var(--light-1);
        font-size: .8em;
    }
    .search-input {
        margin-bottom: 10px;
        padding: 8px;
        border-radius: 5px;
        border: 1px;
        width: 100%;
        box-sizing: border-box;
    }
    .spacer {
        flex-grow: 1;
    }
    .add-friend {
        background-color: var(--light-6);
        border-radius: 10px;
        padding: 5px;

    }
    .pending {
        background-color: var(--light-5);

        border-radius: 10px;
        padding: 5px;

    }
    .view-profile {
        background-color: var(--light-8);

        border-radius: 10px;
        padding: 5px;

    }
    .friends {
        background-color: var(--light-4);

        border-radius: 10px;
        padding: 5px;
    }
</style>