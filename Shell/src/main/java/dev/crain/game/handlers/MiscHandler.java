package dev.crain.game.handlers;

import dev.crain.exceptions.FailedToGiveItemException;
import dev.crain.exceptions.FailedToTakeItemException;
import dev.crain.exceptions.memory.MemoryHandlerException;
import dev.crain.game.data.ItemCategory;
import dev.crain.game.data.ItemInfo;
import dev.crain.game.data.MemoryConstants;
import dev.crain.game.data.StoryFlagInfo;
import dev.crain.game.interfaces.ItemCategoryHandler;
import dev.crain.game.util.MemoryEditorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class MiscHandler extends ItemCategoryHandler {
    @Override
    public Boolean supports(ItemCategory itemCategory) {
        return itemCategory == ItemCategory.MISC;
    }

    @Override
    public Boolean giveItem(ItemInfo info) throws FailedToGiveItemException {
        try {
            Boolean result = switch (info) {
                case PIECE_OF_HEART, PIECE_OF_HEART_ALT -> giveHeartPiece((byte) 1);
                case HEART_CONTAINER -> giveHeartPiece((byte) 4);
                case POWER_BRACELETS -> givePowerBracelets();
                case HURRICANE_SPIN -> giveHurricaneSpin();
                case HEROS_CHARM -> giveHerosCharm();
                case MAGIC_METER_UPGRADE -> giveMagicMeter();
                default -> throw new IllegalArgumentException("Invalid Item passed to MiscHandler: " + info);
            };
            if (!result) {
                throw new FailedToGiveItemException("Failed to give Misc Item", info);
            }
            return true;
        } catch (FailedToGiveItemException e) {
            throw e;
        } catch (Exception e) {
            throw new FailedToGiveItemException(e.getMessage(), info, e);
        }
    }

    @Override
    public Boolean takeItem(ItemInfo info) throws FailedToTakeItemException {
        return true; // We won't worry about remove Misc items, since they don't impact logic.
    }

    @Override
    protected String getClassName() {
        return this.getClass().getSimpleName();
    }

    private Boolean giveMagicMeter() throws FailedToGiveItemException, MemoryHandlerException {
        var memoryAdapter = getMemoryAdapter();
        boolean writeResult = memoryAdapter.writeByte(MemoryConstants.maxMagicAddress, (byte) 0x20);
        if (!writeResult) {
            throw new FailedToGiveItemException("Failed to write Max value", ItemInfo.MAGIC_METER_UPGRADE);
        }
        writeResult = memoryAdapter.writeByte(MemoryConstants.currMagicAddress, (byte) 0x20);
        if (!writeResult) {
            log.debug("Failed to set Max Magic");
        }
        return true;
    }

    private Boolean givePowerBracelets() throws MemoryHandlerException {
        var memoryAdapter = getMemoryAdapter();
        boolean writeResult = memoryAdapter.writeByte(MemoryConstants.powerBraceletAddress, ItemInfo.POWER_BRACELETS.getItemId());
        if (!writeResult) {
            throw new FailedToGiveItemException("Failed to write Inventory value", ItemInfo.POWER_BRACELETS);
        }
        writeResult = memoryAdapter.writeByte(MemoryConstants.powerBraceletFlagAddress, (byte) 0x01);
        if (!writeResult) {
            throw new FailedToGiveItemException("Failed to write ownership value", ItemInfo.POWER_BRACELETS);
        }
        return true;
    }

    private Boolean giveHerosCharm() throws MemoryHandlerException {
        var memoryAdapter = getMemoryAdapter();
        return MemoryEditorUtil.toggleBit(memoryAdapter, StoryFlagInfo.HEROS_CHARM.getMemoryAddress(), StoryFlagInfo.HEROS_CHARM.getBitIndex(), true);
    }

    private Boolean giveHurricaneSpin() throws MemoryHandlerException {
        var memoryAdapter = getMemoryAdapter();
        return MemoryEditorUtil.toggleBit(memoryAdapter, StoryFlagInfo.HURRICANE_SPIN.getMemoryAddress(), StoryFlagInfo.HURRICANE_SPIN.getBitIndex(), true);
    }

    private Boolean giveHeartPiece(Byte amount) throws MemoryHandlerException {
        var memoryAdapter = getMemoryAdapter();
        return memoryAdapter.writeByte(MemoryConstants.incrementHeartPieces, amount);
    }
}
