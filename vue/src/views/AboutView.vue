<template>
    <div>
        <h1>Welcome to<br>Kitchen Jam!</h1>
        <h3>
            Improvise, iterate, improve!
        </h3>
        <div class="copy">
            <p>
                Kitchen Jam is your home for logging meals cooked and becoming a better home chef.
            </p>
            
            Even just jotting down a couple notes will help you: 
            <ul>
                <li>remember your strategies</li>
                <li>duplicate your success</li>
                <li>fix your failures</li>
            </ul>
        </div>
        
        <h3 class="button" @click="$router.push({ name: 'profile', params: { userId: 1001 }})">Will's Cookbook</h3 >
        <h3 class="button" @click="$router.push('walkthrough')">How does it work?</h3>
        <h3 class="button" @click="$router.push('register')">Sign up for a free account</h3>

        <p>Created by Will Barnard 2023-2025</p>

    </div>
</template>

<script>


export default {
    
}
</script>

<style scoped>
    .button {
        background-color: var(--dark-1);
        border-radius: 10px;
        padding: 5px;
        text-align: center;
    }
    .copy {
        background-color: var(--light-1);
        border-radius: 10px;
        padding: 10px;
        margin: 20px;
        padding-bottom: 1px;
        font-weight: 300;
        text-align: center;
    }
    .copy p {
        margin: 5px;
    }
    p {
        text-align: center;
        font-weight: 100;
    }
    li {
        text-align: left;
        padding: 0px;
    }
    h3 {
        text-align: center;
        font-style: italic;
    }
</style>