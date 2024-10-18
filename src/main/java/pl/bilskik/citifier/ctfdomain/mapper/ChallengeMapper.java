package pl.bilskik.citifier.ctfdomain.mapper;

import org.mapstruct.Mapper;
import pl.bilskik.citifier.ctfcreator.challenge.ChallengeDTO;
import pl.bilskik.citifier.ctfdomain.entity.Challenge;

@Mapper(componentModel = "spring")
public interface ChallengeMapper {

    Challenge toChallenge(ChallengeDTO challengeDTO);

    ChallengeDTO toChallengeDTO(Challenge challenge);
}
