INSERT INTO users (id, email, username, password, activated, title, img_url)
VALUES (1, 'admin@example.com', 'Admin',
        '95c1933b8ffe84f085f2839899d1673260be58dbd9c2c787ac35515805502c996417596dae9a92880aaa50a046fc7151', 1,
        'COACH', '/images/profile-avatar.jpg'),
       (2, 'user@example.com', 'User',
        '95c1933b8ffe84f085f2839899d1673260be58dbd9c2c787ac35515805502c996417596dae9a92880aaa50a046fc7151',
        1, 'CLIENT', '/images/profile-avatar.jpg'),
       (3, 'didaka@example.com', 'DidoNikolov',
        '95c1933b8ffe84f085f2839899d1673260be58dbd9c2c787ac35515805502c996417596dae9a92880aaa50a046fc7151',
        1, 'COACH', '/images/profile-avatar.jpg');

INSERT INTO roles (`id`, `role`)
VALUES (1, 'ADMIN'),
       (2, 'COACH'),
       (3, 'CLIENT');

INSERT INTO users_roles(`user_id`, `role_id`)
VALUES (1, 1),
       (2, 3),
       (3, 2);

-- INSERT INTO workouts(`id`, `description`, `img_url`, `level`, `name`, `likes`, `author_id`, `has_started`,
--                      `is_completed`)
-- VALUES (1, 'Start your fitness journey with our beginner workouts.', '/images/beginner.jpg', 'BEGINNER', 'Full Body', 0,
--         2, false, false),
--        (2, 'Take your fitness to the next level with our intermediate workouts.', '/images/intermediate.jpg',
--         'INTERMEDIATE', 'Push', 0, 1, false, false),
--        (3, 'Challenge yourself with our advanced workout programs.', '/images/advanced.jpg', 'ADVANCED', 'Pull', 0, 1,
--         false, false),
--        (4, 'Start your fitness journey with our beginner workouts.', '/images/beginner.jpg', 'BEGINNER', 'LEGS', 0, 1,
--         false, false);


-- INSERT INTO workouts_exercises(`workout_id`, `exercise_id`)
-- VALUES
--     (1, 1),
--     (1, 2),
--     (1, 3),
--     (2, 1),
--     (2, 2),
--     (2, 5),
--     (3, 10),
--     (3, 9),
--     (3, 5),
--     (3, 10),
--     (3, 2),
--     (4, 3),
--     (4, 1),
--     (4, 4);

-- INSERT INTO workout_exercises (reps, sets, exercise_id, workout_id, is_completed)
-- VALUES
--     (12, 3, 1, 1, 0),
--     (10, 4, 2, 1, 0),
--     (15, 3, 3, 2, 0),
--     (8, 5, 1, 2, 0),
--     (10, 3, 2, 3, 0),
--     (12, 4, 3, 3, 0),
--     (12, 4, 3, 3, 0);


INSERT INTO coaches (description, email, img_url, rating, username)
VALUES ('Random Desc', 'didaka@example.com', '/images/profile-avatar.jpg', 5.8, 'DidoNikolov');

INSERT INTO clients (email, height, img_url, target_goals, username, weight, coach_id)
VALUES ('user@example.com', 170, '/images/profile-avatar.jpg', null, 'User', 70, 1);

--
INSERT INTO programs (img_url, name, coach_id)
VALUES ('/images/beginner-program.jpg', 'Beginner', 1),
       ('/images/intermediate-program.jpg', 'Intermediate', 1),
       ('/images/advanced-program.jpg', 'Advanced', 1);

-- Inserting image URLs into the images table
INSERT INTO images (title, url)
VALUES
    ('Grilled Chicken Salad', 'https://res.cloudinary.com/your-cloud-name/image/upload/grilled_chicken_salad.jpg'),
    ('Spaghetti Bolognese', 'https://res.cloudinary.com/your-cloud-name/image/upload/spaghetti_bolognese.jpg'),
    ('Grilled Salmon with Steamed Vegetables', 'https://res.cloudinary.com/your-cloud-name/image/upload/grilled_salmon.jpg'),
    ('Vegetable Stir-Fry', 'https://res.cloudinary.com/your-cloud-name/image/upload/vegetable_stir_fry.jpg'),
    ('Margherita Pizza', 'https://res.cloudinary.com/your-cloud-name/image/upload/margherita_pizza.jpg');


