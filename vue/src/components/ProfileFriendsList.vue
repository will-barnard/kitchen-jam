<template>
    <div class="main-area">
        <div v-for="friend in friendsList" :key="friend.id" class="friend-card" @click="$router.push({name: 'profile', params: {userId: friend.friendId}})">
            <!-- <img :src="friend.img" alt="Friend's profile picture" class="friend-img"> -->
            <i class="fa fa-user"></i>
            <div class="friend-info">
                <h3>&nbsp;{{ friend.username }}</h3>
            </div>

        </div>
    </div>
</template>
<script>
export default {
    props: ['friendsList']
}
</script>
<style scoped>
    .friend-card {
        display: flex;
        align-items: center;
        width: 100%;
        padding: 10px;
        margin: 10px 0;
        background-color: var(--light-1);
        border-radius: 8px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }
    .friend-img {
        width: 50px;
        height: 50px;
        border-radius: 50%;
        margin-right: 10px;
    }
    .friend-info {
        display: flex;
        width: 100%;
        flex-direction: column;
    }
    .friend-info h3 {
        margin: 0;
        font-size: 1.2em;
    }
    .friend-info p {
        margin: 5px 0 0;
        font-size: 0.9em;
        color: var(--dark-2);
    }
    .main-area {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 90%;
    }
</style>