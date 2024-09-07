package pl.bilskik.citifier.ctfdomain.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pl.bilskik.citifier.ctfcreator.challenge.ChallengeDTO;
import pl.bilskik.citifier.ctfcreator.challenge.ChallengeStatus;
import pl.bilskik.citifier.ctfdomain.entity.Challenge;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface ChallengeMapper {

    Challenge toChallenge(ChallengeDTO challengeDTO);

    ChallengeDTO toChallengeDTO(Challenge challenge);

    @AfterMapping
    default void mapChallengeStatus(Challenge challenge, @MappingTarget ChallengeDTO challengeDTO) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = challenge.getStart();
        LocalDateTime finish = challenge.getFinish();

        if(now.isBefore(start)) {
            challengeDTO.setStatus(ChallengeStatus.IN_ACTIVE);
        } else if(now.isAfter(start) && now.isBefore(finish)) {
            challengeDTO.setStatus(ChallengeStatus.IN_PROGRESS);
        } else {
            challengeDTO.setStatus(ChallengeStatus.FINISHED);
        }
    }

}
