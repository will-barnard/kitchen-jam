<template>
    <div>
        <TopBanner />
        <div class="main">
            <div v-if="!loading" class="notifications">
                <Notifications :notifications="notifications" @markAsRead="handleMarkAsRead" />
            </div>
            <div v-if="!loading" class="notifications old-notifications">
                <button v-if="!oldNotifications" @click="loadOldNotifications">Load Old Notifications</button>
                <Notifications 
                    :notifications="oldNotifications" 
                    :isOld="true" 
                    @deleteNotification="handleDeleteNotification" 
                    v-if="oldNotifications" 
                />
            </div>
        </div>    
    </div>
</template>

<script>
import Notifications from '../components/Notifications.vue';
import TopBanner from '../components/TopBanner.vue';

export default {
    components: {
        Notifications,
        TopBanner
    },
    data() {
        return {
            loading: true,
        }
    },
    computed: {
        notifications() {
            return this.$store.state.userNotifications;
        },
        oldNotifications() {
            if (this.$store.state.userOldNotifications.length === 0) {
                return false;
            } else {
                return this.$store.state.userOldNotifications;
            }
        }
    },
    created() {
        if (this.$store.state.loadedNotifications) {
            this.loading = false;
        } else {
            this.loadingTick();
        }
    },
    methods: {
        loadingTick() {
            setTimeout(() => {
                if (this.$store.state.loadedNotifications) {
                    this.loading = false;
                } else {
                    this.loadingTick();
                }
            }, 500);
        },
        handleMarkAsRead(notificationId) {
            this.$store.commit('MARK_NOTIFICATION_AS_READ', notificationId);
        },
        loadOldNotifications() {
            this.$store.commit('LOAD_OLD_NOTIFICATIONS');
        },
        handleDeleteNotification(notificationId) {
            this.$store.commit('DELETE_NOTIFICATION', notificationId);
        }
    }
}
</script>

<style scoped>
    .main {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        width: 100%;
    }
    .notifications {
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
    }
    .old-notifications {
        margin-top: 20px;
    }
</style>