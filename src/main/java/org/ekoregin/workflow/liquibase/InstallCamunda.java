package org.ekoregin.workflow.liquibase;

import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.DatabaseException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.db.sql.DbSqlSessionFactory;
import org.camunda.bpm.engine.impl.history.HistoryLevel;
import org.camunda.bpm.engine.impl.interceptor.Session;

import java.sql.Connection;

public class InstallCamunda implements CustomTaskChange {

    @Override
    public void execute(Database database) throws CustomChangeException {
        try {
            JdbcConnection jdbcConnection = (JdbcConnection) database.getConnection();
            Connection connection = new NonCloseableConnection(jdbcConnection.getUnderlyingConnection());

            String catalogName = jdbcConnection.getCatalog();

            ProcessEngine engine = null;
            try {
                StandaloneProcessEngineConfiguration configuration = new StandaloneProcessEngineConfiguration();
                configuration.setDatabaseTablePrefix(database.getDefaultSchemaName() + ".");
                configuration.setDatabaseSchema(database.getDefaultSchemaName());
                configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
                configuration.setDataSource(new DatasourceFromConnection(connection));
                configuration.setHistoryLevel(HistoryLevel.HISTORY_LEVEL_FULL);
                configuration.setDbSqlSessionFactory(new DbSqlSessionFactory(true) {
                    @Override
                    public Session openSession() {
                        return super.openSession(connection, catalogName, databaseSchema);
                    }
                });

                engine = configuration.buildProcessEngine();
            } finally {
                if (engine != null) {
                    engine.close();
                }
            }
        } catch (DatabaseException e) {
            throw new CustomChangeException(e);
        }
    }

    @Override
    public String getConfirmationMessage() {
        return "Camunda engine is installed!";
    }

    @Override
    public void setUp() throws SetupException {

    }

    @Override
    public void setFileOpener(ResourceAccessor resourceAccessor) {

    }

    @Override
    public ValidationErrors validate(Database database) {
        return null;
    }
}
