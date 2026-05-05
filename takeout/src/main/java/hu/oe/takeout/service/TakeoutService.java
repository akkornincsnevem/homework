package hu.oe.takeout.service;

import hu.oe.takeout.DataValidationException;
import hu.oe.takeout.rdbms.CategoryRepository;
import hu.oe.takeout.rdbms.TakeoutRepository;
import hu.oe.takeout.takeout.generated.entity.Category;
import hu.oe.takeout.takeout.generated.entity.Takeout;
import hu.oe.takeout.takeout.generated.rest.model.IdModel;
import hu.oe.takeout.takeout.generated.rest.model.TakeoutRequest;
import hu.oe.takeout.takeout.generated.rest.model.TakeoutResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TakeoutService {

    private final TakeoutRepository takeoutRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public List<TakeoutResponse> getAll() {
        return takeoutRepository.findAll()
                .stream()
                .map(cat -> modelMapper.map(cat, TakeoutResponse.class))
                .toList();
    }

    public TakeoutResponse getById(String id) {
        return takeoutRepository.findById(UUID.fromString(id))
                .map(cat -> modelMapper.map(cat, TakeoutResponse.class))
                .orElseThrow(() -> new RuntimeException("Takeout not found"));
    }

    public void delete(String id) {
        UUID uuid = UUID.fromString(id);

        if (!takeoutRepository.existsById(uuid)) {
            throw new RuntimeException("Takeout not found");
        }

        takeoutRepository.deleteById(uuid);
    }

    public IdModel create(TakeoutRequest request) {
        if (takeoutRepository.existsByName(request.getName())) {
            throw new DataValidationException("error.takeout.name.exists");
        }
        Takeout entity = modelMapper.map(request, Takeout.class);

        entity.setId(null);

        Category category = categoryRepository
                .findById(UUID.fromString(request.getCategoryId()))
                .orElseThrow(()-> new RuntimeException("Category not found"));

        entity.setCategory(category);

        Takeout saved = takeoutRepository.save(entity);

        return modelMapper.map(saved, IdModel.class);
    }

    public Optional<IdModel> update(String id, TakeoutRequest request) {
        UUID uuid = UUID.fromString(id);

        return takeoutRepository.findById(uuid)
                .map(existing -> {
                    if (takeoutRepository.existsByName(request.getName())
                            && !existing.getName().equals(request.getName())) {
                        throw new DataValidationException("error.takeout.name.exists");
                    }
                    existing.setName(request.getName());
                    existing.setPrice(request.getPrice());

                    if (request.getCategoryId() != null) {
                        Category category = categoryRepository
                                .findById(UUID.fromString(request.getCategoryId()))
                                .orElseThrow();
                        existing.setCategory(category);
                    }

                    Takeout saved = takeoutRepository.save(existing);
                    return modelMapper.map(saved, IdModel.class);
                });
    }
}
