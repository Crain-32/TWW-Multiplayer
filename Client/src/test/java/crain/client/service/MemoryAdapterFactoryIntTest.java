package crain.client.service;

import crain.client.adapters.DolphinAdapter;
import crain.client.adapters.NintendontAdapter;
import crain.client.config.AsyncTestConfig;
import crain.client.config.UtilScanningConfig;
import crain.client.events.CreateMemoryAdapterEvent;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import utils.AsyncExceptionHandlerCache;
import utils.MemoryAwareServiceStub;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Memory Adapter Factory Settings Validation")
@TestExecutionListeners(
        listeners = {
                DependencyInjectionTestExecutionListener.class
        }
)
@SpringJUnitConfig
@Import({UtilScanningConfig.class, AsyncTestConfig.class})
@ComponentScan("")
@ExtendWith(MockitoExtension.class)
public class MemoryAdapterFactoryIntTest {

    MemoryAwareServiceStub memoryAwareServiceStub;

    @Autowired
    ApplicationContext applicationContext;

    @MockBean
    NintendontAdapter nintendontAdapter;

    @MockBean
    DolphinAdapter dolphinAdapter;

    @MockBean
    SettingsService settingsService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    AsyncExceptionHandlerCache exceptionHandlerCache;


    @BeforeTestClass
    public void getMemoryAwareServiceStub() {
        this.memoryAwareServiceStub = applicationContext.getBean(MemoryAwareServiceStub.class);
    }

    @Test
    void itShould_ThrowExceptionOnFailure() {
        applicationContext.publishEvent(CreateMemoryAdapterEvent.builder().build());
        assertTrue(
                exceptionHandlerCache.getThrowableList().stream()
                        .anyMatch(throwable -> {
                            if (throwable instanceof IllegalStateException ex) {
                                return StringUtils.equals(ex.getMessage(), "This Exception should not be reachable!");
                            }
                            return false;
                        })

        );
    }
}
