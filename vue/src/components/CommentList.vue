<template>
    <div>
        <div 
            v-for="comment in comments" 
            :key="comment.commentId" 
            :class="['comment', { 'subcomment': subComments }]"
        >
            <div class="comment-header">
                <h4>{{ comment.userName }}</h4>
                <p>{{ formatDateTime(comment.createdAt) }}</p>
                <div class="comment-actions">
                    <button 
                        v-if="$store.state.userProfile.userId === comment.userId && !editingComments[comment.commentId]" 
                        @click="startEditing(comment.commentId)" 
                        class="edit-button"
                    >
                        <i class="far fa-edit"></i>
                    </button>
                    <button 
                        v-if="$store.state.userProfile.userId === comment.userId" 
                        @click="emitDeleteComment(comment.commentId)" 
                        class="trash-button"
                    >
                        <i class="far fa-trash-alt"></i>
                    </button>
                </div>
            </div>
            <div v-if="editingComments[comment.commentId]">
                <textarea v-model="editingContents[comment.commentId]" class="edit-textarea"></textarea>
                <button @click="submitEdit(comment)" class="submit-edit-button">Submit</button>
                <button @click="cancelEditing(comment.commentId)" class="cancel-edit-button">Cancel</button>
            </div>
            <p v-else>{{ comment.commentContent }}</p>
            <div v-if="comment.subComments && !subComments" class="sub-comments">
                <CommentList 
                    :comments="comment.subComments" 
                    :subComments="true" 
                    :parentId="comment.commentId" 
                    @delete-comment="$emit('delete-comment', $event)" 
                    @edit-comment="$emit('edit-comment', $event)" 
                />
            </div>
            <div v-if="$store.state.token != '' && !subComments" class="new-comment">
                <button v-if="!replyForms[comment.commentId]" @click="toggleReplyForm(comment.commentId)">Reply</button>
                <div v-if="replyForms[comment.commentId]" @submit.prevent="submitReply(comment.commentId)" class="comment-form">
                    <form>
                        <textarea v-model="replyContents[comment.commentId]" placeholder="Write a reply..."></textarea>
                        <button type="submit">Submit</button>
                        <button @click="toggleReplyForm(comment.commentId)">Cancel</button>
                    </form>
                </div>
            </div>
        </div>
        <div v-if="$store.state.token != '' && !subComments">
            <button v-if="!showReplyForm" @click="showReplyForm = !showReplyForm">Add Comment</button>
            <div v-if="showReplyForm" @submit.prevent="submitComment" class="comment-form">
                <form>
                    <textarea v-model="newCommentContent" placeholder="Write a comment..."></textarea>
                    <button type="submit">Submit</button>
                    <button @click="showReplyForm = !showReplyForm">Cancel</button>
                </form>
            </div>
        </div>
    </div>
</template>

<script>

import UtilityService from '../services/UtilityService';

export default {
    props: ['comments', 'subComments', 'mealId', 'recipeId', 'parentId'],
    data() {
        return {
            newComment: {
                commentContent: '',
                recipeId: this.recipeId || null,
                mealId: this.mealId || null,
                parentId: this.parentId || null,
                userId: this.$store.state.userProfile.userId
            },
            showReplyForm: false,
            newCommentContent: '',
            replyForms: {}, // Initialize as an empty object
            replyContents: {}, // Initialize as an empty object
            editingComments: {}, // Track which comments are being edited
            editingContents: {} // Track the content of comments being edited
        }
    },
    methods: {
        submitComment() {
            if (this.newCommentContent.trim() === '') {
                return;
            }
            this.newComment.commentContent = this.newCommentContent;
            this.$emit('submit-comment', this.newComment);
            this.newComment.commentContent = '';
            this.showReplyForm = false; 
        },
        submitReply(parentId) {
            const replyContent = this.replyContents[parentId] || '';
            if (replyContent.trim() === '') {
                return;
            }
            const reply = {
                commentContent: replyContent,
                recipeId: this.recipeId || null,
                mealId: this.mealId || null,
                parentId: parentId,
                userId: this.$store.state.userProfile.userId
            };
            this.$emit('submit-comment', reply);
            this.replyContents = { ...this.replyContents, [parentId]: '' }; // Clear the reply content
            this.replyForms = { ...this.replyForms, [parentId]: false }; // Hide the reply form
        },
        toggleReplyForm(commentId) {
            this.replyForms = { ...this.replyForms, [commentId]: !this.replyForms[commentId] }; // Toggle visibility
        },
        formatDateTime(date) {
            return UtilityService.formatDateTime(date);
        },
        emitDeleteComment(commentId) {
            this.$emit('delete-comment', commentId); // Ensure the event is emitted correctly
        },
        emitEditComment(comment) {
            this.$emit('edit-comment', comment); // Emit the edit-comment event with the comment data
        },
        startEditing(commentId) {
            this.editingComments = { ...this.editingComments, [commentId]: true };
            const comment = this.comments.find(c => c.commentId === commentId);
            if (comment) {
                this.editingContents = { ...this.editingContents, [commentId]: comment.commentContent };
            }
        },
        cancelEditing(commentId) {
            this.editingComments = { ...this.editingComments, [commentId]: false };
            this.editingContents = { ...this.editingContents, [commentId]: '' };
        },
        submitEdit(comment) {
            const updatedContent = this.editingContents[comment.commentId];
            if (updatedContent.trim() === '') {
                return;
            }
            this.$emit('edit-comment', { ...comment, commentContent: updatedContent });
            this.editingComments = { ...this.editingComments, [comment.commentId]: false };
            this.editingContents = { ...this.editingContents, [comment.commentId]: '' };
        }
    }
}
</script>

