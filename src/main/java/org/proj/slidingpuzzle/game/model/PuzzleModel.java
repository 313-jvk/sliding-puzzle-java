package org.proj.slidingpuzzle.game.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PuzzleModel {
    private List<Integer> tiles;
    private final int size;
    private int moves;
    private long startTime;
    private int score;
    private List<Integer> initialState;


    public PuzzleModel(int rows, int cols) {
        this.size = rows * cols;
        this.tiles = new ArrayList<>(size);
        this.moves = 0;
        this.score = 0;
        initTiles();
        shuffle();
    }

    public void incrementMoves() {
        moves++;
        score += 100;
    }


    private void initTiles() {
        tiles.clear();
        for (int i = 0; i < size; i++) {
            tiles.add(i);
        }
    }

    public void loadCustomConfiguration(String config) {
        tiles = new ArrayList<>();
        for (int i = 0; i < config.length(); i++) {
            int value = Character.getNumericValue(config.charAt(i));
            tiles.add(value == 0 ? size - 1 : value - 1);
        }
        initialState = new ArrayList<>(tiles);
        resetGame();

        // Debug
        System.out.println("Config chargée: " + tiles);
    }



    public void shuffle() {
        initTiles();
        Collections.shuffle(tiles);
        initialState = new ArrayList<>(tiles);
        startTime = System.currentTimeMillis();
        moves = 0;
        score = 0;
    }


    private int bestScore = 0;// var qui stocke le best score

    public void updateBestScore(int newScore) {
        if (newScore > bestScore) {
            bestScore = newScore;
        }
    }

    public int getBestScore() {
        return bestScore;
    }

    public void rearrangePuzzle() {
        if (initialState != null) {
            tiles.clear();
            tiles.addAll(initialState);
        }
        moves = 0;
        score = 0;
    }

    public int getTile(int index) {
        return tiles.get(index);
    }



    public void swapTiles(int i, int j) {
        int temp = tiles.get(i); 
        tiles.set(i, tiles.get(j));
        tiles.set(j, temp);
    }

    public boolean isSolved() {
        for (int i = 0; i < size - 1; i++) {
            if (tiles.get(i) != i) return false;
        }
        return tiles.get(size - 1) == size - 1;
    }


    public int getSize() {
        return size;
    }

    public int getMoves() {
        return moves;
    }

    public int getScore() {
        return score;
    }

    public void calculateScore() {
        if (startTime == 0) return;

        long elapsedSeconds = getElapsedTime();
        score = 2000 - (int) elapsedSeconds * 10 - moves * 2;
    }



    public long getElapsedTime() {
        return startTime == 0 ? 0 : (System.currentTimeMillis() - startTime) / 1000;
    }


    public void resetGame() {
        moves = 0;
        score = 0;
        startTime = System.currentTimeMillis(); // Réinitialise aussi le timer
    }


    public boolean isTileCorrect(int index) {
        return tiles.get(index) == index;
    }
}
