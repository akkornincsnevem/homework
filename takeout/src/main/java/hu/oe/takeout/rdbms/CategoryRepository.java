package hu.oe.takeout.rdbms;

import hu.oe.yokudlela.takeout.generated.entity.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CategoryRepository extends CrudRepository<Category, UUID> {

    boolean existsByName(String pName);

    Category getById(UUID id);
}
