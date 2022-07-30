package integration.service;

import crain.repository.PlayerRepo;
import crain.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class PlayerServiceTest {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private PlayerRepo playerRepo;

    
}
