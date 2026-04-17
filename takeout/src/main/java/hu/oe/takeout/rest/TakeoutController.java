package hu.oe.takeout.rest;

import hu.oe.takeout.rdbms.CategoryRepository;
import hu.oe.takeout.rdbms.TakeoutRepository;
import hu.oe.takeout.takeout.generated.rest.api.DefaultApi;
import hu.oe.takeout.takeout.generated.rest.model.*;
import hu.oe.takeout.entity.Category;
import hu.oe.takeout.entity.Takeout;
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
        return null;
    }

    @Override
    public ResponseEntity<Void> categoriesIdDelete(String id) {
        return null;
    }

    @Override
    public ResponseEntity<CategoryResponse> categoriesIdGet(String id) {
        return ResponseEntity.ok(modelMapper.map(categoryRepository.getById(UUID.fromString(id)), CategoryResponse.class));
    }

    @Override
    public ResponseEntity<IdModel> categoriesIdPut(String id, CategoryRequest categoryRequest) {
        return null;
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
        return null;
    }

    @Override
    public ResponseEntity<Void> takeoutIdDelete(String id) {
        return null;
    }

    @Override
    public ResponseEntity<TakeoutResponse> takeoutIdGet(String id) {
        return ResponseEntity.ok(modelMapper.map(takeoutRepository.getById(UUID.fromString(id)), TakeoutResponse.class));
    }

    @Override
    public ResponseEntity<IdModel> takeoutIdPut(String id, TakeoutRequest takeoutRequest) {
        return null;
    }

    @Override
    public ResponseEntity<IdModel> takeoutPost(TakeoutRequest takeoutRequest) {
        return ResponseEntity.ok(
                modelMapper.map(
                        takeoutRepository.save(
                                modelMapper.map(takeoutRequest, Takeout.class)),
                        IdModel.class)
        );
    }
}
