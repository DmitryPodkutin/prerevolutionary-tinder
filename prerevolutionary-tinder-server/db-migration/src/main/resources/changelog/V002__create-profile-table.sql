CREATE TABLE profile (
    id INT PRIMARY KEY AUTO_INCREMENT,
    gender VARCHAR(255),
    name VARCHAR(255),
    descriptionHeader VARCHAR(255),
    description TEXT,
    seeking VARCHAR(255),
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES User(id)
);