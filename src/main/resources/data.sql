-- Additional Users (Coaches & Clients)
INSERT INTO users (id, email, username, password, activated, title, img_url)
VALUES (1, 'admin@example.com', 'Admin',
        '95c1933b8ffe84f085f2839899d1673260be58dbd9c2c787ac35515805502c996417596dae9a92880aaa50a046fc7151', 1,
        'ADMIN', '/images/profile-avatar.jpg'),
       (2, 'user@example.com', 'User',
        '95c1933b8ffe84f085f2839899d1673260be58dbd9c2c787ac35515805502c996417596dae9a92880aaa50a046fc7151',
        1, 'CLIENT', '/images/profile-avatar.jpg'),
       (3, 'didaka@example.com', 'DidoNikolov',
        '95c1933b8ffe84f085f2839899d1673260be58dbd9c2c787ac35515805502c996417596dae9a92880aaa50a046fc7151',
        1, 'COACH', '/images/profile-avatar.jpg'),
       (4, 'ivan.petrov@example.com', 'IvanPetrov',
        '95c1933b8ffe84f085f2839899d1673260be58dbd9c2c787ac35515805502c996417596dae9a92880aaa50a046fc7151', 1,
        'COACH', '/images/profile-avatar.jpg'),
       (5, 'petar.georgiev@example.com', 'PetarGeorgiev',
        '95c1933b8ffe84f085f2839899d1673260be58dbd9c2c787ac35515805502c996417596dae9a92880aaa50a046fc7151', 1,
        'COACH', '/images/profile-avatar.jpg'),
       (6, 'vladislav.dimitrov@example.com', 'VladislavDimitrov',
        '95c1933b8ffe84f085f2839899d1673260be58dbd9c2c787ac35515805502c996417596dae9a92880aaa50a046fc7151', 1,
        'CLIENT', '/images/client-vladislav.jpg'),
       (7, 'georgi.ivanov@example.com', 'GeorgiIvanov',
        '95c1933b8ffe84f085f2839899d1673260be58dbd9c2c787ac35515805502c996417596dae9a92880aaa50a046fc7151', 1,
        'CLIENT', '/images/client-georgi.jpg'),
       (8, 'vladislav.nikolov@example.com', 'VladislavNikolov',
        '95c1933b8ffe84f085f2839899d1673260be58dbd9c2c787ac35515805502c996417596dae9a92880aaa50a046fc7151', 1,
        'COACH', '/images/profile-avatar.jpg'),
       (9, 'simeon.nedelchev@example.com', 'SimeonNedelchev',
        '95c1933b8ffe84f085f2839899d1673260be58dbd9c2c787ac35515805502c996417596dae9a92880aaa50a046fc7151', 1,
        'COACH', '/images/profile-avatar.jpg');

-- Ensure you also add roles and user-role mappings accordingly.


INSERT INTO roles (`id`, `role`)
VALUES (1, 'ADMIN'),
       (2, 'COACH'),
       (3, 'CLIENT');

-- Insert roles for the new users
INSERT INTO users_roles(`user_id`, `role_id`)
VALUES (1, 1),
       (2, 3),
       (3, 2),
       (4, 2), -- IvanPetrov as Coach
       (5, 2), -- PetarGeorgiev as Coach
       (6, 3), -- VladislavDimitrov as Client
       (7, 3), -- GeorgiIvanov as Client
       (8, 2), -- VladislavNikolov as Coach
       (9, 2); -- SimeonNedelchev as Coach

INSERT INTO coaches (description, email, img_url, rating, username)
VALUES ('Strength and endurance coach', 'didaka@example.com', '/images/profile-avatar.jpg', 5.8, 'DidoNikolov'),
       ('Expert in strength training', 'ivan.petrov@example.com', '/images/coach-ivan.jpg', 4.7, 'IvanPetrov'),
       ('HIIT and cardio specialist', 'petar.georgiev@example.com', '/images/coach-petar.jpg', 4.9, 'PetarGeorgiev'),
       ('Expert in calisthenics', 'vladislav.nikolov@example.com', '/images/profile-avatar.jpg', 4.7, 'VladislavNikolov'),
       ('Expert in crossfit', 'simeon.nedelchev@example.com', '/images/profile-avatar.jpg', 4.8, 'SimeonNedelchev');

INSERT INTO clients (email, img_url, username, coach_id)
VALUES ('user@example.com', '/images/user-avatar.jpg', 'User', 1),
       ('vladislav.dimitrov@example.com', '/images/client-vladislav.jpg', 'VladislavDimitrov', 2),
       ('georgi.ivanov@example.com', '/images/client-georgi.jpg', 'GeorgiIvanov', 3);
INSERT INTO scheduled_workouts (coach_id, client_id, scheduled_date)
VALUES
-- DidoNikolov with User
(1, 1, '2024-09-15 09:00:00'),
(1, 1, '2024-09-18 15:15:00'),
(1, 1, '2024-09-23 13:20:00'),
(1, 1, '2024-10-06 17:20:00'),
(1, 1, '2024-10-16 18:30:00'),
(1, 1, '2024-10-21 10:15:00'),
(1, 1, '2024-10-25 11:30:00'),

