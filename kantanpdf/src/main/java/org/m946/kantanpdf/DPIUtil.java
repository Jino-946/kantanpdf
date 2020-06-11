package org.m946.kantanpdf;


/**
 *  1/10"の列幅、1/6"の行高、cm 、inch、pointをDPIに変換する。<br><br>
 *  xcol(), yrow(),cm(), inch(),point()
 */
public class DPIUtil {
	private final static float DPI = (float)72.0;
	
	private DPIUtil(){
		
	}
	
	/**
	 *  引数で指定した列のX座標をDPIに変換する。 1カラムは1/10"
	 * 
	 * @param col 列
	 * @return 変換されたDPI
	 */
	public static float xcol(int col){
		return DPI *  (float)col / (float)10.0;
	}
	
	/**
	 * 引数で指定した行のY座標をDPIに変換する。1行は1/6"
	 * 
	 * @param row 行
	 * @return 変換されたDPI
	 */
	public static float yrow(int row){
		return DPI * (float)row / (float)6.0;
	}
	
	
	/**
	 * 引数で指定した値をcmと見なしてDPIに変換する。 1"は2.54cm
	 * 
	 * @param n cm
	 * @return 変換されたDPI
	 */
	public static float cm(double n){
		return DPI * (float)n / (float)2.54;
	}

	/**
	 *  引数で指定した値をinchとみなしてDPIに変換する。
	 * @param n
	 * @return
	 */
	public static float inch(double n){
		return DPI * (float)n;
	}
	
	public static float point(int p){
		return (float)p; 
	}
}
