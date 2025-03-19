import axios from 'axios';

const API_URL = '/friendship';

export default {
    getFriendList() {
        return axios.get(`${API_URL}/friendlist`);
    },
    getFriendListByUserId(userId) {
        return axios.get(`${API_URL}/friendlist/${userId}`);
    },
    getPendingRequests() {
        return axios.get(`${API_URL}/pending`);
    },
    getBlockedUsers() {
        return axios.get(`${API_URL}/blocked`);
    },
    createFriendRequest(friendId) {
        return axios.post(`${API_URL}/request/${friendId}`);
    },
    acceptFriendRequest(friendId) {
        return axios.post(`${API_URL}/accept/${friendId}`);
    },
    getFriendRequests() {
        return axios.get(`${API_URL}/requests`);
    },
    rejectFriendRequest(friendId) {
        return axios.post(`${API_URL}/reject/${friendId}`);
    },
    removeFriend(friendId) {
        return axios.post(`${API_URL}/remove/${friendId}`);
    },
    blockFriend(friendId) {
        return axios.post(`${API_URL}/block/${friendId}`);
    },
    unblockFriend(friendId) {
        return axios.post(`${API_URL}/unblock/${friendId}`);
    },
    cancelFriendRequest(friendId) {
        return axios.post(`${API_URL}/cancel/${friendId}`);
    },
    isFriend(friendId) {
        return axios.get(`${API_URL}/isfriend/${friendId}`);
    }
}