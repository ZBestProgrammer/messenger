package nesalmanov.ru.chatservice.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "ext_objects")
public class ExtObject {

    @Id
    @Column(name = "ext_id")
    private UUID extId;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private GenericType typeId;

}
