package ru.myitschool.chess;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;


public class ChessMain extends ApplicationAdapter {
	public static final int SCR_WIDTH = 720/4*3, SCR_HEIGHT = 1280/4*3;
	public static final int WHITE = 0, BLACK = 1;
	public static final int OUR_SIDE = 0, OTHER_SIDE = 1;
	public static final int PAWN = 0, BISHOP = 1, HORSE = 2, ROOK = 3, QUEEN = 4, KING = 5;
	int[] figuresOnBoard = {0, 0, 0, 0, 0, 0, 0, 0,
							3, 2, 1, 4, 5, 1, 2, 3,
							0, 0, 0, 0, 0, 0, 0, 0,
							3, 2, 1, 4, 5, 1, 2, 3};
	SpriteBatch batch;
	OrthographicCamera camera;
	Vector3 touch;
	ChessInputProcessor input;
	BitmapFont font;

	Texture imgAtlas;
	TextureRegion[] imgCell = new TextureRegion[2];
	TextureRegion[][] imgFigure = new TextureRegion[2][6];
	Texture imgBorder;
	Texture imgPlayer;
	Sound sndTurn;


	static float size;
	static float paddingBottom;
	Cells[][] board = new Cells[8][8];
	Figure[][] figure = new Figure[2][16];
	int curIndex, curSide;



	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		touch = new Vector3();
		camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
		input = new ChessInputProcessor();
		Gdx.input.setInputProcessor(input);
		imgBorder = new Texture("border.png");
		imgPlayer = new Texture("Player.jpg");
		sndTurn = Gdx.audio.newSound(Gdx.files.internal("Turn.mp3"));



		imgAtlas = new Texture("chessatlas.png");
		for(int i=0; i<2; i++) {
			imgCell[i] = new TextureRegion(imgAtlas, 1400, i * 200, 200, 200);
			for(int j=0; j<6; j++) imgFigure[i][j] = new TextureRegion(imgAtlas, j*200, i*200, 200, 200);


		}



		size = SCR_WIDTH/9f;
		paddingBottom = (SCR_HEIGHT - SCR_WIDTH)/2;





		for(int j = 0; j<8; j++)
			for(int i=0; i<8; i++)
				board[i][j] = new Cells(i, j, (i+(j+1)%2)%2, null);

		int ourColor = MathUtils.random(WHITE, BLACK);
		int otherColor;
		if(ourColor == WHITE) otherColor = BLACK;
		else {
			otherColor = WHITE;
			figuresOnBoard[11] = figuresOnBoard[27] = KING;
			figuresOnBoard[12] = figuresOnBoard[28] = QUEEN;
		}
		

		for(int i=0; i<8; i++) board[i][1].figure = figure[OUR_SIDE][i] = new Figure(i, 1, ourColor, figuresOnBoard[i], OUR_SIDE);
		for(int i=8; i<16; i++) board[i-8][0].figure = figure[OUR_SIDE][i] = new Figure(i-8, 0, ourColor, figuresOnBoard[i], OUR_SIDE);
		for(int i=0; i<8; i++) board[i][6].figure = figure[OTHER_SIDE][i] = new Figure(i, 6, otherColor, figuresOnBoard[i+16], OTHER_SIDE);
		for(int i=8; i<16; i++) board[i-8][7].figure = figure[OTHER_SIDE][i] = new Figure(i-8, 7, otherColor, figuresOnBoard[i+16], OTHER_SIDE);

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		BitmapFont font;
		font = new BitmapFont();


		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("TMNT.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.color = new Color(0.9f, 0.5f, 0.2f, 1);
		parameter.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
		parameter.size = 40;
		font = generator.generateFont(parameter);
		generator.dispose();
		font.draw(batch, "ИГРОК 1", 170, 120);
		batch.draw(imgPlayer, 170, 850);
		batch.draw(imgBorder, board.length-137,board.length+169.15f,size*13.27f,size*10.03f);








		for(int j = 0; j<8; j++)
			for(int i=0; i<8; i++)
				batch.draw(imgCell[board[i][j].color], i*size+27.5f, j*size+27.5f + paddingBottom, size+1, size+1);

		for(int i=0; i<2; i++)
			for(int j=0; j<16; j++)
				batch.draw(imgFigure[figure[i][j].color][figure[i][j].type], figure[i][j].scrX+27.5f, figure[i][j].scrY+27.5f, size, size);
		batch.draw(imgFigure[figure[curSide][curIndex].color][figure[curSide][curIndex].type], figure[curSide][curIndex].scrX+27.5f, figure[curSide][curIndex].scrY+27.5f, size, size);

		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		imgAtlas.dispose();

	}

	class ChessInputProcessor implements InputProcessor{

		@Override
		public boolean keyDown(int keycode) {
			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			return false;
		}

		@Override
		public boolean keyTyped(char character) {
			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			touch.set(screenX, screenY, 0);
			camera.unproject(touch);




			for(int i=0; i<2; i++)
				for(int j=0; j< 16; j++)
					if(figure[i][j].isHit(touch.x-27.5f, touch.y-27.5f)) {
						figure[i][j].isMove = true;
						curIndex = j;
						curSide = i;

					}
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			touch.set(screenX, screenY, 0);
			camera.unproject(touch);
			sndTurn.play();


			if(figure[curSide][curIndex].isMove){
				if(figure[curSide][curIndex].isDropCorrect(touch.x-27.5f, touch.y-27.5f, board)){
					board[figure[curSide][curIndex].boardX][figure[curSide][curIndex].boardY].figure = null;
					figure[curSide][curIndex].screenToBoard();
					board[figure[curSide][curIndex].boardX][figure[curSide][curIndex].boardY].figure = figure[curSide][curIndex];
				}
				figure[curSide][curIndex].isMove = false;
				figure[curSide][curIndex].boardToScreen();
			}
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			touch.set(screenX, screenY, 0);
			camera.unproject(touch);
			if(figure[curSide][curIndex].isMove) figure[curSide][curIndex].move(touch.x-27.5f, touch.y-27.5f);
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			return false;
		}

		@Override
		public boolean scrolled(float amountX, float amountY) {
			return false;
		}
	}
}
