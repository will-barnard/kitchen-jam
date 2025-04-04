<template>
    <div class="main">
        <h2>{{ isOld ? 'Old ' : ""}}Notifications</h2> 
        <p v-if="notifications.length == 0">No new notifications</p>
        <div v-for="notification of notifications" :key="notification.notificationId" class="notification">
            <div class="notification-content">
                <div class="header-content">
                    <i class="fas fa-bell"></i>&nbsp;
                    <span v-if="notification.createdAt" class="notification-time">
                        {{ formatDateTime(notification.createdAt) }}
                    </span>
                    <div class="spacer"></div>
                    <div v-if="isOld" class="trash-icon" @click="$emit('deleteNotification', notification.notificationId)">
                        <i class="far fa-trash-alt"></i>
                    </div>
                </div>
                
                <p v-if="notification.type !== 'friends'" class="message" @click="goToTarget(notification)">{{ notification.message }}</p>
                <div v-if="notification.type === 'friends'">
                    <p class="message" @click="this.$router.push({name: 'friends', query: {view: 'requests'}});">{{ notification.message }}</p>
                </div>
            </div>
            <div class="notification-actions" v-if="notification.type !== 'friends' && !isOld">
                <button @click="markAsRead(notification.notificationId)">Mark&nbsp;Read</button>
            </div>
        </div>
    </div>
</template>

<script>
import UtilityService from '../services/UtilityService';

export default {
    props: ['notifications', 'isOld'],
    methods: {
        formatDateTime(dateTime) {
            return UtilityService.formatDateTime(dateTime);
        },
        markAsRead(notificationId) {
            this.$emit('markAsRead', notificationId); // Ensure correct ID is emitted
        },
        goToTarget(notification) {
            if (notification.targetType === 'meal') {
                if (!this.isOld) {
                    this.markAsRead(notification.notificationId); // Emit ID before navigation
                }
                this.$router.push({ name: 'meal-detail', params: { mealId: notification.targetId } });
            } else if (notification.targetType === 'recipe') {
                if (!this.isOld) {
                    this.markAsRead(notification.notificationId); // Emit ID before navigation
                }
                this.$router.push({ name: 'recipe-detail', params: { recipeId: notification.targetId } });
            }
        }
    },
}
</script>

<style scoped>
    .main {
        background-color: var(--light-1);
        border-radius: 10px;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        padding: 15px;
        width: 100%;
        box-sizing: border-box;
    }
    .notification {
        margin: 5px 0;
        padding: 10px 10px;
        border-radius: 10px;
        background-color: var(--light-2);
        display: flex;
        flex-direction: column;
        align-items: start;
        justify-content: center;
        border-bottom: 1px solid #ccc;
        width: 100%;
        box-sizing: border-box;
    }
    h2 {
        margin: 0px 0px 10px 0px;
    }
    .message {
        text-decoration: underline;
    }
    .notification-content {
        width: 100%;
    }
    .header-content {
        display: flex;
        align-items: center;
        justify-content: space-between;
        width: 100%;
    }
    .spacer {
        flex-grow: 1;
    }
    .trash-icon {
        cursor: pointer;
        margin-right: 5px;
        background-color: var(--light-3);
        padding: 5px;
        border-radius: 5px;
    }
</style>