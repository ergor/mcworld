package no.netb.mc.mcworld.datastructs.raw;

import no.netb.mc.mcworld.LazyGet;
import no.netb.mc.mcworld.datastructs.raw.geolocation.GeoArea;

import java.util.List;

public class World {

    private List<WorldSection> worldSections;

    private LazyGet<GeoArea> area = new LazyGet<>(
            () -> worldSections.stream()
                    .map(WorldSection::getArea)
                    .reduce(GeoArea::makeContainer)
                    .orElseThrow(() -> new RuntimeException("error while processing world sections"))
    );

    public World(List<WorldSection> worldSections) {
        this.worldSections = worldSections;
    }

    public GeoArea getArea() {
        return area.get();
    }

    public List<WorldSection> getSections() {
        return worldSections;
    }
}
