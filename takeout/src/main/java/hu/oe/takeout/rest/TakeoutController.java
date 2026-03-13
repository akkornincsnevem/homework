package hu.oe.takeout.rest;

import hu.oe.takeout.takeout.generated.rest.api.DefaultApi;
import hu.oe.takeout.takeout.generated.rest.model.Categories;
import hu.oe.takeout.takeout.generated.rest.model.Takeout;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("")
public class TakeoutController implements DefaultApi {

    @Override
    public ResponseEntity<List<Categories>> categoriesGet() {
        return null;
    }

    @Override
    public ResponseEntity<Void> categoriesIdDelete(Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<Categories> categoriesIdGet(Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<Categories> categoriesIdPut(Integer id, Categories categories) {
        return null;
    }

    @Override
    public ResponseEntity<Categories> categoriesPost(Categories categories) {
        return null;
    }

    @Override
    public ResponseEntity<List<Takeout>> takeoutGet() {
        return null;
    }

    @Override
    public ResponseEntity<Void> takeoutIdDelete(Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<Takeout> takeoutIdGet(Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<Takeout> takeoutIdPut(Integer id, Takeout takeout) {
        return null;
    }

    @Override
    public ResponseEntity<Takeout> takeoutPost(Takeout takeout) {
        return null;
    }
}
