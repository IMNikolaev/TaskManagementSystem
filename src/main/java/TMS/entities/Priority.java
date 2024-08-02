package TMS.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "priority")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Priority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false)
    @NonNull
    private String name;
}
