SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `miles` ;
CREATE SCHEMA IF NOT EXISTS `miles` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `miles` ;

-- -----------------------------------------------------
-- Table `miles`.`TB_USER`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `miles`.`TB_USER` ;

CREATE  TABLE IF NOT EXISTS `miles`.`TB_USER` (
  `id` VARCHAR(20) NOT NULL COMMENT '�ĺ���' ,
  `password` CHAR(16) NOT NULL COMMENT '��й�ȣ' ,
  `name` VARCHAR(45) NOT NULL COMMENT '����' ,
  `contact` VARCHAR(15) NOT NULL COMMENT '����ó' ,
  `address1` VARCHAR(45) NOT NULL COMMENT '�⺻�ּ�' ,
  `address2` VARCHAR(45) NOT NULL COMMENT '���ּ�' ,
  `join_date` TIMESTAMP NOT NULL COMMENT '��������' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `miles`.`TB_MILES_DEVICE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `miles`.`TB_MILES_DEVICE` ;

CREATE  TABLE IF NOT EXISTS `miles`.`TB_MILES_DEVICE` (
  `idx` INT NOT NULL AUTO_INCREMENT ,
  `id` VARCHAR(45) NOT NULL COMMENT '��� �ĺ���' ,
  `type` INT NOT NULL COMMENT '1 : �����븮����, 2 : ������, 3 : �߻��, ' ,
  `team` INT NOT NULL COMMENT '�׷�' ,
  `status` INT NOT NULL COMMENT '����' ,
  `note` VARCHAR(200) NOT NULL COMMENT '���' ,
  PRIMARY KEY (`idx`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `miles`.`TB_RESERVIST`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `miles`.`TB_RESERVIST` ;

CREATE  TABLE IF NOT EXISTS `miles`.`TB_RESERVIST` (
  `id` VARCHAR(20) NOT NULL COMMENT '�ĺ���' ,
  `name` VARCHAR(45) NOT NULL COMMENT '����' ,
  `team` INT NOT NULL COMMENT '����' ,
  `rank` INT NOT NULL COMMENT '���' ,
  `position` INT NOT NULL COMMENT '��å' ,
  `regiment` VARCHAR(45) NOT NULL COMMENT '�Ҽ�' ,
  `detector_id` INT NOT NULL ,
  `rifle_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  CONSTRAINT `fk_TB_RESERVIST_TB_MILES_DEVICE`
    FOREIGN KEY (`detector_id` )
    REFERENCES `miles`.`TB_MILES_DEVICE` (`idx` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_TB_RESERVIST_TB_MILES_DEVICE1`
    FOREIGN KEY (`rifle_id` )
    REFERENCES `miles`.`TB_MILES_DEVICE` (`idx` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_TB_RESERVIST_TB_MILES_DEVICE_idx` ON `miles`.`TB_RESERVIST` (`detector_id` ASC) ;

CREATE INDEX `fk_TB_RESERVIST_TB_MILES_DEVICE1_idx` ON `miles`.`TB_RESERVIST` (`rifle_id` ASC) ;


-- -----------------------------------------------------
-- Table `miles`.`TB_TRAINING_RESULT`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `miles`.`TB_TRAINING_RESULT` ;

CREATE  TABLE IF NOT EXISTS `miles`.`TB_TRAINING_RESULT` (
  `training_date` TIMESTAMP NOT NULL COMMENT '�Ʒ�����' ,
  `reservist_id` VARCHAR(20) NOT NULL COMMENT '���� �ĺ���' ,
  `issue_count` INT NOT NULL COMMENT '�߱� ź���' ,
  `fire_count` INT NOT NULL COMMENT '��ݹ߼�' ,
  `accuracy_rate` INT NOT NULL COMMENT '���߷�(%)' ,
  `friendly_death` INT NOT NULL COMMENT '�챺���� (���)' ,
  `friendly_serious_injury` INT NOT NULL COMMENT '�챺���� (�߻�)' ,
  `friendly_minor_injury` INT NOT NULL COMMENT '�챺���� (���)' ,
  `enemy_death` INT NOT NULL COMMENT '���� (���)' ,
  `enemy_serious_injury` INT NOT NULL COMMENT '���� (�߻�)' ,
  `enemy_minor_injury` INT NOT NULL COMMENT '���� (���)' ,
  PRIMARY KEY (`reservist_id`, `training_date`) ,
  CONSTRAINT `fk_TB_TRAINING_RESULT_TB_RESERVIST1`
    FOREIGN KEY (`reservist_id` )
    REFERENCES `miles`.`TB_RESERVIST` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `miles`.`TB_BATTLE_CONFIG`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `miles`.`TB_BATTLE_CONFIG` ;

CREATE  TABLE IF NOT EXISTS `miles`.`TB_BATTLE_CONFIG` (
  `persons` INT NOT NULL DEFAULT 10 COMMENT '���� �ο�(��)' ,
  `battle_time` INT NOT NULL DEFAULT 10 COMMENT '�����ð�(��)' ,
  `ammo_count` INT NOT NULL DEFAULT 30 COMMENT 'ź���(��)' ,
  `minor_injury_delay` INT NOT NULL COMMENT '��� ����Ҵ� �ð�(��)' ,
  `serious_injury_delay` INT NOT NULL COMMENT '�߻� ����Ҵ� �ð�(��)' ,
  `death_delay` INT NOT NULL COMMENT '��� ����Ҵ� �ð�(��)' ,
  `auto_time_end_flag` TINYINT(1) NOT NULL DEFAULT true COMMENT '�����ð� ���� �� �ڵ�����' ,
  `auto_annihilation_end_flag` TINYINT(1) NOT NULL DEFAULT true COMMENT '�� ���� �� �ڵ�����' ,
  `auto_report_flag` TINYINT(1) NOT NULL DEFAULT true COMMENT '�������� �� ���� �ڵ����' )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `miles`.`TB_TEAM_SCORE_POLICY`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `miles`.`TB_TEAM_SCORE_POLICY` ;

CREATE  TABLE IF NOT EXISTS `miles`.`TB_TEAM_SCORE_POLICY` (
  `friendly_death` INT NOT NULL DEFAULT -5 COMMENT '�챺 �� ���� - ���' ,
  `friendly_serious_injury` INT NOT NULL DEFAULT -3 COMMENT '�챺 �� ���� - �߻�' ,
  `friendly_minor_injury` INT NOT NULL DEFAULT -1 COMMENT '�챺 �� ���� - ���' ,
  `enemy_death` INT NOT NULL DEFAULT 3 COMMENT '���� - ���' ,
  `enemy_serious_injury` INT NOT NULL DEFAULT 2 COMMENT '���� - �߻�' ,
  `enemy_minor_injury` INT NOT NULL DEFAULT 1 COMMENT '���� - ���' ,
  `survivor_7_above` INT NOT NULL DEFAULT 5 COMMENT '������ ��Ȳ�� ���� ���� �ο� ���� (7 ~ 10��)' ,
  `survivor_3_between_6` INT NOT NULL DEFAULT 3 COMMENT '������ ��Ȳ�� ���� ���� �ο� ���� (3 ~ 6��)' ,
  `survivor_2_below` INT NOT NULL DEFAULT 1 COMMENT '������ ��Ȳ�� ���� ���� �ο� ���� (2�� ����)' )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `miles`.`TB_INDIV_SCORE_POLICY`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `miles`.`TB_INDIV_SCORE_POLICY` ;

CREATE  TABLE IF NOT EXISTS `miles`.`TB_INDIV_SCORE_POLICY` (
  `friendly_death` INT NOT NULL COMMENT '�챺 �� ���� - ���' ,
  `friendly_serious_injury` INT NOT NULL COMMENT '�챺 �� ���� - �߻�' ,
  `friendly_minor_injury` INT NOT NULL COMMENT '�챺 �� ���� - ���' ,
  `enemy_death` INT NOT NULL COMMENT '���� - ���' ,
  `enemy_serious_injury` INT NOT NULL COMMENT '���� - �߻�' ,
  `enemy_minor_injury` INT NOT NULL COMMENT '���� - ���' ,
  `fire_1_between_5` INT NOT NULL COMMENT '��ݹ߼��� ���� ����  (1 ~ 5��)' ,
  `fire_6_between_10` INT NOT NULL COMMENT '��ݹ߼��� ���� ����  (6 ~ 10��)' ,
  `fire_11_between_15` INT NOT NULL COMMENT '��ݹ߼��� ���� ����  (11 ~ 15��)' ,
  `fire_16_between_20` INT NOT NULL COMMENT '��ݹ߼��� ���� ����  (16 ~ 20��)' )
ENGINE = InnoDB;

USE `miles` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
