<template>

  <div id="kitchen-jam-app">
    
    <div >
      <router-view />
    </div>

    <div class="footspace"></div>        


    <div>
      <NavComp class="navcomp" />
    </div>

  </div>
</template>

<script>
import NavComp from "./components/NavComp.vue";

export default {
  components: {NavComp},
  created() {
    if (this.$store.state.token) {
      this.$store.commit('GET_USER_MEALS');
      this.$store.commit('GET_USER_RECIPES');
    }
  }
}
</script>

<style scoped>
  #kitchen-jam-app {
    width: 100%;
  }
  .router {
    position: fixed;
    width: 100%;
    text-align: center;
  }
  .footspace {
    height: 80px;
    background-color: none;
  }
  .navcomp {
    z-index: 1;
  }
</style>

