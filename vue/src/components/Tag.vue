<template>
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
            let newTag = {
                tagId: this.tag.tagId,
                tagName: this.tagName
            }
            TagService.updateTag(newTag).then(
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
</style>