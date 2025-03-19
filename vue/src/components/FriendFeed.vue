<template>
    <div>
        <div v-if="$store.state.loadedFriends && $store.state.userFriends.length === 0">
            <p>No friends yet. Search for users to add as friends.</p>
        </div>
    </div>
</template>

<script>
export default {
}
</script>

<style>

</style>