package com.javarush.task.task35.task3513;

import java.util.*;

public class Model {
    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles;
    int score = 0;
    private int maxTile = 0;

    public Model() {
        resetGameTiles();
    }

    void resetGameTiles(){
        gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int key = 0; key < FIELD_WIDTH; key++) {
            for (int value = 0; value < FIELD_WIDTH; value++) {
                gameTiles[key][value] = new Tile();
            }
        }

        addTile();
        addTile();
    }

    private void addTile(){
        List<Tile> emptyTiles = getEmptyTiles();
        if (!emptyTiles.isEmpty()){
            int index = (int) (Math.random() * emptyTiles.size());
            Tile emptyTile = emptyTiles.get(index);
            emptyTile.value = Math.random() < 0.9 ? 2 : 4;
        }
    }

    private List<Tile> getEmptyTiles(){
        List<Tile> emptyTiles = new ArrayList<>();

        for (Tile[] tileArray :
                gameTiles) {
            for (Tile tile :
                    tileArray) {
                if(tile.isEmpty()){
                    emptyTiles.add(tile);
                }
            }
        }
        return emptyTiles;
    }

    private boolean compressTiles(Tile[] tiles) {
        boolean isChanged = false;

        for (int count = 0; count < FIELD_WIDTH - 1; count++) {
            for (int i = 0; i < tiles.length; i++) {
                if (tiles[i].isEmpty() && i != tiles.length - 1 && !tiles[i+1].isEmpty()) {
                    tiles[i] = tiles[i + 1];
                    tiles[i + 1] = new Tile();

                    isChanged = true;
                }
            }
        }

        return isChanged;
    }

    private boolean mergeTiles(Tile[] tiles){
        boolean isChanged = false;

        for (int i = 0; i < tiles.length; i++) {
            if (i == tiles.length - 1) break;

            if ((tiles[i].value != 0) && (tiles[i].value == tiles[i + 1].value)){
                tiles[i].value = tiles[i].value * 2;
                tiles[i + 1] = new Tile();
                isChanged = true;

                this.score = score + tiles[i].value ;
                if (tiles[i].value * 2 > this.maxTile){
                    maxTile = tiles[i].value ;
                }
            }
        }

        compressTiles(tiles);

        return isChanged;
    }

    public void left(){
        boolean changed = false;

        for (Tile[] tileArray : gameTiles) {
            if (compressTiles(tileArray) | mergeTiles(tileArray)){
                changed = true;
            }
        }

        if (changed){
            addTile();
        }
    }

    public void right(){
        gameTiles = rotate90(gameTiles);
        gameTiles = rotate90(gameTiles);
        left();
        gameTiles = rotate90(gameTiles);
        gameTiles = rotate90(gameTiles);
    }

    public void down(){
        gameTiles = rotate90(gameTiles);
        left();
        gameTiles = rotate90(gameTiles);
        gameTiles = rotate90(gameTiles);
        gameTiles = rotate90(gameTiles);
    }

    public void up(){
        gameTiles = rotate90(gameTiles);
        gameTiles = rotate90(gameTiles);
        gameTiles = rotate90(gameTiles);
        left();
        gameTiles = rotate90(gameTiles);
    }

    private Tile[][] rotate90(Tile[][] gameTiles) {
        Tile[][] tempGameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                tempGameTiles[i][FIELD_WIDTH - 1 - j] = gameTiles[j][i];
            }
        }

        return tempGameTiles;
    }

    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    public boolean canMove(){
        if (!(getEmptyTiles().size() == 0)){
            return true;
        }

        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                Tile testTile = gameTiles[i][j];

                if ((i < FIELD_WIDTH - 1 && testTile.value == gameTiles[i+1][j].value)
                        || (j < FIELD_WIDTH - 1 && testTile.value == gameTiles[i][j+1].value)){
                    return true;
                }
            }
        }

        return false;
    }
}
