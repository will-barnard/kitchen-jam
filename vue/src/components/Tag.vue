<template>
    <div class="tag-container">
        <div class="tag-main">
            <div class="tag-name">
                <p v-show="!editing" @click="$router.push({ name: 'tag-detail', params: { tagId: tag.tagId } })">{{ tag.tagName }}</p>
                <input v-show="editing" v-model="tagName" />
            </div>
            <div class="spacer"></div>
            <div class="tag-actions">
                <div v-show="!isDeleting && !editing">
                    <button @click="editing = true">Edit</button>
                    <button @click="isDeleting = true">Delete</button>
                </div>
                <div v-show="isDeleting">
                    <p>Are you sure? this is permanent</p>
                    <button @click="isDeleting = false">No, cancel</button>
                    <button @click="$emit('delete', tag)">Yes, delete</button>
                </div>
                <div v-show="editing">
                    <button @click="saveEdit">Save</button>
                    <button @click="editing = false">Cancel</button>
                </div>
            </div>
        </div>
        <div class="tag-main tag-type" v-if="editing">
            <select v-model="tag.tagType.tagCategory" @change="updateTypes">
                <option value="">Select Category</option>
                <option v-for="category in $store.state.tagCategories" :key="category.tagCategoryName" :value="category.tagCategoryName">
                    {{ category.tagCategoryName }}
                </option>
            </select>
            <select v-if="tag.tagType.tagCategory" v-model="tag.tagType.tagTypeName">
                <option value="">Select Type</option>
                <option v-for="type in filteredTypes" :key="type.tagTypeName" :value="type.tagTypeName">
                    {{ type.tagTypeName }}
                </option>
            </select>
        </div>
    </div>
</template>

<script>
import TagService from '../services/TagService';

export default {
    props: ['tag', 'edit'],
    data() {
        return {
            editing: false,
            tagName: "",
            isDeleting: false
        }
    },
    created() {
        this.tagName = this.tag.tagName;
        if (!this.tag.tagType) {
            this.tag.tagType = {};
        }

    },
    computed: {
        filteredTypes() {
            if (!this.tag.tagType.tagCategory) return [];
            const category = this.$store.state.tagCategories.find(cat => cat.tagCategoryName === this.tag.tagType.tagCategory);
            return category ? category.tagTypes : [];
        }
    },
    methods: {
        deleteTag() {
            this.isDeleting = true;
        },
        editTag() {
            this.editing = !this.editing;
            this.tagName = this.tag.tagName;
        },
        saveEdit() {
            const category = this.$store.state.tagCategories.find(cat => cat.tagCategoryName === this.tag.tagType.tagCategory);
            const type = category ? category.tagTypes.find(type => type.tagTypeName === this.tag.tagType.tagTypeName) : null;
            let newTag = {
                tagId: this.tag.tagId,
                tagName: this.tagName,
                tagType: {
                    tagTypeId: type ? type.tagTypeId : null
                }
            }
            TagService.updateTag(newTag).then(
                (response) => {
                    if (!response.data.tagType) {
                        response.data.tagType = { tagCategory: "", tagTypeName: "" };
                    }
                    this.$emit('edit', response.data);
                    this.editing = false;
                    
                }
            ).catch(error => {
                console.log(error);
            }
            )
        },
        updateTypes() {
            this.tag.tagType.tagTypeName = this.tag.tagType.tagCategory;
        }
    }
}
</script>

<style scoped>
    p {
        margin: 2px;
        padding: 2px;
    }
    div {
        display: flex;
    }
    .tag-main {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        margin: 10px;
        background-color: var(--light-2);
        border-radius: 10px;
        padding: 5px;
    }
    .tag-name p {
        text-decoration: underline;
    }
    .tag-container {
        width: 100%;
        display: flex;
        flex-direction: column;
    }
</style>