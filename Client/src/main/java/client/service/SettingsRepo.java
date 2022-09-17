package client.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SettingsRepo extends CrudRepository<Settings, Long> {

    Optional<Settings> findByName(String name);

    Boolean existsByName(String name);
}
