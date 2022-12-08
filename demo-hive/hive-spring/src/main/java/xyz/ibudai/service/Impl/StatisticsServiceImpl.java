package xyz.ibudai.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import xyz.ibudai.service.StatisticsService;

public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private JdbcTemplate hiveDruidTemplate;

}
