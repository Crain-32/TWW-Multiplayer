package crain.model.domain;

import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "item")
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int fromPlayerWorldId;

    private int itemId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public int getFromPlayerWorldId() {
        return fromPlayerWorldId;
    }

    public void setFromPlayerWorldId(int fromPlayer) {
        this.fromPlayerWorldId = fromPlayer;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }


    public Item(@NonNull int source, @NonNull int itemId) {
        this.fromPlayerWorldId = source;
        this.itemId = itemId;
    }
}
