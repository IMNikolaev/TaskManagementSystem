package TMS.repositiories;

import TMS.entities.Task;
import TMS.entities.enums.PriorityEnum;
import TMS.entities.enums.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findAllByStatusEnumAndPriorityEnum(StatusEnum statusEnum, PriorityEnum priorityEnum, Pageable pageable);
    Page<Task> findAllByStatusEnum(StatusEnum statusEnum, Pageable pageable);
    Page<Task> findAllByPriorityEnum(PriorityEnum priorityEnum, Pageable pageable);

}
