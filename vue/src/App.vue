<template>

  <div id="kitchen-jam-app">
    
    <div >
      <router-view />
    </div>

    <div class="footspace"></div>        


    <div>
      <NavComp />
    </div>

  </div>
</template>

<script>
import NavComp from "./components/NavComp.vue";

export default {
  components: {NavComp}
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
  }
</style>

