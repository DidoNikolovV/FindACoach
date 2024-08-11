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








