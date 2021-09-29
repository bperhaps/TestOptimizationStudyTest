package com.example.testoptimizationstudytest.config;

import com.example.testoptimizationstudytest.config.db_seperate.DataSourceSelector;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import org.hibernate.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@Component
@ActiveProfiles("test")
public class DatabaseCleaner implements InitializingBean {

    @Autowired
    private SchemaGenerator schemaGenerator;

    @Autowired
    DataSourceSelector dataSourceSelector;

    @Autowired
    private DataSource dataSources;

    private List<String> tableNames;

    public void execute() {
        try {
            dataSourceSelector.toWrite();
            cleanUpDatabase(dataSources.getConnection());
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void afterPropertiesSet() throws SQLException {
        String ddl = schemaGenerator.generate();

        dataSourceSelector.toRead();
        createTables(dataSources.getConnection(), ddl);

        dataSourceSelector.toWrite();
        createTables(dataSources.getConnection(), ddl);

        extractTableNames(dataSources.getConnection());
    }

    public void createTables(Connection conn, String ddl) throws SQLException {
        Statement statement = conn.createStatement();
        statement.executeUpdate(ddl);
    }

    private void extractTableNames(Connection conn) throws SQLException {
        List<String> tableNames = new ArrayList<>();

        ResultSet tables = conn
            .getMetaData()
            .getTables(conn.getCatalog(), null, "%", new String[]{"TABLE"});

        while (tables.next()) {
            tableNames.add(tables.getString("table_name"));
        }

        this.tableNames = tableNames;
    }

    private void cleanUpDatabase(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        statement.executeUpdate("SET REFERENTIAL_INTEGRITY FALSE");

        for (String tableName : tableNames) {
            statement.executeUpdate("TRUNCATE TABLE " + tableName);
            statement.executeUpdate("ALTER TABLE " + tableName + " ALTER COLUMN id RESTART WITH 1");
        }

        statement.executeUpdate("SET REFERENTIAL_INTEGRITY TRUE");
    }
}
