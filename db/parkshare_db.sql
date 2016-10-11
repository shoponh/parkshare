SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

SHOW WARNINGS;
DROP SCHEMA IF EXISTS `users` ;
CREATE SCHEMA IF NOT EXISTS `users` DEFAULT CHARACTER SET utf8 ;
SHOW WARNINGS;
USE `users` ;

-- -----------------------------------------------------
-- Table `address`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `address` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `address` (
  `address1` VARCHAR(100) NOT NULL ,
  `address2` VARCHAR(100) NULL DEFAULT NULL ,
  `city` VARCHAR(100) NOT NULL ,
  `state` VARCHAR(2) NOT NULL ,
  `zip` VARCHAR(15) NOT NULL ,
  `country` VARCHAR(100) NOT NULL ,
  `person_id` MEDIUMTEXT NULL ,
  `modified_by` VARCHAR(100) NULL ,
  `last_modified` DATETIME NULL ,
  `latitude` GEOMETRY NULL ,
  `longitude` GEOMETRY NULL )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `billing_master`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `billing_master` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `billing_master` (
  `id` BIGINT NULL DEFAULT NULL ,
  `person_id` BIGINT NULL DEFAULT NULL ,
  `paypal_account` VARCHAR(50) NULL DEFAULT NULL ,
  `paypal_password` VARCHAR(25) NOT NULL ,
  `billing_address_id` BIGINT NULL DEFAULT NULL ,
  `modified_by` VARCHAR(100) NULL ,
  `last_modified` DATETIME NULL )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `booking`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `booking` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `booking` (
  `id` BIGINT NOT NULL ,
  `booking_from` DATETIME NOT NULL ,
  `booking_to` DATETIME NOT NULL ,
  `modified_by` VARCHAR(100) NULL ,
  `last_modified` DATETIME NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `booking_transaction`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `booking_transaction` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `booking_transaction` (
  `transaction_id` MEDIUMTEXT NOT NULL ,
  `booking_id` MEDIUMTEXT NOT NULL ,
  `producer_id` MEDIUMTEXT NOT NULL ,
  `consumer_id` MEDIUMTEXT NOT NULL )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `dimension`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `dimension` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `dimension` (
  `dimension` VARCHAR(100) NOT NULL ,
  `item_id` MEDIUMTEXT NULL DEFAULT NULL )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `item` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `item` (
  `id` BIGINT NOT NULL ,
  `special_instruction` VARCHAR(100) NULL DEFAULT NULL ,
  `address_id` BIGINT NOT NULL ,
  `modified_by` VARCHAR(100) NOT NULL ,
  `last_modified` DATETIME NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `item_picture`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `item_picture` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `item_picture` (
  `item_id` MEDIUMTEXT NOT NULL ,
  `picture_id` MEDIUMTEXT NOT NULL ,
  `picture` BLOB NOT NULL )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `item_rate_availability`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `item_rate_availability` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `item_rate_availability` (
  `item_id` BIGINT NOT NULL ,
  `daily_rate` DECIMAL(2,0) NULL DEFAULT NULL ,
  `monthly_rate` DECIMAL(2,0) NULL DEFAULT NULL ,
  `hourly_rate` DECIMAL(2,0) NULL DEFAULT NULL ,
  `modified_by` VARCHAR(100) NULL ,
  `last_modified` DATETIME NULL ,
  PRIMARY KEY (`item_id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `person`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `person` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `person` (
  `name` VARCHAR(50) NOT NULL ,
  `phone` CHAR(18) NULL ,
  `email` VARCHAR(100) NOT NULL ,
  `modified_by` VARCHAR(100) NULL ,
  `last_modified` DATETIME NULL ,
  PRIMARY KEY (`email`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `person_profile`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `person_profile` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `person_profile` (
  `person_id` BIGINT NOT NULL ,
  `password` VARCHAR(50) NOT NULL ,
  PRIMARY KEY (`person_id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SHOW WARNINGS;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
