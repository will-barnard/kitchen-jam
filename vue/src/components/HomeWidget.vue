<template>
    <div>
        <RecipeJam v-if="jamModule === 'recipe'" :compact="true"/>
        <CategoryJam v-if="jamModule === 'category'" :compact="true"/>
        <TagJam v-if="jamModule === 'tag'" :compact="true"/>
        <p @click="cycleWidget"><i class="fas fa-exchange-alt cycle-widget"></i></p>
    </div>
</template>

<script>
import RecipeJam from './RecipeJam.vue';
import CategoryJam from './CategoryJam.vue';
import TagJam from './TagJam.vue';

export default {
    components: { RecipeJam, CategoryJam, TagJam },
    data() {
        return {
            loaded: false,
            jamModule: ""
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
</style>