-- IvanPetrov with VladislavDimitrov
(2, 2, '2024-09-18 10:30:00'),
(2, 2, '2024-09-27 09:45:00'),
(2, 2, '2024-10-02 12:00:00'),
(2, 2, '2024-10-11 15:30:00'),
(2, 2, '2024-10-14 10:00:00'),
(2, 2, '2024-10-23 12:30:00'),
(2, 2, '2024-10-28 16:15:00'),
-- PetarGeorgiev with GeorgiIvanov
(3, 3, '2024-09-22 10:15:00'),
(3, 3, '2024-09-25 12:10:00'),
(3, 3, '2024-10-09 10:00:00'),
(3, 3, '2024-10-17 15:25:00'),
(3, 3, '2024-10-23 10:00:00'),
(3, 3, '2024-10-27 17:30:00'),
(3, 3, '2024-11-03 10:00:00');
--
-- Inserting Programs for each coach
INSERT INTO programs (img_url, name, coach_id)
VALUES
    -- Programs for DidoNikolov (Strength and Endurance)
    ('/images/beginner-program.jpg', 'Beginner', 1),
    ('/images/intermediate-program.jpg', 'Intermediate', 1),
    ('/images/advanced-program.jpg', 'Advanced', 1),
    ('/images/endurance-program.jpg', 'Endurance Training', 1),
    ('/images/powerlifting-program.jpg', 'Powerlifting Basics', 1),
    ('/images/full-body-conditioning.jpg', 'Full Body Conditioning', 1),

    -- Programs for IvanPetrov (Strength Training)
    ('/images/strength-foundation.jpg', 'Strength Foundation', 2),
    ('/images/muscle-building.jpg', 'Muscle Building', 2),
    ('/images/weightlifting-techniques.jpg', 'Weightlifting Techniques', 2),
    ('/images/strength-advanced.jpg', 'Advanced Strength Program', 2),
    ('/images/explosive-strength.jpg', 'Explosive Strength', 2),
    ('/images/power-training.jpg', 'Power Training', 2),

    -- Programs for PetarGeorgiev (HIIT and Cardio Specialist)
    ('/images/hiit-beginners.jpg', 'HIIT for Beginners', 3),
    ('/images/hiit-advanced.jpg', 'Advanced HIIT', 3),
    ('/images/cardio-blast.jpg', 'Cardio Blast', 3),
    ('/images/fat-burning-hiit.jpg', 'Fat Burning HIIT', 3),
    ('/images/endurance-cardio.jpg', 'Endurance Cardio Program', 3),
    ('/images/speed-and-agility.jpg', 'Speed and Agility', 3);



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
       ('Bent Over Rows', 'Back', 'https://www.youtube.com/watch?v=JTObkwvNlvM'),
       ('Burpees', 'Full Body', 'https://www.youtube.com/watch?v=2w8fBtmS_V4'),
       ('Mountain Climbers', 'Core/Cardio', 'https://www.youtube.com/watch?v=nmwgirgXLYM'),
       ('Jump Rope', 'Cardio', 'https://www.youtube.com/watch?v=1kNkkESQ5R4'),
       ('High Knees', 'Cardio', 'https://www.youtube.com/watch?v=8dGJts9jFkE'),
       ('Box Jumps', 'Legs', 'https://www.youtube.com/watch?v=52cNmgfDkxk'),
       ('Kettlebell Swings', 'Full Body/Cardio', 'https://www.youtube.com/watch?v=YSjY7GeC59Y'),
       ('Shadow Boxing', 'Cardio/Full Body', 'https://www.youtube.com/watch?v=J5lJ7pIkQ0g'),
       ('Front Squats', 'Legs', 'https://www.youtube.com/watch?v=tF7ZWzNY_Xg'),
       ('Romanian Deadlifts', 'Back/Legs', 'https://www.youtube.com/watch?v=4mtr5zYr34Y'),
       ('Overhead Press', 'Shoulders', 'https://www.youtube.com/watch?v=2yjv9Wn2Wvg'),
       ('Barbell Rows', 'Back', 'https://www.youtube.com/watch?v=K6arhZK6I5E'),
       ('Leg Press Variations', 'Legs', 'https://www.youtube.com/watch?v=E4oSxzS6_nk'),
       ('Hip Thrusts', 'Glutes', 'https://www.youtube.com/watch?v=2D9z8gE9GnM'),
       ('Clean and Press', 'Full Body', 'https://www.youtube.com/watch?v=ZHp78eBrw68'),
       ('Kettlebell Snatch', 'Full Body/Cardio', 'https://www.youtube.com/watch?v=9oWlg7tv7eE'),
       ('TRX Rows', 'Back/Core', 'https://www.youtube.com/watch?v=3j8NRw1uK1E'),
       ('Medicine Ball Slams', 'Full Body/Cardio', 'https://www.youtube.com/watch?v=5kn1l76K3HE'),
       ('Tire Flips', 'Full Body', 'https://www.youtube.com/watch?v=xeYxO5mwm4M'),
       ('Sled Pushes', 'Full Body/Cardio', 'https://www.youtube.com/watch?v=fq2U2TSrN2U'),
       ('Bodyweight Squats', 'Legs/Core', 'https://www.youtube.com/watch?v=NI8G6sO9Fq8'),
       ('Barbell Squats', 'Legs', 'https://www.youtube.com/watch?v=QbOQGMwRZ54'),
       ('Dumbbell Presses', 'Shoulders', 'https://www.youtube.com/watch?v=gw6fL9-19ck'),
       ('Barbell Curls', 'Biceps', 'https://www.youtube.com/watch?v=kwG2ipFRgfo'),
       ('Tricep Extensions', 'Triceps', 'https://www.youtube.com/watch?v=wMNrgs-AB8s'),
       ('Dumbbell Flyes', 'Chest', 'https://www.youtube.com/watch?v=eozdVDA78K0'),
       ('Leg Curls', 'Hamstrings', 'https://www.youtube.com/watch?v=s0C5H1K1v68'),
       ('Lateral Raises', 'Shoulders', 'https://www.youtube.com/watch?v=3aR8y24XJ1Q'),
       ('Cable Rows', 'Back', 'https://www.youtube.com/watch?v=wK5S1_djC7k'),
       ('Incline Bench Press', 'Chest', 'https://www.youtube.com/watch?v=8YlVg6wU5sw'),
       ('Concentration Curls', 'Biceps', 'https://www.youtube.com/watch?v=3t4ph-JtsJk'),
       ('Skull Crushers', 'Triceps', 'https://www.youtube.com/watch?v=3Qd3PLysHtQ'),
       ('Snatches', 'Full Body', 'https://www.youtube.com/watch?v=ykT1I6VoQHc'),
       ('Clean and Jerks', 'Full Body', 'https://www.youtube.com/watch?v=kxC2n8s7PzA'),
       ('Overhead Squats', 'Legs/Shoulders', 'https://www.youtube.com/watch?v=INaSgVvNCP4'),
       ('Power Cleans', 'Full Body', 'https://www.youtube.com/watch?v=wW4uR9h7CEw'),
       ('Push Press', 'Shoulders', 'https://www.youtube.com/watch?v=8M7WGe3LLNc'),
       ('Trap Bar Deadlifts', 'Back/Legs', 'https://www.youtube.com/watch?v=jj02d1LeLZw'),
       ('Close-Grip Bench Press', 'Chest', 'https://www.youtube.com/watch?v=ah2DlssxirQ'),
       ('Box Squats', 'Legs', 'https://www.youtube.com/watch?v=3qG8sFS0K-Q'),
       ('Single-Leg Deadlifts', 'Legs', 'https://www.youtube.com/watch?v=Ix4EN1ndbMo'),
       ('Weighted Pull-ups', 'Back', 'https://www.youtube.com/watch?v=dR0ZXSpB_Yo'),
       ('Dips', 'Chest/Triceps', 'https://www.youtube.com/watch?v=2z8JmcrW-As'),
       ('Plyometric Push-ups', 'Chest', 'https://www.youtube.com/watch?v=IkKAtcT0zLM'),
       ('Broad Jumps', 'Legs', 'https://www.youtube.com/watch?v=F_7u4ed9N9M'),
       ('Sled Drags', 'Full Body/Cardio', 'https://www.youtube.com/watch?v=fq2U2TSrN2U'),
       ('Jump Squats', 'Legs', 'https://www.youtube.com/watch?v=1oUOa8r4oPY'),
       ('Battle Ropes', 'Full Body/Cardio', 'https://www.youtube.com/watch?v=G1tSqaL9RTY'),
       ('Heavy Medicine Ball Throws', 'Full Body', 'https://www.youtube.com/watch?v=8R8H2l4J8Ww'),
       ('Explosive Push-ups', 'Chest', 'https://www.youtube.com/watch?v=IkKAtcT0zLM'),
       ('Jumping Jacks', 'Cardio', 'https://www.youtube.com/watch?v=c4dZbs5G3D8'),
       ('Tuck Jumps', 'Full Body', 'https://www.youtube.com/watch?v=MGcV7MIBCOs'),
       ('Cone Drills', 'Agility', 'https://www.youtube.com/watch?v=mY5-D_bjOMs'),
       ('Sprints', 'Speed', 'https://www.youtube.com/watch?v=9h70eGHt0IU'),
       ('Bounding Drills', 'Speed/Legs', 'https://www.youtube.com/watch?v=ByAayG0OEDQ'),
       ('Shuttle Runs', 'Speed/Agility', 'https://www.youtube.com/watch?v=YAD2QVO5j1Q'),
       ('Lateral Jumps', 'Agility', 'https://www.youtube.com/watch?v=l4A8VRoEq8o');



