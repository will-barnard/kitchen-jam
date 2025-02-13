<template>
    <div>
        <!-- Profile Visibility Setting -->
        <div>
            <label>Make Profile:</label>
            <input type="radio" id="profilePublic" value="public" v-model="profileVisibility" @change="updateProfileVisibility">
            <label for="profilePublic">Public</label>
            <input type="radio" id="profilePrivate" value="private" v-model="profileVisibility" @change="updateProfileVisibility">
            <label for="profilePrivate">Private</label>
        </div>
        
        <!-- Posts Default Visibility Setting -->
        <div>
            <label>Make Posts Default:</label>
            <input type="radio" id="postsPublic" value="public" v-model="postsVisibility" @change="updatePostsVisibility">
            <label for="postsPublic">Public</label>
            <input type="radio" id="postsPrivate" value="private" v-model="postsVisibility" @change="updatePostsVisibility">
            <label for="postsPrivate">Private</label>
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
<style>
/* Add any necessary styles here */
</style>