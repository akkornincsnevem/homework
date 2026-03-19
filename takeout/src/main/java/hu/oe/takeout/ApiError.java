package hu.oe.takeout;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author (K)risztian
 */
@Data
@NoArgsConstructor
public class ApiError {
    private String path;
    private String message;
    private Set<String> errors = new LinkedHashSet<>();

    @Builder
    public ApiError(String path, String message) {
        this.path = path;
        this.message = message;
    }


}
