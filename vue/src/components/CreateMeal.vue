<template>

    <body>
        <div class="image-block">
            <div class="image" v-if="showImage">
                <img :src="imgPath" />
            </div>
            <div class="add-image">
                <h3>Add image</h3>
                <input type="file" name="file" accept="image/*" @change="uploadImage">
            </div>
        </div>
        
        <form v-on:submit.prevent="createMeal()">
            <div class="form">
                <label>Name</label><input type="text" v-model="meal.mealName">
                <label>Date cooked</label><input type="date" v-model="meal.dateCooked" @click="console.log(meal.date)"/>
                <label>Comment</label><input type="text" v-model="meal.mealComment"/>
                <label>Cook time (min)</label><input type="number" v-model="meal.cookTime"/>
                <label>Rating</label><select v-model="meal.rating">
                    <option disabled="true" >----</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                    <option value="9">9</option>
                    <option value="10">10</option>
                </select>
                <label>Ingredient list</label><input type="text" v-model="meal.ingredients"/>
                <label>Notes</label><textarea v-model="meal.notes"></textarea>
            </div>

            <input class="submit" type="submit" value="Submit" />
        </form> 
    </body>

</template>
<script>
import MealService from '../services/MealService.js';
import ImageService from '../services/ImageService.js';

export default {
    data() {
        return {
            meal: {},
            showImage: false,
            imgPath: "",
            newImgId: null
        }
    },
    methods: {
        createMeal() {
            console.log(this.meal.dateCooked);
            this.meal.dateCooked = new Date(this.meal.date);
            MealService.createMeal(this.meal).then(
                (response) => {
                    if (this.newImgId != null) {
                        ImageService.addImageToMeal(response.data.mealId, this.newImgId).then(
                        this.$router.push({ name: 'meal-detail', params: {mealId: response.data.mealId} })
                    );
                    } else {
                        this.$router.push({ name: 'meal-detail', params: {mealId: response.data.mealId} })
                    }
                    
                }
            )
        },
        uploadImage(event){
            this.showImage = false;
            ImageService.createImage(event.target.files[0]).then(
                (response) => {
                    this.newImgId = response.data;
                    ImageService.getImage(response.data).then(
                        (r) => {
                            const base64 = ImageService.parseImg(r);
                            this.imgPath = "data:image/png;base64," + base64;
                            this.showImage = true;
                        }
                    )
                }
            );
        }
    }
}
</script>

<style scoped>
    body {
        display: flex;
        flex-direction: column;
        padding: 5px;
    }
    div {
        display: flex;
        flex-direction: column; 
        flex-grow: 1;
        justify-content: center;
    }
    h2, h3 {
        display: inline;
        margin: 5px;
    }
    p {
        margin: 5px;
        text-align: left;
    }
    input {
        width: 99%;
        margin-bottom: 3px;
    }
    form {
        width: 99%;      
    }
    textarea {
        resize: none;
        width: 99%;
        margin-bottom: 3px;
    }
    .submit {
        background-color: white;
        border: 1px solid var(--border-color);
        border-radius: 3px;
        width: 100%;
    }
    .image-block {
        background-color: var(--light-1);
        padding: 10px;
        border-radius: 10px;
    }
    .image-block h3 {
        margin: 0px;
        margin-bottom: 5px;
    }
</style>