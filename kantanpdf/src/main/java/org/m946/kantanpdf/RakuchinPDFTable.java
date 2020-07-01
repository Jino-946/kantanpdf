package org.m946.kantanpdf;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.lang.reflect.Field;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;

import static org.m946.kantanpdf.DPIUtil.cm;
import org.m946.kantanpdf.TextAlign;

@lombok.extern.slf4j.Slf4j
public class RakuchinPDFTable<T> extends AbstractPDFTable<T> {
	protected AnnotatedColumns<T> annoColumns;
	
	
	public RakuchinPDFTable(Rectangle paperSize, String title, List<T> data) {
		super(paperSize, title, data);
		annoColumns = new AnnotatedColumns<T>(data.get(0));
		paleColor = new Color(0xff, 0xff, 0xe0);
		darkColor = new Color(0xf5, 0xde, 0xb3);
	}

	/**
	 * フィールドの注釈から固定行の列名や列幅を取得しリストに格納する
	 */
	@Override
	protected void buildFixedColumns() {
		colWidths = new ArrayList<Float>();
		fixedColNames = new ArrayList<String>();
		annoColumns.getAnnotatedFields().forEach(field -> {
			field.setAccessible(true);
			PDFColumn anno = field.getAnnotation(PDFColumn.class);
			colWidths.add(cm(anno.width()));
			fixedColNames.add(anno.name());
		}); 
		colCount = colWidths.size();
		
		
	}

	@Override
	protected void printRow(float y, int idx) {
		float x = tableTopLeft.getX();
		T pojo = data.get(idx);
	
		for (Field field: annoColumns.getAnnotatedFields()) {
			field.setAccessible(true);
			PDFColumn anno = field.getAnnotation(PDFColumn.class);
			int textAlign = anno.textAlign();
			float width = cm(anno.width());
			Object value = null;
			try {
				value = field.get(pojo);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				log.error(e.getMessage(), e);
			}
			
			if (value != null) {
				if (value.getClass() == Double.class ) {
					pdf.setTextAlign(TextAlign.LowerRight);
					pdf.textOut(x + width, y, div1000 ? Math.round((Double)value / 1000d) : (Double)value);
				} else if ( value.getClass() == Long.class){
					pdf.setTextAlign(TextAlign.LowerRight);
					pdf.textOut(x + width, y, div1000 ? Math.round((Long)value / 1000d) : (Long)value);
				}else if (value.getClass() == java.sql.Date.class) {
					pdf.textOut(x + width / 2, y, ((java.sql.Date) value).toLocalDate());
				} else if (value.getClass() == java.time.LocalDate.class) {
					pdf.textOut(x + width / 2, y, (java.time.LocalDate) value);
				} else {
					switch (textAlign) {
					case PdfContentByte.ALIGN_CENTER:
						pdf.setTextAlign(TextAlign.LowerCenter);
						pdf.textOut(x + width / 2, y, value.toString());
						break;
					case PdfContentByte.ALIGN_RIGHT:
						pdf.setTextAlign(TextAlign.LowerRight);
						pdf.textOut(x + width, y, value.toString());
						break;
					default:
						pdf.setTextAlign(TextAlign.LowerLeft);
						pdf.textOut(x, y, value.toString());
					}
				}
			}
			x += width;
		};
	}
}
