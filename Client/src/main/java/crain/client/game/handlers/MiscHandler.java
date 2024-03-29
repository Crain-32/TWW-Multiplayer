package crain.client.game.handlers;

import crain.client.exceptions.FailedToGiveItemException;
import crain.client.exceptions.FailedToTakeItemException;
import crain.client.game.data.ItemCategory;
import crain.client.game.data.ItemInfo;
import crain.client.game.data.MemoryConstants;
import crain.client.game.data.StoryFlagInfo;
import crain.client.game.interfaces.ItemCategoryHandler;
import crain.client.game.util.MemoryEditorUtil;
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

    private Boolean giveMagicMeter() throws FailedToGiveItemException {
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

    private Boolean givePowerBracelets() {
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

    private Boolean giveHerosCharm() {
        return MemoryEditorUtil.toggleBit(memoryAdapter, StoryFlagInfo.HEROS_CHARM.getMemoryAddress(), StoryFlagInfo.HEROS_CHARM.getBitIndex(), true);
    }

    private Boolean giveHurricaneSpin() {
        return MemoryEditorUtil.toggleBit(memoryAdapter, StoryFlagInfo.HURRICANE_SPIN.getMemoryAddress(), StoryFlagInfo.HURRICANE_SPIN.getBitIndex(), true);
    }

    private Boolean giveHeartPiece(Byte amount) {
        return memoryAdapter.writeByte(MemoryConstants.incrementHeartPieces, amount);
    }
}
