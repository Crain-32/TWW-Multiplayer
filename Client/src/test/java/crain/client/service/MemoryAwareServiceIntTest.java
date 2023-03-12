package crain.client.service;

import crain.client.config.UtilScanningConfig;
import crain.client.exceptions.MissingMemoryAdapterException;
import crain.client.game.GameInterfaceEvents;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import utils.MemoryAdapterStub;
import utils.MemoryAwareServiceStub;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("MemoryAwareService Event Validation")
@TestExecutionListeners(
        listeners = {
                DependencyInjectionTestExecutionListener.class
        }
)
@SpringJUnitConfig
@Import(UtilScanningConfig.class)
@ExtendWith(MockitoExtension.class)
public class MemoryAwareServiceIntTest {

    @Autowired
    ApplicationContext applicationContext;

    @MockBean
    MemoryAwareServiceStub memoryAwareServiceStub;

    @Captor
    ArgumentCaptor<GameInterfaceEvents.MemoryHandlerEvent> memoryHandlerEventArgumentCaptor;


    MemoryAdapterStub stubMemoryAdapter = Mockito.mock(MemoryAdapterStub.class);
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

    @Test
    void itShould_VerifyTheAdapterState() {
        var serviceStub = new MemoryAwareServiceStub();
        assertThrows(MissingMemoryAdapterException.class, serviceStub::verifyHandler);

        when(stubMemoryAdapter.isConnected()).thenReturn(false);
        serviceStub.setMemoryAdapterEvent(adapterEvent);
        assertThrows(IllegalStateException.class, serviceStub::verifyHandler);
        when(stubMemoryAdapter.isConnected()).thenReturn(true);
        assertDoesNotThrow(serviceStub::verifyHandler);
    }
}