-- Inserting meals into the meals table
INSERT INTO meals (calories, description, name, type, author_id, image_id)
VALUES
    (350, 'Fresh grilled chicken breast served on a bed of mixed greens with cherry tomatoes, cucumbers, and a light vinaigrette dressing.', 'Grilled Chicken Salad', 'Salad', 1, 1),
    (450, 'Classic Italian dish featuring al dente spaghetti noodles topped with a rich and savory Bolognese sauce made with ground beef, tomatoes, onions, and garlic.', 'Spaghetti Bolognese', 'Pasta', 1, 2),
    (300, 'Grilled salmon fillet seasoned with herbs and served with a side of steamed vegetables, including carrots, broccoli, and snap peas.', 'Grilled Salmon with Steamed Vegetables', 'Seafood', 1, 3),
    (250, 'Fresh assortment of colorful vegetables stir-fried to perfection in a light soy sauce, served over a bed of fluffy rice.', 'Vegetable Stir-Fry', 'Vegetarian', 1, 4),
    (400, 'Traditional Italian pizza topped with ripe tomatoes, fresh mozzarella cheese, basil leaves, and a drizzle of olive oil on a thin crust.', 'Margherita Pizza', 'Pizza', 1,  5);


--
-- INSERT INTO programs_weeks_workouts (id, name, program_week_id, has_started, is_completed, level, description,
--                                      program_id)
-- VALUES (1, 'Push', 1, 0, 0, 'Beginner', 'This workout is designed to hit Chest, Triceps and Shoulder effectively', 1),
--        (2, 'Pull', 1, 0, 0, 'Beginner', 'This workout is designed to hit Back, Biceps and Rear Delts effectively', 1),
--        (3, 'Legs', 1, 0, 0, 'Beginner', 'This workout is designed to hit Legs effectively', 1),
--        (4, 'Upper Body', 1, 0, 0, 'Beginner', 'This workout is designed to hit Upper Body effectively', 1),
--        (5, 'Lower Body', 2, 0, 0, 'Beginner', 'This workout is designed to hit Lower Body effectively', 1),
--        (6, 'Back & Back', 2, 0, 0, 'Beginner', 'This workout is designed to hit Back and Biceps effectively', 1),
--        (7, 'Chest & Triceps', 2, 0, 0, 'Beginner', 'This workout is designed to hit Chest and Triceps effectively', 1),
--        (8, 'ABS/Calves', 2, 0, 0, 'Beginner', 'This workout is designed to hit ABS and Calves effectively', 1),
--        (9, 'Full Body', 3, 0, 0, 'Beginner', 'This workout is designed to hit your whole body effectively', 1),
--        (10, 'HIIT ', 3, 0, 0, 'Beginner', 'This workout is designed to make your heart rate go up effectively', 1),
--        (11, 'Core', 3, 0, 0, 'Beginner', 'This workout is designed to hit your Core effectively', 1),
--        (12, 'Push', 3, 0, 0, 'Beginner', 'This workout is designed to hit Chest, Triceps and Shoulder  effectively', 1),
--        (13, 'Pull', 4, 0, 0, 'Beginner', 'This workout is designed to hit Back, Biceps and Rear Delts effectively', 1),
--        (14, 'Legs', 4, 0, 0, 'Beginner', 'This workout is designed to hit Legs effectively effectively', 1),
--        (15, 'Arms', 4, 0, 0, 'Beginner', 'This workout is designed to hit your Arms effectively', 1),
--        (16, 'Shoulders', 4, 0, 0, 'Beginner', 'This workout is designed to hit your Shoulders effectively', 1);
--
-- -- INSERT INTO exercises(`id`, `name`, `video_url`, `program_workout_id`)
-- -- VALUES
-- --     (1, 'Shoulder Press','OLePvpxQEGk', 1),
-- --     (2, 'Squats', 'MLoZuAkIyZI', 1),
-- --     (3, 'Dumbbell Lateral Raise', 'JIhbYYA1Q90', 2),
-- --     (4, 'Chest Fly',  'g3T7LsEeDWQ', 2),
-- --     (5, 'Cable Row', 'G18ysBYu5Mw', 3),
-- --     (6, 'Pull-ups', 'dvG8B2OjfWk', 3),
-- --     (7, 'Dumbbell Curls', '', 4),
-- --     (8, 'RDL', '5rIqP63yWFg', 4),
-- --     (9, 'Deadlift', 'McCDaAsSeRc', 5),
-- --     (10, 'Bench Press','EdDqD4aKwxM', 5);
--
-- INSERT INTO exercises(`id`, `name`, `video_url`)
-- VALUES (1, 'Shoulder Press', 'OLePvpxQEGk'),
--        (2, 'Squats', 'MLoZuAkIyZI'),
--        (3, 'Dumbbell Lateral Raise', 'JIhbYYA1Q90'),
--        (4, 'Chest Fly', 'g3T7LsEeDWQ'),
--        (5, 'Cable Row', 'G18ysBYu5Mw'),
--        (6, 'Pull-ups', 'dvG8B2OjfWk'),
--        (7, 'Dumbbell Curls', ''),
--        (8, 'RDL', '5rIqP63yWFg'),
--        (9, 'Deadlift', 'McCDaAsSeRc'),
--        (10, 'Bench Press', 'EdDqD4aKwxM');
--
-- INSERT INTO program_workouts_exercises(id, workout_id, exercise_id, sets, reps, is_completed)
-- VALUES (1, 1, 1, 3, 12, 0),
--        (2, 1, 2, 4, 10, 0),
--        (3, 2, 3, 5, 5, 0),
--        (4, 2, 4, 2, 20, 0),
--        (5, 3, 2, 3, 12, 0),
--        (6, 3, 1, 4, 6, 0);
--
--
-- INSERT INTO certificates (name, issuing_authority, issue_date)
-- VALUES ('Certified Fitness Trainer', 'Fitness Certification Authority', '2022-01-01'),
--        ('Advanced Yoga Instructor', 'Yoga Certification Institute', '2021-05-15'),
--        ('Professional Life Coach', 'Coaching Association', '2020-10-01');

