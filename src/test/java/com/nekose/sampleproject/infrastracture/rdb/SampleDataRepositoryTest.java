package com.nekose.sampleproject.infrastracture.rdb;

import com.nekose.sampleproject.application.repository.SampleDataRepository;
import com.nekose.sampleproject.config.DatasourceConfig;
import com.nekose.sampleproject.config.property.SampleProperties;
import com.nekose.sampleproject.infrastructure.rdb.SampleTableRepositoryImpl;
import com.nekose.sampleproject.infrastructure.rdb.entity.SampleTableEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles({"local", "test"})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        DataSourceAutoConfiguration.class,
        DatasourceConfig.class,
        JdbcTemplateAutoConfiguration.class,
        SampleTableRepositoryImpl.class,
        SampleProperties.class
}, initializers = ConfigDataApplicationContextInitializer.class)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class
})
@EnableConfigurationProperties
class SampleDataRepositoryTest {
    @Autowired
    private SampleDataRepository sampleDataRepository;

    @Test
    void storeAndGet() {
        var expected = SampleTableEntity.builder()
                .name("testName")
                .value("testValue")
                .build();

        var id = sampleDataRepository.store(expected);
        var actual = sampleDataRepository.findById(id);

        assertTrue(actual.isPresent());
        assertAll(
                () -> {
                    var actualEntity = actual.get();

                    assertAll(
                            () -> assertEquals(expected.getName(), actualEntity.getName()),
                            () -> assertEquals(expected.getValue(), actualEntity.getValue())
                    );
                }
        );
    }
}