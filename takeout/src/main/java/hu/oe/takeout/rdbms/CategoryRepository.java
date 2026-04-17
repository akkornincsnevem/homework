package hu.oe.takeout.rdbms;

import hu.oe.takeout.entity.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    boolean existsByName(String pName);

    Category getById(UUID id);
}
