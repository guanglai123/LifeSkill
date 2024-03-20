package org.guanglai.lifeskill.record;

import org.bukkit.Location;

public record PositionRecord(Location location, String type, String id, Integer amount, Long time, Long coolDown) {
}
