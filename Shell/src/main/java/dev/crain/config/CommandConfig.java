package dev.crain.config;

import dev.crain.game.rel.RelSearchService;
import dev.crain.service.MemoryAdapterFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.command.annotation.CommandScan;
import org.springframework.shell.command.annotation.EnableCommand;


@Configuration
@CommandScan
@EnableCommand({RelSearchService.class, MemoryAdapterFactory.class})
public class CommandConfig {
}
