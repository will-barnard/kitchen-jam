<template>
    <div>
        <TopBanner />
        <div v-if="show" class="main-area">
            <h2>{{ profileName }}</h2>
            <input 
                type="text" 
                v-model="searchQuery" 
                placeholder="Search friends..." 
                class="search-bar"
            />
            <ProfileFriendsList :friendsList="filteredFriendsList" />
        </div>
    </div>
</template>
<script>
import ProfileFriendsList from '../components/ProfileFriendsList.vue';
import TopBanner from '../components/TopBanner.vue';
import ProfileService from '../services/ProfileService';

export default {
    components: {TopBanner, ProfileFriendsList},
    data() {
        return {
            friendsList: [],
            profile: null, // Add a property to store the passed profile
            show: false, // Add a property to control the visibility of the component
            profileName: "",
            searchQuery: "" // Add a property for the search query
        }
    },
    computed: {
        filteredFriendsList() {
            return this.friendsList
                .slice() // Create a copy to avoid mutating the original array
                .sort((a, b) => a.username.localeCompare(b.username)) // Sort alphabetically
                .filter(friend =>
                    friend.username.toLowerCase().includes(this.searchQuery.toLowerCase())
                );
        }
    },
    created() {
        this.profile = this.$route.state?.profile || null; // Retrieve the profile from the state
        if (this.$route.params.userId == this.$store.state.user.id && this.profile != null) {
            if (this.$store.state.loadedProfile && this.$store.state.loadedFriends) {
                this.setFriends();
            } else {
                this.loadingTick();
            }
        } else if (this.profile) {
            this.friendsList = this.profile.friends.sort((a, b) => a.username.localeCompare(b.username)); // Sort alphabetically
            this.profileName = this.profile.displayName + "\'s Friends";
            this.show = true; // Show the component for other profiles
        } else {
            ProfileService.getProfile(this.$route.params.userId).then(
                (response) => {
                    this.friendsList = response.data.friends.sort((a, b) => a.username.localeCompare(b.username)); // Sort alphabetically
                    this.profileName = response.data.displayName + "\'s Friends";
                    this.show = true; // Show the component after fetching the profile
                }
            )
        }
    },
    methods: {
        setFriends() {
            this.friendsList = this.$store.state.userFriends.sort((a, b) => a.username.localeCompare(b.username)); // Sort alphabetically
            this.profileName = "Your Friends";
            this.show = true; // Show the component for own profile
        },
        loadingTick() {
            setTimeout(() => {
                if (this.$store.state.loadedProfile && this.$store.state.loadedFriends) {
                    this.setFriends();
                } else {
                    this.loadingTick();
                }
            }, 1000);
        }
    }
}
</script>

<style scoped>
    .main-area {
        padding: 20px;
        background-color: var(--light-2);
        border-radius: 8px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        display: flex;
        flex-direction: column;
        align-items: center;
    }
    .search-bar {
        margin-bottom: 20px;
        padding: 10px;
        width: 90%;
        max-width: 400px;
        border: 1px;
        border-radius: 4px;
        font-size: 16px;
    }
</style>