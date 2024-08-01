package org.ekoregin.workflow.liquibase;

import liquibase.Scope;
import liquibase.change.custom.CustomTaskChange;
import liquibase.changelog.ChangeSet;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FilenameUtils;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.history.HistoryLevel;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;

@Getter
@Setter
public class DeployProcess implements CustomTaskChange {

    private ResourceAccessor accessor;
    private String file;

    @Override
    public void execute(Database database) {
        JdbcConnection jdbcConnection = (JdbcConnection) database.getConnection();
        Connection connection = new NonCloseableConnection(jdbcConnection.getUnderlyingConnection());

        ProcessEngine engine = null;
        try {
            StandaloneProcessEngineConfiguration configuration = new StandaloneProcessEngineConfiguration();
            configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE);
            configuration.setDataSource(new DatasourceFromConnection(connection));
            configuration.setHistoryLevel(HistoryLevel.HISTORY_LEVEL_FULL);
            engine = configuration.buildProcessEngine();

            String fileName = FilenameUtils.getName(file);
            RepositoryService repositoryService = engine.getRepositoryService();

            ChangeSet changeSet = Scope.getCurrentScope().get("changeSet", ChangeSet.class);
//            try(InputStream inputStream = accessor.openStream(changeSet.getFilePath(), file)) {
//                repositoryService.createDeployment().addInputStream(fileName, inputStream);
//            }
            try(InputStream inputStream = new FileInputStream(ResourceUtils.getFile("classpath:" + file))) {
                repositoryService.createDeployment().addInputStream(fileName, inputStream);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (engine != null) {
                engine.close();
            }
        }
    }

    @Override
    public String getConfirmationMessage() {
        return "";
    }

    @Override
    public void setUp() throws SetupException {

    }

    @Override
    public void setFileOpener(ResourceAccessor resourceAccessor) {
        this.accessor = resourceAccessor;
    }

    @Override
    public ValidationErrors validate(Database database) {
        return null;
    }
}
