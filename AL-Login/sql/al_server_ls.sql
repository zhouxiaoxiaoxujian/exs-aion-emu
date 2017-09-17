CREATE TABLE tasks
(
  id INT(5) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  task_type ENUM('SHUTDOWN', 'RESTART', 'CLEAN_ACCOUNTS') NOT NULL,
  trigger_type ENUM('FIXED_IN_TIME', 'AFTER_RESTART') NOT NULL,
  exec_param TEXT,
  trigger_param TEXT NOT NULL
);
CREATE TABLE account_data
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name VARCHAR(45) NOT NULL,
  password VARCHAR(65) NOT NULL,
  activated TINYINT(1) DEFAULT '1' NOT NULL,
  access_level TINYINT(3) DEFAULT '0' NOT NULL,
  membership TINYINT(3) DEFAULT '0' NOT NULL,
  old_membership TINYINT(3) DEFAULT '0' NOT NULL,
  last_server TINYINT(3) DEFAULT '-1' NOT NULL,
  last_ip VARCHAR(20),
  last_mac VARCHAR(20) DEFAULT 'xx-xx-xx-xx-xx-xx' NOT NULL,
  ip_force VARCHAR(20),
  expire DATE,
  toll BIGINT(13) DEFAULT '0' NOT NULL,
  email VARCHAR(50) DEFAULT '' NOT NULL,
  question VARCHAR(50) DEFAULT '' NOT NULL,
  answer VARCHAR(50) DEFAULT '' NOT NULL,
  balance FLOAT DEFAULT '0' NOT NULL
);
CREATE TABLE account_rewards
(
  uniqId INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  accountId INT(11) NOT NULL,
  added VARCHAR(70) DEFAULT '' NOT NULL,
  points DECIMAL(20) DEFAULT '0' NOT NULL,
  received VARCHAR(70) DEFAULT '0' NOT NULL,
  rewarded TINYINT(1) DEFAULT '0' NOT NULL
);
CREATE TABLE account_time
(
  account_id INT(11) PRIMARY KEY NOT NULL,
  last_active TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  expiration_time TIMESTAMP,
  session_duration INT(10) DEFAULT '0',
  accumulated_online INT(10) DEFAULT '0',
  accumulated_rest INT(10) DEFAULT '0',
  penalty_end TIMESTAMP
);
CREATE TABLE banned_ip
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  mask VARCHAR(45) NOT NULL,
  time_end TIMESTAMP
);
CREATE TABLE gameservers
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  mask VARCHAR(45) NOT NULL,
  password VARCHAR(65) NOT NULL
);
CREATE TABLE player_transfers
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  source_server TINYINT(3) NOT NULL,
  target_server TINYINT(3) NOT NULL,
  source_account_id INT(11) NOT NULL,
  target_account_id INT(11) NOT NULL,
  player_id INT(11) NOT NULL,
  status TINYINT(1) DEFAULT '0' NOT NULL,
  time_added VARCHAR(100),
  time_performed VARCHAR(100),
  time_done VARCHAR(100),
  comment TEXT
);
CREATE TABLE banned_mac
(
  uniId INT(10) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  address VARCHAR(20) NOT NULL,
  time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  details VARCHAR(255) DEFAULT '' NOT NULL
);
CREATE UNIQUE INDEX name ON account_data (name);
ALTER TABLE account_rewards ADD FOREIGN KEY (accountId) REFERENCES account_data (id) ON DELETE CASCADE;
CREATE INDEX FK_account_rewards ON account_rewards (accountId);
ALTER TABLE account_time ADD FOREIGN KEY (account_id) REFERENCES account_data (id) ON DELETE CASCADE;
CREATE UNIQUE INDEX mask ON banned_ip (mask);