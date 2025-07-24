use quizdb;
select * from quiz;
select *
from users user1, users user2
where user1.id<>user2.id and (user1.email=user2.email );
select * from roles;
select *
from users
where users.email='manhanh329@gmail.com';
ALTER TABLE users ADD CONSTRAINT uk_user_email UNIQUE (email);

select * from quiz;
select * from results
order by submitted_at desc;
ALTER TABLE results DROP COLUMN spent_time;
INSERT INTO users (full_name, email, password, created_at, updated_at, created_by, updated_by)
VALUES
('Alice Nguyen', 'alice@example.com', 'password123', NOW(), NOW(), 'system', 'system'),
('Bob Tran', 'bob@example.com', 'password456', NOW(), NOW(), 'system', 'system');

INSERT INTO quiz (title, subject_name, time_limit, total_participants, is_active, difficulty)
VALUES
('Java Basics', 'Java', 900, 0, TRUE, 'EASY'),
('SQL Practice', 'Database', 1200, 0, TRUE, 'MEDIUM'),
('Python Basics', 'DJanggo', 1000, 0, FALSE, 'HARD');


INSERT INTO questions (context)
VALUES
('What is a class in Java?'),
('Which SQL clause is used to filter records?'),
('What is polymorphism in OOP?');

-- alter table options
-- MODIFY column is_correct bit DEFAULT 0;

-- ALTER TABLE options
-- ALTER COLUMN is_correct DROP DEFAULT;



	INSERT INTO options (context, question_id, is_correct)
	VALUES
	-- Question 1
	('A blueprint for objects', 1,TRUE),
	('An SQL keyword', 1, FALSE),
	('An Interface', 1, FALSE),
	('An Object', 1, FALSE),
	-- Question 2
	('WHERE', 2,FALSE),
	('SELECT', 2,FALSE),
	('GROUP', 2,FALSE),
	('JOIN', 2,TRUE),
	-- Question 3
	('Ability to take many forms', 3, FALSE),
	('Update forms', 3, FALSE),
	('Review Data', 3,TRUE),
	('A type of loop', 3,FALSE);


select *
from quiz;
select *
from quiz_questions;


INSERT INTO quiz_questions (quiz_id, question_id)
VALUES
(1, 1),
(1, 3),
(2, 2);

select *
from results;

select *
from users;

INSERT INTO users (id, full_name, email, password, created_at, updated_at, created_by, updated_by)
VALUES 
(3, 'Alice Johnson', 'alice@example.com', 'password123', NOW(), NOW(), 'admin', 'admin'),
(4, 'Bob Smith', 'bob@example.com', 'password123', NOW(), NOW(), 'admin', 'admin'),
(5, 'Charlie Brown', 'charlie@example.com', 'password123', NOW(), NOW(), 'admin', 'admin');


INSERT INTO results (id, duration, quiz_id, user_id, score, submitted_at, total_corrected_answer, total_question)
VALUES 
(1, 1000, 1, 1, 80, NOW(), 8, 10),
(2, 1000, 1, 2, 60, NOW(), 6, 10),
(3, 1000, 2, 1, 90, NOW(), 9, 10),
(4, 1000, 2, 3, 70, NOW(), 7, 10);

insert into roles(description, is_active,name)
values
("Has all authorities in system",true, "ADMIN"),
("Has some authorities in system",true, "USER");





