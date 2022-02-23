package crain.model.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class GameRoomPlayerName {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String gameRoomName;
    private Date gameRoomCreatedDate;
    private int worldId;

    @PrePersist
    public void setCreatedDate() {
        if (gameRoomCreatedDate != null) {
            gameRoomCreatedDate = new Date(System.currentTimeMillis());
        }
    }
}
