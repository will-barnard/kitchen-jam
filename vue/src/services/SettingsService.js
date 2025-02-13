import axios from 'axios';

export default {
    updateIsPublic(isPublic) {
        let dto = {};
        dto.profilePublic = isPublic;
        return axios.put('/settings/isPublic', dto);
    },
    updateDefaultPublic(defaultPublic) {
        let dto = {};
        dto.defaultPublic = defaultPublic;
        return axios.put('/settings/defaultPublic', { defaultPublic });
    }
}