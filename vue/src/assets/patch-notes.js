export default {
    
    // let's use 0 = major deployment, 1 = bug fix, 2 = etc.
    
    patches: [
        {
            order: 0,
            version: "1.2.1",
            date: "01/30/2025",
            headline: "Added ingredient components",
            notes: "First official patch notes. Since the website has been up for almost a year and my updates aren't on a regular schedule I'm going to begin announcing major changes here as I deploy them.\nIngredients, an essential aspect of meals and recipes, is now a useable component on Kitchen Jam. Recipes are looking a lot better - now you can add both steps and ingredients like any standard recipe. \nLooking ahead I'm not sure what is immediately next. Kitchen Jam is desperately in need of work on styles and editing forms. Other updates I'm looking toward on the horizon are User Profiles and friends, tagging friends, and more ways to explore the site. Needed updates include controls for editing your categories and tags.",
            type: 0
        },
        {
            order: 1,
            version: "1.3.1",
            date: "02/27/2025", //
            headline: "Dashboard, profiles, settings, and password reset", //
            notes: "A few things have been in the works over the past few weeks. The interface for copying links has been adjusted, as well as the way public links are stored in the database. A big headline is there is now a dashboard and user profile page. You can now add a little bit of information about yourself and share your profile. Additional profile functionality, like showing your recipe book and log are forthcoming. Additionally, you can now adjust your privacy settings in the settings page, more settings are forthcoming as well. Lastly, the password reset functionality has been implemented and you can now reset your password.",
            type: 0
        }
    ]
}