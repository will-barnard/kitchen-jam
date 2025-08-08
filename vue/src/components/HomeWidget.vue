<template>
    <div>
        <div class="feed">
            <div class="feed-header">
                <!-- <p @click="toggleFeed">
                    <i :class="feedVisible ? 'fas fa-caret-down' : 'fas fa-caret-right'" class="toggle-feed"></i>
                </p> -->
                <h3>Latest from your friends</h3>                
            </div>     
            <FriendFeed v-if="feedVisible" :compact="true"/>
        </div>
        <!-- <div class="jam-holder" v-if="validRecipes">
            <RecipeJam v-if="jamModule === 'recipe'" :compact="true"/>

        </div>
        <div class="jam-holder" v-if="validTags">
            <CategoryJam v-if="jamModule === 'category'" :compact="true"/>

        </div>
        <div class="jam-holder" v-if="validCategories">
            <TagJam v-if="jamModule === 'tag'" :compact="true"/>
        </div>
        <p @click="cycleWidget" v-if="showCycleWidget"><i class="fas fa-exchange-alt cycle-widget"></i></p> -->
    </div>
</template>

<script>
import RecipeJam from './RecipeJam.vue';
import CategoryJam from './CategoryJam.vue';
import TagJam from './TagJam.vue';
import FriendFeed from './FriendFeed.vue';

export default {
    components: { RecipeJam, CategoryJam, TagJam, FriendFeed },
    data() {
        return {
            loaded: false,
            jamModule: "",
            feedVisible: true
        }
    },
    created() {
        const modules = ['recipe', 'category', 'tag'];
        this.jamModule = modules[Math.floor(Math.random() * 3)];
    },
    methods: {
        cycleWidget() {
            const modules = ['recipe', 'category', 'tag'];
            let newModule;
            do {
                newModule = modules[Math.floor(Math.random() * 3)];
            } while (newModule === this.jamModule);
            this.jamModule = newModule;
        },
        toggleFeed() {
            this.feedVisible = !this.feedVisible;
        }
    },
    computed: {
        validRecipes() {
            return this.$store.state.userRecipes.length > 4 ? true : false;
        },
        validTags() {
            return this.$store.state.userTags.length > 10 ? true : false;
        },
        validCategories() {
            return this.$store.state.userCategories.length > 2 ? true : false;
        },
        showCycleWidget() {
            if (this.jamModule === 'recipe' && this.validRecipes) {
                return true;
            } else if (this.jamModule === 'category' && this.validCategories) {
                return true;
            } else if (this.jamModule === 'tag' && this.validTags) {
                return true;
            }
            return false;
        }
    }
}
</script>
<style scoped>
    .cycle-widget {
        cursor: pointer;
        margin-left: 10px;
        background-color: var(--light-5);
        padding: 5px;
        border-radius: 5px;
        margin-right: 20px;
    }
    .cycle-widget:hover {
        background-color: var(--light-5);
    }
    .feed {
        margin: 10px;
        padding: 10px;
        background-color: var(--light-1);
        border-radius: 10px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        transition: background-color 0.3s ease;
    }
    .toggle-feed {
        cursor: pointer;
        margin-left: 10px;
        background-color: var(--light-5);
        padding: 5px;
        border-radius: 5px;
        margin-right: 20px;
    }
    .toggle-feed:hover {
        background-color: var(--light-4);
    }
    .spacer {
        flex-grow: 1;
    }
    .feed-header {
        display: flex;
        align-items: center;
        justify-content: center;
        
    }
    .jam-holder {
        margin-left: 10px;
        margin-right: 10px;
        border-radius: 15px;

        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        transition: background-color 0.3s ease;
    }
</style>