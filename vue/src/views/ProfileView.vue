<template>
    <div>
        <Transition name="fade">
            <div v-if="showProfile">
                <UserProfile :profile="profile" :allowEditing="allowEdit" @reload="reload"/>
            </div>
        </Transition>
    </div>
</template>

<script>
import UserProfile from '../components/UserProfile.vue';
import ProfileService from '../services/ProfileService';

export default {
    components: {UserProfile},
    data() {
        return {
            showProfile: false,
            allowEdit: false,
            profile: {}
        }
    },
    created() {
        if (this.$route.params.userId == this.$store.state.user.id) {
            if (!this.$store.state.loadedProfile) {
                this.loadingTick();
            } else {
                this.profile = this.$store.state.userProfile;
                this.showProfile = true;
                this.allowEdit = true;
            }
        } else {
            ProfileService.getProfile(this.$route.params.userId).then(
                (response) => {
                    this.profile = response.data;
                    this.showProfile = true;
                }
            )
        }
    },
    methods: {
        loadingTick() {
            setTimeout(
                () => {
                    if (!this.$store.state.loadedProfile) {
                        this.loadingTick();
                    } else {
                        this.profile = this.$store.state.userProfile;
                        this.showProfile = true;
                        this.allowEdit = true;
                    }
                }, 500
            )
        },
        reload() {
            this.showProfile = false;
            setTimeout(() => {
                this.profile = this.$store.state.userProfile;
                this.showProfile = true
            }, 250)
        }
    }
}
</script>

<style>
    
</style>