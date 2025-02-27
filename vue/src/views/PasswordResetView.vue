<template>
    <div>
        <TopBanner />
        <div class="password-reset-container">
            <div v-if="show">
                <form v-on:submit.prevent="resetPassword">
                    <h2>Reset Password</h2>
                    <div role="alert" v-if="invalidCredentials">
                        Invalid login credentials
                    </div>
                    <div class="form-input-group">
                        <label for="password">New Password</label>
                        <input type="password" id="password" v-model="password" required autofocus />
                    </div>
                    <div class="form-input-group">
                        <label for="confirmPassword">Confirm Password</label>
                        <input type="password" id="confirmPassword" v-model="confirmPassword" required />
                    </div>
                    <button type="submit">Reset Password</button>
                </form>
            </div>
        </div>
    </div>
</template>

<script>
import TopBanner from '../components/TopBanner.vue';
import AuthService from '../services/AuthService';

export default {
    components: {
        TopBanner
    },
    methods: {
        resetPassword() {
            let dto = {
                password: this.password,
                uuid: this.$route.params.uuid
            };
            if (this.password === this.confirmPassword) {
                AuthService.updatePassword(dto).then(
                    (response) => {
                        if (response.status == 200) {
                            this.$router.push({ name: 'login' });
                        }
                    }
                );
            }
            else {
                this.invalidCredentials = true;
            }
        }
    },
    created() {
        AuthService.checkUuid(this.$route.params.uuid).then(
            (response) => {
                if (response.data == true) {
                    this.show = true;
                }
                else {
                    console.log('invalid uuid, redirecting to login');
                    this.$router.push({ name: 'login' });
                } 
            }
        ).catch(error => {
            console.clear();
            this.$router.push({ name: 'login' });
        });
    
    },
    data() {
        return {
            password: '',
            confirmPassword: '',
            show: false,
            invalidCredentials: false
        };
    }

}
</script>
<style>
.password-reset-container {
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