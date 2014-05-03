SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';


-- -----------------------------------------------------
-- Table `user`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `user` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `username` VARCHAR(255) NOT NULL ,
  `password` VARCHAR(255) NOT NULL ,
  `roles` INT NOT NULL ,
  `status` VARCHAR(45) NOT NULL ,
  `created` TIMESTAMP NOT NULL DEFAULT '2011-05-09 00:00:00' ,
  `modified` TIMESTAMP NOT NULL DEFAULT current_timestamp on update current_timestamp ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) ,
  INDEX `user_created_idx` (`created` ASC) ,
  INDEX `user_password_idx` (`password` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci
COMMENT = '<double-click to overwrite multiple objects>' ;


-- -----------------------------------------------------
-- Table `user_profile`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `user_profile` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `user_id` INT UNSIGNED NOT NULL ,
  `display_name` VARCHAR(45) NOT NULL ,
  `created` TIMESTAMP NOT NULL DEFAULT '2011-05-09 00:00:00' ,
  `modified` TIMESTAMP NOT NULL DEFAULT current_timestamp on update current_timestamp ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_user1` (`user_id` ASC) ,
  INDEX `profile_created_idx` (`created` ASC) ,
  CONSTRAINT `fk_user1`
    FOREIGN KEY (`user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci, 
COMMENT = '<double-click to overwrite multiple objects>' ;


-- -----------------------------------------------------
-- Table `permission`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `permission` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `create_permissions` INT NOT NULL ,
  `read_permissions` INT NOT NULL ,
  `update_permissions` INT NOT NULL ,
  `delete_permissions` INT NOT NULL ,
  `created` TIMESTAMP NULL DEFAULT '2011-05-09 00:00:00' ,
  `modified` TIMESTAMP NULL DEFAULT current_timestamp on update current_timestamp ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci, 
COMMENT = '<double-click to overwrite multiple objects>' ;


-- -----------------------------------------------------
-- Table `forum`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `forum` (
  `id` INT UNSIGNED NULL AUTO_INCREMENT ,
  `user_id` INT UNSIGNED NOT NULL ,
  `permission_id` INT UNSIGNED NOT NULL ,
  `title` VARCHAR(255) NOT NULL ,
  `status` VARCHAR(45) NOT NULL DEFAULT 'ACTIVE' ,
  `parent_forum_status` VARCHAR(45) NOT NULL DEFAULT 'ACTIVE' ,
  `created` TIMESTAMP NOT NULL DEFAULT '2011-05-09 00:00:00' ,
  `modified` TIMESTAMP NOT NULL DEFAULT current_timestamp on update current_timestamp ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_user_id1` (`user_id` ASC) ,
  INDEX `fk_forum_permission1` (`permission_id` ASC) ,
  INDEX `forum_title_index` (`title` ASC) ,
  INDEX `forum_created_index` (`created` ASC) ,
  INDEX `forum_parent_forum_status_index` (`parent_forum_status` ASC) ,
  CONSTRAINT `fk_user_id1`
    FOREIGN KEY (`user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_forum_permission1`
    FOREIGN KEY (`permission_id` )
    REFERENCES `permission` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;


-- -----------------------------------------------------
-- Table `thread`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `thread` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `forum_id` INT UNSIGNED NOT NULL ,
  `user_id` INT UNSIGNED NOT NULL ,
  `permission_id` INT UNSIGNED NOT NULL ,
  `title` VARCHAR(255) NOT NULL ,
  `status` VARCHAR(45) NOT NULL DEFAULT 'ACTIVE' ,
  `forum_status` VARCHAR(45) NOT NULL DEFAULT 'ACTIVE' ,
  `created` TIMESTAMP NOT NULL DEFAULT '2011-05-09 00:00:00' ,
  `modified` TIMESTAMP NOT NULL DEFAULT current_timestamp on update current_timestamp ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_forum` (`forum_id` ASC) ,
  INDEX `fk_creator` (`user_id` ASC) ,
  INDEX `fk_thread_permission1` (`permission_id` ASC) ,
  INDEX `thread_title_index` (`title` ASC) ,
  INDEX `thread_created_index` (`created` ASC) ,
  INDEX `thread_forum_status_idx` (`forum_status` ASC) ,
  CONSTRAINT `fk_forum`
    FOREIGN KEY (`forum_id` )
    REFERENCES `forum` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_creator`
    FOREIGN KEY (`user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_thread_permission1`
    FOREIGN KEY (`permission_id` )
    REFERENCES `permission` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci, 
COMMENT = '<double-click to overwrite multiple objects>' ;


-- -----------------------------------------------------
-- Table `post`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `post` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `thread_id` INT UNSIGNED NOT NULL ,
  `parent_id` INT UNSIGNED NULL ,
  `user_id` INT UNSIGNED NOT NULL ,
  `permission_id` INT UNSIGNED NOT NULL ,
  `depth` INT NOT NULL ,
  `title` VARCHAR(255) NULL ,
  `body` TEXT NOT NULL ,
  `status` VARCHAR(45) NOT NULL DEFAULT 'ACTIVE' ,
  `parent_post_status` VARCHAR(45) NOT NULL DEFAULT 'ACTIVE' ,
  `thread_status` VARCHAR(45) NOT NULL DEFAULT 'ACTIVE' ,
  `forum_status` VARCHAR(45) NOT NULL DEFAULT 'ACTIVE' ,
  `type` VARCHAR(45) NOT NULL DEFAULT 'COMMUNITY_POST' ,
  `created` TIMESTAMP NOT NULL DEFAULT '2011-05-09 00:00:00' ,
  `modified` TIMESTAMP NOT NULL DEFAULT current_timestamp on update current_timestamp ,
  `hashcode` VARCHAR(255) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_thread` (`thread_id` ASC) ,
  INDEX `fk_user` (`user_id` ASC) ,
  INDEX `fk_post_permission1` (`permission_id` ASC) ,
  INDEX `fk_parent_post1` (`parent_id` ASC) ,
  INDEX `post_created_index` (`created` ASC) ,
  INDEX `parent_post_status_idx` (`parent_post_status` ASC) ,
  INDEX `post_thread_status_idx` (`thread_status` ASC) ,
  INDEX `post_forum_status_idx` (`forum_status` ASC) ,
  UNIQUE INDEX `hashcode_UNIQUE` (`hashcode` ASC) ,
  CONSTRAINT `fk_thread`
    FOREIGN KEY (`thread_id` )
    REFERENCES `thread` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user`
    FOREIGN KEY (`user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_post_permission1`
    FOREIGN KEY (`permission_id` )
    REFERENCES `permission` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_parent_post1`
    FOREIGN KEY (`parent_id` )
    REFERENCES `post` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci, 
COMMENT = '<double-click to overwrite multiple objects>' ;


-- -----------------------------------------------------
-- Table `user_profile_field`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `user_profile_field` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `user_profile_id` INT UNSIGNED NOT NULL ,
  `permission_id` INT UNSIGNED NOT NULL ,
  `name` VARCHAR(255) NOT NULL ,
  `value` TEXT NULL ,
  `created` TIMESTAMP NOT NULL DEFAULT '2011-05-09 00:00:00' ,
  `modified` TIMESTAMP NOT NULL DEFAULT current_timestamp on update current_timestamp ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_user_profile1` (`user_profile_id` ASC) ,
  INDEX `fk_profilefield_permission1` (`permission_id` ASC) ,
  INDEX `profile_field_name_idx` (`name` ASC) ,
  INDEX `profile_field_value_idx` (`value`(255) ASC) ,
  INDEX `profile_field_modified_idx` (`modified` ASC) ,
  INDEX `profile_field_created_idx` (`created` ASC) ,
  CONSTRAINT `fk_user_profile1`
    FOREIGN KEY (`user_profile_id` )
    REFERENCES `user_profile` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_profilefield_permission1`
    FOREIGN KEY (`permission_id` )
    REFERENCES `permission` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci, 
COMMENT = '<double-click to overwrite multiple objects>' ;


-- -----------------------------------------------------
-- Table `forum_has_forum`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `forum_has_forum` (
  `parent_forum_id` INT UNSIGNED NOT NULL ,
  `child_forum_id` INT UNSIGNED NOT NULL ,
  INDEX `fk_parent_forum_id1` (`parent_forum_id` ASC) ,
  INDEX `fk_child_forum_id1` (`child_forum_id` ASC) ,
  CONSTRAINT `fk_parent_forum_id1`
    FOREIGN KEY (`parent_forum_id` )
    REFERENCES `forum` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_child_forum_id1`
    FOREIGN KEY (`child_forum_id` )
    REFERENCES `forum` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci, 
COMMENT = '<double-click to overwrite multiple objects>' ;


-- -----------------------------------------------------
-- Table `forum_param`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `forum_param` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `forum_id` INT UNSIGNED NOT NULL ,
  `name` VARCHAR(255) NOT NULL ,
  `value` TEXT NOT NULL ,
  `created` TIMESTAMP NULL DEFAULT '2011-05-09 00:00:00' ,
  `modified` TIMESTAMP NULL DEFAULT current_timestamp on update current_timestamp ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_forum_fk1` (`forum_id` ASC) ,
  INDEX `forum_param_name_index` (`name` ASC) ,
  INDEX `forum_param_value_index` (`value`(255) ASC) ,
  CONSTRAINT `fk_forum_fk1`
    FOREIGN KEY (`forum_id` )
    REFERENCES `forum` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci, 
COMMENT = '<double-click to overwrite multiple objects>' ;


-- -----------------------------------------------------
-- Table `thread_param`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `thread_param` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `thread_id` INT UNSIGNED NOT NULL ,
  `name` VARCHAR(255) NOT NULL ,
  `value` TEXT NOT NULL ,
  `created` TIMESTAMP NOT NULL DEFAULT '2011-05-09 00:00:00' ,
  `modified` TIMESTAMP NOT NULL DEFAULT current_timestamp on update current_timestamp ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_thread_id1` (`thread_id` ASC) ,
  INDEX `thread_param_name_index` (`name` ASC) ,
  INDEX `thread_param_value_index` (`value`(255) ASC) ,
  CONSTRAINT `fk_thread_id1`
    FOREIGN KEY (`thread_id` )
    REFERENCES `thread` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci, 
COMMENT = '<double-click to overwrite multiple objects>' ;


-- -----------------------------------------------------
-- Table `post_param`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `post_param` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `post_id` INT UNSIGNED NOT NULL ,
  `name` VARCHAR(255) NOT NULL ,
  `value` TEXT NOT NULL ,
  `created` TIMESTAMP NOT NULL DEFAULT '2011-05-09 00:00:00' ,
  `modified` TIMESTAMP NOT NULL DEFAULT current_timestamp on update current_timestamp ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_post_id1` (`post_id` ASC) ,
  INDEX `post_param_name_index` (`name` ASC) ,
  INDEX `post_param_value_index` (`value`(255) ASC) ,
  CONSTRAINT `fk_post_id1`
    FOREIGN KEY (`post_id` )
    REFERENCES `post` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci, 
COMMENT = '<double-click to overwrite multiple objects>' ;


-- -----------------------------------------------------
-- Table `blacklist`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `blacklist` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `pattern` VARCHAR(255) NULL ,
  `type` VARCHAR(45) NULL ,
  `blocked_until` TIMESTAMP NULL ,
  `created` TIMESTAMP NOT NULL DEFAULT '2011-05-09 00:00:00' ,
  `modified` TIMESTAMP NOT NULL DEFAULT current_timestamp on update current_timestamp ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `community_context`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `community_context` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `user_id` INT UNSIGNED NOT NULL ,
  `permission_id` INT UNSIGNED NOT NULL ,
  `name` VARCHAR(45) NOT NULL ,
  `description` VARCHAR(255) NULL ,
  `created` TIMESTAMP NOT NULL DEFAULT '2011-05-09 00:00:00' ,
  `modified` TIMESTAMP NOT NULL DEFAULT current_timestamp on update current_timestamp ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_context_permission1` (`permission_id` ASC) ,
  INDEX `fk_context_user1` (`user_id` ASC) ,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) ,
  CONSTRAINT `fk_context_permission1`
    FOREIGN KEY (`permission_id` )
    REFERENCES `permission` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_context_user1`
    FOREIGN KEY (`user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;


-- -----------------------------------------------------
-- Table `community_context_has_forum`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `community_context_has_forum` (
  `context_id` INT UNSIGNED NOT NULL ,
  `forum_id` INT UNSIGNED NOT NULL ,
  INDEX `fk_context_id1` (`context_id` ASC) ,
  INDEX `fk_forum_id1` (`forum_id` ASC) ,
  CONSTRAINT `fk_context_id1`
    FOREIGN KEY (`context_id` )
    REFERENCES `community_context` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_forum_id1`
    FOREIGN KEY (`forum_id` )
    REFERENCES `forum` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;


-- -----------------------------------------------------
-- Table `user_role`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `user_role` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `mask` INT NOT NULL ,
  `created` TIMESTAMP NOT NULL DEFAULT '2011-05-09 00:00:00' ,
  `modified` TIMESTAMP NOT NULL DEFAULT current_timestamp on update current_timestamp ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci, 
COMMENT = '<double-click to overwrite multiple objects>' ;


-- -----------------------------------------------------
-- Table `user_profile_field_ranking`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `user_profile_field_ranking` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `user_id` INT UNSIGNED NOT NULL ,
  `fieldname` VARCHAR(255) NOT NULL ,
  `value` INT NOT NULL ,
  `rank` INT NOT NULL ,
  `created` TIMESTAMP NOT NULL DEFAULT '2011-05-09 00:00:00' ,
  `modified` TIMESTAMP NOT NULL DEFAULT current_timestamp on update current_timestamp ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `unique_user_fieldname_index` (`user_id` ASC, `fieldname` ASC) ,
  INDEX `field_name_index` (`fieldname` ASC) ,
  INDEX `fk_ranking_user_id` (`user_id` ASC) ,
  CONSTRAINT `fk_ranking_user_id`
    FOREIGN KEY (`user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `thread_param_ranking`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `thread_param_ranking` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `thread_id` INT UNSIGNED NOT NULL ,
  `name` VARCHAR(255) NULL ,
  `value` INT NULL ,
  `rank` INT NULL ,
  `created` TIMESTAMP NULL DEFAULT '2011-05-09 00:00:00' ,
  `modified` TIMESTAMP NULL DEFAULT current_timestamp on update current_timestamp ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_name_idx` (`name` ASC) ,
  UNIQUE INDEX `unique_threadid_name_idx` (`name` ASC, `thread_id` ASC) ,
  INDEX `fk_thread_id` (`thread_id` ASC) ,
  CONSTRAINT `fk_thread_id`
    FOREIGN KEY (`thread_id` )
    REFERENCES `thread` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `notification`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `notification` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `status` VARCHAR(45) NOT NULL ,
  `body` TEXT NOT NULL ,
  `user_field_notification_period` VARCHAR(255) NOT NULL ,
  `user_field_notification` VARCHAR(255) NULL ,
  `user_field_notification_value` VARCHAR(255) NULL ,
  `user_id` INT UNSIGNED NULL ,
  `email` VARCHAR(512) NULL ,
  `created` TIMESTAMP NOT NULL DEFAULT '2011-05-09 00:00:00' ,
  `modified` TIMESTAMP NOT NULL DEFAULT current_timestamp on update current_timestamp ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `post_param_ranking`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `post_param_ranking` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `post_id` INT UNSIGNED NULL ,
  `name` VARCHAR(255) NULL ,
  `value` INT NULL ,
  `rank` INT NULL ,
  `created` TIMESTAMP NULL DEFAULT '2011-05-09 00:00:00' ,
  `modified` TIMESTAMP NULL DEFAULT current_timestamp on update current_timestamp ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_post_id` (`post_id` ASC) ,
  INDEX `fk_name_idx` (`name` ASC) ,
  UNIQUE INDEX `unique_postid_name_idx` (`post_id` ASC, `name` ASC) ,
  CONSTRAINT `fk_post_id0`
    FOREIGN KEY (`post_id` )
    REFERENCES `post` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `boiler_plates`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `boiler_plates` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `title` VARCHAR(255) NULL ,
  `body` LONGTEXT NULL ,
  `type` VARCHAR(45) NOT NULL ,
  `status` ENUM('0','1') NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;


-- -----------------------------------------------------
-- Table `block_param`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `block_param` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `blocked_until` TIMESTAMP NOT NULL ,
  `user_id` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_block_param_user1` (`user_id` ASC) ,
  CONSTRAINT `fk_block_param_user1`
    FOREIGN KEY (`user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci, 
COMMENT = 'Optional database' ;


-- -----------------------------------------------------
-- Table `post_history`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `post_history` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `post_id` INT UNSIGNED NOT NULL ,
  `user_id` INT UNSIGNED NULL ,
  `value` LONGTEXT NULL ,
  `name` VARCHAR(255) NOT NULL ,
  `reason` VARCHAR(255) NULL ,
  `created` TIMESTAMP NOT NULL DEFAULT current_timestamp ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_post_history_post1` (`post_id` ASC) ,
  INDEX `fk_post_history_user1` (`user_id` ASC) ,
  CONSTRAINT `fk_post_history_post1`
    FOREIGN KEY (`post_id` )
    REFERENCES `post` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_post_history_user1`
    FOREIGN KEY (`user_id` )
    REFERENCES `user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;


-- -----------------------------------------------------
-- Table `changeLog`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `changeLog` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `version` INT NOT NULL ,
  `changes` TEXT NOT NULL ,
  `created` TIMESTAMP NOT NULL DEFAULT current_timestamp on update current_timestamp ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
