-- test_nifi.tb_file definition
CREATE TABLE `tb_file`
(
    `id`   varchar(100) NOT NULL,
    `file` blob,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- test_nifi.user_info definition
CREATE TABLE `user_info`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `name`        varchar(100) DEFAULT NULL,
    `update_time` datetime     DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;