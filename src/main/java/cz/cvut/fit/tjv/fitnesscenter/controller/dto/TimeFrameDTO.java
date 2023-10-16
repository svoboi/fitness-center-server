package cz.cvut.fit.tjv.fitnesscenter.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TimeFrameDTO {
    @NotNull(message = "timeFrom is required")
    private LocalDateTime timeFrom;
    @NotNull(message = "timeTo is required")
    private LocalDateTime timeTo;
}
