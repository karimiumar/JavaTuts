package com.umar.apps.java8;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static java.util.stream.Collectors.groupingBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapFilterReduceEx {
    private static List<PlayerPoints> playerPoints;
    private static final Logger LOGGER = Logger.getLogger(MapFilterReduceEx.class.getName());

    @BeforeAll
    static void setup() {
        playerPoints = List.of(
                new PlayerPoints("Fred", 200)
                ,new PlayerPoints("Sam", 160)
                ,new PlayerPoints("Lara", 205)
                ,new PlayerPoints("Mac", 167)
                ,new PlayerPoints("Rock", 167)
                ,new PlayerPoints("Lina", 210)
                ,new PlayerPoints("Sheena", 220)
                ,new PlayerPoints("Sara", 200)
                ,new PlayerPoints("Zara", 205)
        );
    }

    @Test
    void given_PlayerPointsList_when_filterOn_highestPoint_then_findsThePlayerWithHighestPoint() {
        PlayerPoints playerWithHighestPoint = 
                playerPoints.stream().map(player -> new PlayerPoints(player.name, getPoints(player)))
                .reduce(new PlayerPoints("", 0),
                        (s1, s2) -> (s1.points > s2.points) ? s1: s2);
        assertEquals(playerWithHighestPoint, new PlayerPoints("Sheena", 220));
        LOGGER.info(playerWithHighestPoint.toString());
    }

    @Test
    void groupPlayerByPoints() {
        Map<Integer, List<PlayerPoints>> pointsWithPlayers =
                playerPoints.stream()
                .collect(groupingBy(PlayerPoints::points));
        LOGGER.info(pointsWithPlayers.toString());
    }

    private static int getPoints(PlayerPoints player) {
        return player.points;
    }

    private static record PlayerPoints(String name, int points) {}
}
