<template>
    <div>
        <div>
            <div class="top">
                <div class="profile-img">
                    <img :src="profile.img ? profile.img : '../img/placeholder.jpeg'" />
                </div>
                <h1 v-if="!editing">{{ profile.displayName }}</h1>
            </div>
            <div class="main bg" v-if="!editing">
                <h2>{{ profile.headline }}</h2>
                <div class="single-row">
                    <h3><i class="fas fa-utensils icon"></i>&nbsp;Meals: {{ profile.countMeals }}</h3>
                    <h3><i class="fas fa-book icon"></i>&nbsp;Recipes: {{ profile.countRecipes }}</h3>
                </div>
                <hr class="separator" />
                <p><i class="fas fa-map-marker-alt icon"></i>&nbsp;{{ profile.location }}</p>
                <hr class="separator" />
                <div class="single-row">
                    <i class="fas fa-star icon"></i>&nbsp;
                    <div>
                        <p class="favorite"><b>Favorite foods:</b>&nbsp;{{ profile.favoriteFoods }}</p>
                        <p class="favorite"><b>Favorite cuisines:</b>&nbsp;{{ profile.favoriteCuisines }}</p>
                    </div>
                </div>
                <hr class="separator" />
                <div class="single-row">
                    <i class="fas fa-user icon"></i>
                    <p>{{ profile.bio }}</p>
                </div>
            </div>
        </div>
        <div v-if="allowEditing && editing">
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
                <img src="/img/link-symbol.svg" />
            </div>
            <div class="spacer"><p v-show="copiedURL">Link copied!</p></div>
            <div class="edit-button button" v-if="!editing" v-on:click="editing=true">
                <img src="/img/edit.png" />
            </div>
            <!-- <div class="trash button" v-if="editing" v-on:click="">
                <img src="/img/trash.png" />
            </div> -->
            <div class="undo button" v-if="editing" v-on:click="undoEdit()">
                <img src="/img/undo.png" />
            </div>
            <div class="check button" v-if="editing" v-on:click="updateProfile()">
                <img src="/img/check.png" />
            </div>
        </div>
    </div>
</template>

<script>
import ImageService from '../services/ImageService';
import ProfileService from '../services/ProfileService';

function cloneProfile(profile) {
    let newProfile = {};
    newProfile = JSON.parse(JSON.stringify(profile));
    return newProfile;
}

export default {
    props: ['profile', 'allowEditing'],
    data() {
        return {
            copiedURL: false,
            editing: false,
            newProfile: {}
        }
    },
    created() {
        if (this.allowEditing) {
            this.newProfile = cloneProfile(this.profile);
        }
        if (this.profile.imageId != null && this.profile.imageId > 0 && !this.allowEditing) {
            ImageService.getImage(this.profile.imageId).then(
                (response) => {
                    const base64 = ImageService.parseImg(response);
                    profile.img = "data:image/png;base64," + base64;
                }
            )
        }
    },
    methods: {
        undoEdit() {
            this.editing = false;
            this.newProfile = this.profile;
        },
        updateProfile() {
            ProfileService.updateProfile(this.newProfile).then(
                (response) => {
                    this.$store.commit('UPDATE_PROFILE', response.data);
                    this.$emit('reload');
                }
            )
        },
        uploadImage(event){
            this.showImage = false;
            
            if (this.meal.imageId == 0 || this.meal.imageId == null) {
                ImageService.createImage(event.target.files[0]).then(
                    (response) => {
                        let id = response.data;
                        ImageService.addImageToProfile(id).then(
                            () => {
                                ImageService.getImage(id).then(
                                    (r) => {
                                        const base64 = ImageService.parseImg(r);
                                        this.imgPath = "data:image/png;base64," + base64;
                                        this.showImage = true;
                                    }
                                )
                            }
                        );
                    }
                ) 
            } else {
                ImageService.createImage(event.target.files[0]).then(
                    (response) => {
                        let id = response.data;
                        ImageService.updateProfileImage(id).then(
                            () => {
                                ImageService.getImage(id).then(
                                    (r) => {
                                        const base64 = ImageService.parseImg(r);
                                        this.imgPath = "data:image/png;base64," + base64;
                                        this.showImage = true;
                                    }
                                )
                            }
                        );
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
    margin-bottom: 20px;
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

.controls img {
    height: 5vh;
}

.controls:hover {
    cursor: pointer;
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
</style>