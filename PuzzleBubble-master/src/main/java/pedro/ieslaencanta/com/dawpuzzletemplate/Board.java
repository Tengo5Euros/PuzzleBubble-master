/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pedro.ieslaencanta.com.dawpuzzletemplate;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.E;
import javafx.scene.paint.Color;

/**
 * Tablero del juego, posee un fondo (imagen estática, solo se cambia cuando se
 * cambia el nivel), Una bola, un vector de niveles, un disparador y una matriz
 * de bolas gestiona la pulsación de tecla derecha e izquierda
 *
 * @author Pedro
 * @see Bubble Level Shttle BallGrid
 */
public class Board implements IKeyListener {
    //variables
    private Rectangle2D game_zone;

    private GraphicsContext gc;
    private GraphicsContext bggc;
    private Dimension2D original_size;
    private BubbleType colores;

    private boolean debug;
    private boolean left_press, right_press;
    private Bubble ball;
    private Shuttle shuttle;
    private Ballgrid grid;
    private Level[] levels;
    public int nivel_actual;
    private boolean terminado;

    /**
     * constructor
     *
     * @param original
     */
    //Constructor sobrecargado de Board, al cual se le pasa como parametro un Dimension2D
    public Board(Dimension2D original) {
        this.gc = null;
        this.game_zone = new Rectangle2D(95, 23, 128, 200);
        this.original_size = original;
        this.right_press = false;
        this.left_press = false;
        //Se inicializa el lanzador
        this.shuttle = new Shuttle(new Point2D((this.game_zone.getMaxX() - this.game_zone.getWidth() / 2), (this.game_zone.getMaxY() - 20)));
        this.debug = false;

            this.grid = new Ballgrid((int) this.game_zone.getMinX(), (int) this.game_zone.getMinY());
            this.init(nivel_actual);


    }

    /**
     * muestra el grid
     */
    //Creamos los niveles
    public void CreateLevel() {
        int nivel_actual = 0;
        setLevels(new Level[3]);
        this.getLevels()[0] = new Level();
    }

    //definimos los niveles que vamos a realizar y los insertamos dentro del array de niveles
    public void init(int nivel_actual) {
        this.setLevels(new Level[3]);
        BubbleType[][] level1 = {
            {this.colores.RED, this.colores.ORANGE, this.colores.BLUE, null, this.colores.WRITE, this.colores.GREEN, this.colores.BLUE, this.colores.GRAY}

            //{this.colores.BLUE, this.colores.BLUE, this.colores.BLUE, this.colores.BLUE, this.colores.BLUE, this.colores.BLUE, this.colores.BLUE, this.colores.BLUE},
            //{this.colores.BLUE, this.colores.BLUE, this.colores.BLUE, this.colores.BLUE, this.colores.BLUE, this.colores.BLUE, this.colores.BLUE}

        };


        this.getLevels()[0] = new Level(new Dimension2D((int) this.game_zone.getMinX(), (int) this.game_zone.getMinY()), level1, 1);
        this.grid = new Ballgrid(this.levels[nivel_actual].getMatrix());

        BubbleType[][] level2 = {
                {this.colores.ORANGE, this.colores.GREEN, this.colores.GRAY, null, this.colores.WRITE, this.colores.YELLOW, this.colores.BLUE, this.colores.YELLOW}

                //{this.colores.BLUE, this.colores.BLUE, this.colores.BLUE, this.colores.BLUE, this.colores.BLUE, this.colores.BLUE, this.colores.BLUE, this.colores.BLUE},
                //{this.colores.BLUE, this.colores.BLUE, this.colores.BLUE, this.colores.BLUE, this.colores.BLUE, this.colores.BLUE, this.colores.BLUE}

        };
        this.getLevels()[1] = new Level(new Dimension2D((int) this.game_zone.getMinX(), (int) this.game_zone.getMinY()), level2, 1);
        this.grid = new Ballgrid(this.levels[nivel_actual].getMatrix());
    }
// Método para realizar el debug de los parámetros que les demos
    private void Debug() {
        this.bggc.setStroke(Color.RED);
        for (int i = 0; i < 12; i++) {
            //se pinta las lineas horizontales

            this.bggc.strokeLine(this.game_zone.getMinX() * Game.SCALE,
                    (this.game_zone.getMinY() + i * 16) * Game.SCALE,
                    (this.game_zone.getMaxX()) * Game.SCALE,
                    (this.game_zone.getMinY() + i * 16) * Game.SCALE);

            //se pintan las líneas verticales desplazando en las impares
            for (int j = 0; j < 8; j++) {
                this.bggc.strokeLine(
                        //inicio x va cambiando entre pares e impares
                        (this.game_zone.getMinX() + j * 16 + ((i % 2 != 0) ? 8 : 0)) * Game.SCALE,
                        //inicio de y, siempre el mismo
                        (this.game_zone.getMinY() + i * 16) * Game.SCALE,
                        //fin de x va cambiando entre pares e impares
                        (this.game_zone.getMinX() + j * 16 + ((i % 2 != 0) ? 8 : 0)) * Game.SCALE,
                        //fin de y es igual en todas
                        (this.game_zone.getMinY() + i * 16 + 16) * Game.SCALE);
            }
        }
    }

