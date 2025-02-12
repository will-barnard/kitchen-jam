<template>
    <div>
        <TopBanner />
        <Transition name="fade">
            <div v-if="show">
                <div v-if="showProfile">
                    <UserProfile :profile="profile" :allowEditing="allowEdit" @reload="reload"/>
                </div>
            </div>
        </Transition>
    </div>
</template>

<script>
import UserProfile from '../components/UserProfile.vue';
import ProfileService from '../services/ProfileService';
import TopBanner from '../components/TopBanner.vue';

export default {
    components: {UserProfile, TopBanner},
    data() {
        return {
            showProfile: false,
            allowEdit: false,
            profile: {},
            show: false
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
        setTimeout(() => {
            this.show = true;
        }, 10); 
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