package wolox.training.repositories;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wolox.training.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findFirstByName(String name);

    public Page<User> findByBirthdayBetweenAndNameContainingIgnoreCase(LocalDate startDate, LocalDate endDate,
            String name, Pageable pageable);

    @Query("SELECT u FROM User u WHERE (cast(:startDate as date) is null or u.birthday >= :startDate) and "
            + "(cast(:endDate as date) is null or u.birthday <= :endDate) and "
            + "(:name is null or lower(u.name) like lower(concat('%', concat(text(:name), '%'))))")
    public Page<User> findByBirthdayBetweenAndNameContainingIgnoreCaseNameParams(
            @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("name") String name, Pageable pageable);
}
