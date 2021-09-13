package com.employees.configurations;

import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;
import lombok.AllArgsConstructor;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@AllArgsConstructor
public class DBUnitConfig {

    private DataSource dataSource;

    @Bean
    public DatabaseDataSourceConnectionFactoryBean dbUnitDatabaseConnection() {
        DatabaseConfigBean bean = new DatabaseConfigBean();
        bean.setDatatypeFactory(new H2DataTypeFactory());

        DatabaseDataSourceConnectionFactoryBean dbConnectionFactory = new com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean(dataSource);
        dbConnectionFactory.setDatabaseConfig(bean);
        return dbConnectionFactory;
    }
}