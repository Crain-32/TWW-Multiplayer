package crain.client.service.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SettingsRepo extends JpaRepository<Settings, Long> {

    Optional<Settings> findByName(String name);

    Boolean existsByName(String name);
}
