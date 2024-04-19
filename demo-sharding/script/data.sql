-- test_db1.user_info_1 definition
CREATE TABLE `user_info_1`
(
    `id`          int(11) NOT NULL,
    `name`        varchar(100) DEFAULT NULL,
    `gender`      varchar(100) DEFAULT NULL,
    `update_time` datetime     DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- test_db1.user_info_2 definition
CREATE TABLE `user_info_2`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `name`        varchar(100) DEFAULT NULL,
    `gender`      varchar(100) DEFAULT NULL,
    `update_time` datetime     DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


-- test_db1.user_account definition
CREATE TABLE `user_account`
(
    `id`       int(11) NOT NULL,
    `user_id`  int(11) DEFAULT NULL,
    `password` varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
