package hu.oe.takeout.rest;

import hu.oe.takeout.rdbms.CategoryRepository;
import hu.oe.takeout.rdbms.TakeoutRepository;
import hu.oe.takeout.takeout.generated.rest.api.DefaultApi;
import hu.oe.takeout.takeout.generated.entity.Category;
import hu.oe.takeout.takeout.generated.entity.Takeout;
import hu.oe.takeout.takeout.generated.rest.model.*;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("")
public class TakeoutController implements DefaultApi {
    private final TakeoutRepository takeoutRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public TakeoutController(TakeoutRepository takeoutRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.takeoutRepository = takeoutRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public ResponseEntity<List<CategoryResponse>> categoriesGet() {
        List<CategoryResponse> result = categoryRepository.findAll()
                .stream()
                .map(cat -> modelMapper.map(cat, CategoryResponse.class))
                .toList();

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> categoriesIdDelete(String id) {
        UUID uuid = UUID.fromString(id);

        if (!categoryRepository.existsById(uuid)) {
            return ResponseEntity.notFound().build();
        }

        categoryRepository.deleteById(uuid);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<CategoryResponse> categoriesIdGet(String id) {
        return ResponseEntity.ok(modelMapper.map(categoryRepository.getById(UUID.fromString(id)), CategoryResponse.class));
    }

    @Override
    public ResponseEntity<IdModel> categoriesIdPut(String id, CategoryRequest categoryRequest) {
        UUID uuid = UUID.fromString(id);

        return categoryRepository.findById(uuid)
                .map(existing -> {
                    existing.setName(categoryRequest.getName());

                    Category saved = categoryRepository.save(existing);

                    return ResponseEntity.ok(modelMapper.map(saved, IdModel.class));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    

    @Override
    public ResponseEntity<IdModel> categoriesPost(CategoryRequest categoryRequest) {
        return ResponseEntity.ok(
                modelMapper.map(
                        categoryRepository.save(
                                modelMapper.map(categoryRequest, Category.class)),
                        IdModel.class)
        );
    }

    @Override
    public ResponseEntity<List<TakeoutResponse>> takeoutGet() {
        List<TakeoutResponse> result = takeoutRepository.findAll()
                .stream()
                .map(t -> modelMapper.map(t, TakeoutResponse.class))
                .toList();

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> takeoutIdDelete(String id) {
        UUID uuid = UUID.fromString(id);

        if (!takeoutRepository.existsById(uuid)) {
            return ResponseEntity.notFound().build();
        }

        takeoutRepository.deleteById(uuid);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<TakeoutResponse> takeoutIdGet(String id) {
        return ResponseEntity.ok(modelMapper.map(takeoutRepository.getById(UUID.fromString(id)), TakeoutResponse.class));
    }

    @Override
    public ResponseEntity<IdModel> takeoutIdPut(String id, TakeoutRequest takeoutRequest) {
        UUID uuid = UUID.fromString(id);

        return takeoutRepository.findById(uuid)
                .map(existing -> {
                    existing.setName(takeoutRequest.getName());
                    existing.setPrice(takeoutRequest.getPrice());

                    // ⚠️ category handling (important)
                    if (takeoutRequest.getCategoryId() != null) {
                        Category category = categoryRepository
                                .findById(UUID.fromString(takeoutRequest.getCategoryId()))
                                .orElseThrow();

                        existing.setCategory(category);
                    }

                    Takeout saved = takeoutRepository.save(existing);

                    return ResponseEntity.ok(modelMapper.map(saved, IdModel.class));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<IdModel> takeoutPost(TakeoutRequest takeoutRequest) {

        Takeout entity = modelMapper.map(takeoutRequest, Takeout.class);

        // 🔥 CRITICAL FIX
        entity.setId(null);  // force INSERT instead of UPDATE

        Category category = categoryRepository
                .findById(UUID.fromString(takeoutRequest.getCategoryId()))
                .orElseThrow(() -> new RuntimeException("Category not found"));

        entity.setCategory(category);

        Takeout saved = takeoutRepository.save(entity);

        return ResponseEntity.status(201)
                .body(modelMapper.map(saved, IdModel.class));
    }
}
