package hu.oe.takeout.takeout.generated.rest.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
* TakeoutResponse
*/

@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.10.0")
public class TakeoutResponse implements Serializable {

    private static final long serialVersionUID = 1L;



            private String id;
    public static final String FIELD_ID="id";

        @hu.oe.takeout.validation.NameExists(message="error.takeout.name.exists")


            private String name;
    public static final String FIELD_NAME="name";



            private Integer price;
    public static final String FIELD_PRICE="price";



            private String categoryId;
    public static final String FIELD_CATEGORYID="categoryId";



            private String description;
    public static final String FIELD_DESCRIPTION="description";

    }

