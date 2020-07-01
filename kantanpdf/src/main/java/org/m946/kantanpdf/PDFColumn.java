package org.m946.kantanpdf;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.lowagie.text.Element;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * PDFに出力したいPOJOのフィールドに付ける注釈<br/><br/>
 * 
 * POJOフィールドを表形式で出力するために列の順番、列幅、列名、文字位置を設定する。
 * 
 * 
 * @author jino946 
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PDFColumn {
	public static final int ALIGN_LEFT = Element.ALIGN_LEFT;
	public static final int ALIGN_CENTER = Element.ALIGN_CENTER;
	public static final int ALIGN_RIGHT = Element.ALIGN_RIGHT;
	
	/**
	 *  表に出力する順番(左から右)
	 */
	int order();
	
	
	/**
	 * 列幅(cm単位で指定する)
	 */
	float width();

	/**
	 * 列名
	 */
	String name();
	
	/**
	 *  テキストアライン(ALIGN_LEFT:0 ALIGN_CENTER:1 ALIGH_RIGHT:2)で指定する。
	 */
	int textAlign();
}
