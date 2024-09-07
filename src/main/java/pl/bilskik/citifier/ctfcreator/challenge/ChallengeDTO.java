package pl.bilskik.citifier.ctfcreator.challenge;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.bilskik.citifier.common.validator.LocalDateTimeComparasion;
import pl.bilskik.citifier.common.validator.NumberComparasion;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@NumberComparasion(firstField = "minPoints", secondField = "maxPoints", message = "maxPoints must be greater than minPoints!")
@LocalDateTimeComparasion(firstField = "start", secondField = "finish", message = "Challenge cannot start before finish!")
public class ChallengeDTO {

    @NotNull
    private String name;
    @NotNull
    private String description;
    private ChallengeType challengeType;
    @NotNull
    private String githubLink;
    private LocalDateTime start;
    private LocalDateTime finish;
    private ChallengeStatus status;
    private boolean shareTaskDetailsAtStart;
    private boolean isTeamsEnabled;
    private FlagGenerationMethod flagGenerationMethod;
    @Min(0)
    private Integer minPoints;
    private Integer maxPoints;
    private PointCalculationFunction pointCalculationFunction;
}
