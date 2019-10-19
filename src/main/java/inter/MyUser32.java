package main.java.inter;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

/**
 * Created by Lei on 2019/10/19.
 */
public interface MyUser32 extends StdCallLibrary {

    MyUser32 INSTANCE = (MyUser32) Native.loadLibrary("user32", MyUser32.class);

    boolean SystemParametersInfoA(int uiAction, int uiParam, String fnm, int fWinIni);
}