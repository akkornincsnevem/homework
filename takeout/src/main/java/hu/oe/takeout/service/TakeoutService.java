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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TakeoutService {

    private final TakeoutRepository takeoutRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public List<TakeoutResponse> getAll() {
        log.info("Fetching all takeouts");

        List<TakeoutResponse> result = takeoutRepository.findAll()
                .stream()
                .map(t -> modelMapper.map(t, TakeoutResponse.class))
                .toList();

        log.info("Fetched {} takeouts", result.size());

        return result;
    }

    public TakeoutResponse getById(String id) {
        log.info("Fetching takeout with id: {}", id);
        return takeoutRepository.findById(UUID.fromString(id))
                .map(t->{
                    log.info("Takeout found: {}", id);
                    return modelMapper.map(t,TakeoutResponse.class);
                })
                .orElseThrow(()->{
                    log.warn("Takeout not found: {}", id);
                    return new BusinessException("error.takeout.notfound");
                });
    }

    public void delete(String id) {
        UUID uuid = UUID.fromString(id);

        if (!takeoutRepository.existsById(uuid)) {
            log.warn("Delete failed - takeout not found: {}", id);
            throw new BusinessException("error.takeout.notfound");
        }

        takeoutRepository.deleteById(uuid);
        log.info("Takeout deleted: {}", id);
    }

    public IdModel create(TakeoutRequest request) {
        log.info("Creating takeout: {}", request.getName());

        if (takeoutRepository.existsByName(request.getName())) {
            log.warn("Duplicate takeout name: {}", request.getName());
            throw new DataValidationException("error.takeout.name.exists");
        }
        Takeout entity = modelMapper.map(request, Takeout.class);

        entity.setId(null);

        Category category = categoryRepository
                .findById(UUID.fromString(request.getCategoryId()))
                .orElseThrow(()-> new RuntimeException("Category not found"));

        entity.setCategory(category);

        Takeout saved = takeoutRepository.save(entity);
        log.info("Takeout created with id: {}", saved.getId());
        return modelMapper.map(saved, IdModel.class);
    }

    public Optional<IdModel> update(String id, TakeoutRequest request) {
        log.info("Updating takeout: {}", id);

        UUID uuid = UUID.fromString(id);

        return takeoutRepository.findById(uuid)
                .map(existing -> {
                    if (takeoutRepository.existsByName(request.getName())
                            && !existing.getName().equals(request.getName())) {

                        log.warn("Duplicate name on update: {}", request.getName());
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
                    log.info("Takeout updated: {}", id);
                    return modelMapper.map(saved, IdModel.class);
                });
    }
}
