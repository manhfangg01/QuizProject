use quizdb;

INSERT INTO users (full_name, email, password, created_at, updated_at, created_by, updated_by)
VALUES
('Alice Nguyen', 'alice@example.com', 'password123', NOW(), NOW(), 'system', 'system'),
('Bob Tran', 'bob@example.com', 'password456', NOW(), NOW(), 'system', 'system');

INSERT INTO quiz (title, subject_name, time_limit, total_participants, is_active, difficulty)
VALUES
('Java Basics', 'Java', 900, 0, TRUE, 'EASY'),
('SQL Practice', 'Database', 1200, 0, TRUE, 'MEDIUM');


INSERT INTO questions (context)
VALUES
('What is a class in Java?'),
('Which SQL clause is used to filter records?'),
('What is polymorphism in OOP?');

alter table options
MODIFY column is_correct bit DEFAULT 0;

ALTER TABLE options
ALTER COLUMN is_correct DROP DEFAULT;



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
from options;


INSERT INTO quiz_questions (quiz_id, question_id)
VALUES
(1, 1),
(1, 3),
(2, 2);


