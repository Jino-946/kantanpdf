package org.m946.kantanpdf;

import static org.m946.kantanpdf.DPIUtil.*;
import org.m946.kantanpdf.Point;
import java.awt.Color;
import java.io.IOException;
import java.util.List;

import com.lowagie.text.Rectangle;



/**
 * 表形式のPDFを作成する抽象クラス<br/><br/>
 * 
 * 印刷したいPOJOのフィールドににColumnAnnotationで注釈し、AbstractPDFTableクラスを
 * 拡張したクラスで2つの抽象メソッドを実装するだけで、ある程度見栄えのする表形式のPDFを作成する。
 * 
 * 
 * @author jino946
 *
 * @param <T> 印刷するPOJO
 */
@lombok.extern.slf4j.Slf4j
public abstract class AbstractPDFTable<T> {
	/** 印刷するデータ */
	protected  List<T> data;
	/** PDF操作クラス */
	protected static KantanPDF pdf;
	/** テーブルの左上座標 */
	protected Point tableTopLeft  = new Point(cm(1.5), cm(3.0));
	/** 列幅のリスト org.m946.kantanpdf.DPIUtil.* をstatic importすることでcm,inchからpointに変換できる */
	protected List<Float> colWidths = null;
	/** 列数 */ 
	protected int colCount;
	/** 固定列名のリスト */
	protected List<String> fixedColNames = null;
	/** 固定行を除くグリッドの行数 */
	protected int rowCount;
	/** 行高 */
	protected float rowHeight = inch(0.2);
	/** 出力中のページ */
	protected int page = 1;
	/** 帳票タイトル */
	protected String title;
	/** 濃い色 */
	protected Color darkColor = new Color(0x7e, 0xce, 0xf4);   //固定行(列名)で使用する色
	/** 薄い色 */
	protected Color paleColor = new Color(0xd3, 0xed, 0xfb);   //データ行を一行置き色付けする時に使用する色
	/** Double、Longの値を1000で割り、丸める*/
	protected boolean div1000 = false;
	/** 用紙サイズ */
	protected Rectangle paperSize;
	/** ロゴの格納パス */
	protected String logoPath = null; 
	/** ロゴの左上座標 */
	protected Point logoTopLeft;
	/** gridTopLeft をデフォルトから変更した時は true にセットされる */
	protected boolean topLeftChanged = false;
	protected double imgScale = 1d;
	protected int fontSizeM = 10;
	protected int fontSizeL = 16;

	/**
	 * 固定列名や列幅などの列の状態を設定する。<br>
	 * <pre>
	 * 	colWidths = Arrays.asList(cm(1), cm(3), cm(5), ....);
	 *	colCount = colWidths.size();
	 *	fixedColNames = Arrays.asList( "ほげ", "ふが", "ぴよ", ....);
     * </pre>
	 */
	protected abstract void buildFixedColumns();
	
	/**
	 * x座標を移動しながら各列のデータを出力する<br>
	 * <pre>
	 *  // メソッドボディーの例
	 * 	float x = tableTopLeft.getX() + colWidths.get(0);
	 *	pdf.textOut(x, y, data.get(idx).getHogeField())
	 *	   .setTextAlign(TextAlign.LowerLeft)
	 *	   .textOut(x, y, data.get(idx).getFugaField());
	 *	x += colWidths.get(1);
	 *	pdf.textOut(x, y, data.get(idx).getPiyoField());
	 *	x += colWidths.get(2) + colWidths.get(3);
	 *	pdf.textOut(x, y, data.get(idx).getHogera());
	 *  ...
	 * </pre>
	 * @param y   出力するY座標
	 * @param idx 出力するbeanのインデックス
	 */
	protected abstract void printRow(float y, int idx);
	
