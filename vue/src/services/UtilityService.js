export default {
    formatDate(date) {
        let result = new Date(date);
        let dateTemp = result.getDate() + 1;
        result.setDate(dateTemp);
        let dateString = result.toDateString().substring(4);
        dateString = dateString.replace(/ /g, '\u00A0');
        return dateString;
    },
    formatDateTime(date) {
        let result = new Date(date);
        let dateTemp = result.getDate() + 1;
        result.setDate(dateTemp);
        let dateString = result.toDateString().substring(4).replace(/ /g, '\u00A0');
        let hours = result.getHours();
        let minutes = result.getMinutes().toString().padStart(2, '0');
        let ampm = hours >= 12 ? 'PM' : 'AM';
        hours = hours % 12 || 12; // Convert to 12-hour format
        let timeString = `${hours}:${minutes} ${ampm}`;
        return `${dateString}, ${timeString}`;
    }
}