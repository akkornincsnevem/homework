package hu.oe.takeout.rest;

import hu.oe.takeout.rdbms.CategoryRepository;
import hu.oe.takeout.rdbms.TakeoutRepository;
import hu.oe.takeout.service.CategoryService;
import hu.oe.takeout.service.TakeoutService;
import hu.oe.takeout.takeout.generated.rest.api.DefaultApi;
import hu.oe.takeout.takeout.generated.entity.Category;
import hu.oe.takeout.takeout.generated.entity.Takeout;
import hu.oe.takeout.takeout.generated.rest.model.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class TakeoutController implements DefaultApi {
    private final CategoryService categoryService;
    private final TakeoutService takeoutService;


    @Override
    public ResponseEntity<List<CategoryResponse>> categoriesGet() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @Override
    public ResponseEntity<Void> categoriesIdDelete(String id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<CategoryResponse> categoriesIdGet(String id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @Override
    public ResponseEntity<IdModel> categoriesIdPut(String id, CategoryRequest categoryRequest) {
        return categoryService.update(id, categoryRequest)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    

    @Override
    public ResponseEntity<IdModel> categoriesPost(CategoryRequest categoryRequest) {
        return ResponseEntity.status(201).body(categoryService.create(categoryRequest));
    }

    @Override
    public ResponseEntity<List<TakeoutResponse>> takeoutGet() {
        return ResponseEntity.ok(takeoutService.getAll());
    }

    @Override
    public ResponseEntity<Void> takeoutIdDelete(String id) {
        takeoutService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<TakeoutResponse> takeoutIdGet(String id) {
        return ResponseEntity.ok(takeoutService.getById(id));
    }

    @Override
    public ResponseEntity<IdModel> takeoutIdPut(String id, TakeoutRequest takeoutRequest) {
        return takeoutService.update(id, takeoutRequest)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<IdModel> takeoutPost(TakeoutRequest takeoutRequest) {
        return ResponseEntity.status(201).body(takeoutService.create(takeoutRequest));
    }
}
