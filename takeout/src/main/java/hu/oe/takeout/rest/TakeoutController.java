package hu.oe.takeout.rest;

import hu.oe.takeout.takeout.generated.rest.api.DefaultApi;
import hu.oe.takeout.takeout.generated.rest.model.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("")
public class TakeoutController implements DefaultApi {
    @Override
    public ResponseEntity<List<Takeout>> takeoutsGet(){
        return null;
    }

    @Override
    public ResponseEntity<Void> takeoutsIdDelete(Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<Takeout> takeoutsIdGet(Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<Takeout> takeoutsIdPut(Integer id, Takeout takeout) {
        return null;
    }

    @Override
    public ResponseEntity<Takeout> takeoutsPost(Takeout takeout) {
        return null;
    }
}
