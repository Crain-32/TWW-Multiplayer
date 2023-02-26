package integration.service;

import crain.client.game.GameInterfaceEvents;
import crain.client.game.interfaces.MemoryAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import utils.MemoryAdapterStub;
import utils.MemoryAwareServiceStub;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@DisplayName("Memory Service Event Validation")
@TestExecutionListeners(
        listeners = {
                DependencyInjectionTestExecutionListener.class
        }
)
@SpringJUnitConfig
@Import(MemoryAwareServiceTest.MemoryAwareServiceTestConfig.class)
@ExtendWith(MockitoExtension.class)
public class MemoryAwareServiceTest {

    @TestConfiguration
    @ComponentScan("utils")
    static class MemoryAwareServiceTestConfig {
    }


    @Autowired
    ApplicationContext applicationContext;

    @MockBean
    MemoryAwareServiceStub memoryAwareServiceStub;

    @Captor
    ArgumentCaptor<GameInterfaceEvents.MemoryHandlerEvent> memoryHandlerEventArgumentCaptor;


    MemoryAdapter stubMemoryAdapter = new MemoryAdapterStub();
    GameInterfaceEvents.MemoryHandlerEvent adapterEvent = new GameInterfaceEvents.MemoryHandlerEvent(stubMemoryAdapter);

    @Test
    void itShould_UpdateTheMemoryService() {
        memoryAwareServiceStub = applicationContext.getBean(MemoryAwareServiceStub.class);

        applicationContext.publishEvent(adapterEvent);

        verify(memoryAwareServiceStub).setMemoryAdapterEvent(memoryHandlerEventArgumentCaptor.capture());
        assertEquals(stubMemoryAdapter, memoryHandlerEventArgumentCaptor.getValue().memoryAdapter());
    }

    @Test
    void itShould_SaveMemoryAdapter() {
        var serviceStub = new MemoryAwareServiceStub();
        serviceStub.setMemoryAdapterEvent(adapterEvent);
        assertEquals(serviceStub.getMemoryAdapter(), stubMemoryAdapter);
    }
}
