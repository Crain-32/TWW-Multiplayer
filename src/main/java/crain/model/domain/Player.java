package crain.model.domain;

import crain.model.dto.PlayerDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int worldId;
    private String userName;
    private boolean connected;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "player_item",
            joinColumns = @JoinColumn(name = "player_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "item_id", referencedColumnName = "id")
    )
    private List<Item> queuedItems = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getWorldId() {
        return worldId;
    }

    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public List<Item> getQueuedItems() {
        return queuedItems;
    }

    public void setQueuedItems(List<Item> queuedItems) {
        this.queuedItems = queuedItems;
    }

    public Player(int worldId) {
        this.worldId = worldId;
        this.userName = null;
        this.connected = false;
    }

    public Player(PlayerDto playerDto) {
        this.worldId = playerDto.getWorldId();
        this.userName = playerDto.getUserName();
        this.connected = false;
    }

}
