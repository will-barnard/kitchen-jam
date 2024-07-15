import axios from "axios";

export default {
    getCategory(categoryId) {
        return axios.get('/category/' + categoryId)
    },
    getCategoriesByUser() {
        return axios.get('/category/user')
    },
    searchCategory(category) {
        return axios.post('/category/search', category)
    },
    createCategory(category) {
        return axios.post('/category', category)
    },
    updateCategory(category) {
        return axios.put('/category', category)
    },
    deleteCategory(categoryId) {
        return axios.delete('/category/' + categoryId)
    }
}