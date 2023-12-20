package cz.cvut.fit.tjv.fitnesscenter.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserIdObject {
    @NotNull(message = "userId is required")
    public Long userId;
}
