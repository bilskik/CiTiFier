package pl.bilskik.citifier.ctfcreator.challengedetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.ctfcreator.kubernetes.data.K8sResourceContext;
import pl.bilskik.citifier.ctfcreator.kubernetes.service.K8sResourceManager;
import pl.bilskik.citifier.ctfdomain.dto.ChallengeDTO;
import pl.bilskik.citifier.ctfdomain.entity.enumeration.ChallengeStatus;
import pl.bilskik.citifier.ctfdomain.service.ChallengeDao;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengeDetailsService {

    private final ChallengeDao challengeDao;
    private final K8sResourceManager k8SResourceManager;
    private final ChallengeDeployerPreparator challengeDeployerPreparator;

    public ChallengeDTO findChallengeById(Long challengeId) {
        if(challengeId == null) {
            return new ChallengeDTO(); // ?
        }

        return challengeDao.findById(challengeId);
    }

    public void createAndStartApp(ChallengeDTO challengeDTO) {
        log.info("Deploy and start app!");
        K8sResourceContext context =
                challengeDeployerPreparator.parseDockerComposeAndBuildImage(challengeDTO, false);
        k8SResourceManager.deployAndStart(context);
        updateStatus(challengeDTO, ChallengeStatus.RUNNING, challengeDTO.getChallengeId());
    }

    public void startApp(ChallengeDTO challengeDTO) {
        log.info("Starting app!");
        K8sResourceContext context =
                challengeDeployerPreparator.parseDockerComposeAndBuildImage(challengeDTO, true);
        k8SResourceManager.deployAndStart(context);
        updateStatus(challengeDTO, ChallengeStatus.RUNNING, challengeDTO.getChallengeId());
    }

    public void stopApp(ChallengeDTO challengeDTO) {
        log.info("Stopping app!");
        k8SResourceManager.stop(challengeDTO.getChallengeAppDataDTO().getNamespace());
        updateStatus(challengeDTO, ChallengeStatus.STOPPED, challengeDTO.getChallengeId());
    }

    public void deleteApp(ChallengeDTO challengeDTO) {
        log.info("Deleting app!");
        k8SResourceManager.delete(challengeDTO.getChallengeAppDataDTO().getNamespace());
        updateStatus(challengeDTO, ChallengeStatus.REMOVED, challengeDTO.getChallengeId());
    }

    public void updateStatus(ChallengeDTO challengeDTO, ChallengeStatus status, Long challengeId) {
        challengeDao.updateChallengeStatus(status, challengeId);
        challengeDTO.setStatus(status); //update status in current response on frontend
    }

}