INSERT INTO workouts (date_completed, description, img_url, level, likes, name, coach_id)
VALUES
    -- For DidoNikolov (Strength and Endurance)
    ('', 'High-Intensity Power: Jump Squats, Kettlebell Swings, Battle Ropes, and Broad Jumps',
     'https://example.com/high_intensity_power.jpg', 'Advanced', 20, 'High-Intensity Power', 1),
    ('', 'Endurance Conditioning: Long-Distance Running, Swimming, Cycling, and Rowing',
     'https://example.com/endurance_conditioning.jpg', 'Advanced', 22, 'Endurance Conditioning', 1),
    ('', 'Power Surge: Deadlifts, Squats, Bench Press, and Overhead Press',
     'https://example.com/power_surge.jpg', 'Intermediate', 18, 'Power Surge', 1),
    ('', 'Strength Precision: Squats, Deadlifts, Bench Press, and Pull-ups',
     'https://example.com/strength_precision.jpg', 'Intermediate', 24, 'Strength Precision', 1),

    -- For IvanPetrov (Strength Training)
    ('', 'Explosive Strength Blast: Box Jumps, Medicine Ball Slams, Battle Ropes, and Sled Pushes',
     'https://example.com/explosive_strength_blast.jpg', 'Advanced', 17, 'Explosive Strength Blast', 2),
    ('', 'Cardio HIIT Blitz: High Knees, Burpees, Jump Rope, and Mountain Climbers',
     'https://example.com/cardio_hiit_blitz.jpg', 'Advanced', 14, 'Cardio HIIT Blitz', 2),
    ('', 'Muscle Building: Bench Press, Squats, Deadlifts, and Pull-ups',
     'https://example.com/muscle_building.jpg', 'Intermediate', 21, 'Muscle Building', 2),
    ('', 'Power Training: Clean and Jerks, Snatches, Box Jumps, and Kettlebell Swings',
     'https://example.com/power_training.jpg', 'Advanced', 16, 'Power Training', 2),

    -- For PetarGeorgiev (HIIT and Cardio Specialist)
    ('', 'Fat Burning HIIT: Burpees, Jump Squats, High Knees, and Plank Jacks',
     'https://example.com/fat_burning_hiit.jpg', 'Advanced', 19, 'Fat Burning HIIT', 3),
    ('', 'Speed and Agility: Ladder Drills, Cone Drills, Sprints, and Agility Hurdles',
     'https://example.com/speed_and_agility.jpg', 'Intermediate', 27, 'Speed and Agility', 3),
    ('', 'Cardio Challenge: Running Intervals, Cycling Sprints, Rowing Intervals, and Jump Rope',
     'https://example.com/cardio_challenge.jpg', 'Advanced', 23, 'Cardio Challenge', 3),
    ('', 'Endurance Cardio Program: Long-Distance Running, Cycling, Swimming, and Rowing',
     'https://example.com/endurance_cardio.jpg', 'Advanced', 26, 'Endurance Cardio Program', 3);



INSERT INTO workout_exercises (reps, sets, name, muscle_group, workout_id, is_completed)
VALUES
    -- For High-Intensity Power
    (10, 4, 'Jump Squats', 'Legs', 1, 0),
    (15, 4, 'Kettlebell Swings', 'Full Body/Cardio', 1, 0),
    (20, 4, 'Battle Ropes', 'Full Body/Cardio', 1, 0),
    (10, 4, 'Broad Jumps', 'Legs', 1, 0),

    -- For Endurance Conditioning
    (60, 1, 'Long-Distance Running', 'Legs', 2, 0),
    (45, 1, 'Swimming', 'Full Body', 2, 0),
    (60, 1, 'Cycling', 'Legs', 2, 0),
    (60, 1, 'Rowing', 'Full Body', 2, 0),

    -- For Power Surge
    (8, 4, 'Deadlifts', 'Back', 3, 0),
    (8, 4, 'Squats', 'Legs', 3, 0),
    (10, 4, 'Bench Press', 'Chest', 3, 0),
    (12, 4, 'Overhead Press', 'Shoulders', 3, 0),

    -- For Strength Precision
    (10, 4, 'Squats', 'Legs', 4, 0),
    (10, 4, 'Deadlifts', 'Back', 4, 0),
    (12, 4, 'Bench Press', 'Chest', 4, 0),
    (12, 4, 'Pull-ups', 'Back', 4, 0),

    -- For Explosive Strength Blast
    (10, 4, 'Box Jumps', 'Legs', 5, 0),
    (15, 4, 'Medicine Ball Slams', 'Full Body/Cardio', 5, 0),
    (12, 4, 'Battle Ropes', 'Full Body/Cardio', 5, 0),
    (10, 4, 'Sled Pushes', 'Full Body/Cardio', 5, 0),

    -- For Cardio HIIT Blitz
    (30, 4, 'High Knees', 'Cardio', 6, 0),
    (20, 4, 'Burpees', 'Full Body', 6, 0),
    (30, 4, 'Jump Rope', 'Cardio', 6, 0),
    (15, 4, 'Mountain Climbers', 'Core/Cardio', 6, 0),

    -- For Muscle Building
    (10, 4, 'Bench Press', 'Chest', 7, 0),
    (10, 4, 'Squats', 'Legs', 7, 0),
    (12, 4, 'Deadlifts', 'Back', 7, 0),
    (12, 4, 'Pull-ups', 'Back', 7, 0),

    -- For Power Training
    (8, 4, 'Clean and Jerks', 'Full Body', 8, 0),
    (10, 4, 'Snatches', 'Full Body', 8, 0),
    (10, 4, 'Box Jumps', 'Legs', 8, 0),
    (12, 4, 'Kettlebell Swings', 'Full Body/Cardio', 8, 0),

    -- For Fat Burning HIIT
    (15, 4, 'Burpees', 'Full Body', 9, 0),
    (30, 4, 'High Knees', 'Cardio', 9, 0),
    (20, 4, 'Jump Squats', 'Legs', 9, 0),
    (15, 4, 'Plank Jacks', 'Core/Cardio', 9, 0),

    -- For Speed and Agility
    (20, 4, 'Ladder Drills', 'Agility', 10, 0),
    (20, 4, 'Cone Drills', 'Agility', 10, 0),
    (30, 4, 'Sprints', 'Speed', 10, 0),
    (20, 4, 'Agility Hurdles', 'Agility', 10, 0),

    -- For Cardio Challenge
    (20, 4, 'Running Intervals', 'Legs', 11, 0),
    (15, 4, 'Cycling Sprints', 'Legs', 11, 0),
    (20, 4, 'Rowing Intervals', 'Full Body', 11, 0),
    (20, 4, 'Jump Rope', 'Cardio', 11, 0),

    -- For Endurance Cardio Program
    (60, 1, 'Long-Distance Running', 'Legs', 12, 0),
    (45, 1, 'Swimming', 'Full Body', 12, 0),
    (60, 1, 'Cycling', 'Legs', 12, 0),
    (60, 1, 'Rowing', 'Full Body', 12, 0);

