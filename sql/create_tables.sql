USE `Conference` ;

-- -----------------------------------------------------
-- Table `Conference`.`permission`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Conference`.`permission` (
                                                         `id` INT NOT NULL AUTO_INCREMENT,
                                                         `name` VARCHAR(255) NOT NULL,
                                                         `modify_any_event` TINYINT(1) NULL DEFAULT 0 null,
                                                         `change_user_permission` TINYINT(1) NULL DEFAULT 0 null,
                                                         `all_users` TINYINT(1) NULL DEFAULT 0 null,
                                                         `delete_user` TINYINT(1) NULL DEFAULT 0 null,
                                                         `add_theme` TINYINT(1) NULL DEFAULT 0 null,
                                                         `add_role` TINYINT(1) NULL DEFAULT 0 NOT null,
                                                         `add_permission` TINYINT(1) NULL DEFAULT 0 null,
                                                         PRIMARY KEY (`id`),
                                                         UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Conference`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Conference`.`user` (
                                                   `id` INT NOT NULL,
                                                   `login` VARCHAR(255) NOT NULL,
                                                   `password` VARCHAR(32) NOT NULL,
                                                   `permission` INT NULL,
                                                   PRIMARY KEY (`id`),
                                                   UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE,
                                                   CONSTRAINT `permission`
                                                       FOREIGN KEY (`id`)
                                                           REFERENCES `Conference`.`permission` (`id`)
                                                           ON DELETE CASCADE
                                                           ON UPDATE CASCADE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Conference`.`user_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Conference`.`user_info` (
                                                        `id` INT NOT NULL AUTO_INCREMENT,
                                                        `user_id` INT NOT NULL,
                                                        `name` VARCHAR(255) NOT NULL,
                                                        `surname` VARCHAR(255) NOT NULL,
                                                        `about` TEXT NULL,
                                                        `picture_link` VARCHAR(255) NULL DEFAULT NULL,
                                                        `email` VARCHAR(255) NOT NULL,
                                                        `date_of_birth` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                                                        `date_of_registration` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                                                        PRIMARY KEY (`id`),
                                                        UNIQUE INDEX `user_if_UNIQUE` (`user_id` ASC) VISIBLE,
                                                        CONSTRAINT `user_id`
                                                            FOREIGN KEY (`id`)
                                                                REFERENCES `Conference`.`user` (`id`)
                                                                ON DELETE CASCADE
                                                                ON UPDATE CASCADE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Conference`.`address`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Conference`.`address` (
                                                      `id` INT NOT NULL AUTO_INCREMENT,
                                                      `country` VARCHAR(255) NOT NULL,
                                                      `city` VARCHAR(255) NOT NULL,
                                                      `street` VARCHAR(255) NOT NULL,
                                                      `building` VARCHAR(255) NOT NULL,
                                                      PRIMARY KEY (`id`))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Conference`.`theme`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Conference`.`theme` (
                                                    `id` INT NOT NULL AUTO_INCREMENT,
                                                    `value` VARCHAR(255) NOT NULL,
                                                    PRIMARY KEY (`id`),
                                                    UNIQUE INDEX `value_UNIQUE` (`value` ASC) VISIBLE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Conference`.`event_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Conference`.`event_info` (
                                                         `id` INT NOT NULL AUTO_INCREMENT,
                                                         `name` VARCHAR(255) NOT NULL,
                                                         `description` TEXT NULL,
                                                         `picture_link` VARCHAR(255) NULL DEFAULT NULL,
                                                         `theme` INT NULL,
                                                         `date` TIMESTAMP NOT NULL,
                                                         `address` INT NULL,
                                                         `author_id` INT NOT NULL,
                                                         `capacity` INT NOT NULL,
                                                         `duration` TIME NOT NULL,
                                                         PRIMARY KEY (`id`),
                                                         CONSTRAINT `author_id`
                                                             FOREIGN KEY (`id`)
                                                                 REFERENCES `Conference`.`user` (`id`)
                                                                 ON DELETE CASCADE
                                                                 ON UPDATE CASCADE,
                                                         CONSTRAINT `address`
                                                             FOREIGN KEY (`id`)
                                                                 REFERENCES `Conference`.`address` (`id`)
                                                                 ON DELETE CASCADE
                                                                 ON UPDATE CASCADE,
                                                         CONSTRAINT `theme`
                                                             FOREIGN KEY (`id`)
                                                                 REFERENCES `Conference`.`theme` (`id`)
                                                                 ON DELETE CASCADE
                                                                 ON UPDATE CASCADE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Conference`.`user_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Conference`.`user_role` (
                                                        `id` INT NOT NULL AUTO_INCREMENT,
                                                        `value` VARCHAR(255) NOT NULL,
                                                        PRIMARY KEY (`id`),
                                                        UNIQUE INDEX `value_UNIQUE` (`value` ASC) VISIBLE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Conference`.`registration`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Conference`.`registration` (
                                                           `id` INT NOT NULL AUTO_INCREMENT,
                                                           `event_id` INT NULL,
                                                           `user_id` INT NULL,
                                                           `user_role` INT NULL,
                                                           PRIMARY KEY (`id`),
                                                           FOREIGN KEY (`id`)
                                                               REFERENCES `Conference`.`event_info` (`id`)
                                                               ON DELETE CASCADE
                                                               ON UPDATE CASCADE,
                                                           FOREIGN KEY (`id`)
                                                               REFERENCES `Conference`.`user` (`id`)
                                                               ON DELETE CASCADE
                                                               ON UPDATE CASCADE,
                                                           FOREIGN KEY (`id`)
                                                               REFERENCES `Conference`.`user_role` (`id`)
                                                               ON DELETE CASCADE
                                                               ON UPDATE CASCADE)
    ENGINE = InnoDB;