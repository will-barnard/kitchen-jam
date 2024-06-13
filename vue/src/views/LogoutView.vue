<template>
  <h1 @click="logout()">Logout</h1>
</template>

<script>
export default {
  methods: {
    logout() {
      this.$store.commit("LOGOUT");
      this.$router.push("/login");
    }
  }
};
</script>

<style>
  h1 {
    text-align: center;
  }
</style>