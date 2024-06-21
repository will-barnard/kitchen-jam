<template>
    <header>
         <h1>Cookbook</h1>
         <nav>
             <p v-on:click="log()">My Cookbook</p>
             <p v-on:click="newMeal()">Add New Recipe</p>
         </nav>
     </header>
 </template>
 <script>
 
 export default {
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
     }
     div {
         margin: 0px;
     }
     p {
         margin: 0px;
         padding: 8px;
         display: inline;
         background-color: var(--light-3);
         margin: 5px;
         border-radius: 10px;
         margin-bottom: 10px;
         margin-top: 0px;
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
 </style>