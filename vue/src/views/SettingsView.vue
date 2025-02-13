<template>
    <div>
        <h1>Settings</h1>
        <div v-if="showSettings">
            <SettingsComp />
        </div>
        
    </div>
</template>
<script>
import SettingsComp from '@/components/SettingsComp.vue'
export default {
    components: {
        SettingsComp
    },
    computed: {
        showSettings() {
            if (this.$store.state.loadedProfile) {
                return true;
            }
            else {
                return false;
            }
        }
    }
}
</script>
<style>
    
</style>