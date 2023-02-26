package utils;

import crain.client.service.MemoryAwareService;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class MemoryAwareServiceStub extends MemoryAwareService {
    @Override
    protected String getClassName() {
        return "Stub";
    }
}