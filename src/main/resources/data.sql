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

INSERT INTO topics (title, content, author, date)
VALUES ('Getting Started with Fitness', 'Hey everyone, I am new to fitness. Can anyone guide me on how to start?',
        'John Doe', '2024-06-04 10:00:00'),
       ('Nutrition Tips', 'What are some good nutrition tips for gaining muscle?', 'Jane Smith', '2024-06-04 11:00:00'),
       ('Best Cardio Exercises', 'I want to improve my cardiovascular health. What exercises would you recommend?',
        'Mike Johnson', '2024-06-04 12:00:00'),
       ('Home Workouts', 'Can anyone suggest some effective home workouts for losing weight?', 'Emily Davis',
        '2024-06-04 13:00:00'),
       ('Strength Training', 'What are the best exercises for building strength?', 'Chris Brown',
        '2024-06-04 14:00:00');

INSERT INTO coaches (description, email, img_url, rating, username)
VALUES ('Random Desc', 'didaka@example.com', '/images/profile-avatar.jpg', 5.8, 'DidoNikolov');

INSERT INTO clients (email, img_url, username, coach_id)
VALUES ('user@example.com', '/images/profile-avatar.jpg', 'User', 1);

--
INSERT INTO programs (img_url, name, coach_id)
VALUES ('/images/beginner-program.jpg', 'Beginner', 1),
       ('/images/intermediate-program.jpg', 'Intermediate', 1),
       ('/images/advanced-program.jpg', 'Advanced', 1);

INSERT INTO images (title, url)
VALUES ('Grilled Chicken Salad', 'https://res.cloudinary.com/your-cloud-name/image/upload/grilled_chicken_salad.jpg'),
       ('Spaghetti Bolognese', 'https://res.cloudinary.com/your-cloud-name/image/upload/spaghetti_bolognese.jpg'),
       ('Grilled Salmon with Steamed Vegetables',
        'https://res.cloudinary.com/your-cloud-name/image/upload/grilled_salmon.jpg'),
       ('Vegetable Stir-Fry', 'https://res.cloudinary.com/your-cloud-name/image/upload/vegetable_stir_fry.jpg'),
       ('Margherita Pizza', 'https://res.cloudinary.com/your-cloud-name/image/upload/margherita_pizza.jpg');


INSERT INTO meals (calories, description, name, type, author_id, image_id, protein, carbohydrates, fats)
VALUES (350,
        'Fresh grilled chicken breast served on a bed of mixed greens with cherry tomatoes, cucumbers, and a light vinaigrette dressing.',
        'Grilled Chicken Salad', 'Salad', 1, 1, 30, 10, 15),
       (450,
        'Classic Italian dish featuring al dente spaghetti noodles topped with a rich and savory Bolognese sauce made with ground beef, tomatoes, onions, and garlic.',
        'Spaghetti Bolognese', 'Pasta', 1, 2, 20, 60, 15),
       (300,
        'Grilled salmon fillet seasoned with herbs and served with a side of steamed vegetables, including carrots, broccoli, and snap peas.',
        'Grilled Salmon with Steamed Vegetables', 'Seafood', 1, 3, 25, 10, 12),
       (250,
        'Fresh assortment of colorful vegetables stir-fried to perfection in a light soy sauce, served over a bed of fluffy rice.',
        'Vegetable Stir-Fry', 'Vegetarian', 1, 4, 5, 35, 10),
       (400,
        'Traditional Italian pizza topped with ripe tomatoes, fresh mozzarella cheese, basil leaves, and a drizzle of olive oil on a thin crust.',
        'Margherita Pizza', 'Pizza', 1, 5, 15, 50, 18);

INSERT INTO exercises (name, muscle_group, video_url)
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



