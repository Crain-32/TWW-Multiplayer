package dev.crain.game.actor;

import dev.crain.game.config.WindWakerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Optional;
import java.util.TreeMap;

@Slf4j
@Service
public class ActorService {

    private final TreeMap<Integer, ActorInfo> ACTOR_ID_INFO_MAP;
    private final TreeMap<String, ActorInfo> ACTOR_NAME_INFO_MAP;

    public ActorService(WindWakerConfig windWakerConfig) {
        ACTOR_ID_INFO_MAP = new TreeMap<>(Integer::compare);
        ACTOR_NAME_INFO_MAP = new TreeMap<>(String::compareTo);
        if (!CollectionUtils.isEmpty(windWakerConfig.getActors())) {
            windWakerConfig.getActors().forEach(val -> {
                ACTOR_ID_INFO_MAP.put(val.actorId(), val);
                val.actorNames().forEach(name -> ACTOR_NAME_INFO_MAP.put(name, val));
            });
        }
    }

    public Optional<ActorInfo> getInfoById(Integer id) {
        return Optional.ofNullable(ACTOR_ID_INFO_MAP.get(id));
    }

    public Optional<ActorInfo> getInfoByName(String name) {
        return Optional.ofNullable(ACTOR_NAME_INFO_MAP.get(name));
    }
}
