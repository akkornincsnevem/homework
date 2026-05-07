package hu.oe.takeout.service;

import ch.qos.logback.classic.Logger;
import hu.oe.takeout.BusinessException;
import hu.oe.takeout.DataValidationException;
import hu.oe.takeout.rdbms.CategoryRepository;
import hu.oe.takeout.takeout.generated.entity.Category;
import hu.oe.takeout.takeout.generated.rest.model.CategoryRequest;
import hu.oe.takeout.takeout.generated.rest.model.CategoryResponse;
import hu.oe.takeout.takeout.generated.rest.model.IdModel;
import hu.oe.takeout.takeout.generated.rest.model.TakeoutResponse;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Cacheable("categories")
    public List<CategoryResponse> getAll() {
        log.info("Fetching all categories");

        List<CategoryResponse> result = categoryRepository.findAll()
                .stream()
                .map(cat -> modelMapper.map(cat, CategoryResponse.class))
                .toList();

        log.info("Fetched {} categories", result.size());

        return result;
    }

    @Cacheable(value = "category", key = "#id")
    public CategoryResponse getById(String id) {
        log.info("Fetching Category with id: {}", id);
        return categoryRepository.findById(UUID.fromString(id))
                .map(cat->{
                    log.info("Category found: {}", id);
                    return modelMapper.map(cat, CategoryResponse.class);
                })
                .orElseThrow(()->{
                    log.warn("Category not found: {}", id);
                    return new BusinessException("error.category.notfound");
                });
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "category", key = "#id"),
                    @CacheEvict(value = "categories", allEntries = true)
            }
    )
    public void delete(String id) {
        UUID uuid = UUID.fromString(id);

        if (!categoryRepository.existsById(uuid)) {
            log.warn("Delete failed - Category not found: {}", id);
            throw new BusinessException("error.category.notfound");
        }

        categoryRepository.deleteById(uuid);
        log.info("Category deleted: {}", id);
    }

    @CacheEvict(value = "categories", allEntries = true)
    public IdModel create(CategoryRequest request) {
        log.info("Creating category: {}", request.getName());
        if (categoryRepository.existsByName(request.getName())) {
            log.warn("Duplicate Category name: {}", request.getName());
            throw new DataValidationException("error.category.name.exists");
        }
        Category saved = categoryRepository.save(
                modelMapper.map(request, Category.class)
        );
        log.info("Category created with id: {}", saved.getId());
        return modelMapper.map(saved, IdModel.class);
    }

    @CachePut(value = "category", key = "#id")
    @CacheEvict(value = "categories", allEntries = true)
    public IdModel update(String id, CategoryRequest request) {
        log.info("Updating category: {}", id);
        UUID uuid = UUID.fromString(id);

        return categoryRepository.findById(uuid)
                .map(existing -> {

                    // ✔ prevent duplicate name (but allow same entity)
                    if (categoryRepository.existsByName(request.getName())
                            && !existing.getName().equals(request.getName())) {
                        log.warn("Duplicate name on update: {}", request.getName());
                        throw new DataValidationException("error.category.name.exists");
                    }

                    existing.setName(request.getName());

                    Category saved = categoryRepository.save(existing);
                    log.info("Category updated: {}", id);
                    return modelMapper.map(saved, IdModel.class);
                }).orElseThrow(() -> new BusinessException("error.category.notfound"));
    }
}