	/**
	 * コンストラクタ<br>
	 * 
	 * 横向き用紙は第1引数で  com.itextpdf.text.PageSize.A4.rotate() のように指定する。 
	 * 
	 * @param paperSize  com.itextpdf.text.PageSize を使用して指定する。
	 * @param title 帳票タイトル
	 * @param data 印刷するデータ
	 */
	public AbstractPDFTable(Rectangle paperSize, String title, List<T> data){
		this.data = data;
		pdf = new KantanPDF(paperSize);
		this.paperSize = paperSize;
		this.title = title;
		rowCount = (int)((paperSize.getHeight() - cm(5.0)) / rowHeight);
	}

	/**
	 * テーブルの左X座標を調整する。<br>
	 * tableTopLeftをデフォルトから変更した時は、左上座標の調整は行わない。
	 * 
	 */
	protected void adjustTableLeft(){
		if (!topLeftChanged){
			float gridWidth = getTableRight() - tableTopLeft.getX();
			float left = (paperSize.getWidth() - gridWidth) / 2;
			tableTopLeft.setX(left);
		}
	}
	/**
	 * @param top 罫線の上端のY座標
	 * @return 最右端の罫線のX座標
	 */
	protected float drawVerticalLines(float top){
		float bottom = top + (rowCount + 1) * rowHeight;
		float x = tableTopLeft.getX();
		
		pdf.moveTo(x, top).lineTo(x, bottom);
		for (int i = 0; i < colWidths.size(); i++){
			x += colWidths.get(i);
			pdf.moveTo(x, top).lineTo(x, bottom);
			
		}
		return x;
	}
	
	/**
	 * 横罫線を引く
	 * 
	 * @param top 最上段の罫線のY座標
	 * @param right 罫線の右端のX座標
	 * @return 最下段の罫線のY座標
	 */
	protected float drawHolizontalLines(float top, float right){
		float left = tableTopLeft.getX();
		float y = top;
		
		pdf.moveTo(left, y).lineTo(right, y);
		for (int i = 0; i < rowCount + 1; i++){
			y += rowHeight;
			pdf.moveTo(left, y).lineTo(right, y);
		}
		return y;
	}
	
	/**
	 * グリッドの右端のX座標を取得する
	 * 
	 * 
	 * @return 右端のX座標
	 */
	protected float getTableRight(){
		float right = tableTopLeft.getX();
		for (int i = 0; i < colWidths.size(); i++){
			right += colWidths.get(i);
		}
		return right;
	}
	
	/**
	 * グリッドの下端のY座標を取得する
	 * 
	 * @return 下端のY座標
	 */
	protected float getTableBottom(){
		float bottom = tableTopLeft.getY();
		for (int i = 0; i < rowCount; i++){
			bottom += rowHeight;
		}
		return bottom;
	}

	/**
	 * 強調する行を着色する
	 */
	protected void colorRows(){
		float left = tableTopLeft.getX();
		float top = tableTopLeft.getY() - rowHeight;
		float bottom = top + rowHeight;
		float right = getTableRight();
		
		// 固定行を着色
		pdf.rectangle(left, top, right, bottom, darkColor);
	
		// 偶数行を着色
		for (int i = 0; i < rowCount ; i++){
			top += rowHeight;
			if (i % 2 != 0) {
				pdf.rectangle(left, top, right, top + rowHeight, paleColor);
			}
		}
	}
	
	/**
	 * グリッドの罫線を引く
	 */
	protected void drawRuledLines(){
		float right = drawVerticalLines(tableTopLeft.getY() - rowHeight);
		@SuppressWarnings("unused")
		float bottom = drawHolizontalLines(tableTopLeft.getY() - rowHeight, right);
	}
	
	/**
	 * グリッドに固定列名を出力する
	 */
	protected void printColNames(){
		float x = tableTopLeft.getX();
		float y = tableTopLeft.getY();
		float xPos = x;
		pdf.setTextAlign(TextAlign.LowerLeft).setFontSize(fontSizeM);
		for (int i = 0; i < colCount; i++){
			xPos = x + colWidths.get(i) / 2;
			pdf.textOut(xPos, y, fixedColNames.get(i));
			x += colWidths.get(i);
		}
	}	

