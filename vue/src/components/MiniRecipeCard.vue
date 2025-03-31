<template>
    <div class="main" ref="observerTarget">
        <div class="recipe-img">
            <img src="../img/placeholder.jpeg" v-if="!recipe.imageId">
            <img :src="recipe.img ? recipe.img : '../img/placeholder.jpeg'" v-if="recipe.imageId"/>
        </div>
        <div>
            <h3>{{ recipe.recipeName }}</h3>
        </div>
        <h3><i class="fas fa-share" @click="goToRecipe"></i></h3>
    </div>
</template>

<script>
import ImageService from '../services/ImageService';

export default {
    props: ['recipe'],
    methods: {
        async loadImage() {
            if (this.recipe.imageId && !this.recipe.img) {
                const response = await ImageService.getImage(this.recipe.imageId);
                const base64 = ImageService.parseImg(response);
                this.$store.commit('SAVE_IMAGE', { id: this.recipe.imageId, base64, type: 'recipe' });
            }
        },
        goToRecipe() {
            this.$router.push({ name: 'recipe-detail', params: { recipeId: this.recipe.recipeId } });
        },
        observeVisibility() {
            const observer = new IntersectionObserver((entries) => {
                if (entries[0].isIntersecting) {
                    this.loadImage();
                    observer.disconnect();
                }
            });
            observer.observe(this.$refs.observerTarget);
        }
    },
    mounted() {
        this.observeVisibility();
    }
};
</script>

<style scoped>
    h3 {
        margin: 0px;
        text-align: center;
    }
    .main {
        display: flex;
        justify-content: space-between;
        align-items: center;
        background-color: var(--light-1);
        border-radius: 15px;
        padding: 10px;
        margin: 5px;
        margin-left: 10px;
        margin-right: 10px;
        width: 100%;
    }
    .recipe-img img {
        width: 100px; /* Adjust the width as needed */
        height: 75px; /* Ensure all images have the same height */
        object-fit: cover;
        border-radius: 15px;
    }
    .spacer {
        flex-grow: 1;
    }
    i {
        margin: 10px;
        border: 1px;
        border-radius: 10px;
        padding: 5px;
        background-color: var(--light-5);
    }
</style>