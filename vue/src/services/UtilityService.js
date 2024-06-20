export default {
    formatDate(date) {
        let result = new Date(date);
        let dateTemp = result.getDate() + 1;
        result.setDate(dateTemp);
        return result.toDateString().substring(4);
    }
}