INSERT INTO exercises (name, target_muscle_group, video_url)
VALUES ('Squats', 'Legs', 'https://www.youtube.com/watch?v=U3HcD0PnczY'),
       ('Bench Press', 'Chest', 'https://www.youtube.com/watch?v=rT7DgCr-3pg'),
       ('Deadlifts', 'Back', 'https://www.youtube.com/watch?v=op9kVnSso6Q'),
       ('Pull-ups', 'Back', 'https://www.youtube.com/watch?v=JB2oyawG9KI'),
       ('Push-ups', 'Chest', 'https://www.youtube.com/watch?v=IODxDxX7oi4'),
       ('Leg Press', 'Legs', 'https://www.youtube.com/watch?v=IZxyjW7MPJQ'),
       ('Dumbbell Rows', 'Back', 'https://www.youtube.com/watch?v=HJMHwJsAnxo'),
       ('Dumbbell Shoulder Press', 'Shoulders', 'https://www.youtube.com/watch?v=eozdVDA78K0'),
       ('Lunges', 'Legs', 'https://www.youtube.com/watch?v=QOVaHwm-Q6U'),
       ('Barbell Curl', 'Biceps', 'https://www.youtube.com/watch?v=kwG2ipFRgfo'),
       ('Tricep Dips', 'Triceps', 'https://www.youtube.com/watch?v=2z8JmcrW-As'),
       ('Russian Twists', 'Core', 'https://www.youtube.com/watch?v=VcJq1tZJUI0'),
       ('Calf Raises', 'Calves', 'https://www.youtube.com/watch?v=6uK8GAXKdAc'),
       ('Plank', 'Core', 'https://www.youtube.com/watch?v=pSHjTRCQxIw'),
       ('Bent Over Rows', 'Back', 'https://www.youtube.com/watch?v=JTObkwvNlvM');



INSERT INTO workouts (date_completed, description, img_url, level, likes, name, author_id)
VALUES ('', 'Full Body Workout: Squats, Deadlifts, Bench Press, Pull-ups, and Planks',
        'https://example.com/full_body_workout.jpg',  'Intermediate', 25, 'Full Body Blast', 1),
       ('', 'Cardio HIIT: Jumping Jacks, Burpees, High Knees, Mountain Climbers, and Jump Rope',
        'https://example.com/cardio_hiit.jpg', 'Advanced', 15, 'HIIT Cardio Burn', 1),
       ('', 'Yoga Flow: Sun Salutations, Warrior Poses, Downward Dog, and Child’s Pose',
        'https://example.com/yoga_flow.jpg', 'Beginner', 30, 'Morning Yoga Flow', 1);






