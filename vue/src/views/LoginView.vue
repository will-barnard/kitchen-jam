<template>

  <h1>Kitchen Jam</h1>
  <div id="login">
    <form v-on:submit.prevent="login">
      <h2>Sign In</h2>
      <div role="alert" v-if="invalidCredentials">
        Invalid login credentials
      </div>
      <div role="alert" v-if="this.$route.query.registration">
        Thank you for registering, please sign in.
      </div>
      <div class="form-input-group">
        <label for="email">Email</label>
        <input type="email" id="email" v-model="user.email" required autofocus />
      </div>
      <div class="form-input-group">
        <label for="password">Password</label>
        <input type="password" id="password" v-model="user.password" required />
      </div>
      <button type="submit">Sign in</button>
      <p>
        <router-link v-bind:to="{ name: 'register' }">New user? Sign up for a free account</router-link>
      </p>
      <p>
        <router-link v-bind:to="{ name: 'password-reset-request' }">Password Reset</router-link>
      </p>

    </form>
  </div>

</template>

<script>
import authService from "../services/AuthService";

export default {
  components: {},
  data() {
    return {
      user: {
        email: "",
        password: ""
      },
      invalidCredentials: false
    };
  },
  methods: {
    login() {
      authService
        .login(this.user)
        .then(response => {
          if (response.status == 200) {
            this.$store.commit("INITIALIZE_USER");
            this.$router.push("/");
          }
        })
        .catch(error => {
          const response = error.response;

          if (response.status === 401) {
            this.invalidCredentials = true;
          }
        });
    }
  }
};
</script>

<style scoped>
.form-input-group {
  margin-bottom: 1rem;
  display: flex;
  flex-wrap: wrap;
  margin: 10px;
}
label {
  margin-right: 0.5rem;
  width: 5em;
}
input {
  flex-grow: 1;
}
button {
  margin: 10px;
  width: 5em;
}
h2 {
  margin: 10px;
  margin-bottom: 15px;
}
#login {
  background-color: var(--light-2);
  border-radius: 10px;
  margin: 10px;
  padding: 10px;
  
}

</style>