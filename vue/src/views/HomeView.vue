<template>
  <div class="home">
    <h1>Kitchen Jam</h1>

    <div class="icon">
      <img src="/img/kitchen-jam.jpeg" />
    </div>
    <div class="title">
      <h2>Improvise, Iterate, Improve!</h2>
      <p>Welcome to Kitchen Jam - log your meals, discover your own recipes, and beome a better chef.</p>
    </div>
    <div class="buttons">
      <h3 v-if="this.$store.state.token == ''" class="button" @click="$router.push({ name: 'about' })">About</h3>
      <h3 v-if="this.$store.state.token == ''" class="button" @click="$router.push({ name: 'login' })">Login</h3>
      <h3 v-if="this.$store.state.token == ''" class="button" @click="$router.push({ name: 'register' })">Create Free Account</h3>
      <h3 v-if="this.$store.state.token != ''" class="button" @click="$router.push({ name: 'meal-log' })">Meal Log</h3>
      <h3 v-if="this.$store.state.token != ''" class="button" @click="$router.push({  name: 'cookbook'})">Cookbook</h3>
    </div>
    
  </div>
</template>

<script>
export default {
};
</script>

<style scoped>
  .icon {
    display: flex;
    flex-direction: row;
    justify-content: center;
  }
  .icon img {
    width: 40vw;
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
      border-radius: 10px;
      padding: 10px;
  }
    
</style>