BEGIN TRANSACTION;

DROP TABLE IF EXISTS tags_meal, recipe_category, meal_image, recipe_image, users, tags, recipe, image, meal, category;

CREATE TABLE users (
	user_id serial PRIMARY KEY,
	username varchar(50) NOT NULL,
	password_hash varchar(200) NOT NULL,
	CONSTRAINT UQ_username UNIQUE (username)
);

CREATE TABLE image (
	image_id serial PRIMARY KEY,
	image_path varchar(200)
);

CREATE TABLE recipe (
	recipe_id serial primary key,
	user_id int REFERENCES users(user_id),
	recipe_name varchar(50) not null,
	avg_cook_time int,
	description varchar(200),
	image_id int REFERENCES image(image_id)
);

CREATE TABLE meal (
	meal_id serial PRIMARY KEY,
	user_id int REFERENCES users(user_id),
	recipe_id int REFERENCES recipe(recipe_id),
	meal_name varchar(50),
	meal_comment varchar(200),
	date_created date,
	cook_time int,
	notes varchar(200),
	ingredients varchar(200),
	rating int check(rating <= 10 and rating > 0),
	image_id int REFERENCES image(image_id)
);

CREATE TABLE category (
	category_id serial PRIMARY KEY,
	category_name varchar(50)
);

CREATE TABLE tags (
	tag_id serial PRIMARY KEY,
	tag_name varchar(50) not null
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