-- Insert weeks for each program with 3 weeks per program
INSERT INTO weeks (id, number, program_id)
VALUES
    -- Program 1
    (1, 1, 1),
    (2, 2, 1),
    (3, 3, 1),
    -- Program 2
    (4, 1, 2),
    (5, 2, 2),
    (6, 3, 2),
    -- Program 3
    (7, 1, 3),
    (8, 2, 3),
    (9, 3, 3),
    -- Program 7
    (10, 1, 7),
    (11, 2, 7),
    (12, 3, 7),
    -- Program 8
    (13, 1, 8),
    (14, 2, 8),
    (15, 3, 8),
    -- Program 9
    (16, 1, 9),
    (17, 2, 9),
    (18, 3, 9),
    -- Program 13
    (19, 1, 13),
    (20, 2, 13),
    (21, 3, 13),
    -- Program 14
    (22, 1, 14),
    (23, 2, 14),
    (24, 3, 14),
    -- Program 15
    (25, 1, 15),
    (26, 2, 15),
    (27, 3, 15);



-- Week 1, Program 1
-- Week Days for Programs 1 to 3
-- Program 1
INSERT INTO days (id, week_id, name)
VALUES
    -- Week 1
    (1, 1, 'MONDAY'),
    (2, 1, 'TUESDAY'),
    (3, 1, 'WEDNESDAY'),
    (4, 1, 'THURSDAY'),
    (5, 1, 'FRIDAY'),
    (6, 1, 'SATURDAY'),
    (7, 1, 'SUNDAY'),
    -- Week 2
    (8, 2, 'MONDAY'),
    (9, 2, 'TUESDAY'),
    (10, 2, 'WEDNESDAY'),
    (11, 2, 'THURSDAY'),
    (12, 2, 'FRIDAY'),
    (13, 2, 'SATURDAY'),
    (14, 2, 'SUNDAY'),
    -- Week 3
    (15, 3, 'MONDAY'),
    (16, 3, 'TUESDAY'),
    (17, 3, 'WEDNESDAY'),
    (18, 3, 'THURSDAY'),
    (19, 3, 'FRIDAY'),
    (20, 3, 'SATURDAY'),
    (21, 3, 'SUNDAY'),

-- Program 2
    -- Week 1
    (22, 4, 'MONDAY'),
    (23, 4, 'TUESDAY'),
    (24, 4, 'WEDNESDAY'),
    (25, 4, 'THURSDAY'),
    (26, 4, 'FRIDAY'),
    (27, 4, 'SATURDAY'),
    (28, 4, 'SUNDAY'),
    -- Week 2
    (29, 5, 'MONDAY'),
    (30, 5, 'TUESDAY'),
    (31, 5, 'WEDNESDAY'),
    (32, 5, 'THURSDAY'),
    (33, 5, 'FRIDAY'),
    (34, 5, 'SATURDAY'),
    (35, 5, 'SUNDAY'),
    -- Week 3
    (36, 6, 'MONDAY'),
    (37, 6, 'TUESDAY'),
    (38, 6, 'WEDNESDAY'),
    (39, 6, 'THURSDAY'),
    (40, 6, 'FRIDAY'),
    (41, 6, 'SATURDAY'),
    (42, 6, 'SUNDAY'),

-- Program 3
    -- Week 1
    (43, 7, 'MONDAY'),
    (44, 7, 'TUESDAY'),
    (45, 7, 'WEDNESDAY'),
    (46, 7, 'THURSDAY'),
    (47, 7, 'FRIDAY'),
    (48, 7, 'SATURDAY'),
    (49, 7, 'SUNDAY'),
    -- Week 2
    (50, 8, 'MONDAY'),
    (51, 8, 'TUESDAY'),
    (52, 8, 'WEDNESDAY'),
    (53, 8, 'THURSDAY'),
    (54, 8, 'FRIDAY'),
    (55, 8, 'SATURDAY'),
    (56, 8, 'SUNDAY'),
    -- Week 3
    (57, 9, 'MONDAY'),
    (58, 9, 'TUESDAY'),
    (59, 9, 'WEDNESDAY'),
    (60, 9, 'THURSDAY'),
    (61, 9, 'FRIDAY'),
    (62, 9, 'SATURDAY'),
    (63, 9, 'SUNDAY');

-- Repeat for Programs 4 to 6 if needed


