package dev.crain.game.rel;

import dev.crain.exceptions.memory.MemoryHandlerException;
import dev.crain.game.actor.ActorService;
import dev.crain.game.config.WindWakerConfig;
import dev.crain.game.util.CollectionUtil;
import dev.crain.service.HeapService;
import dev.crain.service.MemoryAwareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Command
public class RelSearchService extends MemoryAwareService {

    private final HeapService heapService;
    private final WindWakerConfig windWakerConfig;
    private final ActorService actorService;

    public RelSearchService(HeapService heapService, WindWakerConfig windWakerConfig, ActorService actorService) {
        this.heapService = heapService;
        this.windWakerConfig = windWakerConfig;
        this.actorService = actorService;
    }

    @Command(command = "rel-location")
    public String findRelLocation(@Option(longNames = "relName") String relName) throws MemoryHandlerException {
        var fileNameMap = CollectionUtil.swapMap(heapService.getRelFileNameMap()); // String -> ActorID now
        var loc = CollectionUtil.extractFromMap(
                fileNameMap, relName, (value) ->
                {
                    try {
                        return CollectionUtil.extractFromMap(
                                heapService.getRelLocations(), value, (location) -> location);
                    } catch (MemoryHandlerException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        var toParse = loc == null ? -1 : loc;
        return "%s found at 0x%s".formatted(relName, Integer.toHexString(toParse));
    }

    @Command(command = "rel-func")
    public String findRelFunction(String relName, String symbolName) throws MemoryHandlerException {
        var fileNameMap = CollectionUtil.swapMap(heapService.getRelFileNameMap()); // String -> ActorID now
        var loc = CollectionUtil.extractFromMap(
                fileNameMap, relName, (value) ->
                {
                    try {
                        return CollectionUtil.extractFromMap(
                                heapService.getRelLocations(), value, (location) -> location);
                    } catch (MemoryHandlerException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        var adapter = getMemoryAdapter();
        Integer prolog = adapter.readInteger(loc + 0x34);
        int funcLoc = prolog + windWakerConfig.getRels().get(relName).symbolOffset().get(symbolName);
        return "The Symbol %s for the Rel %s should be at %s, prolog at %s".formatted(symbolName, relName, Integer.toHexString(funcLoc), Integer.toHexString(prolog));
    }

    @Override
    protected String getClassName() {
        return this.getClass().getSimpleName();
    }
}
