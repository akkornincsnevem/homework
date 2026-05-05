package hu.oe.takeout.service;

import hu.oe.takeout.rdbms.CategoryRepository;
import hu.oe.takeout.takeout.generated.entity.Category;
import hu.oe.takeout.takeout.generated.rest.model.CategoryRequest;
import hu.oe.takeout.takeout.generated.rest.model.CategoryResponse;
import hu.oe.takeout.takeout.generated.rest.model.IdModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(cat -> modelMapper.map(cat, CategoryResponse.class))
                .toList();
    }

    public CategoryResponse getById(String id) {
        return categoryRepository.findById(UUID.fromString(id))
                .map(cat -> modelMapper.map(cat, CategoryResponse.class))
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public void delete(String id) {
        UUID uuid = UUID.fromString(id);

        if (!categoryRepository.existsById(uuid)) {
            throw new RuntimeException("Category not found");
        }

        categoryRepository.deleteById(uuid);
    }

    public IdModel create(CategoryRequest request) {
        Category saved = categoryRepository.save(
                modelMapper.map(request, Category.class)
        );

        return modelMapper.map(saved, IdModel.class);
    }

    public Optional<IdModel> update(String id, CategoryRequest request) {
        UUID uuid = UUID.fromString(id);

        return categoryRepository.findById(uuid)
                .map(existing -> {
                    existing.setName(request.getName());

                    Category saved = categoryRepository.save(existing);
                    return modelMapper.map(saved, IdModel.class);
                });
    }
}
