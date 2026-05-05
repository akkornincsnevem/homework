package hu.oe.takeout.service;

import hu.oe.takeout.rdbms.TakeoutRepository;
import hu.oe.takeout.takeout.generated.entity.Takeout;
import hu.oe.takeout.takeout.generated.rest.model.IdModel;
import hu.oe.takeout.takeout.generated.rest.model.TakeoutRequest;
import hu.oe.takeout.takeout.generated.rest.model.TakeoutResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TakeoutService {

    private final TakeoutRepository categoryRepository;
    private final ModelMapper modelMapper;

    public List<TakeoutResponse> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(cat -> modelMapper.map(cat, TakeoutResponse.class))
                .toList();
    }

    public TakeoutResponse getById(String id) {
        return categoryRepository.findById(UUID.fromString(id))
                .map(cat -> modelMapper.map(cat, TakeoutResponse.class))
                .orElseThrow(() -> new RuntimeException("Takeout not found"));
    }

    public void delete(String id) {
        UUID uuid = UUID.fromString(id);

        if (!categoryRepository.existsById(uuid)) {
            throw new RuntimeException("Takeout not found");
        }

        categoryRepository.deleteById(uuid);
    }

    public IdModel create(TakeoutRequest request) {
        Takeout saved = categoryRepository.save(
                modelMapper.map(request, Takeout.class)
        );

        return modelMapper.map(saved, IdModel.class);
    }

    public IdModel update(String id, TakeoutRequest request) {
        UUID uuid = UUID.fromString(id);

        Takeout existing = categoryRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("Takeout not found"));

        existing.setName(request.getName());

        Takeout saved = categoryRepository.save(existing);

        return modelMapper.map(saved, IdModel.class);
    }
}
