package crain.client.game.handlers;

import crain.client.exceptions.FailedToGiveItemException;
import crain.client.exceptions.FailedToTakeItemException;
import crain.client.game.data.ChartInfo;
import crain.client.game.data.ItemCategory;
import crain.client.game.data.ItemInfo;
import crain.client.game.interfaces.ItemCategoryHandler;
import org.springframework.stereotype.Service;

@Service
public class ChartHandler extends ItemCategoryHandler {

    @Override
    public Boolean supports(ItemCategory itemCategory) {
        return itemCategory == ItemCategory.CHARTS;
    }

    @Override
    public Boolean giveItem(ItemInfo info) throws FailedToGiveItemException {
        try {
            ChartInfo chart = ChartInfo.fromItemInfo(info);
            Integer currFlagValue = memoryAdapter.readInteger(chart.getConsoleAddress());
            Boolean writeResult = memoryAdapter.writeInteger(chart.getConsoleAddress(), (currFlagValue | chart.getMask()));
            if (!writeResult) {
                throw new FailedToGiveItemException("Failed to mark the chart, current flag: " + currFlagValue, info);
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
        try {
            ChartInfo chart = ChartInfo.fromItemInfo(info);
            Integer currFlagValue = memoryAdapter.readInteger(chart.getConsoleAddress());
            Integer mask = 0xFFFFFFFF;
            Boolean writeResult = memoryAdapter.writeInteger(chart.getConsoleAddress(), (currFlagValue & (mask ^ chart.getMask())));
            if (!writeResult) {
                throw new FailedToTakeItemException("Failed to remove the chart, current flag: " + currFlagValue, info);
            }
            return true;
        } catch (FailedToTakeItemException e) {
            throw e;
        } catch (Exception e) {
            throw new FailedToTakeItemException(e.getMessage(), info, e);
        }
    }

    @Override
    protected String getClassName() {
        return this.getClass().getSimpleName();
    }
}
