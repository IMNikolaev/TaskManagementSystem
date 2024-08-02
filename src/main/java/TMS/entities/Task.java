package TMS.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title", nullable = false)
    @NonNull
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    @NonNull
    private Status status;

    @ManyToOne
    @JoinColumn(name = "priority_id", nullable = false)
    @NonNull
    private Priority priority;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    @NonNull
    private User author;

    @ManyToOne
    @JoinColumn(name = "assignee_id", nullable = false)
    @NonNull
    private User assignee;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
