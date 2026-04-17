package hu.oe.takeout.rdbms;

import hu.oe.takeout.entity.Takeout;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface TakeoutRepository extends CrudRepository<Takeout, UUID> {

    boolean existsByName(String pName);

    Takeout getById(UUID id);
}
