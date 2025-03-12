<template>
  <Transition name="fade">
    <div class="home" v-show="show">
      <h1>Kitchen Jam</h1>

      <div class="icon" v-if="this.$store.state.token == ''">
        <img src="/img/kitchen-jam.jpeg" />
      </div>
      <div v-if="this.$store.state.token != ''">
        <RecipeJam :compact="true"/>
      </div>
      <div class="title" v-if="this.$store.state.token == ''">
        <h2>Improvise, Iterate, Improve!</h2>
        <p>Welcome to Kitchen Jam - log your meals, discover your own recipes, and beome a better chef.</p>
      </div>
      <div class="title" v-if="this.$store.state.token != '' && this.$store.state.loadedProfile">
        <h3>Welcome back, {{ $store.state.userProfile.displayName }}!</h3>
      </div>
      <div class="buttons">
        <h3 v-if="this.$store.state.token == ''" class="button" @click="$router.push({ name: 'about' })">About</h3>
        <h3 v-if="this.$store.state.token == ''" class="button" @click="$router.push({ name: 'login' })">Login</h3>
        <h3 v-if="this.$store.state.token == ''" class="button" @click="$router.push({ name: 'register' })">Create Free Account</h3>
        <h3 v-if="this.$store.state.token != ''" class="button" @click="$router.push({ name: 'meal-log' })">Meal Log</h3>
        <h3 v-if="this.$store.state.token != ''" class="button" @click="$router.push({  name: 'cookbook'})">Cookbook</h3>
        <h3 v-if="this.$store.state.token != ''" class="button2" @click="$router.push({  name: 'dashboard'})">Dashboard</h3>
      </div>
      
    </div>
  </Transition>
</template>

<script>
import RecipeJam from '../components/RecipeJam.vue';

export default {
  components: {RecipeJam},
  data() {
    return {
      show: false
    }
  },
  created() {
    setTimeout(
      () => {this.show = true}
      , 10)
  }
};
</script>

<style scoped>
  .icon {
    display: flex;
    flex-direction: row;
    justify-content: center;
  }
  .icon img {
    width: 60vw; /* Adjusted width */
  }
  .home {
    text-align: center;
  }
  .home h1 {
    text-align: center;
    padding: 15px;
  }
  .title {
    margin: 30px;
  }
  .buttons {
    margin: 10px;
  }
  .button {
      background-color: var(--dark-1);
      border: 2px solid var(--dark-1);
      border-radius: 10px;
      padding: 10px;
  }
  .button2 {
      background-color: var(--bg-color);
      border: 2px solid var(--dark-1);
      border-radius: 10px;
      padding: 10px;
  }
    
</style>