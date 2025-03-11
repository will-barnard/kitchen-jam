<template>
    <Transition name="fade">
    <div v-show="show">
        <TopBanner />
        <div class="main" v-if="!isNewTag">
            <div class="single-row top-area">
                <h2>Edit Tags</h2>
                <div class="spacer"></div>
                <p @click="isNewTag = true"><i class="fas fa-plus"></i></p>
                <p @click="searchButton"><i class="fas fa-search"></i></p>
            </div>
            <div class="search-area" v-show="filter === 'search'">
                <input type="text" v-model="search" placeholder="Search tags" />
                <p @click="search = ''"><i class="fas fa-times"></i></p>
            </div>
            <div>

            </div>
            <div>
                <Tag v-for="tag in tags" :key="tag.tagId" :tag="tag" @edit="editTag" @delete="deleteTag(tag)" />
            </div>
        </div>
        <div class="main" v-if="isNewTag">
            <div>
                <h2>New Tag</h2>
                <div class="single-row top-area">   
                    <input type="text" v-model="newTag" placeholder="Tag Name" />
                    <div class="spacer"></div>
                    <p @click="addTag"><i class="fas fa-plus"></i></p>
                    <p @click="isNewTag = false"><i class="fas fa-times"></i></p>
                </div>
            </div>
        </div>
    </div>
    </Transition>
</template>

<script>
import TopBanner from '@/components/TopBanner.vue';
import Tag from '../components/Tag.vue';
import TagService from '../services/TagService';

export default {
    components: {
        TopBanner, Tag
    },
    created() {
        if (this.$store.state.loadedTags) {
            setTimeout(() => this.show = true, 1);
        } else {
            this.loadingTick()
        }
    },
    data() {
        return {
            filter: "default",
            search: "",
            isNewTag: false,
            newTag: "",
            show: false
        }
    },
    computed: {
        tags() {
            if (this.filter === "default" || this.search === "") {
                return this.$store.state.userTags.slice().sort((a, b) => {
                    if (a.tagName < b.tagName) return -1;
                    if (a.tagName > b.tagName) return 1;
                    return 0;
                });
            } else if (this.filter === "search" && this.search) {
                return this.$store.state.userTags.filter(tag => tag.tagName.toLowerCase().includes(this.search.toLowerCase())).sort((a, b) => {
                    if (a.tagName < b.tagName) return -1;
                    if (a.tagName > b.tagName) return 1;
                    return 0;
                });
            }
        }
    },
    methods: {
        editTag(tag) {
            this.$store.commit('EDIT_TAG', tag);
        },
        deleteTag(tag) {
            this.$store.commit('DELETE_TAG', tag.tagId);
        },
        searchButton() {
            if (this.filter === "search") {
                this.filter = "default";
            } else {
                this.filter = "search";
            }
        },
        addTag() {
            let tag = {
                tagName: this.newTag
            }
            TagService.createTag(tag).then(
                (response) => {
                    this.$store.commit('ADD_TAG', response.data);
                    this.newTag = "";
                    this.isNewTag = false;
                }
            ).catch(error => {
                console.log(error);
            }
            )
        },
        loadingTick() {
            setTimeout(() => {
                if (this.$store.state.loadedTags) {
                    this.show = true;
                } else {
                    this.loadingTick();
                }
            }, 500);
        }
    }
}
</script>
<style scoped>
    h2 {
        margin: 10px;
    }
    p {
        margin: 0px;
        
    }
    .main {
        display: flex;
        flex-direction: column;
        background-color: var(--light-1);
        border-radius: 10px;
        padding: 5px;
        margin-bottom: 10px;
    }
    .top-area {
        display: flex;
        flex-direction: row;
        margin: 5px;
    }
    .spacer {
        flex-grow: 1;
    }
    .search-area {
        margin: 5px;
        display: flex;
        justify-content: center;
    }
    .search-area input {
        padding: 5px;
        border-radius: 5px;
        border: 1px solid var(--border-color);
        width: 75%;
    }
    i {
        margin: 5px;
        border: 1px solid var(--border-color);
        padding: 5px;
        border-radius: 5px;
        background-color: var(--light-2);
    }
</style>