-- Week 1, Program 1
INSERT INTO days_workouts (week_id, workout_id, name)
VALUES
    -- Week 1, Program 1
    (1, 1, 'MONDAY'),
    (1, 2, 'TUESDAY'),
    (1, 3, 'WEDNESDAY'),
    (1, 4, 'THURSDAY'),
    (1, 5, 'FRIDAY'),
    (1, 6, 'SATURDAY'),
    (1, 7, 'SUNDAY'),

    -- Week 2, Program 1
    (2, 8, 'MONDAY'),
    (2, 9, 'TUESDAY'),
    (2, 10, 'WEDNESDAY'),
    (2, 11, 'THURSDAY'),
    (2, 12, 'FRIDAY'),
    (2, 1, 'SATURDAY'),
    (2, 2, 'SUNDAY'),

    -- Week 3, Program 1
    (3, 3, 'MONDAY'),
    (3, 4, 'TUESDAY'),
    (3, 5, 'WEDNESDAY'),
    (3, 6, 'THURSDAY'),
    (3, 7, 'FRIDAY'),
    (3, 8, 'SATURDAY'),
    (3, 9, 'SUNDAY'),

    -- Week 1, Program 2
    (4, 10, 'MONDAY'),
    (4, 11, 'TUESDAY'),
    (4, 12, 'WEDNESDAY'),
    (4, 1, 'THURSDAY'),
    (4, 2, 'FRIDAY'),
    (4, 3, 'SATURDAY'),
    (4, 4, 'SUNDAY'),

    -- Week 2, Program 2
    (5, 5, 'MONDAY'),
    (5, 6, 'TUESDAY'),
    (5, 7, 'WEDNESDAY'),
    (5, 8, 'THURSDAY'),
    (5, 9, 'FRIDAY'),
    (5, 10, 'SATURDAY'),
    (5, 11, 'SUNDAY'),

    -- Week 3, Program 2
    (6, 12, 'MONDAY'),
    (6, 1, 'TUESDAY'),
    (6, 2, 'WEDNESDAY'),
    (6, 3, 'THURSDAY'),
    (6, 4, 'FRIDAY'),
    (6, 5, 'SATURDAY'),
    (6, 6, 'SUNDAY'),

    -- Week 1, Program 3
    (7, 7, 'MONDAY'),
    (7, 8, 'TUESDAY'),
    (7, 9, 'WEDNESDAY'),
    (7, 10, 'THURSDAY'),
    (7, 11, 'FRIDAY'),
    (7, 12, 'SATURDAY'),
    (7, 1, 'SUNDAY'),

    -- Week 2, Program 3
    (8, 2, 'MONDAY'),
    (8, 3, 'TUESDAY'),
    (8, 4, 'WEDNESDAY'),
    (8, 5, 'THURSDAY'),
    (8, 6, 'FRIDAY'),
    (8, 7, 'SATURDAY'),
    (8, 8, 'SUNDAY'),

    -- Week 3, Program 3
    (9, 9, 'MONDAY'),
    (9, 10, 'TUESDAY'),
    (9, 11, 'WEDNESDAY'),
    (9, 12, 'THURSDAY'),
    (9, 1, 'FRIDAY'),
    (9, 2, 'SATURDAY'),
    (9, 3, 'SUNDAY'),

    -- Week 1, Program 7
    (10, 4, 'MONDAY'),
    (10, 5, 'TUESDAY'),
    (10, 6, 'WEDNESDAY'),
    (10, 7, 'THURSDAY'),
    (10, 8, 'FRIDAY'),
    (10, 9, 'SATURDAY'),
    (10, 10, 'SUNDAY'),

    -- Week 2, Program 7
    (11, 11, 'MONDAY'),
    (11, 12, 'TUESDAY'),
    (11, 1, 'WEDNESDAY'),
    (11, 2, 'THURSDAY'),
    (11, 3, 'FRIDAY'),
    (11, 4, 'SATURDAY'),
    (11, 5, 'SUNDAY'),

    -- Week 3, Program 7
    (12, 6, 'MONDAY'),
    (12, 7, 'TUESDAY'),
    (12, 8, 'WEDNESDAY'),
    (12, 9, 'THURSDAY'),
    (12, 10, 'FRIDAY'),
    (12, 11, 'SATURDAY'),
    (12, 12, 'SUNDAY'),

    -- Week 1, Program 8
    (13, 1, 'MONDAY'),
    (13, 2, 'TUESDAY'),
    (13, 3, 'WEDNESDAY'),
    (13, 4, 'THURSDAY'),
    (13, 5, 'FRIDAY'),
    (13, 6, 'SATURDAY'),
    (13, 7, 'SUNDAY'),

    -- Week 2, Program 8
    (14, 8, 'MONDAY'),
    (14, 9, 'TUESDAY'),
    (14, 10, 'WEDNESDAY'),
    (14, 11, 'THURSDAY'),
    (14, 12, 'FRIDAY'),
    (14, 1, 'SATURDAY'),
    (14, 2, 'SUNDAY'),

    -- Week 3, Program 8
    (15, 3, 'MONDAY'),
    (15, 4, 'TUESDAY'),
    (15, 5, 'WEDNESDAY'),
    (15, 6, 'THURSDAY'),
    (15, 7, 'FRIDAY'),
    (15, 8, 'SATURDAY'),
    (15, 9, 'SUNDAY'),

    -- Week 1, Program 9
    (16, 10, 'MONDAY'),
    (16, 11, 'TUESDAY'),
    (16, 12, 'WEDNESDAY'),
    (16, 1, 'THURSDAY'),
    (16, 2, 'FRIDAY'),
    (16, 3, 'SATURDAY'),
    (16, 4, 'SUNDAY'),

    -- Week 2, Program 9
    (17, 5, 'MONDAY'),
    (17, 6, 'TUESDAY'),
    (17, 7, 'WEDNESDAY'),
    (17, 8, 'THURSDAY'),
    (17, 9, 'FRIDAY'),
    (17, 10, 'SATURDAY'),
    (17, 11, 'SUNDAY'),

    -- Week 3, Program 9
    (18, 12, 'MONDAY'),
    (18, 1, 'TUESDAY'),
    (18, 2, 'WEDNESDAY'),
    (18, 3, 'THURSDAY'),
    (18, 4, 'FRIDAY'),
    (18, 5, 'SATURDAY'),
    (18, 6, 'SUNDAY'),

    -- Week 1, Program 13
    (19, 7, 'MONDAY'),
    (19, 8, 'TUESDAY'),
    (19, 9, 'WEDNESDAY'),
    (19, 10, 'THURSDAY'),
    (19, 11, 'FRIDAY'),
    (19, 12, 'SATURDAY'),
    (19, 1, 'SUNDAY'),

    -- Week 2, Program 13
    (20, 2, 'MONDAY'),
    (20, 3, 'TUESDAY'),
    (20, 4, 'WEDNESDAY'),
    (20, 5, 'THURSDAY'),
    (20, 6, 'FRIDAY'),
    (20, 7, 'SATURDAY'),
    (20, 8, 'SUNDAY'),

    -- Week 3, Program 13
    (21, 9, 'MONDAY'),
    (21, 10, 'TUESDAY'),
    (21, 11, 'WEDNESDAY'),
    (21, 12, 'THURSDAY'),
    (21, 1, 'FRIDAY'),
    (21, 2, 'SATURDAY'),
    (21, 3, 'SUNDAY'),

    -- Week 1, Program 14
    (22, 4, 'MONDAY'),
    (22, 5, 'TUESDAY'),
    (22, 6, 'WEDNESDAY'),
    (22, 7, 'THURSDAY'),
    (22, 8, 'FRIDAY'),
    (22, 9, 'SATURDAY'),
    (22, 10, 'SUNDAY'),

    -- Week 2, Program 14
    (23, 11, 'MONDAY'),
    (23, 12, 'TUESDAY'),
    (23, 1, 'WEDNESDAY'),
    (23, 2, 'THURSDAY'),
    (23, 3, 'FRIDAY'),
    (23, 4, 'SATURDAY'),
    (23, 5, 'SUNDAY'),

    -- Week 3, Program 14
    (24, 6, 'MONDAY'),
    (24, 7, 'TUESDAY'),
    (24, 8, 'WEDNESDAY'),
    (24, 9, 'THURSDAY'),
    (24, 10, 'FRIDAY'),
    (24, 11, 'SATURDAY'),
    (24, 12, 'SUNDAY'),

    -- Week 1, Program 15
    (25, 1, 'MONDAY'),
    (25, 2, 'TUESDAY'),
    (25, 3, 'WEDNESDAY'),
    (25, 4, 'THURSDAY'),
    (25, 5, 'FRIDAY'),
    (25, 6, 'SATURDAY'),
    (25, 7, 'SUNDAY'),

    -- Week 2, Program 15
    (26, 8, 'MONDAY'),
    (26, 9, 'TUESDAY'),
    (26, 10, 'WEDNESDAY'),
    (26, 11, 'THURSDAY'),
    (26, 12, 'FRIDAY'),
    (26, 1, 'SATURDAY'),
    (26, 2, 'SUNDAY'),

    -- Week 3, Program 15
    (27, 3, 'MONDAY'),
    (27, 4, 'TUESDAY'),
    (27, 5, 'WEDNESDAY'),
    (27, 6, 'THURSDAY'),
    (27, 7, 'FRIDAY'),
    (27, 8, 'SATURDAY'),
    (27, 9, 'SUNDAY');