INSERT INTO workouts (date_completed, description, img_url, level, likes, name, coach_id)
VALUES ('', 'Full Body Workout: Squats, Deadlifts, Bench Press, Pull-ups, and Planks',
        'https://example.com/full_body_workout.jpg', 'Intermediate', 25, 'Full Body Blast', 1),
       ('', 'Cardio HIIT: Jumping Jacks, Burpees, High Knees, Mountain Climbers, and Jump Rope',
        'https://example.com/cardio_hiit.jpg', 'Advanced', 15, 'HIIT Cardio Burn', 1),
       ('', 'Yoga Flow: Sun Salutations, Warrior Poses, Downward Dog, and Child’s Pose',
        'https://example.com/yoga_flow.jpg', 'Beginner', 30, 'Morning Yoga Flow', 1);


INSERT INTO workout_exercises (reps, sets, name, muscle_group, workout_id, is_completed)
VALUES (8, 3, 'Squats', 'Legs', 1, 0),
       (12, 3, 'Bench Press', 'Chest, Shoulders & Triceps', 1, 0),
       (10, 3, 'Deadlifts', 'Legs & Back', 1, 0),
       (12, 4, 'Pull-ups', 'Back & Biceps', 2, 0),
       (15, 3, 'Pull-ups', 'Back & Biceps', 2, 0),
       (8, 5, 'Push-ups', 'Chest, Triceps & Shoulders', 2, 0),
       (10, 4, 'Leg Press', 'Legs', 3, 0),
       (12, 3, 'Tricep Dips', 'Triceps', 3, 0),
       (15, 4, 'Bent Over Rows', 'Back', 3, 0);


INSERT INTO weeks (id, number, program_id)
VALUES
    (1, 1, 1),
    (2, 2, 1),
    (3, 3, 1),
    (4, 4, 1),
    (5, 1, 2),
    (6, 2, 2),
    (7, 3, 2),
    (8, 4, 2),
    (9, 1, 3),
    (10, 2, 3),
    (11, 3, 3),
    (12, 4, 3);

INSERT INTO days (id, week_id, name)
VALUES
    -- Week 1, Program 1
    (1, 1, 'MONDAY'),
    (2, 1, 'TUESDAY'),
    (3, 1, 'WEDNESDAY'),
    (4, 1, 'THURSDAY'),
    (5, 1, 'FRIDAY'),
    (6, 1, 'SATURDAY'),
    (7, 1, 'SUNDAY'),

    -- Week 2, Program 1
    (8, 2, 'MONDAY'),
    (9, 2, 'TUESDAY'),
    (10, 2, 'WEDNESDAY'),
    (11, 2, 'THURSDAY'),
    (12, 2, 'FRIDAY'),
    (13, 2, 'SATURDAY'),
    (14, 2, 'SUNDAY'),

    -- Week 3, Program 1
    (15, 3, 'MONDAY'),
    (16, 3, 'TUESDAY'),
    (17, 3, 'WEDNESDAY'),
    (18, 3, 'THURSDAY'),
    (19, 3, 'FRIDAY'),
    (20, 3, 'SATURDAY'),
    (21, 3, 'SUNDAY'),

    -- Week 1, Program 2
    (22, 5, 'MONDAY'),
    (23, 5, 'TUESDAY'),
    (24, 5, 'WEDNESDAY'),
    (25, 5, 'THURSDAY'),
    (26, 5, 'FRIDAY'),
    (27, 5, 'SATURDAY'),
    (28, 5, 'SUNDAY'),

    -- Week 2, Program 2
    (29, 6, 'MONDAY'),
    (30, 6, 'TUESDAY'),
    (31, 6, 'WEDNESDAY'),
    (32, 6, 'THURSDAY'),
    (33, 6, 'FRIDAY'),
    (34, 6, 'SATURDAY'),
    (35, 6, 'SUNDAY'),

    -- Week 3, Program 2
    (36, 7, 'MONDAY'),
    (37, 7, 'TUESDAY'),
    (38, 7, 'WEDNESDAY'),
    (39, 7, 'THURSDAY'),
    (40, 7, 'FRIDAY'),
    (41, 7, 'SATURDAY'),
    (42, 7, 'SUNDAY'),

    -- Week 1, Program 3
    (43, 9, 'MONDAY'),
    (44, 9, 'TUESDAY'),
    (45, 9, 'WEDNESDAY'),
    (46, 9, 'THURSDAY'),
    (47, 9, 'FRIDAY'),
    (48, 9, 'SATURDAY'),
    (49, 9, 'SUNDAY'),

    -- Week 2, Program 3
    (50, 10, 'MONDAY'),
    (51, 10, 'TUESDAY'),
    (52, 10, 'WEDNESDAY'),
    (53, 10, 'THURSDAY'),
    (54, 10, 'FRIDAY'),
    (55, 10, 'SATURDAY'),
    (56, 10, 'SUNDAY'),

    -- Week 3, Program 3
    (57, 11, 'MONDAY'),
    (58, 11, 'TUESDAY'),
    (59, 11, 'WEDNESDAY'),
    (60, 11, 'THURSDAY'),
    (61, 11, 'FRIDAY'),
    (62, 11, 'SATURDAY'),
    (63, 11, 'SUNDAY');

