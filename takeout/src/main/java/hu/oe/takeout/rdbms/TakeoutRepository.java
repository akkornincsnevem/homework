package hu.oe.takeout.rdbms;

import hu.oe.yokudlela.takeout.generated.entity.Takeout;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TakeoutRepository extends CrudRepository<Takeout, UUID> {

    boolean existsByName(String pName);

    Takeout getById(UUID id);
}
