package st.netb.mc.mcworld.datastructs.raw;

import st.netb.mc.mcworld.LazyGet;
import st.netb.mc.mcworld.datastructs.raw.coordinates.GeoArea;

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
