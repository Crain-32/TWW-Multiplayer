package crain.model.dto;

import lombok.Data;

@Data
public class ConnectionRequestDto {

    private PlayerDto playerDto;
    private String gameRoomName;
    private String gameRoomPassword;

}