	/**
	 * 拡張したクラスで実装されたprintRowメソッドを利用し、グリッドにデータを出力する
	 * 
	 * @param y グリッドの左上Y座標
	 */
	protected void printTableContents(float y){
		pdf.setFontSize(fontSizeM);
		for (int i = 0; i < rowCount; i++){
			int idx = rowCount * (page - 1) + i;
			if (idx == data.size()) return;
			printRow(y, idx);
			y += rowHeight;
		}
	}

	/**
	 * 帳票タイトルを出力する
	 */
	protected void printTitle(){
		pdf.setTextAlign(TextAlign.LowerCenter);
		pdf.setFontSize(fontSizeL);
		pdf.textOut(tableTopLeft.getX(), tableTopLeft.getY() - rowHeight - cm(0.2), title);
	}
	
	
	protected void printLogo() {
		if (logoPath != null){
			try {
				pdf.imageOut(logoPath, logoTopLeft.getX(), logoTopLeft.getY(), (float)imgScale);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}
	

	
	protected void printHeader(){
		printLogo();
		printTitle();
	}
	
	protected void printFooter(){
		pdf.printPageNumber();
	}
	
	
	/**
	 * 帳票1ページを出力する
	 */
	protected void printPage(){
		printHeader();
		colorRows();
		drawRuledLines();
		printColNames();
		printTableContents(tableTopLeft.getY() + rowHeight);
		printFooter();
		page++;
	}
			
	/**
	 * PDFを作成する
	 */
	protected void createPDF(){
		buildFixedColumns();
		adjustTableLeft();

		int pageCount = data.size() / rowCount ;
		if ((data.size() % rowCount ) > 0) pageCount++;
		if (pageCount > 0){
			printPage();
			for (int i = 1; i < pageCount; i++){
				pdf.newPage();
				printPage();
			}
		}
	}


	
	/**
	 * PDFを作成し、ファイルに書き出す
	 * 
	 * @param filename 書き出すファイルパス
	 * @throws IOException 
	 */
	public void create(String filename) throws IOException{
		createPDF();
		pdf.saveTo(filename);
	}
	

	/**
	 * PDFを作成し、byte配列に変換して返す
	 * 
	 * @return 変換されたbyte配列
	 */
	public byte[] create(){
		createPDF();
		return pdf.getByteArray();
	}
	

	/**
	 * doubleまたはlongのフィールド値を1000円単位に設定するフラグ
	 * 
	 * @param bool true:千円単位 false:そのまま
	 */
	public void setDiv1000(boolean bool){
		div1000 = bool;
	}
	

	
	/** 
	 * デフォルトの行高をheightに変更する
	 * 
	 * @param height 行高
	 */
	public void setRowHeight(float height){
		rowHeight = height;
	}
	
	
	
	/**
	 * デフォルトの「濃い色」をcolorに変更する 
	 * 
	 * @param color 行の色
	 */
	public void setDarkColor(Color color){
		darkColor = color;
	}
	
	
	/**
	 * デフォルトの「薄い色」をcolorに変更する
	 * @param color
	 */
	public void setPaleColor(Color color){
		paleColor = color;
	}

	
	/**
	 * デフォルトの行数をrowCountに変更する。
	 * 
	 * @param rowCount
	 */
	public void setRowCount(int rowCount){
		this.rowCount = rowCount;
	}
	
	
	/**
	 * グリッドの左上座標を設定する。<br>
	 * gridTopLeft をデフォルトから修正した時は adjustGridLeft()による
	 * 左端X座標の調整は行わない。
	 * @param topLeft kenta.commons.pdf.Point
	 */
	public void setTableTopLeft(Point topLeft){
		tableTopLeft = topLeft;
		topLeftChanged = true;
	}
	
	public void setLogo(String path, Point topLeft, double scale){
		logoPath = path;
		logoTopLeft = topLeft;
		imgScale = scale;
	}

	public void setFontSizeM(int fontSizeM){
		this.fontSizeM = fontSizeM;
	}
	
	
	public void setFontSizeL(int fontSizeL){
		this.fontSizeL = fontSizeL;
	}
}	
	
	
	
