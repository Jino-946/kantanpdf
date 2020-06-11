package org.m946.kantanpdf;


import java.awt.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfOutline;

/**
* ・ KantanPDFについて
* <pre>
*  1) 座標系は用紙の左上を原点とする。
*  2) 単位はpointを使用する
*    1 point = 1 unit = 1/72 inch
*    1inch = 25.4mm = 72 unit
*  3) メートルまたはインチからpointに変換するには
*     DPIUtilのメソッドをスタティックインポートして利用する
*     import static org.m946.kantanpdf.DPIUtil.*;
*     float point = cm(5);
* 
* 
* 
*KantanDF pdf = new KantanPDFiTextImpl(PageSize.A4);
*pdf.newPage()
*   .moveTo(cm(1), cm(10))
*   .lineTo(cm(20), cm(10))
*   .setTextAlign(EasyPDF.TEXTPOS_LL)
*   .textOut(cm(5), cm(10), "TEXTPOS_LL")
*   .setTextAlign(EasyPDF.TEXTPOS_UL)
*   .textOut(cm(5), cm(10),"TEXTPOS_UL")
*   .setTextAlign(EasyPDF.TEXTPOS_ML)
*   .textOut(cm(8), cm(10), "TEXTPOS_ML");
*		
*pdf.createFile("textalign.pdf");
* </pre>
*/

public interface KantanPDF {
	//水平・垂直方向のテキストアライン
	/** lower left */
	int TEXTPOS_LL = 0;
	/** lower center */
	int TEXTPOS_LC = 1;
	/** lower right */
	int TEXTPOS_LR = 2;
	/** middle left */
	int TEXTPOS_ML = 3;
	/** middle center */
	int TEXTPOS_MC = 4;
	/** middle right */
	int TEXTPOS_MR = 5;
	/** upper left */
	int TEXTPOS_UL = 6;
	/** upper center */
	int TEXTPOS_UC = 7;
	/** upper right */
	int TEXTPOS_UR = 8;
	DateTimeFormatter DEFAULT_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	DateTimeFormatter DEFAULT_DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

	/**
	 * 作成したPDFをbyte配列に変換する。
	 * 
	 * @return 変換されたbyte配列
	 */
	byte[] getByteArray();

	/**
	 * 作成したPDFをファイルに出力する。
	 * 
	 * @param filename 出力するファイル名
	 */
	void createFile(String filename);

	/**
	 *  PDF作成を終了する。
	 * 
	 */
	void close();

	/**
	 * 現在のテキストアライン、フォントサイズを使用して、指定した座標に文字列を出力する。<br>
	 * 
	 * 
	 * @param x　x座標値
	 * @param y  y座標値
	 * @param text 出力する文字列
	 */	
	KantanPDF textOut(float x, float y, String text);

	/**
	 * 指定した座標に左揃えで文字列を出力する。
	 * 
	 * @param x
	 * @param y
	 * @param text
	 */
	KantanPDF textOutL(float x, float y, String text);

	/**
	 * 指定した座標に中央揃えで文字列を出力する。 
	 * 
	 * 
	 * @param x 
	 * @param y
	 * @param text
	 */
	KantanPDF textOutC(float x, float y, String text);

	/**
	 * 指定した座標に右揃えで文字列を出力する。
	 * 
	 * @param x
	 * @param y
	 * @param text
	 */
	KantanPDF textOutR(float x, float y, String text);

	/**
	 * double値を指定した座標にカンマ区切り、右揃えで出力する。
	 * 
	 * 
	 * @param x
	 * @param y
	 * @param val
	 */
	KantanPDF textOut(float x, float y, double val);

	/**
	 * long値を指定した座標にカンマ区切り、右揃えで出力する。
	 * 
	 * @param x
	 * @param y
	 * @param val
	 */
	KantanPDF textOut(float x, float y, long val);

	/**
	 * java.time.LocalDate値を指定した座標に中央揃えyyyy/mm/dd形式で出力する。
	 * 
	 * @param x
	 * @param y
	 * @param date
	 */
	KantanPDF textOut(float x, float y, LocalDate date);

	/**
	 * ページ番号を出力する。
	 * 
	 * @param page
	 * @return
	 */
	KantanPDF printPageNum(int page);

	/**
	 * 改ページする。
	 * 
	 */
	KantanPDF newPage();

	/**
	 * 指定した座標に移動する。
	 * 
	 * 
	 * @param x
	 * @param y
	 */
	KantanPDF moveTo(float x, float y);

	/**
	 * 指定した座標まで直線を引く。
	 * 
	 * 
	 * @param x
	 * @param y
	 */
	KantanPDF lineTo(float x, float y);

	/**
	 * 左上と右下の座標を指定し、矩形を描く
	 * 
	 * 
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	KantanPDF rectangle(float left, float top, float right, float bottom);

	/**
	 * 左上、右下の座標と背景色を指定し、矩形を描く 
	 * 
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 * @param red
	 * @param green
	 * @param blue
	 */
	KantanPDF rectangle(float left, float top, float right, float bottom, int red, int green, int blue);

	/**
	 * 左上、右下の座標と背景色を指定し、矩形を描く 
	 * 
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 * @param col
	 */
	KantanPDF rectangle(float left, float top, float right, float bottom, Color col);

	/**
	 * 現在ページに章を追加する。
	 * 
	 * @param title
	 */
	PdfOutline addChapter(String title);

	/**
	 * 現在の章に節を追加する。
	 * 
	 * @param chapter
	 * @param title
	 */
	PdfOutline addSection(PdfOutline chapter, String title);

	/**
	 * 指定した座標を左上にし、ファイルから読み込んだイメージを出力する。
	 * 
	 * 
	 * @param filename イメージファイルのパス
	 * @param x 座標
	 * @param y 座標
	 * @param scale 縮小率 (scale==2 縦横とも1/2) 
	 */
	KantanPDF imageOut(String filename, float x, float y, float scale);

	/**
	 * 指定した座標を左上にしファイルから読み込んだイメージを等倍で出力する。
	 * 
	 * @param filename イメージファイルのパス
	 * @param x 座標
	 * @param y 座標
	 */
	KantanPDF imageOut(String filename, float x, float y);

	/**
	 * 指定した座標を左上にしbyte配列に変換されたイメージを出力する。
	 * 
	 * 
	 * @param image 出力イメージ
	 * @param x
	 * @param y
	 * @param scale 縮小率
	 */
	KantanPDF imageOut(byte[] image, float x, float y, float scale);

	/**
	 * 指定した座標を左上にしbyte配列に変換されたイメージを等倍で出力する。
	 * 
	 * @param image 出力イメージ
	 * @param x
	 * @param y
	 */
	KantanPDF imageOut(byte[] image, float x, float y);

	/* accessors*/
	KantanPDF setFontSize(int fontSize);

	KantanPDF setTextAlign(int textAlign);

	KantanPDF setFontMincho();

	KantanPDF setFontGothic();

	KantanPDF setLineWidth(float lineWidth);

	KantanPDF setLeftMargin(float lm);

	KantanPDF setTopMargin(float tm);

	KantanPDF setUseEmbeddedFont(boolean value);
	
	Font getFont();

}
