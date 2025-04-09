<template>
    <div>
        <TopBanner />
        <LoadingWidget v-if="!show"/>
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
import ImageService from '../services/ImageService';
import LoadingWidget from '../components/LoadingWidget.vue';

export default {
    components: {UserProfile, TopBanner, LoadingWidget},
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
            document.title = "Your Profile - Kitchen Jam"; // Set title for own profile
            if (!this.$store.state.loadedProfile && !this.$store.state.loadedFriends) {
                this.loadingTick();
            } else {
                this.profile = this.$store.state.userProfile;
                if (this.profile.friends == null) {
                    this.profile.friends = this.$store.state.userFriends;
                }
                this.showProfile = true;
                this.allowEdit = true;
            }
        } else {
            ProfileService.getProfile(this.$route.params.userId).then(
                (response) => {
                    document.title = `${response.data.displayName}'s Profile - Kitchen Jam`; // Set title for other profiles
                    if (response.data.imageId != null && response.data.imageId > 0) {
                        ImageService.getImage(response.data.imageId).then(
                            (r) => {
                                const base64 = ImageService.parseImg(r);
                                let imgPath = "data:image/png;base64," + base64;
                                response.data.img = imgPath;
                                this.profile = response.data;
                                if (this.profile.friends == null) {
                                    this.profile.friends = [];
                                }
                                this.showProfile = true;
                            }
                        )
                    } else {
                        this.profile = response.data;
                        this.showProfile = true;
                    }
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
                    if (!this.$store.state.loadedProfile && !this.$store.state.loadedFriends) {
                        this.loadingTick();
                    } else {
                        this.profile = this.$store.state.userProfile;
                        if (this.profile.friends == null) {
                            this.profile.friends = this.$store.state.userFriends;
                        }
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