INSERT INTO days_workouts (week_id, workout_id, name)
VALUES
    -- Week 1, Program 1
    (1, 1, 'MONDAY'),
    (1, 2, 'TUESDAY'),
    (1, 3, 'WEDNESDAY'),
    (1, 1, 'THURSDAY'),
    (1, 2, 'FRIDAY'),
    (1, 3, 'SATURDAY'),
    (1, 1, 'SUNDAY'),

    -- Week 2, Program 1
    (2, 1, 'MONDAY'),
    (2, 2, 'TUESDAY'),
    (2, 3, 'WEDNESDAY'),
    (2, 1, 'THURSDAY'),
    (2, 2, 'FRIDAY'),
    (2, 3, 'SATURDAY'),
    (2, 1, 'SUNDAY'),

    -- Week 3, Program 1
    (3, 1, 'MONDAY'),
    (3, 2, 'TUESDAY'),
    (3, 3, 'WEDNESDAY'),
    (3, 1, 'THURSDAY'),
    (3, 2, 'FRIDAY'),
    (3, 3, 'SATURDAY'),
    (3, 1, 'SUNDAY'),

    -- Week 1, Program 2
    (5, 1, 'MONDAY'),
    (5, 2, 'TUESDAY'),
    (5, 3, 'WEDNESDAY'),
    (5, 1, 'THURSDAY'),
    (5, 2, 'FRIDAY'),
    (5, 3, 'SATURDAY'),
    (5, 1, 'SUNDAY'),

    -- Week 2, Program 2
    (6, 1, 'MONDAY'),
    (6, 2, 'TUESDAY'),
    (6, 3, 'WEDNESDAY'),
    (6, 1, 'THURSDAY'),
    (6, 2, 'FRIDAY'),
    (6, 3, 'SATURDAY'),
    (6, 1, 'SUNDAY'),

    -- Week 3, Program 2
    (7, 1, 'MONDAY'),
    (7, 2, 'TUESDAY'),
    (7, 3, 'WEDNESDAY'),
    (7, 1, 'THURSDAY'),
    (7, 2, 'FRIDAY'),
    (7, 3, 'SATURDAY'),
    (7, 1, 'SUNDAY'),

    -- Week 1, Program 3
    (9, 1, 'MONDAY'),
    (9, 2, 'TUESDAY'),
    (9, 3, 'WEDNESDAY'),
    (9, 1, 'THURSDAY'),
    (9, 2, 'FRIDAY'),
    (9, 3, 'SATURDAY'),
    (9, 1, 'SUNDAY'),

    -- Week 2, Program 3
    (10, 1, 'MONDAY'),
    (10, 2, 'TUESDAY'),
    (10, 3, 'WEDNESDAY'),
    (10, 1, 'THURSDAY'),
    (10, 2, 'FRIDAY'),
    (10, 3, 'SATURDAY'),
    (10, 1, 'SUNDAY'),

    -- Week 3, Program 3
    (11, 1, 'MONDAY'),
    (11, 2, 'TUESDAY'),
    (11, 3, 'WEDNESDAY'),
    (11, 1, 'THURSDAY'),
    (11, 2, 'FRIDAY'),
    (11, 3, 'SATURDAY'),
    (11, 1, 'SUNDAY');


