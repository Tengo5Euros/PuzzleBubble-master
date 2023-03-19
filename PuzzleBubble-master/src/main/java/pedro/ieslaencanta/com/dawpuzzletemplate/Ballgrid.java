/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pedro.ieslaencanta.com.dawpuzzletemplate;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author Jose Maria
 */
public class Ballgrid {

    private int startx;
    private int starty;
    private static final int ROWS = 12;
    private static final int COLS = 8;
    private static final int MIN_BALLS_CONECT = 3;
    private Bubble bubblegrid[][];

    public Ballgrid(BubbleType[][] matrix) {
        this.bubblegrid = new Bubble[Ballgrid.ROWS][Ballgrid.COLS];
        int f,c;
        for(int i=0;i<matrix.length;i++){
            for(int j=0;j<matrix[i].length;j++){
                if(matrix[i][j]!=null){
                    f = i*Bubble.HEIGHT- this.starty+31;
                    c = j*Bubble.WIDTH - this.startx+103;
                    this.bubblegrid[i][j]=new Bubble(c,f,matrix[i][j]);
                }
            }
        }
    }

    public Ballgrid(int startx, int starty) {
        this.startx = startx;
        this.starty = starty;
        this.bubblegrid = new Bubble[Ballgrid.ROWS][Ballgrid.COLS];
    }

    public Ballgrid(BubbleType matrix[][], int startx, int starty) {
        this.startx = startx;
        this.starty = starty;
        this.bubblegrid = new Bubble[Ballgrid.ROWS][Ballgrid.COLS];
        //matrix = BubbleType.values();
    }

    /**
     * @return the startx
     */
    public int getStartx() {
        return startx;
    }

    /**
     * @param startx the startx to set
     */
    public void setStartx(int startx) {
        this.startx = startx;
    }

    /**
     * @return the starty
     */
    public int getStarty() {
        return starty;
    }

    /**
     * @param starty the starty to set
     */
    public void setStarty(int starty) {
        this.starty = starty;
    }

    public boolean colision(Bubble b) {
        int f = 0, c = 0;
        boolean colision = false;
        //si la bola llega a ese punto, se parará
        if (b.getPosicion().getY() - (Bubble.WIDTH / 2) <= this.starty&& this.bubblegrid!=null) {
            b.stop();
            //Damos valor a las filas y columnas
            f = (int) ((b.getPosicion().getY() - this.starty) / Bubble.HEIGHT);
            c = (int) ((b.getPosicion().getX() - this.startx) / Bubble.WIDTH);
            this.bubblegrid[f][c] = b;
            //calculamos la posicion de la bola donde tenemos que moverla(dist*pos+1/2de la dist)
            b.setPosicion(new Point2D(this.startx + Bubble.WIDTH * c + Bubble.WIDTH / 2,
                    this.starty + Bubble.HEIGHT * f + Bubble.HEIGHT / 2));
            return true;
        } else {
            for (int i = 0; i < this.bubblegrid.length && !colision; i++) {
                for (int j = 0; j < this.bubblegrid[i].length && !colision; j++) {
                    //
                    if (this.bubblegrid[i][j] != null && b.getPosicion().distance(this.bubblegrid[i][j].getPosicion()) <= 16) {
                        b.stop();
                        colision = true;
                        f = (int) ((b.getPosicion().getY() - this.starty) / Bubble.HEIGHT);
                        c = (int) ((b.getPosicion().getX() - this.startx) / Bubble.WIDTH);
                        this.bubblegrid[f][c] = b;
                        //Si la fila es par, se moverá un poco hacia la izquierda
                        if (f % 2 == 0) {
                            //c = (int)((b.getPosicion().getX() - this.startx ) / Bubble.WIDTH);

                            b.setPosicion(new Point2D( this.startx + Bubble.WIDTH * c + Bubble.WIDTH/2,
                                    this.starty + Bubble.HEIGHT * f + Bubble.HEIGHT / 2));



                            //Las burbujas en las filas impares
                        } else {
                            //Si la fila es impar, se pondra justo debajo, sin mover a la izquierda
                            //c = (int)((b.getPosicion().getX() - this.startx - Bubble.WIDTH / 2) / Bubble.WIDTH);
                            b.setPosicion(new Point2D( this.startx + Bubble.WIDTH * c + Bubble.WIDTH ,
                                    this.starty + Bubble.HEIGHT * f + Bubble.HEIGHT/2));




                        }
                        Point2D igual[] = new Point2D[70];
                        explosion(b.getBalltype(),igual,f,c);
                        int pegadas = pegadas(igual);
                        if(pegadas >= MIN_BALLS_CONECT){
                            for (int k = 0; k > pegadas; k++){
                                int y = (int) igual[k].getY();
                                int x = (int) igual[k].getX();
                                this.bubblegrid[y][x] = null;
                            }
                        }
                    }

                }
            }
            return colision;
        }
    }

    //Metodo que recorre la matriz de burbujas y las pinta
    public void paint(GraphicsContext gc) {
        for (int i = 0; i < this.bubblegrid.length; i++) {
            for (int j = 0; j < this.bubblegrid[i].length; j++) {
                if (this.bubblegrid[i][j] != null) {
                    this.bubblegrid[i][j].paint(gc);
                }

            }
        }
    }
    public void insertBall(Point2D igual[], Point2D bola){
        for(int i = 0; i < igual.length; i++){
            if(igual[i] == null){
                igual[i]=bola;
            }
        }
    }
    public boolean eval_colision(Point2D igual[], Point2D bola){
        boolean eval_colision = false;
        for(int i = 0; i < igual.length; i++){
            if(igual[i] == null){
                eval_colision = true;
            }
        }
        return eval_colision;
    }
    public int pegadas(Point2D igual[]){
        return igual.length;
    }
    public void explosion (BubbleType col, Point2D igual[], int f, int c){
        Point2D bola = new Point2D(c, f);

        if (this.bubblegrid[f][c] != null && this.bubblegrid[f][c].getBalltype() == col){
            insertBall(igual,bola);

            if(f%2==0){ //filas pares
                if(c - 1 >= 0){ //izquierda
                    explosion(col,igual,f,c - 1);
                }
                if(c + 1 < this.bubblegrid[0].length){ //derecha
                    explosion(col,igual,f,c + 1);
                }
                if(f + 1 < this.bubblegrid[0].length && c - 1 >= 0){ //izquierda abajo
                    explosion(col,igual,f + 1,c - 1);
                }
                if(f + 1 < this.bubblegrid[0].length){ //abajo
                    explosion(col,igual,f + 1,c);
                }
                if(f - 1 >= 0 && c - 1 >= 0){ //arriba izquierda
                    explosion(col,igual,f - 1,c - 1);
                }
                if(f - 1 >= 0){ //ariba
                    explosion(col,igual,f - 1,c);
                }
            }else{ //filas impares
                if(c - 1 >= 0){ //izquierda
                    explosion(col,igual,f,c - 1);
                }
                if(c + 1 < this.bubblegrid[1].length){ //derecha
                    explosion(col,igual,f,c + 1);
                }
                if(f - 1 < this.bubblegrid[1].length && c + 1 >= 0){ //derecha abajo
                    explosion(col,igual,f - 1,c + 1);
                }
                if(f + 1 < this.bubblegrid[1].length){ //abajo
                    explosion(col,igual,f + 1,c);
                }
                if(f - 1 >= 0 && c + 1 >= 0){ //arriba derecha
                    explosion(col,igual,f - 1,c + 1);
                }
                if(f - 1 >= 0){ //ariba
                    explosion(col,igual,f - 1,c);
                }
            }
        }
    }
}