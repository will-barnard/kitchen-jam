import axios from "axios";

export default {

    getNotifications() {
        return axios.get('/notification')
    },
    getOldNotifications() {
        return axios.get('/notification/old')
    },
    markNotificationAsRead(notificationId) {
        return axios.put('/notification/' + notificationId)
    },
    deleteNotification(notificationId) {
        return axios.delete('/notification/' + notificationId)
    }
    
}