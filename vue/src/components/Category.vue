<template>
    <div class="category-main">
        <div class="category-name">
            <p v-show="!editing" @click="$router.push({name: 'category-detail', params: {categoryId: category.categoryId}})">{{ category.categoryName }}</p>
            <input v-show="editing" v-model="categoryName" />
        </div>
        <div class="spacer"></div>
        <div class="category-actions">
            <div v-show="!isDeleting && !editing">
                <button @click="editing = true">Edit</button>
                <button @click="isDeleting = true">Delete</button>
            </div>
            <div v-show="isDeleting">
                <p>Are you sure? this is permanent</p>
                <button @click="isDeleting = false">No, cancel</button>
                <button @click="$emit('delete', category)">Yes, delete</button>
            </div>
            <div v-show="editing">
                <button @click="saveEdit">Save</button>
                <button @click="editing = false">Cancel</button>
            </div>
        </div>
    </div>
</template>

<script>
import CategoryService from '../services/CategoryService';

export default {
    props: ['category', 'edit'],
    data() {
        return {
            editing: false,
            categoryName: "",
            isDeleting: false
        }
    },
    created() {
        this.categoryName = this.category.categoryName;
    },
    methods: {
        deleteCategory() {
            this.isDeleting = true;
        },
        editCategory() {
            this.editing = !this.editing;
            this.categoryName = this.category.categoryName;
        },
        saveEdit() {
            let newCategory = {
                categoryId: this.category.categoryId,
                categoryName: this.categoryName
            }
            CategoryService.updateCategory(newCategory).then(
                (response) => {
                    this.$emit('edit', response.data);
                    this.editing = false;
                }
            ).catch(error => {
                console.log(error);
            }
            )
        }
    }
}
</script>

<style scoped>
    p {
        margin: 2px;
        padding: 2px;
        text-decoration: underline;
    }
    div {
        display: flex;
    }
    .category-main {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        margin: 10px;
        background-color: var(--light-2);
        border-radius: 10px;
        padding: 5px;
    }
</style>