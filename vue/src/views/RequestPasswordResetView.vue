<template>
    <div>
        <TopBanner />        
        <div class="request-password-reset-container" v-if="!emailSent && !loading">
            <form v-on:submit.prevent="requestPasswordReset">
                <h2>Request Password Reset</h2>
                <div role="alert" v-if="invalidEmail">
                    Invalid email address
                </div>
                <div class="form-input-group" v-if="!emailSent">
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
        <LoadingWidgetQuick v-if="loading" />
    </div>
</template>

<script>
import AuthService from '../services/AuthService';
import LoadingWidgetQuick from '../components/LoadingWidgetQuick.vue';
import TopBanner from '../components/TopBanner.vue';

export default {
    components: {
        LoadingWidgetQuick, TopBanner
    },
    data() {
        return {
            email: "",
            invalidEmail: false,
            emailSent: false,
            loading: false
        };
    },
    methods: {
        requestPasswordReset() {
            let emailDto = {
                email: this.email
            }
            this.loading = true;
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
                    console.clear();
                    this.loading = false;
                    if (error.response && error.response.status === 404) {
                        this.invalidEmail = true;
                        this.emailSent = true;
                    }
                });
        }
    }
}
</script>

<style>

.request-password-reset-container {
    max-width: 400px;
    margin: 0 auto;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 0 10px var(--box-shadow-color);
    background-color: var(--light-2);
}

.form-input-group {
    margin-bottom: 15px;
}

.form-input-group label {
    display: block;
    margin-bottom: 5px;
    color: var(--label-color);
}

.form-input-group input {
    width: 100%;
    padding: 8px;
    box-sizing: border-box;
    border: 1px solid var(--input-border-color);
    border-radius: 3px;
}
</style>