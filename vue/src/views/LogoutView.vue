<template>
  <h1>Logout</h1>
  <div class="context">
    <p>Are you sure you want to log out?</p>
    <button @click="logout()">Log Out</button>
  </div>
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
  .context {
    background-color: var(--light-2);
    margin: 10px;
    padding: 10px;
    border-radius: 10px;
  }
</style>