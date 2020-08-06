package wooteco.subway.maps.map.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum FareByDistance {
	TO_TEN(10, (distance) -> 1_250),
	TO_FIFTY(50, (distance) -> 1_250 + 100 * (distance - 10) / 5),
	REST(Integer.MAX_VALUE, (distance) -> 1_250 + 800 + 100 * (distance - 50) / 8);

	private final int maxDistance;
	private final Function<Integer, Integer> policy;

	FareByDistance(int maxDistance, Function<Integer, Integer> policy) {
		this.maxDistance = maxDistance;
		this.policy = policy;
	}

	public static int apply(int distance) {
		return Arrays.stream(FareByDistance.values())
			.filter(fareByDistance -> fareByDistance.isMaxDistanceBiggerThan(distance))
			.findFirst().orElseThrow(IllegalArgumentException::new)
			.policy.apply(distance);
	}

	public boolean isMaxDistanceBiggerThan(int distance) {
		return this.maxDistance > distance;
	}
}
