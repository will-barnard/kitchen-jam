import axios from "axios";

export default {
    getCategory(categoryId) {
        axios.get('/category/' + categoryId)
    },
    getCategoriesByUser() {
        axios.get('/category/user')
    },
    createCategory(category) {
        axios.post('/category', category)
    },
    updateCategory(category) {
        axios.put('/category', category)
    },
    addCategoryToRecipe(recipeId, categoryId) {
        axios.post('/category/' + recipeId + '/' + categoryId)
    },
    removeCategoryFromRecipe(recipeId, categoryId) {
        axios.delete('/category/' + recipeId + '/' + categoryId)
    },
    deleteCategory(categoryId) {
        axios.delete('/category/' + categoryId)
    }
}