    /**
     * @return the debug
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * @param debug the debug to set
     */
    public void setDebug(boolean debug) {
        this.debug = debug;

    }

    public void setGraphicsContext(GraphicsContext gc) {
        this.gc = gc;

    }

    public void setBackGroundGraphicsContext(GraphicsContext gc) {
        this.bggc = gc;
        this.paintBackground();
    }

    /**
     * cuando se produce un evento
     */
    public synchronized void TicTac() {
        this.clear();
        this.render();
        this.process_input();
        //actualizar
        this.update();
    }

    private void update() {
        //actualizar el juego
        if (this.ball != null && this.ball.getBalltype() != null) {
            this.ball.move(this.game_zone);
        }
        if (this.ball != null && this.grid != null) {
            if (this.grid.colision(ball)) {
                this.ball = null;
            }
        }

    }

    /**
     * Pinta el lanzador y el grid de bolas
     */
    private void render() {
        if (this.ball != null && this.ball.getBalltype() != null) {
            this.ball.paint(gc);
        }

            this.shuttle.paint(gc);
            this.grid.paint(gc);


    }

    private void process_input() {
        if (this.left_press) {
            this.shuttle.moveRight();
        } else if (this.right_press) {
            this.shuttle.moveLeft();
        } else {

        }
    }

    /**
     * limpiar la pantalla
     */
    private void clear() {
        this.gc.restore();
        this.gc.clearRect(0, 0, this.original_size.getWidth() * Game.SCALE, this.original_size.getHeight() * Game.SCALE);
    }

    /**
     * pintar el fonodo
     */

    //Método que define que nivel es para cambiar el fondo
    public void paintBackground() {
        Image imagen = Resources.getInstance().getImage("fondos");
        //Segun el nivel actual, cambiamos el fondo
        if(this.nivel_actual==-1){
            this.bggc.drawImage(imagen, 1000, 454, this.original_size.getWidth(),
                    this.original_size.getHeight(), 0, 0, this.original_size.getWidth() * Game.SCALE, this.original_size.getHeight() * Game.SCALE);
        }
        if(this.nivel_actual==0){
            this.bggc.drawImage(imagen, 16, 16, this.original_size.getWidth(),
                    this.original_size.getHeight(), 0, 0, this.original_size.getWidth() * Game.SCALE, this.original_size.getHeight() * Game.SCALE);
        }
        if (this.nivel_actual==1){
            this.bggc.drawImage(imagen, 16, 544, this.original_size.getWidth(),
                    this.original_size.getHeight(), 0, 0, this.original_size.getWidth() * Game.SCALE, this.original_size.getHeight() * Game.SCALE);
        }
        if (this.nivel_actual==2) {
            this.bggc.drawImage(imagen, 344, 544, this.original_size.getWidth(),
                    this.original_size.getHeight(), 0, 0, this.original_size.getWidth() * Game.SCALE, this.original_size.getHeight() * Game.SCALE);
        }
        //se dibuja la línea del fondo
        if (this.debug) {
            this.Debug();
        }

    }

    /**
     * gestión de pulsación
     *
     * @param code
     */
    @Override
    public void onKeyPressed(KeyCode code) {
        switch (code) {
            case LEFT:
                this.left_press = true;
                break;
            case RIGHT:
                this.right_press = true;
                break;
        }
    }
    //Si se presiona el boton asignado, realizará lo que pone a continuación
    @Override
    public void onKeyReleased(KeyCode code) {
        switch (code) {
            case LEFT:
                this.left_press = false;
                break;
            case RIGHT:
                this.right_press = false;
                break;

            case ENTER: //En caso de pulsar Enter, cambiamos el nivel actual


                this.nivel_actual++;
                this.grid=new Ballgrid((int) this.game_zone.getMinX(), (int) this.game_zone.getMinY());

                if(nivel_actual>2){
                    nivel_actual=0;
                }

                this.paintBackground();

                break;
            case SPACE: // Si presionamos SPACE, dispararemos
                //this.ball=new Bubble();
                //se coloca el tipo de forma aleatorioa
                this.ball = this.shuttle.shoot();
                //se pone la posición (centro) y ángulo aleatorio
                //this.ball.init(new Point2D((this.game_zone.getMaxX() - this.game_zone.getWidth() / 2),(this.game_zone.getMaxY() - 18)), (float) (Math.random()*360));
                this.ball.play();
                break;
            case P://Si se pulsa la letra P, saldrá por pantalla el GAME OVER
                if(this.grid!=null){
                    this.shuttle = new Shuttle(new Point2D((this.game_zone.getMaxX()*1000 - this.game_zone.getWidth() / 2), (this.game_zone.getMaxY()*1000 - 20)));
                    nivel_actual=-1;
                }

                System.out.println("GAME OVER");
                this.paintBackground();
                break;
            case E:
                break;
            case D:
                this.setDebug(!this.debug);
                this.paintBackground();

        }

    }

    public Level[] getLevels() {
        return levels;
    }

    public void setLevels(Level[] levels) {
        this.levels = levels;
    }

}
