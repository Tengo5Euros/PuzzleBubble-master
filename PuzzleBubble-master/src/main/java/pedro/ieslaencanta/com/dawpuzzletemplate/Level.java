/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pedro.ieslaencanta.com.dawpuzzletemplate;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 *
 * @author Jose Maria, Bruno
 */
public class Level {
    //Variables
    private String sound;
    private Dimension2D tablero;
    private int starty;
    private BubbleType[][] matrix;

    //Constructor por defecto de Level
    public Level(){
        this.tablero = new Dimension2D(70, 70);
        this.matrix = new BubbleType[70][70];
    }
    //Constructor sobrecargado de Level
    public Level(Dimension2D tablero, BubbleType[][] matrix, int starty) {
        this.tablero = tablero;
        this.matrix = matrix;
        this.starty = starty;
    }
    //Getters y Setters
    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public int getStarty() {
        return starty;
    }

    public void setStarty(int starty) {
        this.starty = starty;
    }


    public BubbleType[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(BubbleType[][] matrix) {
        this.matrix = matrix;
    }

    public Dimension2D getTablero() {
        return tablero;
    }

    public void setTablero(Dimension2D tablero) {
        this.tablero = tablero;
    }
    
}
