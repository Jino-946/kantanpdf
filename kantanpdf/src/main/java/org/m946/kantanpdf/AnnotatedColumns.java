package org.m946.kantanpdf;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.m946.kantanpdf.ColumnAnnotation;

/**
 * ColumnAnnotationで注釈されたフィールド情報を格納するクラス 
 * 
 * 
 * @author jino946
 *
 * @param <T> ColumnAnnotationで注釈したPOJO
 */
@lombok.extern.slf4j.Slf4j
public class AnnotatedColumns<T> {
	private List<Field> annotatedFields;
	
	
	/**
	 * コンストラクタ
	 * 
	 * @param pojo
	 */
	public AnnotatedColumns(T pojo){
		Field[] declaredFields = pojo.getClass().getDeclaredFields();
		annotatedFields = new ArrayList<Field>();
		for (Field f : declaredFields){
			if (f.getAnnotation(ColumnAnnotation.class) != null){
				annotatedFields.add(f);
			}
		}
		Collections.sort(annotatedFields, comparator);
	}
	
	

	/**
	 * ColumnAnnotationをseqで指定された値でソートするためのComparator
	 * 
	 */
	Comparator<Field> comparator = new Comparator<Field>(){
		public int compare(Field fld1, Field fld2){
			fld1.setAccessible(true);
			fld2.setAccessible(true);
			Integer val1 = fld1.getAnnotation(ColumnAnnotation.class).seq();
			Integer val2 = fld2.getAnnotation(ColumnAnnotation.class).seq();
			
			return val1.compareTo(val2);
		}
	};
	
	/**
	 * インデックスnで示された位置のColumnAnnotation返す。
	 * 
	 * @param n インデックス
	 * @return ColumnAnnotation
	 */
	public ColumnAnnotation getColumnAnnotation(int n){
		Field field = annotatedFields.get(n);
		field.setAccessible(true);
		ColumnAnnotation ano = field.getAnnotation(ColumnAnnotation.class);
		return ano;
	}

	
	/**
	 * fieldListからインデックスnで指定したフィールドの値を返す。
	 * 
	 * 
	 * @param n インデックス
	 * @param pojo データビーン
	 * @return フィールドの値オブジェクト、呼び出し側で適切なクラスにキャストすること
	 */
	public Object getValue(int n, T pojo){
		Object value = null;
		try {
			value = annotatedFields.get(n).get(pojo);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			log.error(e.toString(), e);
		}
		return value;
	}
	
	
	/**
	 * 注釈されたフィールドの数
	 * 
	 * @return フィールドの数
	 */
	public int size(){
		return annotatedFields.size();
	}
}

