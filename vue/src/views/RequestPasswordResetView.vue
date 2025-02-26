<template>
    <div>
        <h1>Request Password Reset</h1>
        
        <div v-if="!emailSent && !loading">
            <form v-on:submit.prevent="requestPasswordReset">
                <h2>Request Password Reset</h2>
                <div role="alert" v-if="invalidEmail">
                    Invalid email address
                </div>
                <div class="form-input" v-if="!emailSent">
                    <label for="email">Email</label>
                    <input type="email" id="email" v-model="email" required autofocus />
                    <button type="submit">Request Password Reset</button>
                </div>
            </form>
        </div>
        <div>
            <div role="alert" v-if="emailSent">
                Password reset email sent
            </div>
        </div>
        <LoadingWidget v-if="loading" />
    </div>
</template>

<script>
import AuthService from '../services/AuthService';
import LoadingWidget from '../components/LoadingWidget.vue';

export default {
    components: {
        LoadingWidget
    },
    methods: {
        requestPasswordReset() {
            let emailDto = {
                email: this.email
            }
            loading = true;
            AuthService
                .requestPasswordReset(emailDto)
                .then(
                    (response) => {
                        if (response.status == 200) {
                            this.emailSent = true;
                            this.loading = false;
                        }
                    }
                )
                .catch(error => {
                    this.invalidEmail = true;
                });
        }
    },
    data() {
        return {
            email: "",
            invalidEmail: false,
            emailSent: false,
            loading: false
        };
    }
}
</script>

<style>
    
</style>