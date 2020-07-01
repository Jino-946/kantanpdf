package org.m946.kantanpdf;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * POJOのフィールドに付ける注釈<br/><br/>
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
	/**
	 *  表に出力する順番
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
	 *  テキストアライン
	 */
	int textAlign();
}
