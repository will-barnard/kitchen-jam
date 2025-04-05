<template>
    <div>
        <div>
            <div class="top" v-if="!editing">
                <div class="profile-img">
                    <img :src="profile.img ? profile.img : '../img/placeholder.jpeg'" />
                </div>
                <h1>{{ profile.displayName }}</h1>
                <div class="sub-header-area" v-if="!allowEditing">
                    <div class="left-tabs">
                        <div @click="currentTab = 'main'">
                            <h3 :class="{ active: currentTab === 'main' }">Profile</h3>
                        </div>
                        <div @click="currentTab = 'meals'">
                            <h3 :class="{ active: currentTab === 'meals' }">Meals</h3>
                        </div>
                        <div @click="currentTab = 'recipes'">
                            <h3 :class="{ active: currentTab === 'recipes' }">Recipes</h3>
                        </div>
                    </div>
                    <div class="spacer"></div>
                    <div class="friends-area" v-show="$store.state.token != ''">
                        <div class="add-friend">
                            <h3 v-if="!isPending(profile.userId) && !isFriend(profile.userId) && !isRequest(profile.userId)" @click="addFriend(profile)">Add&nbsp;Friend</h3>
                        </div>
                        <div class="respond">
                            <h3 v-if="isRequest(profile.userId)" @click="respondToRequest">Respond to Request</h3>
                        </div>
                        <div class="respond">
                            <h3 v-if="isPending(profile.userId)">Requested&nbsp;<i class="fa fa-times" @click="cancelRequest(profile)"></i></h3>
                        </div>
                        <div class="friends">
                            <div v-if="isFriend(profile.userId)" class="dropdown">
                                <h3 @click="toggleDropdown">Friends</h3>
                                <div v-if="showDropdown" class="dropdown-content">
                                    <p @click="removeFriend(profile)">Remove Friend</p>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                </div>
            </div>
            <div class="main-area">
                <div v-if="currentTab === 'main'">
                    <div class="main bg" v-if="!editing">
                        <h2 v-if="profile.headline">{{ profile.headline }}</h2>
                        <div class="single-row">
                            <h3><i class="fas fa-utensils icon"></i>&nbsp;Meals: {{ profile.countMeals }}</h3>
                            <h3><i class="fas fa-book icon"></i>&nbsp;Recipes: {{ profile.countRecipes }}</h3>
                        </div>
                        <hr class="separator" />
                        <p v-if="profile.location"><i class="fas fa-map-marker-alt icon"></i>&nbsp;{{ profile.location }}</p>
                        <hr class="separator" v-if="profile.location"/>
                        <div class="single-row" v-if="profile.favoriteFoods || profile.favoriteCuisines">
                            <i class="fas fa-star icon"></i>&nbsp;
                            <div>
                                <p class="favorite" v-if="profile.favoriteFoods"><b>Favorite foods:</b>&nbsp;{{ profile.favoriteFoods }}</p>
                                <p class="favorite" v-if="profile.favoriteCuisines"><b>Favorite cuisines:</b>&nbsp;{{ profile.favoriteCuisines }}</p>
                            </div>
                        </div>
                        <hr class="separator" v-if="profile.favoriteFoods || profile.favoriteCuisines"/>
                        <div class="single-row" v-if="profile.bio">
                            <i class="fas fa-user icon"></i>
                            <p>{{ profile.bio }}</p>
                        </div>
                    </div>
                </div>
                <div v-if="currentTab === 'meals'">
                    <div class="meal-feed">
                        <input type="text" v-model="searchMeals" placeholder="Search meals..." class="search-bar" />
                        <MealCard 
                            v-for="meal in filteredMeals" 
                            :key="meal.id + searchMeals" 
                            :meal="meal" 
                            :isFeed="true" 
                        />
                    </div>
                </div>
                <div v-if="currentTab === 'recipes'">
                    <div class="recipe-feed">
                        <input type="text" v-model="searchRecipes" placeholder="Search recipes..." class="search-bar" />
                        <RecipeCard 
                            v-for="recipe in filteredRecipes" 
                            :key="recipe.id + searchRecipes" 
                            :recipe="recipe" 
                            :isFeed="true" 
                        />
                    </div>
                </div>
                <div v-if="allowEditing && editing">
                    <div class="top">
                        <div class="profile-img">
                            <img :src="profile.img ? profile.img : '../img/placeholder.jpeg'" v-if="!showNewPhoto"/>
                            <img :src="newPhoto" v-if="showNewPhoto"/>
                        </div>
                    </div>
                    <div class="bg">
                        <h3>Profile picture:</h3>
                        <input type="file" name="file" accept="image/*" @change="uploadImage">
                    </div>
                    <div class="bg main-edit">
                        <div class="edit-row">
                            <p>Display&nbsp;name:</p>
                            <input type="text" v-model="newProfile.displayName">
                        </div>
                        <div class="edit-row">
                            <p>Headline:</p>
                            <input type="text" v-model="newProfile.headline">
                        </div>
                        <div class="edit-row">
                            <p>Location</p>
                            <input type="text" v-model="newProfile.location">
                        </div>
                        <div class="edit-row">
                            <p>Favorite&nbsp;foods:</p>
                            <input type="text" v-model="newProfile.favoriteFoods">
                        </div>
                        <div class="edit-row">
                            <p>Favorite&nbsp;cuisines:</p>
                            <input type="text" v-model="newProfile.favoriteCuisines">
                        </div>
                        <div class="edit-column">
                            <p>Bio:</p>
                            <textarea v-model="newProfile.bio"></textarea>
                        </div>
                    </div>
                </div>
                <div class="controls" v-if="allowEditing">
                    <div class="link-button button" v-show="profile.public" @click="unsecuredCopyToClipboard('http://kitchen-jam.com/profile/' + profile.userId)">
                        <i class="fas fa-link"></i>
                    </div>
                    <div class="spacer"><p v-show="copiedURL">Link copied!</p></div>
                    <div class="edit-button button" v-if="!editing" v-on:click="editing=true">
                        <i class="fas fa-edit"></i>
                    </div>
                    <div class="undo button" v-if="editing" v-on:click="undoEdit()">
                        <i class="fas fa-undo"></i>
                    </div>
                    <div class="check button" v-if="editing" v-on:click="updateProfile()">
                        <i class="fas fa-check"></i>
                    </div>
                </div>
            </div>
        </div>    
    </div>