<style scoped>
.spacer {
    flex-grow: 1;
}
form {
    width: 100%;
}
.comment {
    border: 1px solid #ddd;
    padding: 10px;
    margin-bottom: 10px;
    border-radius: 5px;
    background-color: var(--light-1);
}

.comment.subcomment {
    background-color: var(--light-2);
}

.comment-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 5px;
}

.comment-header h4 {
    margin: 0;
    font-size: 16px;
    font-weight: bold;
}

.comment-header p {
    margin: 0;
    font-size: 12px;
    color: #888;
}
.new-comment {
    display: flex;
    flex-direction: column; /* Stack elements vertically */
    align-items: flex-end; /* Align buttons to the right */
    width: 100%; /* Ensure it takes up the full width */
}
.sub-comments {
    margin-left: 20px;
    margin-top: 10px;
}
.comment-form {
    width: 100%; /* Ensure the form takes up the full width */
    display: flex;
    justify-content: center; /* Center the textarea horizontally */
    align-items: center; /* Align items vertically */
    flex-direction: column; /* Stack elements vertically */
}

textarea {
    width: 95%; /* Set width to 90% */
    height: 60px;
    margin-bottom: 10px;
    padding: 5px;
    border: 1px solid #ddd;
    border-radius: 5px;
    resize: none;
}

button {
    background-color: #007bff;
    color: white;
    border: none;
    padding: 3px 6px; /* Reduce padding for a smaller button */
    border-radius: 5px;
    cursor: pointer;
    width: auto; /* Allow the button to size dynamically */
    height: auto; /* Remove fixed height */
    text-align: center;
    font-size: 12px; /* Reduce font size for better fit */
    margin-right: 5px; /* Add margin between buttons */
}

button:last-child {
    margin-right: 0; /* Remove margin for the last button */
}

button:hover {
    background-color: #0056b3;
}
p {
    margin-top: 0px;
    margin-bottom: 0px;
}
.trash-button {
    background-color: var(--light-3);
    color: black;
    border: none;
    cursor: pointer;
    font-size: 16px;
    margin-left: 2px;
}
.comment-actions {
    display: flex;
    gap: 0px; /* Reduce the gap between buttons */
}

.edit-button {
    background-color: var(--light-5);
    color: black;
    border: none;
    cursor: pointer;
    font-size: 16px;
}

.edit-button:hover {
    color: darkblue;
}
.edit-textarea {
    width: 95%;
    height: 60px;
    margin-bottom: 10px;
    padding: 5px;
    border: 1px solid #ddd;
    border-radius: 5px;
    resize: none;
}

.submit-edit-button {
    color: white;
    border: none;
    padding: 5px 10px;
    border-radius: 5px;
    cursor: pointer;
    margin-right: 5px;
}

.submit-edit-button:hover {
    background-color: #218838;
}

.cancel-edit-button {
    color: white;
    border: none;
    padding: 5px 10px;
    border-radius: 5px;
    cursor: pointer;
}

.cancel-edit-button:hover {
    background-color: #c82333;
}
</style>