package crain.client.service;

import crain.client.adapters.DolphinAdapter;
import crain.client.adapters.NintendontAdapter;
import crain.client.config.AsyncTestConfig;
import crain.client.config.GameRoomConfig;
import crain.client.config.UtilScanningConfig;
import crain.client.events.CreateMemoryAdapterEvent;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import utils.AsyncExceptionHandlerCache;
import utils.GeneralMessageListener;
import utils.MemoryAwareServiceStub;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Memory Adapter Factory Settings Validation")
@Import({UtilScanningConfig.class, AsyncTestConfig.class})
@SpringBootTest
@ActiveProfiles("test")
public class MemoryAdapterFactoryIntTest {

    MemoryAwareServiceStub memoryAwareServiceStub;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    GeneralMessageListener generalMessageListener;

    @MockBean
    NintendontAdapter nintendontAdapter;

    @MockBean
    DolphinAdapter dolphinAdapter;

    @MockBean
    SettingsService settingsService;

    @MockBean
    GameRoomConfig gameRoomConfig;

    @Autowired
    AsyncExceptionHandlerCache exceptionHandlerCache;


    @BeforeTestClass
    public void getMemoryAwareServiceStub() {
        this.memoryAwareServiceStub = applicationContext.getBean(MemoryAwareServiceStub.class);
    }


    @Test
    void itShould_notThrowAnExceptionOnFailure() {
        applicationContext.publishEvent(CreateMemoryAdapterEvent.builder().build());
        assertFalse(
                exceptionHandlerCache.getThrowableList().stream()
                        .anyMatch(throwable -> {
                            if (throwable instanceof IllegalStateException ex) {
                                return StringUtils.equals(ex.getMessage(), "This Exception should not be reachable!");
                            }
                            return false;
                        })

        );
    }

    @Test
    void itShould_PublishAGeneralMessageOnFailure() throws InterruptedException {
        applicationContext.publishEvent(CreateMemoryAdapterEvent.builder().build());
        Thread.sleep(20); // Since our MemoryFactory Listener is Async, we need to wait for the General Event to get published.
        assertEquals(1, generalMessageListener.getGeneralMessageEventList().size());
        assertTrue(generalMessageListener.getGeneralMessageEventList().get(0).message().contains("Failed to create Memory Adapter!"));

    }
}