-- User 2, Program 4, Week 1
-- User 2, Program 1
-- Week 1
INSERT INTO user_progress (exercise_completed, workout_started, workout_completed, week_completed, user_id, workout_id,
                           week_id, program_id, day_name)
VALUES
    -- User 2, Program 1, Week 1
    (false, false, false, false, 2, 1, 1, 1, 'MONDAY'),
    (false, false, false, false, 2, 2, 1, 1, 'TUESDAY'),
    (false, false, false, false, 2, 3, 1, 1, 'WEDNESDAY'),
    (false, false, false, false, 2, 4, 1, 1, 'THURSDAY'),
    (false, false, false, false, 2, 5, 1, 1, 'FRIDAY'),
    (false, false, false, false, 2, 6, 1, 1, 'SATURDAY'),
    (false, false, false, false, 2, 7, 1, 1, 'SUNDAY'),

    -- User 2, Program 1, Week 2
    (false, false, false, false, 2, 8, 2, 1, 'MONDAY'),
    (false, false, false, false, 2, 9, 2, 1, 'TUESDAY'),
    (false, false, false, false, 2, 10, 2, 1, 'WEDNESDAY'),
    (false, false, false, false, 2, 11, 2, 1, 'THURSDAY'),
    (false, false, false, false, 2, 12, 2, 1, 'FRIDAY'),
    (false, false, false, false, 2, 13, 2, 1, 'SATURDAY'),
    (false, false, false, false, 2, 14, 2, 1, 'SUNDAY'),

    -- User 2, Program 1, Week 3
    (false, false, false, false, 2, 15, 3, 1, 'MONDAY'),
    (false, false, false, false, 2, 16, 3, 1, 'TUESDAY'),
    (false, false, false, false, 2, 17, 3, 1, 'WEDNESDAY'),
    (false, false, false, false, 2, 18, 3, 1, 'THURSDAY'),
    (false, false, false, false, 2, 19, 3, 1, 'FRIDAY'),
    (false, false, false, false, 2, 20, 3, 1, 'SATURDAY'),
    (false, false, false, false, 2, 21, 3, 1, 'SUNDAY'),

    -- User 2, Program 2, Week 1
    (false, false, false, false, 2, 22, 5, 2, 'MONDAY'),
    (false, false, false, false, 2, 23, 5, 2, 'TUESDAY'),
    (false, false, false, false, 2, 24, 5, 2, 'WEDNESDAY'),
    (false, false, false, false, 2, 25, 5, 2, 'THURSDAY'),
    (false, false, false, false, 2, 26, 5, 2, 'FRIDAY'),
    (false, false, false, false, 2, 27, 5, 2, 'SATURDAY'),
    (false, false, false, false, 2, 28, 5, 2, 'SUNDAY'),

    -- User 2, Program 2, Week 2
    (false, false, false, false, 2, 29, 6, 2, 'MONDAY'),
    (false, false, false, false, 2, 30, 6, 2, 'TUESDAY'),
    (false, false, false, false, 2, 31, 6, 2, 'WEDNESDAY'),
    (false, false, false, false, 2, 32, 6, 2, 'THURSDAY'),
    (false, false, false, false, 2, 33, 6, 2, 'FRIDAY'),
    (false, false, false, false, 2, 34, 6, 2, 'SATURDAY'),
    (false, false, false, false, 2, 35, 6, 2, 'SUNDAY'),

    -- User 2, Program 2, Week 3
    (false, false, false, false, 2, 36, 7, 2, 'MONDAY'),
    (false, false, false, false, 2, 37, 7, 2, 'TUESDAY'),
    (false, false, false, false, 2, 38, 7, 2, 'WEDNESDAY'),
    (false, false, false, false, 2, 39, 7, 2, 'THURSDAY'),
    (false, false, false, false, 2, 40, 7, 2, 'FRIDAY'),
    (false, false, false, false, 2, 41, 7, 2, 'SATURDAY'),
    (false, false, false, false, 2, 42, 7, 2, 'SUNDAY'),

    -- User 3, Program 1, Week 1
    (false, false, false, false, 3, 1, 1, 1, 'MONDAY'),
    (false, false, false, false, 3, 2, 1, 1, 'TUESDAY'),
    (false, false, false, false, 3, 3, 1, 1, 'WEDNESDAY'),
    (false, false, false, false, 3, 4, 1, 1, 'THURSDAY'),
    (false, false, false, false, 3, 5, 1, 1, 'FRIDAY'),
    (false, false, false, false, 3, 6, 1, 1, 'SATURDAY'),
    (false, false, false, false, 3, 7, 1, 1, 'SUNDAY'),

    -- User 3, Program 1, Week 2
    (false, false, false, false, 3, 8, 2, 1, 'MONDAY'),
    (false, false, false, false, 3, 9, 2, 1, 'TUESDAY'),
    (false, false, false, false, 3, 10, 2, 1, 'WEDNESDAY'),
    (false, false, false, false, 3, 11, 2, 1, 'THURSDAY'),
    (false, false, false, false, 3, 12, 2, 1, 'FRIDAY'),
    (false, false, false, false, 3, 13, 2, 1, 'SATURDAY'),
    (false, false, false, false, 3, 14, 2, 1, 'SUNDAY'),

    -- User 3, Program 1, Week 3
    (false, false, false, false, 3, 15, 3, 1, 'MONDAY'),
    (false, false, false, false, 3, 16, 3, 1, 'TUESDAY'),
    (false, false, false, false, 3, 17, 3, 1, 'WEDNESDAY'),
    (false, false, false, false, 3, 18, 3, 1, 'THURSDAY'),
    (false, false, false, false, 3, 19, 3, 1, 'FRIDAY'),
    (false, false, false, false, 3, 20, 3, 1, 'SATURDAY'),
    (false, false, false, false, 3, 21, 3, 1, 'SUNDAY'),
    -- User 3, Program 2, Week 1
    (false, false, false, false, 3, 22, 5, 2, 'MONDAY'),
    (false, false, false, false, 3, 23, 5, 2, 'TUESDAY'),
    (false, false, false, false, 3, 24, 5, 2, 'WEDNESDAY'),
    (false, false, false, false, 3, 25, 5, 2, 'THURSDAY'),
    (false, false, false, false, 3, 26, 5, 2, 'FRIDAY'),
    (false, false, false, false, 3, 27, 5, 2, 'SATURDAY'),
    (false, false, false, false, 3, 28, 5, 2, 'SUNDAY'),

    -- User 3, Program 2, Week 2
    (false, false, false, false, 3, 29, 6, 2, 'MONDAY'),
    (false, false, false, false, 3, 30, 6, 2, 'TUESDAY'),
    (false, false, false, false, 3, 31, 6, 2, 'WEDNESDAY'),
    (false, false, false, false, 3, 32, 6, 2, 'THURSDAY'),
    (false, false, false, false, 3, 33, 6, 2, 'FRIDAY'),
    (false, false, false, false, 3, 34, 6, 2, 'SATURDAY'),
    (false, false, false, false, 3, 35, 6, 2, 'SUNDAY'),

    -- User 3, Program 2, Week 3
    (false, false, false, false, 3, 36, 7, 2, 'MONDAY'),
    (false, false, false, false, 3, 37, 7, 2, 'TUESDAY'),
    (false, false, false, false, 3, 38, 7, 2, 'WEDNESDAY'),
    (false, false, false, false, 3, 39, 7, 2, 'THURSDAY'),
    (false, false, false, false, 3, 40, 7, 2, 'FRIDAY'),
    (false, false, false, false, 3, 41, 7, 2, 'SATURDAY'),
    (false, false, false, false, 3, 42, 7, 2, 'SUNDAY'),

    -- User 4, Program 7, Week 1
    (false, false, false, false, 4, 64, 10, 7, 'MONDAY'),
    (false, false, false, false, 4, 65, 10, 7, 'TUESDAY'),
    (false, false, false, false, 4, 66, 10, 7, 'WEDNESDAY'),
    (false, false, false, false, 4, 67, 10, 7, 'THURSDAY'),
    (false, false, false, false, 4, 68, 10, 7, 'FRIDAY'),
    (false, false, false, false, 4, 69, 10, 7, 'SATURDAY'),
    (false, false, false, false, 4, 70, 10, 7, 'SUNDAY'),

    -- User 4, Program 7, Week 2
    (false, false, false, false, 4, 71, 11, 7, 'MONDAY'),
    (false, false, false, false, 4, 72, 11, 7, 'TUESDAY'),
    (false, false, false, false, 4, 73, 11, 7, 'WEDNESDAY'),
    (false, false, false, false, 4, 74, 11, 7, 'THURSDAY'),
    (false, false, false, false, 4, 75, 11, 7, 'FRIDAY'),
    (false, false, false, false, 4, 76, 11, 7, 'SATURDAY'),
    (false, false, false, false, 4, 77, 11, 7, 'SUNDAY'),

    -- User 4, Program 7, Week 3
    (false, false, false, false, 4, 78, 12, 7, 'MONDAY'),
    (false, false, false, false, 4, 79, 12, 7, 'TUESDAY'),
    (false, false, false, false, 4, 80, 12, 7, 'WEDNESDAY'),
    (false, false, false, false, 4, 81, 12, 7, 'THURSDAY'),
    (false, false, false, false, 4, 82, 12, 7, 'FRIDAY'),
    (false, false, false, false, 4, 83, 12, 7, 'SATURDAY'),
    (false, false, false, false, 4, 84, 12, 7, 'SUNDAY'),

    -- User 5, Program 13, Week 1
    (false, false, false, false, 5, 127, 19, 13, 'MONDAY'),
    (false, false, false, false, 5, 128, 19, 13, 'TUESDAY'),
    (false, false, false, false, 5, 129, 19, 13, 'WEDNESDAY'),
    (false, false, false, false, 5, 130, 19, 13, 'THURSDAY'),
    (false, false, false, false, 5, 131, 19, 13, 'FRIDAY'),
    (false, false, false, false, 5, 132, 19, 13, 'SATURDAY'),
    (false, false, false, false, 5, 133, 19, 13, 'SUNDAY'),

    -- User 5, Program 13, Week 2
    (false, false, false, false, 5, 134, 20, 13, 'MONDAY'),
    (false, false, false, false, 5, 135, 20, 13, 'TUESDAY'),
    (false, false, false, false, 5, 136, 20, 13, 'WEDNESDAY'),
    (false, false, false, false, 5, 137, 20, 13, 'THURSDAY'),
    (false, false, false, false, 5, 138, 20, 13, 'FRIDAY'),
    (false, false, false, false, 5, 139, 20, 13, 'SATURDAY'),
    (false, false, false, false, 5, 140, 20, 13, 'SUNDAY'),

    -- User 5, Program 13, Week 3
    (false, false, false, false, 5, 141, 21, 13, 'MONDAY'),
    (false, false, false, false, 5, 142, 21, 13, 'TUESDAY'),
    (false, false, false, false, 5, 143, 21, 13, 'WEDNESDAY'),
    (false, false, false, false, 5, 144, 21, 13, 'THURSDAY'),
    (false, false, false, false, 5, 145, 21, 13, 'FRIDAY'),
    (false, false, false, false, 5, 146, 21, 13, 'SATURDAY'),
    (false, false, false, false, 5, 147, 21, 13, 'SUNDAY'),

    -- User 6, Program 7, Week 1
    (false, false, false, false, 6, 64, 10, 7, 'MONDAY'),
    (false, false, false, false, 6, 65, 10, 7, 'TUESDAY'),
    (false, false, false, false, 6, 66, 10, 7, 'WEDNESDAY'),
    (false, false, false, false, 6, 67, 10, 7, 'THURSDAY'),
    (false, false, false, false, 6, 68, 10, 7, 'FRIDAY'),
    (false, false, false, false, 6, 69, 10, 7, 'SATURDAY'),
    (false, false, false, false, 6, 70, 10, 7, 'SUNDAY'),

    -- User 6, Program 7, Week 2
    (false, false, false, false, 6, 71, 11, 7, 'MONDAY'),
    (false, false, false, false, 6, 72, 11, 7, 'TUESDAY'),
    (false, false, false, false, 6, 73, 11, 7, 'WEDNESDAY'),
    (false, false, false, false, 6, 74, 11, 7, 'THURSDAY'),
    (false, false, false, false, 6, 75, 11, 7, 'FRIDAY'),
    (false, false, false, false, 6, 76, 11, 7, 'SATURDAY'),
    (false, false, false, false, 6, 77, 11, 7, 'SUNDAY'),

    -- User 6, Program 7, Week 3
    (false, false, false, false, 6, 78, 12, 7, 'MONDAY'),
    (false, false, false, false, 6, 79, 12, 7, 'TUESDAY'),
    (false, false, false, false, 6, 80, 12, 7, 'WEDNESDAY'),
    (false, false, false, false, 6, 81, 12, 7, 'THURSDAY'),
    (false, false, false, false, 6, 82, 12, 7, 'FRIDAY'),
    (false, false, false, false, 6, 83, 12, 7, 'SATURDAY'),
    (false, false, false, false, 6, 84, 12, 7, 'SUNDAY'),

    -- User 7, Program 13, Week 1
    (false, false, false, false, 7, 127, 19, 13, 'MONDAY'),
    (false, false, false, false, 7, 128, 19, 13, 'TUESDAY'),
    (false, false, false, false, 7, 129, 19, 13, 'WEDNESDAY'),
    (false, false, false, false, 7, 130, 19, 13, 'THURSDAY'),
    (false, false, false, false, 7, 131, 19, 13, 'FRIDAY'),
    (false, false, false, false, 7, 132, 19, 13, 'SATURDAY'),
    (false, false, false, false, 7, 133, 19, 13, 'SUNDAY'),

    -- User 7, Program 13, Week 2
    (false, false, false, false, 7, 134, 20, 13, 'MONDAY'),
    (false, false, false, false, 7, 135, 20, 13, 'TUESDAY'),
    (false, false, false, false, 7, 136, 20, 13, 'WEDNESDAY'),
    (false, false, false, false, 7, 137, 20, 13, 'THURSDAY'),
    (false, false, false, false, 7, 138, 20, 13, 'FRIDAY'),
    (false, false, false, false, 7, 139, 20, 13, 'SATURDAY'),
    (false, false, false, false, 7, 140, 20, 13, 'SUNDAY'),

    -- User 7, Program 13, Week 3
    (false, false, false, false, 7, 141, 21, 13, 'MONDAY'),
    (false, false, false, false, 7, 142, 21, 13, 'TUESDAY'),
    (false, false, false, false, 7, 143, 21, 13, 'WEDNESDAY'),
    (false, false, false, false, 7, 144, 21, 13, 'THURSDAY'),
    (false, false, false, false, 7, 145, 21, 13, 'FRIDAY'),
    (false, false, false, false, 7, 146, 21, 13, 'SATURDAY'),
    (false, false, false, false, 7, 147, 21, 13, 'SUNDAY');


