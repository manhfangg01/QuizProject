use quizdb;
select * from quiz;
select * from options;
select *
from users user1, users user2
where user1.id<>user2.id and (user1.email=user2.email );
select * from roles;
select *
from users
where users.email='manhanh329@gmail.com';
ALTER TABLE users ADD CONSTRAINT uk_user_email UNIQUE (email);
select * from questions;
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


insert into options(context, is_correct) values
("in front of", true),
("opposed to", false),
("against",false),
("versus", false),
("somewhat",false),
("extremely",true),
("relatively",false),
("objectively",false),
("keen on him", false),
("stuck on him", false),
("fell for him", true),
("wed him", false),
("mainly", false),
("fairly",true),
("especially",false),
("slightly",false),
("challenge", false),
("realization",false),
("achievement",true),
("experiment",false),
("empathetic",false),
("selfish",true),
("juvenile",false),
("frivolous", false),
("look him up at", false),
("make a reservation from",false),
("make contact for", false),
("book him into", true),
("optimistic",true),
("gleeful",false),
("humble",false),
("respectful",false),
("gathering",false),
("extravaganza",true),
("fete", false),
("ritual", false),
("My sister always wears my clothes", false),
("I feel this is too dressy for the occasion.", false),
("I bought it in a charity shop last week.", true),
("I haven’t worn this in a while.", false),
("My brother just turned five, too.", false),
("Haven’t I told you that I only have a sister?", false),
("I know what you mean; mine drives me crazy.", true),
("I told my mom not to let him come in my room.", false),
("I cook quite often.", false),
("Please help yourself.", false),
("I’ll see if I can get it for you", false),
("You’ll never guess.", true),
("Where are you taking him?", false),
("Of course, I’d love to.", true),
("I already fed him today.", false),
("I used to have a cat.", false),
("That jacket looks too tight on you.", false),
("I’m not sure my sister would like that style.", false),
("I bought it online because it was cheaper.", false),
("Better make up your mind before the store closes.", true),
("Yes, would you like some tea?", false),
("Isn’t it warm to put the heater on?", false),
("When is it going to be ready?", false),
("Don’t make it too hot, OK?", true),
("Have you taken anything for it?", true),
("For how long will you be away?", false),
("I wish he would leave you alone.", false),
("Stop making so much noise.", false),
("It looks like it’s going to rain any minute.", false),
("Sure, let me grab my jacket because it’s chilly outside.", true),
("Let’s wait until the sun goes down in an hour or so.", false),
(" Astronomy is not my expertise, so I’m not sure.", false),
("He has enough credits to graduate as a French major.", false),
("His teacher recommended me for this course.", false),
("I need to take one more class to fulfill my requirements.", false),
("I had a terrible experience with him last semester.", true),
("I wanted to call you, but my phone battery died on me right before I made a call.", false),
("I already left her a message saying you won’t be home until late tonight.", false),
("She is a responsible girl, so she will contact you if she feels she needs to.", true),
("She does have a habit of getting up late on weekends.", false);












