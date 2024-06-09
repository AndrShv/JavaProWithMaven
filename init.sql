
CREATE TABLE Homework (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

CREATE TABLE Lesson (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    homework_id INT,
    FOREIGN KEY (homework_id) REFERENCES Homework(id)
);

CREATE TABLE Schedule (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE Schedule_Lesson (
    schedule_id INT,
    lesson_id INT,
    PRIMARY KEY (schedule_id, lesson_id),
    FOREIGN KEY (schedule_id) REFERENCES Schedule(id),
    FOREIGN KEY (lesson_id) REFERENCES Lesson(id)
);
