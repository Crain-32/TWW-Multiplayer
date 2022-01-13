package crain.model.dto;

import crain.model.domain.GameRoom;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.StringUtils;

@Data
@NoArgsConstructor
public class SetUpDto {

    private int worldAmount;
    private String gameRoomName;
    private boolean extraValidation;

    @ToString.Exclude
    private String gameRoomPassword;


    public static SetUpDto fromGameRoom(GameRoom gameRoom) {
        SetUpDto setUpDto = new SetUpDto();
        setUpDto.worldAmount = gameRoom.getPlayers().size();
        setUpDto.gameRoomName = gameRoom.getGameRoomName();
        setUpDto.extraValidation = StringUtils.hasText(gameRoom.getGameRoomPassword());
        return setUpDto;
    }
}
