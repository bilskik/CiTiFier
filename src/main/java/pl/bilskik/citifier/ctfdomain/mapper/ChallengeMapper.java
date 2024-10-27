package pl.bilskik.citifier.ctfdomain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.bilskik.citifier.ctfdomain.dto.ChallengeDTO;
import pl.bilskik.citifier.ctfdomain.entity.Challenge;

@Mapper(componentModel = "spring")
public interface ChallengeMapper {

    @Mapping(target = "challengeAppData", source = "challengeAppDataDTO")
    Challenge toChallenge(ChallengeDTO challengeDTO);

    @Mapping(target = "challengeAppDataDTO", source = "challengeAppData")
    ChallengeDTO toChallengeDTO(Challenge challenge);
}
