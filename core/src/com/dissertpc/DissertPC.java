package com.dissertpc;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class DissertPC extends ApplicationAdapter {
	SpriteBatch batch;


	ArrayList<Texture> textures200x200 = new ArrayList<>();
	ArrayList<int[]> coordTextures200x200 = new ArrayList<>();
	ArrayList<String> imagesPath = new ArrayList<>();
	ArrayList<String> galleryPath = new ArrayList<>();
	ShapeRenderer shapeRenderer;//рисуем линии
	boolean checkTouch = false;
	int stage = 0;//текущая стадия 0 интро, 1 работа с выбранным изображением
	int chosenPicture;//номер выбранной картинки, в массиве с абсолютыми путями
	BitmapFont bitmapFont;//шрифт

	Pixmap chosenTexturePixmap;
	Texture chosenTexture;

	Pixmap chosenGraphPixmap;
	Texture chosenGraph;
	int cGX;
	int cGY;

	Pixmap colorPicturePixmap;
	boolean masPixLeaves[][];
	Texture colorPicture;
	Texture colorPictureNoGreen;

	int summPix;
	int corrSummPix;
	String outputSum;

	Pixmap masRedPixelsPixmap;
	Pixmap masBluePixelsPixmap;
	Texture masRedPixelsTexture;
	Texture masBluePixelsTexture;
	int cGXRed;
	int cGYRed;
	int cGXBlue;
	int cGYBlue;
	boolean masPixLeavesRed[][];
	boolean masPixLeavesBlue[][];
	Pixmap noGreenPixelsPixmap;
	int masKoeffRed[];
	int masKoeffBlue[];
	Texture galleryTexture;
	int currentGalleryImage;

	Skin skin;
	Stage stage1;

	Texture purpleBox;
	TextArea textField;
	String descriptionString="";

	@Override
	public void create () {

		Pixmap introPictures;
		Pixmap introPictures200x200;
		FileHandle pixmapHandle = Gdx.files.internal(Gdx.files.getLocalStoragePath()+"/input");
		for (FileHandle entry: pixmapHandle.list()) {
			if("jpg".equals(entry.toString().substring(entry.toString().length()-3))||"png".equals(entry.toString().substring(entry.toString().length()-3))){

				imagesPath.add(entry.toString());//добавляем путь картинки
				coordTextures200x200.add(new int[2]);//добавлем координаты картинки на экране
				coordTextures200x200.get(coordTextures200x200.size()-1)[0]=Gdx.graphics.getWidth()/2-100;
				coordTextures200x200.get(coordTextures200x200.size()-1)[1]=Gdx.graphics.getHeight() - 210 * coordTextures200x200.size();

				introPictures = new Pixmap(Gdx.files.internal(imagesPath.get(imagesPath.size()-1)));//получаем картинку из пути
				introPictures200x200 = new Pixmap(200,200,introPictures.getFormat());//подготавливаем к переводу в 200х200
				introPictures200x200.drawPixmap(introPictures,0,0,introPictures.getWidth(),introPictures.getHeight(),0,0,introPictures200x200.getWidth(),introPictures200x200.getHeight());//переводим в 200х200
				textures200x200.add(new Texture(introPictures200x200));
			}
		}
		shapeRenderer = new ShapeRenderer();
		bitmapFont = new BitmapFont(Gdx.files.internal("шрифт/ran.fnt"));
		batch = new SpriteBatch();

		skin = new Skin(Gdx.files.internal("skin/cloud-form-ui.json"));
		stage1 = new Stage();
		textField = new TextArea("",skin);
		textField.setPosition(350,100);
		stage1.addActor(textField);
		Gdx.input.setInputProcessor(stage1);


	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(95/255f, 48/255f, 90/255f, 1);//1, 200/255f, 191/255f, 1
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage0Render();
		stage1Preproc();
		stage2Render();
		stage3Preproc();
		stage4Render();
		stage5Preproc();
		stage6Render();

		mouseTouch();

	}
	
	@Override
	public void dispose () {

	}

	void mouseTouch(){

		if(Gdx.input.isTouched()){
			stage0Mouse();
			stage2Mouse();
			stage4Mouse();
			stage6Mouse();
		}else
		if(checkTouch)checkTouch=false;
	}

	void stage0Render(){
		if(stage==0) {
			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			shapeRenderer.line(100, 0, 100, Gdx.graphics.getHeight());
			shapeRenderer.line(50, 100, 50, Gdx.graphics.getHeight() - 100);
			shapeRenderer.line(50, 100, 35, 120);
			shapeRenderer.line(50, 100, 65, 120);
			shapeRenderer.line(Gdx.graphics.getWidth() - 100, 0, Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight());
			shapeRenderer.line(Gdx.graphics.getWidth() - 50, 100, Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() - 100);
			shapeRenderer.line(Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() - 100, Gdx.graphics.getWidth() - 35, Gdx.graphics.getHeight() - 120);
			shapeRenderer.line(Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() - 100, Gdx.graphics.getWidth() - 65, Gdx.graphics.getHeight() - 120);
			shapeRenderer.rect(435,Gdx.graphics.getHeight()-75,85,35);
			shapeRenderer.end();

			batch.begin();
			bitmapFont.draw(batch,"Gallery",440,Gdx.graphics.getHeight()-50);
			for (int i = 0; i < textures200x200.size(); i++)
				batch.draw(textures200x200.get(i), coordTextures200x200.get(i)[0], coordTextures200x200.get(i)[1]);
			batch.end();
		}
	}
	void stage0Mouse(){
		if(stage==0){
			for (int j=0;j<coordTextures200x200.size();j++){

				if(Gdx.input.getX()<100)
					for (int i=0;i<coordTextures200x200.size();i++){
						coordTextures200x200.get(i)[1]-=1;
					}
				if(Gdx.input.getX()>Gdx.graphics.getWidth()-100)
					for (int i=0;i<coordTextures200x200.size();i++){
						coordTextures200x200.get(i)[1]+=1;
					}
			}

			if(!checkTouch)
				for(int i=0;i<coordTextures200x200.size();i++)
					if(Gdx.input.getX()>=coordTextures200x200.get(i)[0])
						if(Gdx.input.getX()<=coordTextures200x200.get(i)[0]+200)
							if(Gdx.graphics.getHeight()-Gdx.input.getY()>=coordTextures200x200.get(i)[1])
								if(Gdx.graphics.getHeight()-Gdx.input.getY()<=coordTextures200x200.get(i)[1]+200){
									chosenPicture=i;
									stage=1;
									checkTouch=true;
								}
			if(!checkTouch){
				if(Gdx.input.getX()>=435)
					if(Gdx.input.getX()<=520)
						if(Gdx.graphics.getHeight() - Gdx.input.getY()>=Gdx.graphics.getHeight()-75)
							if(Gdx.graphics.getHeight() - Gdx.input.getY()<=Gdx.graphics.getHeight()-40){
								stage=5;
								checkTouch=true;
							}
			}
		}

	}
	void stage1Preproc(){
		if(stage==1) {
			int razmerWH = 200; //размер изображения на экране
			int razmerDelenia = 1;
			Pixmap pixmap = new Pixmap(Gdx.files.internal(imagesPath.get(chosenPicture)));

			if((int)(pixmap.getWidth()/razmerWH)>(int)(pixmap.getHeight()/razmerWH))
				razmerDelenia = (int)(pixmap.getWidth()/razmerWH);
			else razmerDelenia = (int)(pixmap.getHeight()/razmerWH);

			Pixmap pixmap200x200 = new Pixmap((int)(pixmap.getWidth()/razmerDelenia),(int)(pixmap.getHeight()/razmerDelenia),pixmap.getFormat());//подготавливаем к переводу в 200х200
			pixmap200x200.drawPixmap(pixmap,0,0,pixmap.getWidth(),pixmap.getHeight(),0,0,(int)(pixmap.getWidth()/razmerDelenia),(int)(pixmap.getHeight()/razmerDelenia));//переводим в 200х200

			colorPicturePixmap = pixmap200x200;
			masPixLeaves = new boolean[pixmap200x200.getWidth()][pixmap200x200.getHeight()];
			Pixmap sBW = setBlackAndWhite(pixmap200x200);

			chosenTexturePixmap = sBW;
			chosenTexture = new Texture(chosenTexturePixmap);
			chosenGraphPixmap = writeGraph(sBW);
			chosenGraph = new Texture(chosenGraphPixmap);
			cGX = 300;
			cGY = Gdx.graphics.getHeight()/2-chosenTexture.getHeight()/2-25;
			colorPicture = new Texture(colorPicturePixmap);
			colorPictureNoGreen = new Texture(colorPicturePixmap);
			stage=2;

		}
	}
	void stage2Render(){
		if(stage==2) {
			Pixmap chosenGraphPixmapRed = new Pixmap(chosenGraphPixmap.getWidth(),chosenGraphPixmap.getHeight(),chosenGraphPixmap.getFormat());
			for (int i=0;i<chosenGraphPixmap.getWidth();i++)
				for (int j=0;j<chosenGraphPixmap.getHeight();j++)
					if(i<=cGX-300)
						if (chosenGraphPixmap.getPixel(i,j)!=255)
							chosenGraphPixmapRed.drawPixel(i,j,Color.rgba8888(1,0,0,1));
						else;
					else 	chosenGraphPixmapRed.drawPixel(i,j,chosenGraphPixmap.getPixel(i,j));
			chosenGraph.draw(chosenGraphPixmapRed,0,0);
			chosenGraphPixmapRed.dispose();

			Pixmap chosenTexturePixmapRed = new Pixmap(chosenTexturePixmap.getWidth(),chosenTexturePixmap.getHeight(),chosenTexturePixmap.getFormat());
			Color color = new Color(Color.rgba8888(1,1,1,1));
			masPixLeaves = null;
			masPixLeaves = new boolean[chosenTexturePixmap.getWidth()][chosenTexturePixmap.getHeight()];

			for (int i=0;i<chosenTexturePixmap.getWidth();i++)
				for (int j=0;j<chosenTexturePixmap.getHeight();j++){
					chosenTexturePixmapRed.drawPixel(i,j,chosenTexturePixmap.getPixel(i,j));
						Color.rgba8888ToColor(color,chosenTexturePixmap.getPixel(i,j));
						if(color.r*255f<=cGX-300){
							chosenTexturePixmapRed.drawPixel(i,j,Color.rgba8888(1,0,0,100/255f));
							masPixLeaves[i][j] = true;
						}
				}

			chosenTexture.draw(chosenTexturePixmapRed,0,0);
			chosenTexturePixmapRed.dispose();

			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			shapeRenderer.rect(100-20+200/4,50,100,60);
			shapeRenderer.rect(400,50,100,60);
			shapeRenderer.rect(cGX,cGY,20,20);
			shapeRenderer.end();

			batch.begin();
			batch.draw(chosenTexture, 100-20, Gdx.graphics.getHeight()/2-chosenTexture.getHeight()/2);
			batch.draw(chosenGraph, 320-20, Gdx.graphics.getHeight()/2-chosenTexture.getHeight()/2);
			bitmapFont.draw(batch,"Back",100-20+200/4+23,87);
			bitmapFont.draw(batch,"Next",423,87);
			batch.end();
		}
	}
	void stage2Mouse(){
		if(stage==2){
			if(!checkTouch){
				if(Gdx.input.getX()>=100-20+200/4)
					if(Gdx.input.getX()<=100-20+200/4+100)
						if(Gdx.graphics.getHeight() - Gdx.input.getY()>=50)
							if(Gdx.graphics.getHeight() - Gdx.input.getY()<=110){
								backToStage0();
								checkTouch=true;
							}
			}
			if(!checkTouch){
				if(Gdx.input.getX()>=400)
					if(Gdx.input.getX()<=500)
						if(Gdx.graphics.getHeight() - Gdx.input.getY()>=50)
							if(Gdx.graphics.getHeight() - Gdx.input.getY()<=110){
								goToStage3();
								checkTouch=true;
							}
			}

			if(Gdx.input.getX()>=300)
				if(Gdx.input.getX()<=555)
					if(Gdx.graphics.getHeight() - Gdx.input.getY()>110)
						cGX = Gdx.input.getX();

		}

	}
	void backToStage0(){
		chosenTexture.dispose();
		chosenGraph.dispose();
		stage=0;
	}
	void goToStage3(){
		stage=3;
	}

	private Pixmap setBlackAndWhite(Pixmap pixmap){
		Pixmap setterBW = new Pixmap(pixmap.getWidth(),pixmap.getHeight(),pixmap.getFormat());

		Color color = new Color(Color.rgba8888(1,1,1,1));

		for (int i=0;i<setterBW.getWidth();i++)
			for (int j=0;j<setterBW.getHeight();j++){
				Color.rgba8888ToColor(color,pixmap.getPixel(i,j));
				float bW = (color.r+color.g+color.b)/3f;
				setterBW.drawPixel(i,j,Color.rgba8888(bW,bW,bW,1));

			}

		return setterBW;
	}
	private Pixmap writeGraph(Pixmap pixmap){
		int w = 256,h = 200;
		Pixmap pixmapGraph = new Pixmap(w,h,pixmap.getFormat());
		Color color = new Color(Color.rgba8888(1,1,1,1));
		int[] cells = new int[256];

		for (int i=0;i<pixmap.getWidth();i++)
			for (int j=0;j<pixmap.getHeight();j++){
				Color.rgba8888ToColor(color,pixmap.getPixel(i,j));
				cells[(int)(((color.r+color.g+color.b)/3f)*255f)]++;
			}
		int maxCel=0;
		for (int i =0;i<cells.length;i++)
			if(cells[i]>maxCel)maxCel=cells[i];

		for (int i=0;i<w;i++)
			if(maxCel>0)
			for (int j=0;j<cells[i]/(maxCel/(double)h);j++)//cells[i]/(maxCel/h)
				pixmapGraph.drawPixel(i,h-1-j,Color.rgba8888(1,1,1,1));
		return pixmapGraph;
	}

	void stage3Preproc(){
		if(stage==3) {
			Color color = new Color(Color.rgba8888(1,1,1,1));
			summPix = 0;


			Pixmap greenPixels = new Pixmap(colorPicturePixmap.getWidth(),colorPicturePixmap.getHeight(),colorPicturePixmap.getFormat());
			Pixmap noGreenPixels = new Pixmap(colorPicturePixmap.getWidth(),colorPicturePixmap.getHeight(),colorPicturePixmap.getFormat());
			for (int i=0;i<colorPicturePixmap.getWidth();i++)
				for (int j=0;j<colorPicturePixmap.getHeight();j++)
					if(masPixLeaves[i][j]){
						greenPixels.drawPixel(i,j,colorPicturePixmap.getPixel(i,j));
						summPix++;
						Color.rgba8888ToColor(color,colorPicturePixmap.getPixel(i,j));

						if(color.r>=color.g) {
							noGreenPixels.drawPixel(i, j, colorPicturePixmap.getPixel(i, j));
						}
						if(color.b>=color.g) {
							noGreenPixels.drawPixel(i, j, colorPicturePixmap.getPixel(i, j));
						}

					}
			masRedPixelsPixmap = writeGraphRed(noGreenPixels);
			masRedPixelsTexture = new Texture(masRedPixelsPixmap);
			masBluePixelsPixmap = writeGraphBlue(noGreenPixels);
			masBluePixelsTexture = new Texture(masBluePixelsPixmap);

			colorPicture.draw(greenPixels,0,0);
			colorPictureNoGreen.draw(noGreenPixels,0,0);
			noGreenPixelsPixmap = noGreenPixels;

			cGXRed = 100-20;
			cGYRed = Gdx.graphics.getHeight()/2+masRedPixelsTexture.getHeight()/2+160;
			cGXBlue = 100-20;
			cGYBlue = Gdx.graphics.getHeight()/2+masRedPixelsTexture.getHeight()/2+100;

			stage=4;
		}
	}
	void stage4Mouse(){
		if(stage==4){
			if(!checkTouch){
				if(Gdx.input.getX()>=100-20+200/4)
					if(Gdx.input.getX()<=100-20+200/4+100)
						if(Gdx.graphics.getHeight() - Gdx.input.getY()>=50)
							if(Gdx.graphics.getHeight() - Gdx.input.getY()<=110){
								backToStage1();
								checkTouch=true;
							}
			}
			if(!checkTouch){
				if(Gdx.input.getX()>=400)
					if(Gdx.input.getX()<=540)
						if(Gdx.graphics.getHeight() - Gdx.input.getY()>=50)
							if(Gdx.graphics.getHeight() - Gdx.input.getY()<=110){
								saveGallery();
								checkTouch=true;
							}
			}
			if(Gdx.input.getX()>=100-20)
				if(Gdx.input.getX()<100-20+500)
					if(Gdx.graphics.getHeight() - Gdx.input.getY()>Gdx.graphics.getHeight()/2+masRedPixelsTexture.getHeight()/2+160)
						cGXRed = Gdx.input.getX();
			if(Gdx.input.getX()>=100-20)
				if(Gdx.input.getX()<100-20+500)
					if(Gdx.graphics.getHeight() - Gdx.input.getY()>Gdx.graphics.getHeight()/2+masRedPixelsTexture.getHeight()/2+100)
						if(Gdx.graphics.getHeight() - Gdx.input.getY()<Gdx.graphics.getHeight()/2+masRedPixelsTexture.getHeight()/2+150)
							cGXBlue = Gdx.input.getX();

		}

	}
	void backToStage1(){
		stage=1;
	}

	void stage4Render(){
		if(stage==4) {

			Pixmap masRedPixelsPixmapRed = new Pixmap(masRedPixelsPixmap.getWidth(),masRedPixelsPixmap.getHeight(),masRedPixelsPixmap.getFormat());
			for (int i=0;i<masRedPixelsPixmap.getWidth();i++)
				for (int j=0;j<masRedPixelsPixmap.getHeight();j++)
					if(i<=cGXRed-(100-20))
						if (masRedPixelsPixmap.getPixel(i,j)!=255)
							masRedPixelsPixmapRed.drawPixel(i,j,Color.rgba8888(1,0,0,1));
						else;
					else 	masRedPixelsPixmapRed.drawPixel(i,j,masRedPixelsPixmap.getPixel(i,j));


			masRedPixelsTexture.draw(masRedPixelsPixmapRed,0,0);
			masRedPixelsPixmapRed.dispose();

			Pixmap masBluePixelsPixmapBlue = new Pixmap(masBluePixelsPixmap.getWidth(),masBluePixelsPixmap.getHeight(),masBluePixelsPixmap.getFormat());
			for (int i=0;i<masBluePixelsPixmap.getWidth();i++)
				for (int j=0;j<masBluePixelsPixmap.getHeight();j++)
					if(i<=cGXBlue-(100-20))
						if (masBluePixelsPixmap.getPixel(i,j)!=255)
							masBluePixelsPixmapBlue.drawPixel(i,j,Color.rgba8888(1,0,0,1));
						else;
					else 	masBluePixelsPixmapBlue.drawPixel(i,j,masBluePixelsPixmap.getPixel(i,j));


			masBluePixelsTexture.draw(masBluePixelsPixmapBlue,0,0);
			masBluePixelsPixmapBlue.dispose();

			Color color = new Color(Color.rgba8888(1,1,1,1));
			corrSummPix = 0;
			outputSum="";

			Pixmap noGreenPixelsPixmapEnd = new Pixmap(noGreenPixelsPixmap.getWidth(),noGreenPixelsPixmap.getHeight(),noGreenPixelsPixmap.getFormat());
			for (int i=0;i<noGreenPixelsPixmap.getWidth();i++)
				for (int j=0;j<noGreenPixelsPixmap.getHeight();j++){
					noGreenPixelsPixmapEnd.drawPixel(i,j,noGreenPixelsPixmap.getPixel(i,j));
					Color.rgba8888ToColor(color,noGreenPixelsPixmap.getPixel(i,j));

					if(cGXRed>(100-20))
					if(color.r/color.g*100f<=cGXRed-(100-20)){
						noGreenPixelsPixmapEnd.drawPixel(i,j,Color.rgba8888(0,0,0,1));
					}
					if(cGXBlue>(100-20))
					if(color.b/color.g*100f<=cGXBlue-(100-20)){
						noGreenPixelsPixmapEnd.drawPixel(i,j,Color.rgba8888(0,0,0,1));
					}
					if(noGreenPixelsPixmapEnd.getPixel(i,j)!=255)corrSummPix++;
				}

			colorPictureNoGreen.draw(noGreenPixelsPixmapEnd,0,0);
			noGreenPixelsPixmapEnd.dispose();

			outputSum+="Affected "+corrSummPix+" pel, selected "+summPix+" pel. \nLeaf damage percentage - "+ Math.round(corrSummPix/(double)summPix*100d)+" %";


			batch.begin();
			batch.draw(colorPicture, 100-20, Gdx.graphics.getHeight()/2-chosenTexture.getHeight()/2);
			batch.draw(colorPictureNoGreen, 350, Gdx.graphics.getHeight()/2-chosenTexture.getHeight()/2);
			batch.draw(masRedPixelsTexture, 100-20, Gdx.graphics.getHeight()/2+masRedPixelsTexture.getHeight()/2+160);
			batch.draw(masBluePixelsTexture, 100-20, Gdx.graphics.getHeight()/2+masRedPixelsTexture.getHeight()/2+100);
			bitmapFont.draw(batch,outputSum,120,Gdx.graphics.getHeight()/2-100+220);
			bitmapFont.draw(batch,"Back",100-20+200/4+23,87);
			bitmapFont.draw(batch,"Save",450,87);
			batch.end();

			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			shapeRenderer.rect(100-20+200/4,50,100,60);
			shapeRenderer.rect(400,50,140,60);
			shapeRenderer.setColor(Color.PURPLE);
			shapeRenderer.rect(cGXRed,cGYRed,20,20);
			shapeRenderer.rect(cGXBlue,cGYBlue,20,20);
			shapeRenderer.setColor(Color.WHITE);
			shapeRenderer.end();

		}
	}
	private Pixmap writeGraphRed(Pixmap pixmap){
		int w = 500,h = 50;
		Pixmap pixmapGraph = new Pixmap(w,h,pixmap.getFormat());
		Color color = new Color(Color.rgba8888(1,1,1,1));
		int[] cells = new int[w];

		int targetPixel;

		for (int i=0;i<pixmap.getWidth();i++)
			for (int j=0;j<pixmap.getHeight();j++){
				Color.rgba8888ToColor(color,pixmap.getPixel(i,j));
				targetPixel = (int)((color.r/color.g)*100f);//разница между красными и зелеными в процентах, до 200 процентов
				if(targetPixel<w&&targetPixel>1)//больше, тк мы уже исключили все пиксели меньше 100% r>g
					cells[targetPixel]++;
			}
		int maxCel=0;
		for (int i =0;i<cells.length;i++)
			if(cells[i]>maxCel)maxCel=cells[i];

		for (int i=0;i<w;i++)
			if(maxCel>0)
			for (int j=0;j<cells[i]/(maxCel/(double)h);j++)//cells[i]/(maxCel/h)
				pixmapGraph.drawPixel(i,h-1-j,Color.rgba8888(1,1,1,1));
		return pixmapGraph;
	}
	private Pixmap writeGraphBlue(Pixmap pixmap){
		int w = 500,h = 50;
		Pixmap pixmapGraph = new Pixmap(w,h,pixmap.getFormat());
		Color color = new Color(Color.rgba8888(1,1,1,1));
		int[] cells = new int[w];

		int targetPixel;

		for (int i=0;i<pixmap.getWidth();i++)
			for (int j=0;j<pixmap.getHeight();j++){
				Color.rgba8888ToColor(color,pixmap.getPixel(i,j));
				targetPixel = (int)((color.b/color.g)*100f);//разница между синими и зелеными в процентах, до 200 процентов
				if(targetPixel<w&&targetPixel>1)//больше, тк мы уже исключили все пиксели меньше 100% b>g
					cells[targetPixel]++;
			}
		int maxCel=0;
		for (int i =0;i<cells.length;i++)
			if(cells[i]>maxCel)maxCel=cells[i];

		for (int i=0;i<w;i++)
			if(maxCel>0)
				for (int j=0;j<cells[i]/(maxCel/(double)h);j++)//cells[i]/(maxCel/h)
					pixmapGraph.drawPixel(i,h-1-j,Color.rgba8888(1,1,1,1));
		return pixmapGraph;
	}

	public Pixmap flipPixmap(Pixmap src) {
		final int width = src.getWidth();
		final int height = src.getHeight();
		Pixmap flipped = new Pixmap(width, height, src.getFormat());

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				flipped.drawPixel(x, y, src.getPixel(0, 0));
			}
		}
		return flipped;
	}

	Pixmap getScreenshot(int x, int y, int w, int h, boolean yDown){
		Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(x, y, w, h);

		/*	if (yDown) {
				// Переворачиваем изображение, так как оно должно храниться в перевернутом формате.
				ByteBuffer pixels = pixmap.getPixels();
				int numBytes = w * h * 4;
				byte[] lines = new byte[numBytes];
				int numBytesPerLine = w * 4;
				for (int i = 0; i < h; i++) {
					pixels.position((h - i - 1) * numBytesPerLine);
					pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
				}
				pixels.clear();
				pixels.put(lines);
			}*/

		return pixmap;
	}


	void saveGallery(){

			try{
				//сохраняем оригинал
				FileHandle from = new FileHandle(imagesPath.get(chosenPicture));
				byte[] data = from.readBytes();
				FileHandle to = new FileHandle(Gdx.files.getLocalStoragePath()+"/Gallery with originals/"+from.name());
				to.writeBytes(data,true);
				//сохраняем результаты проверки
				FileHandle fh1 = new FileHandle(Gdx.files.getLocalStoragePath()+"/Gallery with tests only/"+from.nameWithoutExtension()+".png");
				Pixmap pixmap = getScreenshot(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
				PixmapIO.writePNG(fh1, pixmap,0,true);
				pixmap.dispose();

				//сохраняем процент поражения
				FileHandle f4 = new FileHandle(Gdx.files.getLocalStoragePath()+"/Gallery with description/"+from.nameWithoutExtension()+".txt");

				String score = "leaf damage percentage "+Math.round(corrSummPix/(double)summPix*100d)+"%"+"\n"+"Add Description";
				f4.writeString(score, false);

			}catch (Exception e){
			}
	}
	//предподготовка галереи
	void stage5Preproc(){
		if(stage==5) {
			currentGalleryImage=0;
			if(galleryPath.size()>0)
			galleryPath.clear();

			FileHandle pixmapHandle = Gdx.files.internal(Gdx.files.getLocalStoragePath()+"/Gallery with tests only/");
			for (FileHandle entry: pixmapHandle.list())
				if("jpg".equals(entry.toString().substring(entry.toString().length()-3))||"png".equals(entry.toString().substring(entry.toString().length()-3)))
					galleryPath.add(entry.toString());//добавляем путь картинки

			if (galleryPath.size()>0){
				galleryTexture = new Texture(galleryPath.get(currentGalleryImage));
				Pixmap pixmap = new Pixmap(170,75, Pixmap.Format.RGBA8888);
				pixmap.setColor(95/255f, 48/255f, 90/255f, 1);
				pixmap.fill();
				purpleBox = new Texture(pixmap);

				FileHandle fh = new FileHandle(galleryPath.get(currentGalleryImage));
				String galleryName ="/Gallery with tests only/"+fh.name();
				String pathGallery = galleryPath.get(currentGalleryImage).substring(0,galleryPath.get(currentGalleryImage).length()-galleryName.length());

				File f4 = new File(pathGallery+"/Gallery with description/"+fh.nameWithoutExtension()+".txt");
				ArrayList<String> arrayList = new ArrayList<>();

				String line = null;
				try(BufferedReader br = new BufferedReader(new FileReader(f4))) {
					while((line = br.readLine()) != null){
						arrayList.add(line);
					}
				} catch (FileNotFoundException e) {

				} catch (IOException e) {

				}


				descriptionString="";
				for(int i=1;i<arrayList.size();i++)descriptionString+=arrayList.get(i)+"\n";

			}

			stage=6;
		}
	}
	//отображение галереи
	void stage6Render(){
		if(stage==6) {

			batch.begin();
			if (galleryPath.size()>0){
				batch.draw(galleryTexture,0,0);
				batch.draw(purpleBox,400,40);
			}


			batch.end();


			batch.begin();
			if(galleryPath.size()>0)
			bitmapFont.draw(batch,currentGalleryImage+1+"/"+(galleryPath.size()),20,Gdx.graphics.getHeight()-20);
			else
			bitmapFont.draw(batch,currentGalleryImage+"/"+(galleryPath.size()),20,Gdx.graphics.getHeight()-20);
			bitmapFont.draw(batch,"Delete",7,Gdx.graphics.getHeight()-60);
			bitmapFont.draw(batch,"Change description",342,80);
			bitmapFont.draw(batch,descriptionString,120,Gdx.graphics.getHeight()/2-100+180);
			bitmapFont.draw(batch,"Back",100-20+200/4+23,87);
			batch.end();

			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			shapeRenderer.rect(0,Gdx.graphics.getHeight()/2-50,60,100);
			shapeRenderer.rect(Gdx.graphics.getWidth()-61,Gdx.graphics.getHeight()/2-50,60,100);
			shapeRenderer.rect(1,Gdx.graphics.getHeight()-80,77,30);

			shapeRenderer.line(0,Gdx.graphics.getHeight()/2,40,Gdx.graphics.getHeight()/2);
			shapeRenderer.line(0,Gdx.graphics.getHeight()/2,20,Gdx.graphics.getHeight()/2+20);
			shapeRenderer.line(0,Gdx.graphics.getHeight()/2,20,Gdx.graphics.getHeight()/2-20);

			shapeRenderer.line(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()/2,Gdx.graphics.getWidth()-40,Gdx.graphics.getHeight()/2);
			shapeRenderer.line(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()/2,Gdx.graphics.getWidth()-20,Gdx.graphics.getHeight()/2+20);
			shapeRenderer.line(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()/2,Gdx.graphics.getWidth()-20,Gdx.graphics.getHeight()/2-20);

			shapeRenderer.rect(340,57,200,30);

			shapeRenderer.rect(100-20+200/4,50,100,60);
			shapeRenderer.end();

			stage1.draw();


		}
	}
	void stage6Mouse(){
		if(stage==6){
			if(!checkTouch){
				if(Gdx.input.getX()>=100-20+200/4)
					if(Gdx.input.getX()<=100-20+200/4+100)
						if(Gdx.graphics.getHeight() - Gdx.input.getY()>=50)
							if(Gdx.graphics.getHeight() - Gdx.input.getY()<=110){
								if(galleryPath.size()>0)
								galleryTexture.dispose();
								stage=0;
								checkTouch=true;
							}
			}

			if(!checkTouch){
				if(Gdx.input.getX()>=Gdx.graphics.getWidth()-61)
					if(Gdx.input.getX()<=Gdx.graphics.getWidth())
						if(Gdx.graphics.getHeight() - Gdx.input.getY()>=Gdx.graphics.getHeight()/2-50)
							if(Gdx.graphics.getHeight() - Gdx.input.getY()<=Gdx.graphics.getHeight()/2-50+100){
								if (currentGalleryImage<galleryPath.size()-1)
								currentGalleryImage++;
								if(galleryPath.size()>0){
									galleryTexture.dispose();
									galleryTexture = new Texture(galleryPath.get(currentGalleryImage));
								}
								if (galleryPath.size()>0){
									galleryTexture = new Texture(galleryPath.get(currentGalleryImage));
									Pixmap pixmap = new Pixmap(170,75, Pixmap.Format.RGBA8888);
									pixmap.setColor(95/255f, 48/255f, 90/255f, 1);
									pixmap.fill();
									purpleBox = new Texture(pixmap);

									FileHandle fh = new FileHandle(galleryPath.get(currentGalleryImage));
									String galleryName ="/Gallery with tests only/"+fh.name();
									String pathGallery = galleryPath.get(currentGalleryImage).substring(0,galleryPath.get(currentGalleryImage).length()-galleryName.length());

									File f4 = new File(pathGallery+"/Gallery with description/"+fh.nameWithoutExtension()+".txt");
									ArrayList<String> arrayList = new ArrayList<>();

									String line = null;
									try(BufferedReader br = new BufferedReader(new FileReader(f4))) {
										while((line = br.readLine()) != null){
											arrayList.add(line);
										}
									} catch (FileNotFoundException e) {

									} catch (IOException e) {

									}


									descriptionString="";
									for(int i=1;i<arrayList.size();i++)descriptionString+=arrayList.get(i)+"\n";

								}
								checkTouch=true;
							}
			}
			if(!checkTouch){
				if(Gdx.input.getX()>=0)
					if(Gdx.input.getX()<=60)
						if(Gdx.graphics.getHeight() - Gdx.input.getY()>=Gdx.graphics.getHeight()/2-50)
							if(Gdx.graphics.getHeight() - Gdx.input.getY()<=Gdx.graphics.getHeight()/2-50+100){
								if (currentGalleryImage>0)
									currentGalleryImage--;
								if(galleryPath.size()>0){
									galleryTexture.dispose();
									galleryTexture = new Texture(galleryPath.get(currentGalleryImage));
								}
								if (galleryPath.size()>0){
									galleryTexture = new Texture(galleryPath.get(currentGalleryImage));
									Pixmap pixmap = new Pixmap(170,75, Pixmap.Format.RGBA8888);
									pixmap.setColor(95/255f, 48/255f, 90/255f, 1);
									pixmap.fill();
									purpleBox = new Texture(pixmap);

									FileHandle fh = new FileHandle(galleryPath.get(currentGalleryImage));
									String galleryName ="/Gallery with tests only/"+fh.name();
									String pathGallery = galleryPath.get(currentGalleryImage).substring(0,galleryPath.get(currentGalleryImage).length()-galleryName.length());

									File f4 = new File(pathGallery+"/Gallery with description/"+fh.nameWithoutExtension()+".txt");
									ArrayList<String> arrayList = new ArrayList<>();

									String line = null;
									try(BufferedReader br = new BufferedReader(new FileReader(f4))) {
										while((line = br.readLine()) != null){
											arrayList.add(line);
										}
									} catch (FileNotFoundException e) {

									} catch (IOException e) {

									}


									descriptionString="";
									for(int i=1;i<arrayList.size();i++)descriptionString+=arrayList.get(i)+"\n";

								}
								checkTouch=true;
							}
			}
			if(!checkTouch){
				if(Gdx.input.getX()>=1)
					if(Gdx.input.getX()<=78)
						if(Gdx.graphics.getHeight() - Gdx.input.getY()>=Gdx.graphics.getHeight()-80)
							if(Gdx.graphics.getHeight() - Gdx.input.getY()<=Gdx.graphics.getHeight()-80+30){

								if(galleryPath.size()>0){
									galleryTexture.dispose();

									FileHandle fh = new FileHandle(galleryPath.get(currentGalleryImage));
									String galleryName ="/Gallery with tests only/"+fh.name();
									String pathGallery = galleryPath.get(currentGalleryImage).substring(0,galleryPath.get(currentGalleryImage).length()-galleryName.length());

									File f1 = new File(pathGallery+"/Gallery with originals/"+fh.nameWithoutExtension()+".jpg");
									f1.delete();
									f1 = new File(pathGallery+"/Gallery with tests only/"+fh.nameWithoutExtension()+".png");
									f1.delete();
									f1 = new File(pathGallery+"/Gallery with description/"+fh.nameWithoutExtension()+".txt");
									f1.delete();

								}
								stage=0;
								checkTouch=true;
							}
			}
			if(!checkTouch){
				if(Gdx.input.getX()>=340)
					if(Gdx.input.getX()<=540)
						if(Gdx.graphics.getHeight() - Gdx.input.getY()>=57)
							if(Gdx.graphics.getHeight() - Gdx.input.getY()<=87){
								if(galleryPath.size()>0){
									FileHandle fh = new FileHandle(galleryPath.get(currentGalleryImage));
									String galleryName ="/Gallery with tests only/"+fh.name();
									String pathGallery = galleryPath.get(currentGalleryImage).substring(0,galleryPath.get(currentGalleryImage).length()-galleryName.length());

									File f4 = new File(pathGallery+"/Gallery with description/"+fh.nameWithoutExtension()+".txt");
									ArrayList<String> arrayList = new ArrayList<>();

									String line = null;
									int counter=0;
									try(BufferedReader br = new BufferedReader(new FileReader(f4))) {
										while((line = br.readLine()) != null){
											if (counter<2)
											arrayList.add(line);
											counter++;
										}
										arrayList.set(1,textField.getText());
									} catch (FileNotFoundException e) {

									} catch (IOException e) {

									}


									String s="";
									for(int i=0;i<arrayList.size();i++)s+=arrayList.get(i)+"\n";

									FileWriter writer;

									try {
										writer = new FileWriter(f4);
										writer.append(s);
										writer.flush();
										writer.close();
									} catch (IOException e) {
										e.printStackTrace();
									}


								}

								if (galleryPath.size()>0){
									galleryTexture = new Texture(galleryPath.get(currentGalleryImage));
									Pixmap pixmap = new Pixmap(170,75, Pixmap.Format.RGBA8888);
									pixmap.setColor(95/255f, 48/255f, 90/255f, 1);
									pixmap.fill();
									purpleBox = new Texture(pixmap);

									FileHandle fh = new FileHandle(galleryPath.get(currentGalleryImage));
									String galleryName ="/Gallery with tests only/"+fh.name();
									String pathGallery = galleryPath.get(currentGalleryImage).substring(0,galleryPath.get(currentGalleryImage).length()-galleryName.length());

									File f4 = new File(pathGallery+"/Gallery with description/"+fh.nameWithoutExtension()+".txt");
									ArrayList<String> arrayList = new ArrayList<>();

									String line = null;
									try(BufferedReader br = new BufferedReader(new FileReader(f4))) {
										while((line = br.readLine()) != null){
											arrayList.add(line);
										}
									} catch (FileNotFoundException e) {

									} catch (IOException e) {

									}


									descriptionString="";
									for(int i=1;i<arrayList.size();i++)descriptionString+=arrayList.get(i)+"\n";

								}

								checkTouch=true;
							}
			}

		}

	}

}
