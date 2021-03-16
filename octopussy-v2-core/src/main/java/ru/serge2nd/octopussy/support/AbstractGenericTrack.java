package ru.serge2nd.octopussy.support;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import ru.serge2nd.octopussy.spi.GenericTrack;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

@Getter
public abstract class AbstractGenericTrack implements GenericTrack {
    @NotNull @Pattern(regexp = "[-_\\p{Alnum}]+")
    private final String id;

    @NotNull
    private final Map<@NotBlank String, @NotNull ?> definition;

    public AbstractGenericTrack(@JsonProperty("id")         String id,
                                @JsonProperty("definition") Map<String, ?> definition) {
        this.id = id;
        this.definition = definition != null ? unmodifiableMap(definition) : emptyMap();
    }
}
