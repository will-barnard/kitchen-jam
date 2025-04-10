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
        },
        {
            order: 2,
            version: "1.3.2",
            date: "03/19/2025",
            headline: "Jam Page, Friendships, and other bug fixes and improvements",
            notes: "The Jam Page is now implemented, where you can generate insights based on your meals and recipes logged. You can now also add friends and create friendships, a social friend feed where you can see what your friends are cooking is forthcoming. A few bugs have been fixed, and recipe/meal creation now allows all parameters to be set on creation. Also, ratings now display as stars.",
            type: 0
        },
        {
            order: 3,
            version: "1.3.3",
            date: "03/31/2025",
            headline: "Bug fixes and improvements",
            notes: "Images now lazy load, greatly speeding up the performance of the application. There is now a button to grab ingredients from last time a recipe was logged, both in meal logging and on the edit recipe page. This should save some time when logging meals. Lastly, some styles were updated for a better look. It still doesn't look totally polished but I'm working on it. Forthcoming still are friend feeds, commenting, and collaboration.",
            type: 1
        },
        {
            order: 4,
            version: "1.4.0",
            date: "04/10/2025",
            headline: "Friend feeds, commenting, this is the big social update",
            notes: "You can now see your friends' meals and recipes in a feed, and comment on their meals and recipes. This is a big step toward the social aspect of Kitchen Jam. I'm still working on collaboration, but as of now I'm officially broadcasting that the site is ready for use. Still more work to be done, but let's get cooking!",
            type: 0
        }
    ]
}