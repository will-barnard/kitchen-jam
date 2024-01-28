START TRANSACTION;


CREATE TABLE recipe (
	recipe_id serial primary key,
	recipe_name varchar(50) not null,
	avg_cook_time int,
	notes varchar(200)

);

CREATE TABLE meal (
	meal_id serial PRIMARY KEY,
	recipe_id int REFERENCES recipe(recipe_id),
	date_created date,
	cook_time int,
	notes varchar(200),
	ingredients_used varchar(200),
	rating int check(rating <= 10 and rating > 0)
S
);

CREATE TABLE category (
	category_id serial PRIMARY KEY,
	category_name varchar(50)

);

CREATE TABLE recipe_category (
	recipe_id int REFERENCES recipe(recipe_id),
	category_id int REFERENCES category(category_id),
	CONSTRAINT PK_recipe_category PRIMARY KEY (recipe_id, category_id)
);

