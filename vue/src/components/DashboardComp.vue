<template>
    <div>
        <div class="main">
            <h3 class="child" @click="clickable ? $router.push({ name: 'meal-log' }) : ''">
                <i class="fas fa-utensils"></i>
                <span>Meal Log</span>
            </h3>
            <h3 class="child" @click="$clickable ? router.push({ name: 'cookbook' }) : ''">
                <i class="fas fa-book"></i>
                <span>Cookbook</span>
            </h3>
            <h3 class="child" @click="clickable ? $router.push({ name: 'jam' }) : ''">
                <i class="fas fa-guitar"></i>
                <span>Jam Page</span>
            </h3>
            <h3 class="child" @click="clickable ? $router.push({ name: 'friends' }) : ''">
                <i class="fas fa-users"></i>
                <span>Friends</span>
            </h3>
            <h3 class="child" @click="clickable ? $router.push({ name: 'notifications' }) : ''">
                <i class="fas fa-bell"></i>
                <span>Notifications</span>
                <span v-if="$store.state.loadedNotifications && $store.state.userNotifications.length > 0" class="notification-badge">
                    {{ $store.state.userNotifications.length }}
                </span>
            </h3>
            <h3 class="child" @click="clickable ? $router.push({ name: 'profile' , params: {userId: $store.state.user.id}}) : ''">
                <i class="fas fa-user"></i>
                <span>Profile</span>
            </h3>
            <h3 class="child" @click="clickable ? $router.push({ name: 'settings' }) : ''">
                <i class="fas fa-cog"></i>
                <span>Settings</span>
            </h3>
            <h3 class="child" @click="clickable ? $router.push({ name: 'about' }) : ''">
                <i class="fas fa-info-circle"></i>
                <span>About Kitchen Jam</span>
            </h3>
            <h3 class="child" @click="clickable ? $router.push({ name: 'updates' }) : ''">
                <i class="fas fa-bell"></i>
                <span>Site Updates</span>
            </h3>
        </div>
    </div>
</template>
<script>
export default {
    props: ['admin', 'clickable'],
}
</script>
<style scoped>
.main {
    display: flex;
    justify-content: center;
    width: 100%;
    background-color: var(--light-2);
    border-radius: 15px;
    flex-wrap: wrap;
    padding-top: 10px;
    padding-bottom: 10px;
}

.child {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    text-align: center;
    border: 3px solid var(--dark-2);
    border-radius: 10px;
    padding: 5px 10px;
    margin: 10px;
    width: 25vw;
    height: 120px;
    transition: transform 0.3s, background-color 0.3s, color 0.3s;
    cursor: pointer;
    color: var(--dark-1);
    position: relative; /* Add this to make the badge position relative to the child */
}

.child i {
    font-size: 1.8em;
    margin-bottom: 0;
    display: block;
}

.child span {
    margin-top: 5px;
}

.child:hover {
    background-color: var(--light-1);
    color: var(--dark-1);
}

.child:active {
    background-color: var(--dark-1);
    color: var(--light-1);
}

.notification-badge {
    position: absolute;
    top: 15px; /* Adjusted to position above the bell */
    right: 15px;
    background-color: red;
    color: white;
    border-radius: 50%;
    padding: 2px 6px;
    font-size: 0.8em;
    font-weight: bold;
}
</style>