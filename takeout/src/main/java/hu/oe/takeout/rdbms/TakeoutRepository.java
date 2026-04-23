package hu.oe.takeout.rdbms;

import hu.oe.takeout.takeout.generated.entity.Takeout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface TakeoutRepository extends JpaRepository<Takeout, UUID> {

    boolean existsByName(String pName);

    Takeout getById(UUID id);
}
