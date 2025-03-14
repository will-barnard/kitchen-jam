<template>
    <div class="main-tag-card">
        <h4 :class="showGoDetail ? 'go-detail' : ''" @click="showGoDetail ? $router.push({name: 'tag-detail', params: {tagId: tag.tagId}}) : ''">
            {{ tag.tagName }}
        </h4>
        <div class="spacer"></div>
        <div class="tag-info">
            <p v-if="showTimesUsed">
                Times Used: {{ tag.timesUsed || 0 }}
            </p>
            <p v-if="showLastUsed" :class="tag.lastUsed ? 'go-detail' : ''" @click="$router.push({name: 'meal-detail', params: {mealId: tag.lastUsedMeal}})">
                Last Used: {{ formatDate(tag.lastUsed) }}
            </p>
        </div>
    </div>
</template>

<script>
import UtilityService from '../services/UtilityService';

export default {
    props: ['tag', 'showLastUsed', 'showTimesUsed', 'showGoDetail'],
    methods: {
        formatDate(date) {
            return date ? UtilityService.formatDate(date) : "Never";
            
        }
    }
}
</script>

<style scoped>
    p {
        margin: 0;
    }
    .main-tag-card {
        display: flex;
        justify-content: center;
        align-items: center;
        background-color: var(--light-1);
        padding: 5px;
        padding-left: 10px;
        padding-right: 10px;
        border-radius: 10px;
        margin: 5px;
        gap: 10px;
        width: 100%;
    }
    .spacer {
        flex-grow: 1;
    }
    .go-detail {
        text-decoration: underline;;
    }
    .tag-info {
        display: flex;
        flex-direction: column;
        gap: 5px;
    }
</style>