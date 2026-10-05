package foo.starred.nebulune;

import foo.starred.nebulune.modules.impl.general.TrevorEspTargets;
import foo.starred.nebulune.modules.impl.general.TrevorEspTargets.Candidate;
import foo.starred.nebulune.modules.impl.general.TrevorEspTargets.Species;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class TrevorEspTargetsTest {
    @Test public void acceptsTrapperLabelsWithFormattingLevelsAndHealth() {
        assertEquals(Species.Cow, TrevorEspTargets.nametagSpecies("§c[Lv1] §fTrackable Cow §a100/100❤", "Trackable"));
        assertEquals(Species.Horse, TrevorEspTargets.nametagSpecies("ELUSIVE Horse 20,000❤", "Elusive"));
        assertEquals(Species.Rabbit, TrevorEspTargets.nametagSpecies("[Lv 1] Endangered Rabbit 5k/5k❤", "Endangered"));
        for (Species species : Species.values())
            assertEquals(species, TrevorEspTargets.nametagSpecies("Undetected " + species.name(), "Undetected"));
    }

    @Test public void rejectsWrongRarityDeadLabelsPetsAndUnrelatedNames() {
        for (String text : List.of("Cow", "Trevor", "[NPC] Elusive Cow", "[Lvl 100] Elusive Cow",
                "Elusive Zombie", "Find an Elusive Cow", "Elusive Cow minion", "Elusive Cow 0/10k❤", "Elusive Cow 0k❤"))
            assertNull(text, TrevorEspTargets.nametagSpecies(text, "Elusive"));
        assertNull(TrevorEspTargets.nametagSpecies("Trackable Cow 100❤", "Elusive"));
    }

    @Test public void animalAndItsHologramProduceExactlyOneMarker() {
        var body = candidate("animal", Species.Cow, 0, 60, 0);
        var tag = candidate("nametag", Species.Cow, 0.1, 61.8, 0.1);
        assertEquals(List.of(body), TrevorEspTargets.select(List.of(body), List.of(tag)));
        // Multiple label entities attached to the same animal still cannot add tracers.
        var duplicate = candidate("second nametag", Species.Cow, 0, 62.1, 0);
        assertEquals(List.of(body), TrevorEspTargets.select(List.of(body), List.of(tag, duplicate)));
    }

    @Test public void fallbackHandsOverToAnimalAndBackWithoutKeepingStaleMarkers() {
        var body = candidate("animal", Species.Sheep, 3, 70, 4);
        var tag = candidate("nametag", Species.Sheep, 3, 71, 4);
        assertEquals(List.of(tag), TrevorEspTargets.select(List.of(), List.of(tag)));
        assertEquals(List.of(body), TrevorEspTargets.select(List.of(body), List.of(tag)));
        assertEquals(List.of(tag), TrevorEspTargets.select(List.of(), List.of(tag)));
        assertTrue(TrevorEspTargets.select(List.of(), List.of()).isEmpty());
    }

    @Test public void separateAnimalsAndDifferentSpeciesKeepTheirOwnMarkers() {
        var cow = candidate("cow", Species.Cow, 0, 60, 0);
        var distantCow = candidate("distant cow", Species.Cow, 5, 61, 0);
        var rabbit = candidate("rabbit", Species.Rabbit, 0, 61, 0);
        var higherCow = candidate("cow above", Species.Cow, 0, 70, 0);
        assertEquals(List.of(cow, distantCow, rabbit, higherCow),
                TrevorEspTargets.select(List.of(cow), List.of(distantCow, rabbit, higherCow)));
        var cow2 = candidate("nearby second cow", Species.Cow, 1, 60, 0);
        var tag = candidate("cow label", Species.Cow, 0, 61, 0);
        assertEquals(List.of(cow, cow2), TrevorEspTargets.select(List.of(cow, cow2), List.of(tag)));
    }

    private static Candidate<String> candidate(String id, Species species, double x, double y, double z) {
        return new Candidate<>(id, species, x, y, z);
    }
}
