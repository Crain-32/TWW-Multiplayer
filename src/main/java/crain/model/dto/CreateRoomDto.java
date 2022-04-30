package crain.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Data
public class CreateRoomDto {

    @NotEmpty(message = "Game Room Name must be Provided!")
    private String gameRoomName;
    @Positive(message = "Player Amount must be Positive!")
    private int playerAmount;
    @Positive(message = "World Amount must be Positive!")
    private int worldAmount;
    @NotEmpty(message = "All Game Rooms need a Password!")
    private String gameRoomPassword;

}