</template>

<script>
import ImageService from '../services/ImageService';
import ProfileService from '../services/ProfileService';
import FriendshipService from '../services/FriendshipService';
import MealCard from './MealCard.vue';
import RecipeCard from './RecipeCard.vue';

function cloneProfile(profile) {
    let newProfile = {};
    newProfile = JSON.parse(JSON.stringify(profile));
    return newProfile;
}

export default {
    props: ['profile', 'allowEditing'],
    components: {
        MealCard,
        RecipeCard
    },
    data() {
        return {
            copiedURL: false,
            editing: false,
            newProfile: {},
            newPhoto: false,
            showNewPhoto: false,
            showDropdown: false,
            currentTab: 'main', // Default to 'main' tab
            profileMeals: [],
            profileRecipes: [],
            searchMeals: '',
            searchRecipes: ''
        }
    },
    created() {
        if (this.allowEditing) {
            this.newProfile = cloneProfile(this.profile);
        } else {
            ProfileService.getUserProfileLog(this.profile.userId).then(
                (response) => {
                    let meals = response.data.meals;
                    for (let meal of meals) {
                        if (!meal.tags) {
                            meal.tags = [];
                        }
                    }
                    this.profileMeals = meals;
                    this.profileRecipes = response.data.recipes;
                }
            ).catch(
                (error) => {
                    console.error('Error fetching user profile log:', error);
                }
            )
        }
    },
    computed: {
        userPending() {
            return this.$store.state.userPending;
        },
        userFriends() {
            return this.$store.state.userFriends;
        },
        userRequests() {
            return this.$store.state.userRequests;
        },
        filteredMeals() {
            return this.profileMeals.filter(meal =>
                meal.mealName.toLowerCase().includes(this.searchMeals.toLowerCase())
            );
        },
        filteredRecipes() {
            return this.profileRecipes.filter(recipe =>
                recipe.recipeName.toLowerCase().includes(this.searchRecipes.toLowerCase())
            );
        }
    },
    methods: {
        isPending(userId) {
            return this.userPending.some(request => request.friendId === userId);
        },
        isFriend(userId) {
            return this.userFriends.some(friend => friend.friendId === userId);
        },
        isRequest(userId) {
            return this.userRequests.some(request => request.friendId === userId);
        },
        async addFriend(user) {
            try {
                await FriendshipService.createFriendRequest(user.userId);
                this.$store.commit('ADD_FRIEND_TO_PENDING', user);
            } catch (error) {
                console.error('Error sending friend request:', error);
            }
        },
        async cancelRequest(user) {
            try {
                await FriendshipService.cancelFriendRequest(user.userId);
                this.$store.commit('CANCEL_FRIEND_REQUEST', user.userId);
            } catch (error) {
                console.error('Error cancelling friend request:', error);
            }
        },
        async removeFriend(user) {
            try {
                await FriendshipService.removeFriend(user.userId);
                this.$store.commit('DELETE_FRIEND', user.userId);
                this.showDropdown = false;
            } catch (error) {
                console.error('Error removing friend:', error);
            }
        },
        respondToRequest() {
            this.$router.push({name: 'friends', query: {view: 'requests'}});
        },
        toggleDropdown() {
            this.showDropdown = !this.showDropdown;
        },
        undoEdit() {
            this.editing = false;
            this.newProfile = this.profile;
        },
        updateProfile() {
            ProfileService.updateProfile(this.newProfile).then(
                (response) => {
                    if (response.data.imageId != null && response.data.imageId > 0 && response.data.imageId != this.profile.imageId) {
                        ImageService.addImageToProfile(response.data.imageId).then(
                            (r) => {
                                const base64 = ImageService.parseImg(r);
                                let imgPath = "data:image/png;base64," + base64;
                                this.showImage = true;
                                response.data.img = imgPath;
                                this.$store.commit('UPDATE_PROFILE', response.data);
                                this.$emit('reload');
                            }
                        );
                    } else {
                        this.$store.commit('UPDATE_PROFILE', response.data);
                        this.$emit('reload');
                    }
                    
                    
                }
            )
        },
        uploadImage(event){
            this.showImage = false;
            if (this.profile.imageId == 0 || this.profile.imageId == null) {
                ImageService.createImage(event.target.files[0]).then(
                    (response) => {
                        let id = response.data;
                        this.newProfile.imageId = id;
                        this.newPhoto = URL.createObjectURL(event.target.files[0]);
                        this.showNewPhoto = true;
                    }
                ) 
            } else {
                ImageService.createImage(event.target.files[0]).then(
                    (response) => {
                        let id = response.data;
                        this.newProfile.imageId = id;
                        this.newPhoto = URL.createObjectURL(event.target.files[0]);
                        this.showNewPhoto = true;
                    }
                ) 
            }
        },
        unsecuredCopyToClipboard(text) {
            const textArea = document.createElement("textarea");
            textArea.value = text;
            document.body.appendChild(textArea);
            textArea.focus();
            textArea.select();
            try {
                document.execCommand('copy');
                this.copiedURL = true;
                setTimeout(() => this.copiedURL = false, 2000);
            } catch (err) {
                console.error('Unable to copy to clipboard', err);
            }
            document.body.removeChild(textArea);
        }
    }
}
</script>

