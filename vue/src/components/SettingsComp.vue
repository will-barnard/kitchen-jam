<template>
    <div>
        <!-- Profile Visibility Setting -->
        <div class="main">
            <h2>Privacy</h2>
            <div class="child">
                <label>User Profile:</label>
                <input type="radio" id="profilePublic" value="public" v-model="profileVisibility" @change="updateProfileVisibility">
                <label for="profilePublic">Public</label>
                <input type="radio" id="profilePrivate" value="private" v-model="profileVisibility" @change="updateProfileVisibility">
                <label for="profilePrivate">Private</label>
            </div>
            
            <!-- Posts Default Visibility Setting -->
            <div class="child">
                <label>Posts Are Default:</label>
                <input type="radio" id="postsPublic" value="public" v-model="postsVisibility" @change="updatePostsVisibility">
                <label for="postsPublic">Public</label>
                <input type="radio" id="postsPrivate" value="private" v-model="postsVisibility" @change="updatePostsVisibility">
                <label for="postsPrivate">Private</label>
            </div>
        </div>
        <div class="main">
            <h2>Attributes&nbsp;<i class="fas fa-external-link-alt"></i></h2>
            <div class="attributes-row">
                <h3 class="attributes-child" @click="$router.push({name: 'edit-tags'})"><i class="fas fa-tags"></i> Tags</h3>
                <h3 class="attributes-child" @click="$router.push({name: 'edit-categories'})"><i class="fas fa-list"></i> Categories</h3>
            </div>
        </div>
    </div>
</template>
<script>
import SettingsService from '@/services/SettingsService.js';

export default {
    data() {
        return {
            profileVisibility: this.$store.state.userProfile.public ? 'public' : 'private',
            postsVisibility: this.$store.state.userProfile.defaultPublic ? 'public' : 'private'
        }
    },
    methods: {
        updateProfileVisibility() {
            const isPublic = this.profileVisibility === 'public';
            this.$store.commit('UPDATE_PROFILE_VISIBILITY', isPublic);
            SettingsService.updateIsPublic(isPublic)
                .then()
                .catch(error => {
                    console.log(error);
                });
        },
        updatePostsVisibility() {
            const defaultPublic = this.postsVisibility === 'public';
            this.$store.commit('UPDATE_POSTS_VISIBILITY', defaultPublic);
            SettingsService.updateDefaultPublic(defaultPublic)
                .then()
                .catch(error => {
                    console.log(error);
                });
        }
    }
}
</script>
<style scoped>
    .main {
        display: flex;
        flex-direction: column;
        background-color: var(--light-1);
        border-radius: 10px;
        padding: 5px;
        margin-bottom: 10px;
    }
    .main h2 {
        text-align: center;
        margin: 5px;
    }
    .child {
        margin: 10px;
    }
    .attributes-row {
        display: flex;
        justify-content: space-around;
    }
    .attributes-row h3 {
        display: flex;
        align-items: center;
    }
    .attributes-row i {
        margin-right: 5px;
    }
    .attributes-child {
        background-color: var(--light-2);
        border-radius: 10px;
        padding: 10px;
        margin: 5px;
        transition: background-color 0.3s, color 0.3s;
    }
    .attributes-child:hover {
        background-color: var(--light-3);
        cursor: pointer;
    }
</style>