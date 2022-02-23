package crain.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventDto {

    private int stageId;
    private int mainOffset;
    private int secondaryOffset;
    private int sourceWorldId;


}