<style scoped>
.profile-img {
    width: 50vw;
    height: 50vw;
    margin-bottom: 20px;
}

.profile-img img {
    height: 100%;
    width: 100%;
    object-fit: cover;
    border-radius: 100%;
}

.top {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.main {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 20px;
    background-color: var(--light-1);
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.separator {
    width: 80%;
    border: 1px solid var(--dark-2);
    margin: 5px 0;
}

.icon {
    width: 1em;
    height: 1em;
    vertical-align: middle;
}

h1 {
    margin: 5px;
    text-align: center;
    font-size: 2em;
    font-weight: bold;
    color: var(--primary);
}

h2 {
    margin: 5px;
    text-align: center;
    font-size: 1.5em;
    font-weight: bold;
    color: var(--secondary);
}

h3 {
    margin: 5px;
    text-align: center;
    font-size: 1.2em;
    font-style: italic;
    color: var(--tertiary);
}

p {
    margin: 5px;
    text-align: center;
    font-size: 1em;
    font-weight: 100;
    line-height: 1.5;
}

.controls {
    text-align: center;
    display: flex;
    flex-direction: row;
    justify-content: end;
    align-items: center;
    margin-right: 5px;
    margin-top: 10px;
}

.controls i {
    font-size: 2em; /* Increase the size of the icons */
    margin: 0 10px; /* Add some margin for spacing */
}

.controls:hover {
    cursor: pointer;
}

.controls img {
    height: 5vh;
}

.button {
    width: 15vw;
    border-radius: 10px;
    padding: 5px;
    margin-right: 5px;
}

.edit-button {
    background-color: var(--edit);
}

.trash {
    background-color: var(--light-3);
}

.check {
    background-color: var(--light-4);
}

.undo {
    background-color: var(--edit);
}

.link-button {
    background-color: var(--light-7);
    margin-left: 10px;
}

.edit-row {
    display: flex;
    margin-bottom: 5px;
}

.edit-row input {
    width: 100%;
    border-radius: 5px;
    border: 1px solid var(--border-color);
}

.edit-column {
    display: flex;
    flex-direction: column;
    align-items: start;
    justify-content: start;
}

.edit-column textarea {
    flex-grow: 1;
    width: 85vw;
    margin-left: 5px;
    height: 20vh;
    border-radius: 5px;
    border: 1px solid var(--border-color);
}

.bg {
    background-color: var(--light-1);
    padding: 10px;
    border-radius: 10px;
    margin-bottom: 10px;
}

.main-edit {
    padding: 10px;
}

.favorite {
    text-align: left;
}
.sub-header-area {
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    width: 95%;
    margin-left: 20px;
    margin-right: 20px;
    
}
.sub-header-area h3 {
    font-style: normal;
    border-radius: 10px 10px 0px 0px;
    padding: 7px;
    margin: 0px;
    cursor: pointer;
    background-color: var(--light-2);
    font-size: 1em;

}
.spacer {
    flex-grow: 1;
}
.left-tabs {
    display: flex;
}
.dropdown {
    position: relative;
    display: inline-block;
}

.dropdown-content {
    display: none;
    background-color: var(--light-9); /* Ensure background color is applied */
    border-radius: 0px 0px 10px 10px;
    min-width: 160px;
    box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
    z-index: 10; /* Ensure it displays above other elements */
    position: absolute; /* Make it absolute */
    right: 0; /* Align flush with the right side of the parent */
}

.dropdown-content p {
    color: black;
    padding: 12px 16px;
    text-decoration: none;
    display: block;
    cursor: pointer;
    /* Removed position: absolute to avoid overriding parent styles */
}

.dropdown-content p:hover {
    background-color: var(--light-2);
}

.dropdown:hover .dropdown-content {
    display: block;
}
.add-friend h3 {
    background-color: var(--light-6);
}
.respond h3 {
    background-color: var(--light-5);
}
.friends h3 {
    background-color: var(--light-4);
}
.left-tabs h3.active {
    background-color: var(--light-1);
}
.meal-feed {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    padding: 20px;
    background-color: var(--light-1);
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}
.recipe-feed {
    display: grid;
    flex-direction: column;
    align-items: center;
    padding: 20px;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    justify-content: center;
    background-color: var(--light-1);
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}
.search-bar {
    width: 80%;
    padding: 10px;
    margin-bottom: 20px;
    border: 1px solid var(--border-color);
    border-radius: 5px;
    font-size: 1em;
    display: block;
    margin-left: auto;
    margin-right: auto;
}
</style>