INSERT INTO program_workout_exercises (is_completed, exercise_id, id, workout_id)
VALUES
    -- Week 1, Program 1
    (false, 1, 1, 1),
    (false, 2, 2, 1),
    (false, 3, 3, 1),
    (false, 4, 4, 2),
    (false, 5, 5, 2),
    (false, 6, 6, 2),
    (false, 7, 7, 3),
    (false, 8, 8, 3),
    (false, 9, 9, 3),
    (false, 10, 10, 4),
    (false, 11, 11, 4),
    (false, 12, 12, 4),
    (false, 13, 13, 5),
    (false, 14, 14, 5),
    (false, 15, 15, 5),
    (false, 16, 16, 6),
    (false, 17, 17, 6),
    (false, 18, 18, 6),
    (false, 19, 19, 7),
    (false, 20, 20, 7),
    (false, 21, 21, 7),
    (false, 22, 22, 8),
    (false, 23, 23, 8),
    (false, 24, 24, 8),
    (false, 25, 25, 9),
    (false, 26, 26, 9),
    (false, 27, 27, 9);

INSERT INTO week_metrics (number)
VALUES
(1),
(2),
(1),
(2),
(1),
(2),
(3);


INSERT INTO daily_metrics (calories_intake, date, energy_levels, sleep_duration, steps_count, weight, client_id, id, week_id)
VALUES
-- Week 1 for Client 1
(2200, '2024-09-01', 8, 7.5, 10000, 75.0, 1, 1, 1),
(2100, '2024-09-02', 7, 6.8, 9500, 74.8, 1, 2, 1),
(2300, '2024-09-03', 6, 5.5, 8000, 74.6, 1, 3, 1),
(2500, '2024-09-04', 9, 8.0, 12000, 74.4, 1, 4, 1),
(1800, '2024-09-05', 7, 6.0, 7000, 74.2, 1, 5, 1),
(2400, '2024-09-06', 8, 7.2, 11000, 74.0, 1, 6, 1),
(2000, '2024-09-07', 7, 7.0, 9000, 73.8, 1, 7, 1),