INSERT INTO user_progress (exercise_completed, workout_started, workout_completed, week_completed, user_id, workout_id, exercise_id, week_id, program_id, day_name)
VALUES
    -- User 1, Program 1, Week 1
    (false, false, false, false, 1, 1, 1, 1, 1, 'MONDAY'),
    (false, false, false, false, 1, 2, 2, 1, 1, 'TUESDAY'),
    (false, false, false, false, 1, 3, 3, 1, 1, 'WEDNESDAY'),
    (false, false, false, false, 1, 4, 1, 1, 1, 'THURSDAY'),
    (false, false, false, false, 1, 5, 2, 1, 1, 'FRIDAY'),
    (false, false, false, false, 1, 6, 3, 1, 1, 'SATURDAY'),
    (false, false, false, false, 1, 7, 1, 1, 1, 'SUNDAY'),

    -- User 1, Program 1, Week 2
    (false, false, false, false, 1, 8, 1, 2, 1, 'MONDAY'),
    (false, false, false, false, 1, 9, 2, 2, 1, 'TUESDAY'),
    (false, false, false, false, 1, 10, 3, 2, 1, 'WEDNESDAY'),
    (false, false, false, false, 1, 11, 1, 2, 1, 'THURSDAY'),
    (false, false, false, false, 1, 12, 2, 2, 1, 'FRIDAY'),
    (false, false, false, false, 1, 13, 3, 2, 1, 'SATURDAY'),
    (false, false, false, false, 1, 14, 1, 2, 1, 'SUNDAY'),

    -- User 1, Program 1, Week 3
    (false, false, false, false, 1, 15, 1, 3, 1, 'MONDAY'),
    (false, false, false, false, 1, 16, 2, 3, 1, 'TUESDAY'),
    (false, false, false, false, 1, 17, 3, 3, 1, 'WEDNESDAY'),
    (false, false, false, false, 1, 18, 1, 3, 1, 'THURSDAY'),
    (false, false, false, false, 1, 19, 2, 3, 1, 'FRIDAY'),
    (false, false, false, false, 1, 20, 3, 3, 1, 'SATURDAY'),
    (false, false, false, false, 1, 21, 1, 3, 1, 'SUNDAY'),


-- User 1, Program 2, Week 2
    (false, false, false, false, 1, 29, 1, 6, 2, 'MONDAY'),
    (false, false, false, false, 1, 30, 2, 6, 2, 'TUESDAY'),
    (false, false, false, false, 1, 31, 3, 6, 2, 'WEDNESDAY'),
    (false, false, false, false, 1, 32, 1, 6, 2, 'THURSDAY'),
    (false, false, false, false, 1, 33, 2, 6, 2, 'FRIDAY'),
    (false, false, false, false, 1, 34, 3, 6, 2, 'SATURDAY'),
    (false, false, false, false, 1, 35, 1, 6, 2, 'SUNDAY'),

-- User 1, Program 2, Week 3
    (false, false, false, false, 1, 36, 1, 7, 2, 'MONDAY'),
    (false, false, false, false, 1, 37, 2, 7, 2, 'TUESDAY'),
    (false, false, false, false, 1, 38, 3, 7, 2, 'WEDNESDAY'),
    (false, false, false, false, 1, 39, 1, 7, 2, 'THURSDAY'),
    (false, false, false, false, 1, 40, 2, 7, 2, 'FRIDAY'),
    (false, false, false, false, 1, 41, 3, 7, 2, 'SATURDAY'),
    (false, false, false, false, 1, 42, 1, 7, 2, 'SUNDAY'),

    -- User 2, Program 1, Week 1
    (false, false, false, false, 2, 1, 1, 1, 1, 'MONDAY'),
    (false, false, false, false, 2, 2, 2, 1, 1, 'TUESDAY'),
    (false, false, false, false, 2, 3, 3, 1, 1, 'WEDNESDAY'),
    (false, false, false, false, 2, 4, 1, 1, 1, 'THURSDAY'),
    (false, false, false, false, 2, 5, 2, 1, 1, 'FRIDAY'),
    (false, false, false, false, 2, 6, 3, 1, 1, 'SATURDAY'),
    (false, false, false, false, 2, 7, 1, 1, 1, 'SUNDAY'),

