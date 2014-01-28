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
  `id` VARCHAR(20) NOT NULL COMMENT '식별자' ,
  `password` CHAR(16) NOT NULL COMMENT '비밀번호' ,
  `name` VARCHAR(45) NOT NULL COMMENT '성명' ,
  `contact` VARCHAR(15) NOT NULL COMMENT '연락처' ,
  `address1` VARCHAR(45) NOT NULL COMMENT '기본주소' ,
  `address2` VARCHAR(45) NOT NULL COMMENT '상세주소' ,
  `join_date` TIMESTAMP NOT NULL COMMENT '가입일자' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `miles`.`TB_MILES_DEVICE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `miles`.`TB_MILES_DEVICE` ;

CREATE  TABLE IF NOT EXISTS `miles`.`TB_MILES_DEVICE` (
  `idx` INT NOT NULL AUTO_INCREMENT ,
  `id` VARCHAR(45) NOT NULL COMMENT '장비 식별자' ,
  `type` INT NOT NULL COMMENT '1 : 통제용리모컨, 2 : 감지기, 3 : 발사기, ' ,
  `team` INT NOT NULL COMMENT '그룹' ,
  `status` INT NOT NULL COMMENT '상태' ,
  `note` VARCHAR(200) NOT NULL COMMENT '비고' ,
  PRIMARY KEY (`idx`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `miles`.`TB_RESERVIST`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `miles`.`TB_RESERVIST` ;

CREATE  TABLE IF NOT EXISTS `miles`.`TB_RESERVIST` (
  `id` VARCHAR(20) NOT NULL COMMENT '식별자' ,
  `name` VARCHAR(45) NOT NULL COMMENT '성명' ,
  `team` INT NOT NULL COMMENT '조편성' ,
  `rank` INT NOT NULL COMMENT '계급' ,
  `position` INT NOT NULL COMMENT '직책' ,
  `regiment` VARCHAR(45) NOT NULL COMMENT '소속' ,
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
  `training_date` TIMESTAMP NOT NULL COMMENT '훈련일자' ,
  `reservist_id` VARCHAR(20) NOT NULL COMMENT '예비군 식별자' ,
  `issue_count` INT NOT NULL COMMENT '발급 탄약수' ,
  `fire_count` INT NOT NULL COMMENT '사격발수' ,
  `accuracy_rate` INT NOT NULL COMMENT '명중률(%)' ,
  `friendly_death` INT NOT NULL COMMENT '우군피해 (사망)' ,
  `friendly_serious_injury` INT NOT NULL COMMENT '우군피해 (중상)' ,
  `friendly_minor_injury` INT NOT NULL COMMENT '우군피해 (경상)' ,
  `enemy_death` INT NOT NULL COMMENT '전과 (사망)' ,
  `enemy_serious_injury` INT NOT NULL COMMENT '전과 (중상)' ,
  `enemy_minor_injury` INT NOT NULL COMMENT '전과 (경상)' ,
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
  `persons` INT NOT NULL DEFAULT 10 COMMENT '팀당 인원(명)' ,
  `battle_time` INT NOT NULL DEFAULT 10 COMMENT '교전시간(분)' ,
  `ammo_count` INT NOT NULL DEFAULT 30 COMMENT '탄약수(발)' ,
  `minor_injury_delay` INT NOT NULL COMMENT '경상 제어불능 시간(초)' ,
  `serious_injury_delay` INT NOT NULL COMMENT '중상 제어불능 시간(초)' ,
  `death_delay` INT NOT NULL COMMENT '사망 제어불능 시간(초)' ,
  `auto_time_end_flag` TINYINT(1) NOT NULL DEFAULT true COMMENT '교전시간 만료 시 자동종료' ,
  `auto_annihilation_end_flag` TINYINT(1) NOT NULL DEFAULT true COMMENT '팀 전멸 시 자동종료' ,
  `auto_report_flag` TINYINT(1) NOT NULL DEFAULT true COMMENT '교전종류 후 보고서 자동출력' )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `miles`.`TB_TEAM_SCORE_POLICY`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `miles`.`TB_TEAM_SCORE_POLICY` ;

CREATE  TABLE IF NOT EXISTS `miles`.`TB_TEAM_SCORE_POLICY` (
  `friendly_death` INT NOT NULL DEFAULT -5 COMMENT '우군 간 피해 - 사망' ,
  `friendly_serious_injury` INT NOT NULL DEFAULT -3 COMMENT '우군 간 피해 - 중상' ,
  `friendly_minor_injury` INT NOT NULL DEFAULT -1 COMMENT '우군 간 피해 - 경상' ,
  `enemy_death` INT NOT NULL DEFAULT 3 COMMENT '전과 - 사망' ,
  `enemy_serious_injury` INT NOT NULL DEFAULT 2 COMMENT '전과 - 중상' ,
  `enemy_minor_injury` INT NOT NULL DEFAULT 1 COMMENT '전과 - 경상' ,
  `survivor_7_above` INT NOT NULL DEFAULT 5 COMMENT '생존자 현황에 따른 가점 부여 기준 (7 ~ 10명)' ,
  `survivor_3_between_6` INT NOT NULL DEFAULT 3 COMMENT '생존자 현황에 따른 가점 부여 기준 (3 ~ 6명)' ,
  `survivor_2_below` INT NOT NULL DEFAULT 1 COMMENT '생존자 현황에 따른 가점 부여 기준 (2명 이하)' )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `miles`.`TB_INDIV_SCORE_POLICY`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `miles`.`TB_INDIV_SCORE_POLICY` ;

CREATE  TABLE IF NOT EXISTS `miles`.`TB_INDIV_SCORE_POLICY` (
  `friendly_death` INT NOT NULL COMMENT '우군 간 피해 - 사망' ,
  `friendly_serious_injury` INT NOT NULL COMMENT '우군 간 피해 - 중상' ,
  `friendly_minor_injury` INT NOT NULL COMMENT '우군 간 피해 - 경상' ,
  `enemy_death` INT NOT NULL COMMENT '전과 - 사망' ,
  `enemy_serious_injury` INT NOT NULL COMMENT '전과 - 중상' ,
  `enemy_minor_injury` INT NOT NULL COMMENT '전과 - 경상' ,
  `fire_1_between_5` INT NOT NULL COMMENT '사격발수별 가점 기준  (1 ~ 5명)' ,
  `fire_6_between_10` INT NOT NULL COMMENT '사격발수별 가점 기준  (6 ~ 10명)' ,
  `fire_11_between_15` INT NOT NULL COMMENT '사격발수별 가점 기준  (11 ~ 15명)' ,
  `fire_16_between_20` INT NOT NULL COMMENT '사격발수별 가점 기준  (16 ~ 20명)' )
ENGINE = InnoDB;

USE `miles` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
