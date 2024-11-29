package pl.bilskik.citifier.core.docker.parser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.bilskik.citifier.core.docker.entity.Volume;
import pl.bilskik.citifier.core.docker.enumeration.VolumeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class VolumeParser {

    public Map<String, Volume> parseVolumes(Map<String, Map<String, Object>> volumeMap) {
        if(volumeMap == null || volumeMap.isEmpty()) {
            log.info("Skipping volumes! No value provided.");
            return new HashMap<>();
        }

        Map<String, Volume> volumeList = new HashMap<>();
        for(var volumeEntry: volumeMap.entrySet()) {
            Volume volumeObj = new Volume();
            volumeObj.setVolumeName(volumeEntry.getKey());
            volumeObj.setVolumeType(VolumeType.VOLUME);

            volumeList.put(volumeEntry.getKey(), volumeObj);
        }

        return volumeList;
    }


    public List<Volume> parseServiceVolume(Object values, Map<String, Volume> existingVolumes) {
        List<Volume> volumeList = new ArrayList<>();
        if(!(values instanceof List)) {
            log.info("Skipping service volumes provided. No value provided!");
            return volumeList;
        }

        for(var val: (List<?>) values) {
            if(val == null) {
                continue;
            }
            String stringVal = val.toString();
            String[] parts = stringVal.split(":");
            if(parts.length == 2) {
                if(isHostPath(parts[0])) {
                    volumeList.add(createBindMountVolume(parts[0], parts[1]));
                } else if(existingVolumes.containsKey(parts[0])) {
                    volumeList.add(createNamedVolume(parts[0], parts[1]));
                }
            }
        }
        return volumeList;
    }

    private boolean isHostPath(String path) {
        return path.contains("/");
    }

    private Volume createBindMountVolume(String hostPath, String containerPath) {
        Volume volume = new Volume();
        volume.setVolumeType(VolumeType.BIND_MOUNT);
        volume.setHostPath(hostPath);
        volume.setContainerPath(containerPath);
        return volume;
    }

    private Volume createNamedVolume(String volumeName, String containerPath) {
        Volume volume = new Volume();
        volume.setVolumeName(volumeName);
        volume.setVolumeType(VolumeType.VOLUME);
        volume.setContainerPath(containerPath);
        return volume;
    }

}
