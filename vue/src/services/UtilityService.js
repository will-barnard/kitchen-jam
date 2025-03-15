export default {
    formatDate(date) {
        let result = new Date(date);
        let dateTemp = result.getDate() + 1;
        result.setDate(dateTemp);
        let dateString = result.toDateString().substring(4);
        dateString = dateString.replace(/ /g, '\u00A0');
        return dateString;
    }
}