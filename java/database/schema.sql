BEGIN TRANSACTION;

DROP TABLE IF EXISTS tags_meal, recipe_category, meal_image, recipe_image, users, tags, recipe, image, meal, category;

CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  START WITH 1001
  NO MAXVALUE;

CREATE TABLE users (
	user_id int NOT NULL DEFAULT nextval('seq_user_id'),
	username varchar(50) NOT NULL,
	password_hash varchar(200) NOT NULL,
	CONSTRAINT PK_users PRIMARY KEY (user_id),
	CONSTRAINT UQ_username UNIQUE (username)
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
	date_created date,
	cook_time int,
	notes varchar(200),
	ingredients varchar(200),
	rating int check(rating <= 10 and rating > 0),
	image_id int REFERENCES image(image_id),
	is_public boolean,
	CONSTRAINT PK_meal PRIMARY KEY (meal_id)
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

CREATE TABLE category (
	category_id serial PRIMARY KEY,
	category_name varchar(50)
);

CREATE TABLE tags (
	tag_id serial PRIMARY KEY,
	tag_name varchar(50) not null
);

CREATE TABLE image (
	image_id serial PRIMARY KEY,
	image_path varchar(200)
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