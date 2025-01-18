<template>
    <header>

        <div>
            <h1 v-on:click="log()">Cookbook</h1>
            <div class="spacer">

            </div>
            <nav>
                <p v-on:click="newMeal()" v-show="!create">+ Add New Recipe</p>
            </nav>
        </div>
         
     </header>
 </template>
 <script>
 
 export default {
    props: ['create'],
     methods: {
         log() {
             this.$router.push({ name: 'cookbook'});
         },
         newMeal() {
             this.$router.push({ name: 'new-recipe'})
         }
     }
 }
 </script>
 
 
 <style scoped>
     header {
         text-align: center;
     }
     nav {
         display: flex;
         flex-direction: row;
         justify-content: center;
         align-items: center;
     }
     div {
         margin: 0px;
         display: flex;
         justify-content: center;
         align-items: center;
     }
     p {
         margin: 0px;
         padding: 8px;
         display: inline;
         background-color: var(--light-3);
         margin: 5px;
         border-radius: 10px;
         font-weight: bold;
     }
     p:hover {
         cursor: pointer;
     }
     h1 {
        margin: 0px;
        padding-top: 10px;
        margin-bottom: 5px;
     }
     .spacer {
        flex-grow: 1;
     }
 </style>