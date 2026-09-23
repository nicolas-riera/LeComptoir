package com.andrenicolas.src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PointsManagement {
    private static final Path FILE_PATH = Paths.get("data.txt");

    public static int loadPoints() {
        if (!Files.exists(FILE_PATH)) {
            savePoints(0);
            return 0;
        }
        try {
            String content = Files.readString(FILE_PATH).trim();
            if (content.isEmpty()) {
                return 0;
            }
            return Integer.parseInt(content);
        } catch (IOException | NumberFormatException e) {
            return 0;
        }
    }

    public static void savePoints(int points) {
        try {
            Files.writeString(FILE_PATH, String.valueOf(points));
        } catch (IOException e) {
            System.err.println("Cannot save points: " + e.getMessage());
        }
    }

    public static void addPointsFromTotal(double spentAmount) {
        int currentPoints = loadPoints();
        int earnedPoints = (int) spentAmount;
        savePoints(currentPoints + earnedPoints);
    }

    public static void removePointsByNumber(int number) {
        int currentPoints = loadPoints();
        savePoints(currentPoints - (100*number));
    }
}