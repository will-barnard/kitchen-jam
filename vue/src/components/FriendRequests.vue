<template>
    <div class="request-area">
        <div v-if="$store.state.loadedRequests && $store.state.userRequests.length === 0">
            <p>No friend requests</p>
        </div>
        <div v-else>
            <div v-for="request in $store.state.userRequests" :key="request.userId">
                <div class="request">
                    <p class="user-name">{{ request.username }}</p>
                    <div class="spacer"></div>
                    <div class="user-button accept">
                        <p @click="acceptRequest(request)">Accept</p>
                    </div>
                    <div class="user-button reject">
                        <p @click="rejectRequest(request)">Reject</p>
                    </div>
                    <div class="user-button view-profile" @click="$router.push({name: 'profile', params: {userId: request.friendId}})">
                        <p>View Profile</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import FriendshipService from '../services/FriendshipService';

export default {
    methods: {
        async acceptRequest(request) {
            try {
                FriendshipService.acceptFriendRequest(request.friendId).then(
                    () => {
                        this.$store.commit('LOAD_USER_FRIENDS');
                    }
                );
            } catch (error) {
                console.error('Error accepting friend request:', error);
            }
        },
        async rejectRequest(request) {
            try {
                FriendshipService.rejectFriendRequest(request.friendId).then(
                    () => {
                       this.$store.commit('REJECT_FRIEND_REQUEST', request);
                    }
                );
            } catch (error) {
                console.error('Error rejecting friend request:', error);
            }
        }
    }
}
</script>

<style scoped>
p {
    margin: 0;
}
    .request-area {
        background-color: var(--light-2);
        border-radius: 10px;
        padding: 15px;
        margin: 5px;
        display: flex;
        flex-direction: column;
    }
    .request {
        padding: 10px;
        margin-bottom: 5px;
        margin-top: 5px;
        background-color: var(--light-1);
        border-radius: 10px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    .spacer {
        flex-grow: 1;
    }
    .user-name {
        font-weight: bold;
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
    .accept {
        background-color: var(--light-6);
    }
    .reject {
        background-color: var(--light-9);
    }
    .view-profile {
        background-color: var(--light-8);
    }
</style>