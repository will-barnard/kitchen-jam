<template>
  <div id="kitchen-jam-app">
    <div id="nav">
      <router-link v-bind:to="{ name: 'home' }">Home</router-link>&nbsp;|&nbsp;
      <router-link v-bind:to="{ name: 'meal-log' }" v-if="$store.state.token != ''">Log</router-link>&nbsp;|&nbsp;
      <router-link v-bind:to="{ name: 'cookbook' }" v-if="$store.state.token != ''">Cookbook</router-link>&nbsp;|&nbsp;
      <router-link v-bind:to="{ name: 'logout' }" v-if="$store.state.token != ''">Logout</router-link>
      
    </div>
    <router-view />
  </div>
</template>

<style scoped>
  #nav {
    text-align: center;
  }
</style>