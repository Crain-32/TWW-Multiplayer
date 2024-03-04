package dev.crain.game.config;

import dev.crain.game.actor.ActorInfo;
import dev.crain.game.rel.RelInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties("wind-waker")
public class WindWakerConfig {

    private List<ActorInfo> actors;
    private Map<String, RelInfo> rels;

}
