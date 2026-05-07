package hu.oe.takeout.service;

import hu.oe.takeout.BusinessException;
import hu.oe.takeout.DataValidationException;
import hu.oe.takeout.rdbms.CategoryRepository;
import hu.oe.takeout.rdbms.TakeoutRepository;
import hu.oe.takeout.takeout.generated.entity.Category;
import hu.oe.takeout.takeout.generated.entity.Takeout;
import hu.oe.takeout.takeout.generated.rest.model.IdModel;
import hu.oe.takeout.takeout.generated.rest.model.TakeoutRequest;
import hu.oe.takeout.takeout.generated.rest.model.TakeoutResponse;
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
class TakeoutServiceTest {

    @Mock
    private TakeoutRepository takeoutRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TakeoutService takeoutService;

    @Test
    void getById_shouldReturnTakeout() {
        UUID id = UUID.randomUUID();

        Takeout takeout = new Takeout();
        takeout.setId(id);
        takeout.setName("Pizza");

        TakeoutResponse response = new TakeoutResponse();
        response.setId(id.toString());
        response.setName("Pizza");

        when(takeoutRepository.findById(id))
                .thenReturn(Optional.of(takeout));

        when(modelMapper.map(takeout, TakeoutResponse.class))
                .thenReturn(response);

        TakeoutResponse result = takeoutService.getById(id.toString());

        assertNotNull(result);
        assertEquals("Pizza", result.getName());
        assertEquals(id.toString(), result.getId());

        verify(takeoutRepository).findById(id);
        verify(modelMapper).map(takeout, TakeoutResponse.class);
    }

    @Test
    void getById_shouldThrowException_whenTakeoutNotFound() {
        UUID id = UUID.randomUUID();

        when(takeoutRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                BusinessException.class,
                () -> takeoutService.getById(id.toString())
        );

        verify(takeoutRepository).findById(id);

        verify(modelMapper, never())
                .map(any(), eq(TakeoutResponse.class));
    }

    @Test
    void getAll_shouldReturnAllTakeouts() {

        Takeout t1 = new Takeout();
        t1.setName("Pizza");

        Takeout t2 = new Takeout();
        t2.setName("Burger");

        TakeoutResponse r1 = new TakeoutResponse();
        r1.setName("Pizza");

        TakeoutResponse r2 = new TakeoutResponse();
        r2.setName("Burger");

        when(takeoutRepository.findAll())
                .thenReturn(List.of(t1, t2));

        when(modelMapper.map(t1, TakeoutResponse.class))
                .thenReturn(r1);

        when(modelMapper.map(t2, TakeoutResponse.class))
                .thenReturn(r2);

        List<TakeoutResponse> result = takeoutService.getAll();

        assertEquals(2, result.size());
        assertEquals("Pizza", result.get(0).getName());
        assertEquals("Burger", result.get(1).getName());

        verify(takeoutRepository).findAll();
    }

    @Test
    void create_shouldCreateTakeout() {
        UUID categoryId = UUID.randomUUID();

        TakeoutRequest request = new TakeoutRequest();
        request.setName("Pizza");
        request.setPrice(2500);
        request.setCategoryId(categoryId.toString());

        Category category = new Category();
        category.setId(categoryId);

        Takeout entity = new Takeout();
        entity.setName("Pizza");

        Takeout saved = new Takeout();
        saved.setId(UUID.randomUUID());
        saved.setName("Pizza");

        IdModel idModel = new IdModel();
        idModel.setId(saved.getId().toString());

        when(takeoutRepository.existsByName("Pizza"))
                .thenReturn(false);

        when(modelMapper.map(request, Takeout.class))
                .thenReturn(entity);

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        when(takeoutRepository.save(entity))
                .thenReturn(saved);

        when(modelMapper.map(saved, IdModel.class))
                .thenReturn(idModel);

        IdModel result = takeoutService.create(request);

        assertNotNull(result);
        assertEquals(saved.getId().toString(), result.getId());

        verify(takeoutRepository).save(entity);
    }

    @Test
    void create_shouldThrowException_whenDuplicateName() {
        TakeoutRequest request = new TakeoutRequest();
        request.setName("Pizza");

        when(takeoutRepository.existsByName("Pizza"))
                .thenReturn(true);

        assertThrows(
                DataValidationException.class,
                () -> takeoutService.create(request)
        );

        verify(takeoutRepository, never()).save(any());
    }

    @Test
    void update_shouldUpdateTakeout() {
        UUID id = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        TakeoutRequest request = new TakeoutRequest();
        request.setName("Updated Pizza");
        request.setPrice(3000);
        request.setCategoryId(categoryId.toString());

        Category category = new Category();
        category.setId(categoryId);

        Takeout existing = new Takeout();
        existing.setId(id);
        existing.setName("Pizza");

        Takeout saved = new Takeout();
        saved.setId(id);
        saved.setName("Updated Pizza");

        IdModel response = new IdModel();
        response.setId(id.toString());

        when(takeoutRepository.findById(id))
                .thenReturn(Optional.of(existing));

        when(takeoutRepository.existsByName("Updated Pizza"))
                .thenReturn(false);

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        when(takeoutRepository.save(existing))
                .thenReturn(saved);

        when(modelMapper.map(saved, IdModel.class))
                .thenReturn(response);

        IdModel result = takeoutService.update(id.toString(), request);

        assertNotNull(result);
        assertEquals(id.toString(), result.getId());

        assertEquals("Updated Pizza", existing.getName());
        assertEquals(3000, existing.getPrice());

        verify(takeoutRepository).save(existing);
    }

    @Test
    void update_shouldThrowException_whenDuplicateName() {
        UUID id = UUID.randomUUID();

        TakeoutRequest request = new TakeoutRequest();
        request.setName("Pizza2");

        Takeout existing = new Takeout();
        existing.setId(id);
        existing.setName("Pizza");

        when(takeoutRepository.findById(id))
                .thenReturn(Optional.of(existing));

        when(takeoutRepository.existsByName("Pizza2"))
                .thenReturn(true);

        assertThrows(
                DataValidationException.class,
                () -> takeoutService.update(id.toString(), request)
        );

        verify(takeoutRepository, never()).save(any());
    }

    @Test
    void delete_shouldDeleteTakeout() {
        UUID id = UUID.randomUUID();

        when(takeoutRepository.existsById(id))
                .thenReturn(true);

        takeoutService.delete(id.toString());

        verify(takeoutRepository).deleteById(id);
    }

    @Test
    void delete_shouldThrowException_whenTakeoutNotFound() {
        UUID id = UUID.randomUUID();

        when(takeoutRepository.existsById(id))
                .thenReturn(false);

        assertThrows(
                BusinessException.class,
                () -> takeoutService.delete(id.toString())
        );

        verify(takeoutRepository, never()).deleteById(any());
    }
}