-- Week 2 for Client 1
(2100, '2024-09-08', 8, 6.9, 9500, 73.6, 1, 8, 2),
(1900, '2024-09-09', 7, 7.5, 9000, 73.4, 1, 9, 2),
(2300, '2024-09-10', 6, 6.0, 8000, 73.2, 1, 10, 2),
(2400, '2024-09-11', 9, 7.0, 10000, 73.0, 1, 11, 2),
(1800, '2024-09-12', 7, 6.5, 8500, 72.8, 1, 12, 2),
(2200, '2024-09-13', 8, 7.8, 10500, 72.6, 1, 13, 2),
(2000, '2024-09-14', 7, 7.2, 9000, 72.4, 1, 14, 2),

-- Week 3 for Client 1
(2100, '2024-09-15', 6, 6.9, 9750, 73.6, 1, 43, 7),
(1900, '2024-09-16', 9, 7.5, 8000, 73.2, 1, 44, 7),
(2300, '2024-09-17', 10, 6.0, 7250, 73.0, 1, 45, 7),
(2400, '2024-09-18', 8, 7.0, 12000, 73.2, 1, 46, 7),
(1800, '2024-09-19', 3, 6.5, 13000, 73.2, 1, 47, 7),
(2200, '2024-09-20', 6, 7.8, 8500, 73.0, 1, 48, 7),
(2000, '2024-09-21', 5, 7.2, 9000, 73.1, 1, 49, 7),

-- Week 1 for Client 2
(1800, '2024-09-01', 7, 6.5, 7500, 80.0, 2, 15, 3),
(1700, '2024-09-02', 6, 5.0, 6000, 79.8, 2, 16, 3),
(1900, '2024-09-03', 8, 8.0, 9000, 79.6, 2, 17, 3),
(2100, '2024-09-04', 9, 7.2, 10000, 79.4, 2, 18, 3),
(2000, '2024-09-05', 7, 6.7, 8500, 79.2, 2, 19, 3),
(1800, '2024-09-06', 6, 6.0, 7000, 79.0, 2, 20, 3),
(2200, '2024-09-07', 9, 7.5, 11000, 78.8, 2, 21, 3),


-- Week 2 for Client 2
(2100, '2024-09-08', 7, 7.0, 9500, 78.6, 2, 22, 4),
(2000, '2024-09-09', 6, 6.5, 8500, 78.4, 2, 23, 4),
(2200, '2024-09-10', 8, 7.8, 10500, 78.2, 2, 24, 4),
(1900, '2024-09-11', 6, 6.0, 8000, 78.0, 2, 25, 4),
(2400, '2024-09-12', 9, 8.2, 12000, 77.8, 2, 26, 4),
(2300, '2024-09-13', 8, 7.2, 9500, 77.6, 2, 27, 4),
(2100, '2024-09-14', 7, 7.5, 10000, 77.4, 2, 28, 4),

-- Week 1 for Client 3
(2500, '2024-09-01', 9, 8.0, 12000, 65.0, 3, 29, 5),
(2400, '2024-09-02', 8, 7.0, 11000, 64.8, 3, 30, 5),
(2300, '2024-09-03', 7, 6.0, 10000, 64.6, 3, 31, 5),
(2200, '2024-09-04', 8, 7.5, 10500, 64.4, 3, 32, 5),
(2100, '2024-09-05', 7, 6.5, 9500, 64.2, 3, 33, 5),
(2300, '2024-09-06', 9, 7.8, 11500, 64.0, 3, 34, 5),
(2000, '2024-09-07', 7, 7.0, 9000, 63.8, 3, 35, 5),

-- Week 2 for Client 3
(2100, '2024-09-08', 8, 7.5, 9500, 63.6, 3, 36, 6),
(1900, '2024-09-09', 7, 6.5, 8500, 63.4, 3, 37, 6),
(2200, '2024-09-10', 9, 7.8, 11000, 63.2, 3, 38,6),
(2000, '2024-09-11', 7, 7.0, 9000, 63.0, 3, 39, 6),
(2400, '2024-09-12', 8, 8.0, 11500, 62.8, 3, 40, 6),
(2300, '2024-09-13', 8, 7.2, 10000, 62.6, 3, 41, 6),
(2200, '2024-09-14', 7, 7.0, 9500, 62.4, 3, 42, 6);











