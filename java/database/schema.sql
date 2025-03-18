BEGIN TRANSACTION;

DROP TABLE IF EXISTS tags_meal, recipe_category, meal_image, recipe_image, user_email, users, tags, recipe, image, meal, category;

DROP SEQUENCE IF EXISTS seq_user_id, seq_meal_id, seq_recipe_id;

CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  START WITH 1001
  NO MAXVALUE;

CREATE TABLE image (
	image_id serial PRIMARY KEY,
	image_path varchar(200)
);

CREATE TABLE users (
	user_id int NOT NULL DEFAULT nextval('seq_user_id'),
	username varchar(50) NOT NULL,
	password_hash varchar(200) NOT NULL,
	role varchar(50) NOT NULL,
	CONSTRAINT PK_users PRIMARY KEY (user_id),
	CONSTRAINT UQ_username UNIQUE (username)
);

CREATE TABLE user_attributes (
    user_id int REFERENCES users(user_id),
    email varchar(100) NOT NULL,
    display_name varchar(200),
    nurture_state int NOT NULL
);

alter table user_attributes add column is_public boolean;

alter table user_attributes add column default_public boolean;

alter table user_attributes add column image_id int references image(image_id);

alter table user_attributes add column headline varchar(100);

alter table user_attributes add column bio varchar(2000);

alter table user_attributes add column user_location varchar(100);

alter table user_attributes add column favorite_foods varchar(500);

alter table user_attributes add column favorite_cuisines varchar(500);

alter table user_attributes add column created_at timestamp default CURRENT_TIMESTAMP;

CREATE TABLE password_reset (
    uuid_value varchar(200) PRIMARY KEY,
    user_id int REFERENCES users(user_id),
    time_generated timestamp
);

CREATE SEQUENCE seq_recipe_id
  INCREMENT BY 1
  START WITH 1001
  NO MAXVALUE;

CREATE TABLE recipe (
	recipe_id int NOT NULL DEFAULT nextval('seq_recipe_id'),
	user_id int REFERENCES users(user_id),
	recipe_name varchar(50) not null,
	avg_cook_time int,
	description varchar(200),
	image_id int REFERENCES image(image_id),
	is_public boolean,
	CONSTRAINT PK_recipe PRIMARY KEY (recipe_id)
);

CREATE SEQUENCE seq_meal_id
  INCREMENT BY 1
  START WITH 1001
  NO MAXVALUE;

CREATE TABLE meal (
	meal_id int NOT NULL DEFAULT nextval('seq_meal_id'),
	user_id int REFERENCES users(user_id),
	recipe_id int REFERENCES recipe(recipe_id),
	meal_name varchar(50),
	meal_comment varchar(200),
	date_cooked date,
	date_created date,
	last_modified date,
	cook_time int,
	notes varchar(200),
	ingredients varchar(200),
	rating int check(rating <= 10 and rating >= 0),
	image_id int REFERENCES image(image_id),
	is_public boolean,
	CONSTRAINT PK_meal PRIMARY KEY (meal_id)
);

CREATE TABLE category (
	category_id serial PRIMARY KEY,
	user_id int REFERENCES users(user_id),
	category_name varchar(50)
);

CREATE TABLE tags (
	tag_id serial PRIMARY KEY,
	user_id int REFERENCES users(user_id),
	tag_name varchar(50) not null
);

CREATE TABLE step (
    step_id serial PRIMARY KEY,
	user_id int REFERENCES users(user_id),
	recipe_id int REFERENCES recipe(recipe_id),
    step_description varchar(200),
    step_order int
);

CREATE TABLE ingredient (
    ingredient_id serial PRIMARY KEY,
	user_id int REFERENCES users(user_id),
    ingredient_name varchar(200),
    quantity varchar(50),
    list_order int,
    recipe_id int REFERENCES recipe(recipe_id),
	meal_id int REFERENCES meal(meal_id)
);

CREATE TABLE recipe_category (
	recipe_id int REFERENCES recipe(recipe_id),
	category_id int REFERENCES category(category_id),
	CONSTRAINT PK_recipe_category PRIMARY KEY (recipe_id, category_id)
);

CREATE TABLE tags_meal (
	tag_id int REFERENCES tags(tag_id),
	meal_id int REFERENCES meal(meal_id),
	CONSTRAINT PK_tags_meal PRIMARY KEY (tag_id, meal_id)
);

CREATE TABLE tag_type (
    tag_type_id serial PRIMARY KEY,
    -- so on so forth
);

INSERT INTO tag_type (tag_type_name, tag_category) VALUES

