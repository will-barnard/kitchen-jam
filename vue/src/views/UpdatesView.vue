<template>
    <Transition name="fade">
        <div v-if="show">
            <h1 @click="$router.push({  name: 'home'})">
            Kitchen Jam Updates
            </h1>
            <div>
                <div v-for="patch of updates" :key="patch.order" class="patch-container">
                    <div class="single-row top">
                        <p>{{ patch.date }}</p>
                        <div class="spacer"></div>
                        <p>v. {{ patch.version }}</p>
                    </div>
                    <h4>{{ patch.typeDisplay }}</h4>
                    <h2>{{ patch.headline }}</h2>
                    <p>{{ patch.notes }}</p>
                </div>
            </div>
        </div>
    </Transition>
</template>
<script>
import Updates from '../assets/patch-notes.js'

export default {
    data() {
        return {
            updates: {},
            show: false,
        }
    },
    created() {
        this.updates = Updates.patches.reverse();
        for (let patch of this.updates) {
            if (patch.type == 0) {
                patch.typeDisplay = "Major Deployment"
            } else if (patch.type == 1) {
                patch.typeDisplay = "Minor Deployment"
            } else if (patch.type == 2) {
                patch.typeDisplay = "Hotfix"
            } else if (patch.type == 3) {
                patch.typeDisplay = "Feature"
            } else if (patch.type == 4) {
                patch.typeDisplay = "Bugfix"
            } else if (patch.type == 5) {
                patch.typeDisplay = "Security Patch"
            }
        }
        setTimeout(() => {
            this.show = true
        }, 10);
    }
}
</script>
<style scoped>
    .spacer {
        flex-grow: 1;
    }
    .patch-container {
        background-color: var(--light-1);
        padding: 15px;
        margin: 10px;
        border-radius: 20px;
    }
    p {
        margin: 5px;
    }
    h4 {
        margin: 5px;
        margin-top: 15px;
    }
    h2 {
        margin: 5px;
        margin-bottom: 15px;
    }
    .top {
        font-weight: bold;
    }

</style>