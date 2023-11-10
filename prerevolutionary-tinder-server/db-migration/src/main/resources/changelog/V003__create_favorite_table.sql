CREATE TABLE favorite (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    favorite_user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES User(id),
    FOREIGN KEY (favorite_user_id) REFERENCES User(id),
    UNIQUE KEY unique_favorite (user_id, favorite_user_id)
);