-- Proteins
('Proteins', 'Proteins'), ('Beef', 'Proteins'), ('Pork', 'Proteins'), ('Chicken', 'Proteins'), ('Turkey', 'Proteins'), ('Lamb', 'Proteins'), ('Fish', 'Proteins'), ('Shellfish', 'Proteins'), ('Tofu', 'Proteins'), ('Tempeh', 'Proteins'), ('Seitan', 'Proteins'), ('Eggs', 'Proteins'), ('Game Meat', 'Proteins'), ('Plant-Based Protein', 'Proteins'),

-- Vegetables
('Vegetables', 'Vegetables'), ('Leafy Greens', 'Vegetables'), ('Root Vegetables', 'Vegetables'), ('Cruciferous', 'Vegetables'), ('Nightshades', 'Vegetables'), ('Squash', 'Vegetables'), ('Alliums', 'Vegetables'), ('Mushrooms', 'Vegetables'), ('Sea Vegetables', 'Vegetables'),

-- Fruits
('Fruits', 'Fruits'), ('Citrus', 'Fruits'), ('Berries', 'Fruits'), ('Stone Fruits', 'Fruits'), ('Tropical Fruits', 'Fruits'), ('Pome Fruits', 'Fruits'), ('Melons', 'Fruits'), ('Dried Fruits', 'Fruits'),

-- Grains & Starches
('Grains & Starches', 'Grains & Starches'), ('Rice', 'Grains & Starches'), ('Pasta & Noodles', 'Grains & Starches'), ('Bread & Baked Goods', 'Grains & Starches'), ('Quinoa', 'Grains & Starches'), ('Barley', 'Grains & Starches'), ('Oats', 'Grains & Starches'), ('Corn', 'Grains & Starches'), ('Potatoes & Sweet Potatoes', 'Grains & Starches'), ('Legumes', 'Grains & Starches'),

-- Dairy & Alternatives
('Dairy & Alternatives', 'Dairy & Alternatives'), ('Milk', 'Dairy & Alternatives'), ('Cheese', 'Dairy & Alternatives'), ('Yogurt', 'Dairy & Alternatives'), ('Cream & Butter', 'Dairy & Alternatives'), ('Plant-Based Milks', 'Dairy & Alternatives'),

-- Fats & Oils
('Fats & Oils', 'Fats & Oils'), ('Animal Fats', 'Fats & Oils'), ('Vegetable Oils', 'Fats & Oils'), ('Nut & Seed Oils', 'Fats & Oils'),

-- Sweeteners
('Sweeteners', 'Sweeteners'), ('Sugar', 'Sweeteners'), ('Honey', 'Sweeteners'), ('Maple Syrup', 'Sweeteners'), ('Artificial & Natural Sweeteners', 'Sweeteners'),

-- Spices & Seasonings
('Spices & Seasonings', 'Spices & Seasonings'), ('Herbs', 'Spices & Seasonings'), ('Spices', 'Spices & Seasonings'), ('Salt & Pepper', 'Spices & Seasonings'), ('Sauces & Condiments', 'Spices & Seasonings'),

-- Beverages
('Beverages', 'Beverages'), ('Coffee & Tea', 'Beverages'), ('Alcohol', 'Beverages'), ('Juices & Smoothies', 'Beverages'),

-- Cookware
('Cookware', 'Cookware'), ('Skillet', 'Cookware'), ('Saucepan', 'Cookware'), ('Dutch Oven', 'Cookware'), ('Baking Sheet', 'Cookware'), ('Mixing Bowl', 'Cookware'), ('Grill Pan', 'Cookware'), ('Casserole Dish', 'Cookware'),

-- Appliances
('Appliances', 'Appliances'), ('Oven', 'Appliances'), ('Microwave', 'Appliances'), ('Blender', 'Appliances'), ('Food Processor', 'Appliances'), ('Air Fryer', 'Appliances'), ('Slow Cooker', 'Appliances'), ('Instant Pot', 'Appliances'),

-- Preparation Technique
('Preparation Technique', 'Preparation Technique'), ('Chopping', 'Preparation Technique'), ('Sautéing', 'Preparation Technique'), ('Grilling', 'Preparation Technique'), ('Baking', 'Preparation Technique'), ('Roasting', 'Preparation Technique'), ('Boiling', 'Preparation Technique'), ('Steaming', 'Preparation Technique');


CREATE TABLE meal_image (
	meal_id int REFERENCES meal(meal_id),
	image_id int REFERENCES image(image_id),
	CONSTRAINT PK_meal_image PRIMARY KEY (meal_id, image_id)
);

CREATE TABLE recipe_image (
	recipe_id int REFERENCES recipe(recipe_id),
	image_id int REFERENCES image(image_id),
	CONSTRAINT PK_recipe_image PRIMARY KEY (recipe_id, image_id)
);


COMMIT;