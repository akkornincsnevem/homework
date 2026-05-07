package hu.oe.takeout.service;

import hu.oe.takeout.BusinessException;
import hu.oe.takeout.DataValidationException;
import hu.oe.takeout.rdbms.CategoryRepository;
import hu.oe.takeout.takeout.generated.entity.Category;
import hu.oe.takeout.takeout.generated.rest.model.IdModel;
import hu.oe.takeout.takeout.generated.rest.model.CategoryResponse;
import hu.oe.takeout.takeout.generated.rest.model.CategoryRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void getById_shouldReturnCategory(){
        UUID id = UUID.randomUUID();

        Category category = new Category();
        category.setId(id);
        category.setName("Soups");

        CategoryResponse response = new CategoryResponse();
        response.setId(id.toString());
        response.setName("Soups");

        when(categoryRepository.findById(id))
                .thenReturn(Optional.of(category));

        when(modelMapper.map(category, CategoryResponse.class))
                .thenReturn(response);

        CategoryResponse result = categoryService.getById(id.toString());

        assertNotNull(result);
        assertEquals("Soups", result.getName());
        assertEquals(id.toString(), result.getId());

        verify(categoryRepository).findById(id);
        verify(modelMapper).map(category, CategoryResponse.class);
    }

    @Test
    void getById_shouldThrowException_whenCategoryNotFound(){
        UUID id = UUID.randomUUID();

        when(categoryRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                BusinessException.class,
                () -> categoryService.getById(id.toString())
        );

        verify(categoryRepository).findById(id);

        verify(modelMapper, never())
                .map(any(), eq(CategoryResponse.class));
    }

    @Test
    void getAll_shouldReturnAllCategory(){
        Category c1 = new Category();
        c1.setName("Soups");

        Category c2 = new Category();
        c2.setName("Pizzas");

        CategoryResponse r1 = new CategoryResponse();
        r1.setName("Soups");

        CategoryResponse r2 = new CategoryResponse();
        r2.setName("Pizzas");

        when(categoryRepository.findAll())
                .thenReturn(List.of(c1,c2));
        when(modelMapper.map(c1, CategoryResponse.class))
                .thenReturn(r1);
        when(modelMapper.map(c2, CategoryResponse.class))
                .thenReturn(r2);

        List<CategoryResponse> result = categoryService.getAll();

        assertEquals(2, result.size());
        assertEquals("Soups", result.get(0).getName());
        assertEquals("Pizzas", result.get(1).getName());

        verify(categoryRepository).findAll();
    }

    @Test
    void create_ShouldCreateCategory(){
        CategoryRequest request = new CategoryRequest();
        request.setName("Soups");

        Category entity = new Category();
        entity.setName("Soups");

        Category saved = new Category();
        saved.setId(UUID.randomUUID());
        saved.setName("Soups");

        IdModel idModel = new IdModel();
        idModel.setId(saved.getId().toString());

        when(categoryRepository.existsByName("Soups"))
                .thenReturn(false);
        when(modelMapper.map(request, Category.class))
                .thenReturn(entity);
        when(categoryRepository.save(entity))
                .thenReturn(saved);
        when(modelMapper.map(saved, IdModel.class))
                .thenReturn(idModel);

        IdModel result = categoryService.create(request);

        assertNotNull(result);
        assertEquals(saved.getId().toString(), result.getId());

        verify(categoryRepository).save(entity);
    }

    @Test
    void create_shouldThrowException_whenDuplicateName() {

        // Arrange
        CategoryRequest request = new CategoryRequest();
        request.setName("Soups");

        when(categoryRepository.existsByName("Soups"))
                .thenReturn(true);

        // Act + Assert
        assertThrows(
                DataValidationException.class,
                () -> categoryService.create(request)
        );

        verify(categoryRepository, never()).save(any());
    }

    @Test
    void update_shouldUpdateCategory() {
        UUID id = UUID.randomUUID();

        CategoryRequest request = new CategoryRequest();
        request.setName("Updated Soups");

        Category existing = new Category();
        existing.setId(id);
        existing.setName("Soups");

        Category saved = new Category();
        saved.setId(id);
        saved.setName("Updated Soups");

        IdModel response = new IdModel();
        response.setId(id.toString());

        when(categoryRepository.findById(id))
                .thenReturn(Optional.of(existing));
        when(categoryRepository.existsByName("Updated Soups"))
                .thenReturn(false);
        when(categoryRepository.save(existing))
                .thenReturn(saved);
        when(modelMapper.map(saved, IdModel.class))
                .thenReturn(response);

        IdModel result = categoryService.update(id.toString(), request);

        assertNotNull(result);
        assertEquals(id.toString(), result.getId());

        assertEquals("Updated Soups", existing.getName());

        verify(categoryRepository).save(existing);
    }

    @Test
    void update_shouldThrowException_whenDuplicateName() {
        UUID id = UUID.randomUUID();

        CategoryRequest request = new CategoryRequest();
        request.setName("Soups2");

        Category existing = new Category();
        existing.setId(id);
        existing.setName("Soup");

        when(categoryRepository.findById(id))
                .thenReturn(Optional.of(existing));

        when(categoryRepository.existsByName("Soups2"))
                .thenReturn(true);

        assertThrows(
                DataValidationException.class,
                () -> categoryService.update(id.toString(), request)
        );

        verify(categoryRepository, never()).save(any());
    }

    @Test
    void delete_shouldDeleteCategory() {
        UUID id = UUID.randomUUID();

        when(categoryRepository.existsById(id))
                .thenReturn(true);

        categoryService.delete(id.toString());

        verify(categoryRepository).deleteById(id);
    }

    @Test
    void delete_shouldThrowException_whenTakeoutNotFound() {
        UUID id = UUID.randomUUID();

        when(categoryRepository.existsById(id))
                .thenReturn(false);

        assertThrows(
                BusinessException.class,
                () -> categoryService.delete(id.toString())
        );

        verify(categoryRepository, never()).deleteById(any());
    }
}
