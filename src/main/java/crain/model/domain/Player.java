package crain.model.domain;

import crain.model.constants.WorldType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String playerName;

    @ManyToOne
    @JoinColumn(name="gameroom_id", nullable = false)
    private GameRoom gameRoom;

    @Enumerated
    private WorldType worldType;
    private Integer worldId;
    private Boolean connected;
}
