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
         padding: 5px;
         display: inline;
         border: 1px solid black;
     }
     p:hover {
         cursor: pointer;
     }
 </style>