-- User 2, Program 1, Week 2
    (false, false, false, false, 2, 8, 1, 2, 1, 'MONDAY'),
    (false, false, false, false, 2, 9, 2, 2, 1, 'TUESDAY'),
    (false, false, false, false, 2, 10, 3, 2, 1, 'WEDNESDAY'),
    (false, false, false, false, 2, 11, 1, 2, 1, 'THURSDAY'),
    (false, false, false, false, 2, 12, 2, 2, 1, 'FRIDAY'),
    (false, false, false, false, 2, 13, 3, 2, 1, 'SATURDAY'),
    (false, false, false, false, 2, 14, 1, 2, 1, 'SUNDAY'),

    -- User 2, Program 1, Week 3
    (false, false, false, false, 2, 15, 1, 3, 1, 'MONDAY'),
    (false, false, false, false, 2, 16, 2, 3, 1, 'TUESDAY'),
    (false, false, false, false, 2, 17, 3, 3, 1, 'WEDNESDAY'),
    (false, false, false, false, 2, 18, 1, 3, 1, 'THURSDAY'),
    (false, false, false, false, 2, 19, 2, 3, 1, 'FRIDAY'),
    (false, false, false, false, 2, 20, 3, 3, 1, 'SATURDAY'),
    (false, false, false, false, 2, 21, 1, 3, 1, 'SUNDAY'),

    -- User 2, Program 2, Week 1
    (false, false, false, false, 2, 22, 1, 5, 2, 'MONDAY'),
    (false, false, false, false, 2, 23, 2, 5, 2, 'TUESDAY'),
    (false, false, false, false, 2, 24, 3, 5, 2, 'WEDNESDAY'),
    (false, false, false, false, 2, 25, 1, 5, 2, 'THURSDAY'),
    (false, false, false, false, 2, 26, 2, 5, 2, 'FRIDAY'),
    (false, false, false, false, 2, 27, 3, 5, 2, 'SATURDAY'),
    (false, false, false, false, 2, 28, 1, 5, 2, 'SUNDAY'),

    -- User 2, Program 2, Week 2
    (false, false, false, false, 2, 29, 1, 6, 2, 'MONDAY'),
    (false, false, false, false, 2, 30, 2, 6, 2, 'TUESDAY'),
    (false, false, false, false, 2, 31, 3, 6, 2, 'WEDNESDAY'),
    (false, false, false, false, 2, 32, 1, 6, 2, 'THURSDAY'),
    (false, false, false, false, 2, 33, 2, 6, 2, 'FRIDAY'),
    (false, false, false, false, 2, 34, 3, 6, 2, 'SATURDAY'),
    (false, false, false, false, 2, 35, 1, 6, 2, 'SUNDAY'),

    -- User 2, Program 2, Week 3
    (false, false, false, false, 2, 36, 1, 7, 2, 'MONDAY'),
    (false, false, false, false, 2, 37, 2, 7, 2, 'TUESDAY'),
    (false, false, false, false, 2, 38, 3, 7, 2, 'WEDNESDAY'),
    (false, false, false, false, 2, 39, 1, 7, 2, 'THURSDAY'),
    (false, false, false, false, 2, 40, 2, 7, 2, 'FRIDAY'),
    (false, false, false, false, 2, 41, 3, 7, 2, 'SATURDAY'),
    (false, false, false, false, 2, 42, 1, 7, 2, 'SUNDAY'),

    -- User 3, Program 1, Week 1
    (false, false, false, false, 3, 1, 1, 1, 1, 'MONDAY'),
    (false, false, false, false, 3, 2, 2, 1, 1, 'TUESDAY'),
    (false, false, false, false, 3, 3, 3, 1, 1, 'WEDNESDAY'),
    (false, false, false, false, 3, 4, 1, 1, 1, 'THURSDAY'),
    (false, false, false, false, 3, 5, 2, 1, 1, 'FRIDAY'),
    (false, false, false, false, 3, 6, 3, 1, 1, 'SATURDAY'),
    (false, false, false, false, 3, 7, 1, 1, 1, 'SUNDAY'),

    -- User 3, Program 1, Week 2
    (false, false, false, false, 3, 8, 1, 2, 1, 'MONDAY'),
    (false, false, false, false, 3, 9, 2, 2, 1, 'TUESDAY'),
    (false, false, false, false, 3, 10, 3, 2, 1, 'WEDNESDAY'),
    (false, false, false, false, 3, 11, 1, 2, 1, 'THURSDAY'),
    (false, false, false, false, 3, 12, 2, 2, 1, 'FRIDAY'),
    (false, false, false, false, 3, 13, 3, 2, 1, 'SATURDAY'),
    (false, false, false, false, 3, 14, 1, 2, 1, 'SUNDAY'),

    -- User 3, Program 1, Week 3
    (false, false, false, false, 3, 15, 1, 3, 1, 'MONDAY'),
    (false, false, false, false, 3, 16, 2, 3, 1, 'TUESDAY'),
    (false, false, false, false, 3, 17, 3, 3, 1, 'WEDNESDAY'),
    (false, false, false, false, 3, 18, 1, 3, 1, 'THURSDAY'),
    (false, false, false, false, 3, 19, 2, 3, 1, 'FRIDAY'),
    (false, false, false, false, 3, 20, 3, 3, 1, 'SATURDAY'),
    (false, false, false, false, 3, 21, 1, 3, 1, 'SUNDAY'),

    -- User 3, Program 2, Week 1
    (false, false, false, false, 3, 22, 1, 5, 2, 'MONDAY'),
    (false, false, false, false, 3, 23, 2, 5, 2, 'TUESDAY'),
    (false, false, false, false, 3, 24, 3, 5, 2, 'WEDNESDAY'),
    (false, false, false, false, 3, 25, 1, 5, 2, 'THURSDAY'),
    (false, false, false, false, 3, 26, 2, 5, 2, 'FRIDAY'),
    (false, false, false, false, 3, 27, 3, 5, 2, 'SATURDAY'),
    (false, false, false, false, 3, 28, 1, 5, 2, 'SUNDAY'),

    -- User 3, Program 2, Week 2
    (false, false, false, false, 3, 29, 1, 6, 2, 'MONDAY'),
    (false, false, false, false, 3, 30, 2, 6, 2, 'TUESDAY'),
    (false, false, false, false, 3, 31, 3, 6, 2, 'WEDNESDAY'),
    (false, false, false, false, 3, 32, 1, 6, 2, 'THURSDAY'),
    (false, false, false, false, 3, 33, 2, 6, 2, 'FRIDAY'),
    (false, false, false, false, 3, 34, 3, 6, 2, 'SATURDAY'),
    (false, false, false, false, 3, 35, 1, 6, 2, 'SUNDAY'),

    -- User 3, Program 2, Week 3
    (false, false, false, false, 3, 36, 1, 7, 2, 'MONDAY'),
    (false, false, false, false, 3, 37, 2, 7, 2, 'TUESDAY'),
    (false, false, false, false, 3, 38, 3, 7, 2, 'WEDNESDAY'),
    (false, false, false, false, 3, 39, 1, 7, 2, 'THURSDAY'),
    (false, false, false, false, 3, 40, 2, 7, 2, 'FRIDAY'),
    (false, false, false, false, 3, 41, 3, 7, 2, 'SATURDAY'),
    (false, false, false, false, 3, 42, 1, 7, 2, 'SUNDAY');


















