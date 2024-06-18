export default {
    formatDate(date) {
        return new Date(date).toDateString().substring(4);
    }
}