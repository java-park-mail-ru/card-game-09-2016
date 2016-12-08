CREATE TABLE `Users`
(
  `id` INT PRIMARY KEY AUTO_INCREMENT,
  `email` VARCHAR(50) NOT NULL,
  `login` VARCHAR(50) NOT NULL,
  `password` VARCHAR(128) NOT NULL,
  `score` INT DEFAULT 0
);
CREATE UNIQUE INDEX `User_email_uindex` ON `Users` (`email`);
CREATE UNIQUE INDEX `User_login_uindex`  ON `Users` (`login`);