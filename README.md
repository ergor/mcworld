# mcworld

A program for generating Minecraft maps from real world data.

Currently there is support for generating contigous terrain from multiple GeoTIFF heightmaps, 
where location data is defined in the UTM coordinate system.

The goal is to support multiple input data formats and multiple coordinate systems, as well as
additional data layers for things like streets/roads, biomes (forests, beaches, ...) etc.

## Usage

Run program:

```
java -jar mcworld.jar <input data directory> [flags]
```

where `<input data directory>` is the path to the folder containing terrain data,
and `flags` can be any of:

- `-f` fast render (re-render using stored intermediate data)

mcworld rendering will produce output in the follow locations:

- `tmp/`: raw, intermediate data is stored here.
- `preview/`: a preview image of the rendered map is stored here.
- `anvil/`: the map itself is stored here. 

To use the rendered map in Minecraft, copy and paste files from `anvil` into:
```
<minacraft_game_directory>/saves/<your_save>/region/
```

## Compilation

This is a maven-based IntelliJ project. Open it in IntelliJ, or build using maven directly; use `mvn package` 
to build a JAR file, or use `mvn compile` to only build .class files.

### Credits / resources

[Querz/NBT](https://github.com/Querz/NBT)

[Norwegian Mapping Authority height data](https://www.hoydedata.no)
