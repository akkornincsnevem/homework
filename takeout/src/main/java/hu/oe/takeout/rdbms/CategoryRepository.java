package hu.oe.takeout.rdbms;

import hu.oe.takeout.entity.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface CategoryRepository extends CrudRepository<Category, UUID> {

    boolean existsByName(String pName);

    Category getById(UUID id);
}
