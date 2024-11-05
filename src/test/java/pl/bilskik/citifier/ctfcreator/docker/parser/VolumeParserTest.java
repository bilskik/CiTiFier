package pl.bilskik.citifier.ctfcreator.docker.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.bilskik.citifier.ctfcreator.docker.entity.Volume;
import pl.bilskik.citifier.ctfcreator.docker.enumeration.VolumeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VolumeParserTest {

    @InjectMocks
    private VolumeParser volumeParser;

    @Test
    void testParseVolumesWithNullInput() {
        Map<String, Map<String, Object>> volumeMap = null;

        Map<String, Volume> result = volumeParser.parseVolumes(volumeMap);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testParseVolumesWithEmptyMap() {
        Map<String, Map<String, Object>> volumeMap = new HashMap<>();

        Map<String, Volume> result = volumeParser.parseVolumes(volumeMap);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testParseVolumesWithValidInput() {
        Map<String, Map<String, Object>> volumeMap = new HashMap<>();
        volumeMap.put("volume1", new HashMap<>());
        volumeMap.put("volume2", new HashMap<>());

        Map<String, Volume> result = volumeParser.parseVolumes(volumeMap);

        assertEquals(2, result.size(), "Result should contain two volumes");
        assertTrue(result.containsKey("volume1"));
        assertTrue(result.containsKey("volume2"));
        assertEquals(VolumeType.VOLUME, result.get("volume1").getVolumeType());
        assertEquals("volume1", result.get("volume1").getVolumeName());
    }

    @Test
    void testParseServiceVolumeWithNullInput() {
        Object values = null;
        Map<String, Volume> existingVolumes = new HashMap<>();

        List<Volume> result = volumeParser.parseServiceVolume(values, existingVolumes);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testParseServiceVolumeWithInvalidType() {
        Object values = "invalid-type";
        Map<String, Volume> existingVolumes = new HashMap<>();

        List<Volume> result = volumeParser.parseServiceVolume(values, existingVolumes);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testParseServiceVolumeWithEmptyList() {
        Object values = new ArrayList<>();
        Map<String, Volume> existingVolumes = new HashMap<>();

        List<Volume> result = volumeParser.parseServiceVolume(values, existingVolumes);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testParseServiceVolumeWithBindMountPath() {
        Object values = List.of("/host/path:/container/path");
        Map<String, Volume> existingVolumes = new HashMap<>();

        List<Volume> result = volumeParser.parseServiceVolume(values, existingVolumes);

        assertEquals(1, result.size());
        Volume volume = result.getFirst();
        assertEquals(VolumeType.BIND_MOUNT, volume.getVolumeType());
        assertEquals("/host/path", volume.getHostPath());
        assertEquals("/container/path", volume.getContainerPath());
    }

    @Test
    void testParseServiceVolumeWithNamedVolume() {
        Object values = List.of("named_volume:/container/path");
        Map<String, Volume> existingVolumes = new HashMap<>();
        Volume namedVolume = new Volume();
        namedVolume.setVolumeName("named_volume");
        namedVolume.setVolumeType(VolumeType.VOLUME);
        existingVolumes.put("named_volume", namedVolume);

        List<Volume> result = volumeParser.parseServiceVolume(values, existingVolumes);

        assertEquals(1, result.size());
        Volume volume = result.getFirst();
        assertEquals(VolumeType.VOLUME, volume.getVolumeType());
        assertEquals("named_volume", volume.getVolumeName());
        assertEquals("/container/path", volume.getContainerPath());
    }

    @Test
    void testParseServiceVolumeWithUnknownVolume() {
        List<String> values = List.of("unknown_volume:/container/path");
        Map<String, Volume> existingVolumes = new HashMap<>();

        List<Volume> result = volumeParser.parseServiceVolume(values, existingVolumes);

        assertTrue(result.isEmpty());
    }
}
