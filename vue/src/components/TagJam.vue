<template>
    <div>
        <div class="jam-block" v-show="!compact || compactShow == 'unusedTags'">
            <div class="single-row">
                <h3 class="jam-title">Tags not used recently:</h3>
                <div class="spacer"></div>
                <i class="fas fa-sync-alt refresh-icon" @click="shuffleUnusedTags"></i>
            </div>
            <MiniTagCard v-for="tag in unusedTags" :key="tag.tagId" :tag="tag" :showLastUsed="true" :showGoDetail="true"/>
            <div v-if="compact">
                <h4 class="jam-link" @click="$router.push({name: 'jam'})">Jam Page <i class="fas fa-arrow-right"></i></h4>
            </div>
        </div>
        <div class="jam-block" v-show="!compact || compactShow == 'popularTags'">
            <div class="single-row">
                <h3 class="jam-title">{{ popularTagsTitle }}:</h3>
                <div class="spacer"></div>
                <i class="fas fa-exchange-alt toggle-icon" @click="togglePopularTags"></i>
                <i class="fas fa-sync-alt refresh-icon" @click="shufflePopularTags"></i>
            </div>
            <MiniTagCard v-for="tag in popularTags" :key="tag.tagId" :tag="tag" :showTimesUsed="true" :showLastUsed="!showMostUsed" :showGoDetail="true"/>
            <div v-if="compact">
                <h4 class="jam-link" @click="$router.push({name: 'jam'})">Jam Page <i class="fas fa-arrow-right"></i></h4>
            </div>
        </div>
    </div>
</template>

<script>
import MiniTagCard from './MiniTagCard.vue';

export default {
    props: ['compact'],
    components: { MiniTagCard },
    data() {
        return {
            compactShow: "",
            unusedRand: 0,
            popularRand: 0,
            unusedTagsList: [],
            popularTagsList: [],
            showMostUsed: false
        }
    },
    computed: {
        popularTagsTitle() {
            return this.showMostUsed ? 'Popular Tags' : 'Least Used Tags';
        },
        unusedTags() {
            if (this.unusedTagsList.length === 0) {
                let sortedTags = this.$store.state.userTags.sort((a, b) => {
                    if (!a.lastUsed) return 1;
                    if (!b.lastUsed) return -1;
                    return new Date(a.lastUsed) - new Date(b.lastUsed);
                });
                let firstHalfTags = sortedTags.slice(0, Math.ceil(sortedTags.length / 2));
                let shuffledTags = firstHalfTags.sort(() => this.unusedRand);
                this.unusedTagsList = shuffledTags.slice(0, 3).map(() => firstHalfTags.splice(Math.floor(Math.random() * firstHalfTags.length), 1)[0]);
            }
            return this.unusedTagsList;
        },
        popularTags() {
            if (this.popularTagsList.length === 0) {
                let sortedTags = this.$store.state.userTags.sort((a, b) => this.showMostUsed ? b.timesUsed - a.timesUsed : a.timesUsed - b.timesUsed);
                let topHalfTags = sortedTags.slice(0, Math.ceil(sortedTags.length / 2));
                let shuffledTags = topHalfTags.sort(() => this.popularRand);
                this.popularTagsList = shuffledTags.slice(0, 3).map(() => topHalfTags.splice(Math.floor(Math.random() * topHalfTags.length), 1)[0]);
            }
            return this.popularTagsList;
        }
    },
    created() {
        this.unusedRand = 0.5 - Math.random();
        this.popularRand = 0.5 - Math.random();

        if (this.compact) {
            let randomInt = Math.floor(Math.random() * 2);
            if (randomInt == 0) {
                this.compactShow = "unusedTags";
            } else {
                this.compactShow = "popularTags";
            }
        }
    },
    methods: {
        shuffleUnusedTags() {
            this.unusedRand = 0.5 - Math.random();
            this.unusedTagsList = [];
        },
        shufflePopularTags() {
            this.popularRand = 0.5 - Math.random();
            this.popularTagsList = [];
        },
        togglePopularTags() {
            this.showMostUsed = !this.showMostUsed;
            this.popularTagsList = [];
        }
    }
}
</script>

<style scoped>
    .jam-link {
        cursor: pointer;
        padding: 5px;
        margin: 2px;
        background-color: var(--light-5);
        border-radius: 5px;
    }
    .refresh-icon {
        cursor: pointer;
        margin-left: 10px;
        background-color: var(--light-4);
        padding: 5px;
        border-radius: 5px;
        margin-right: 20px;
    }
    .refresh-icon:hover {
        background-color: var(--light-6);
    }
    .single-row {
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: space-between;
        width: 100%;
    }
    .spacer {
        flex-grow: 1;
    }
    .jam-title {
        margin-left: 20px;
    }
    .toggle-icon {
        cursor: pointer;
        margin-left: 10px;
        background-color: var(--light-5);
        padding: 5px;
        border-radius: 5px;
        margin-right: 20px;
    }
    .toggle-icon:hover {
        background-color: var(--light-6);
    }
</style>