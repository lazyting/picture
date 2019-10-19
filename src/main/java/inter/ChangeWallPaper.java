package main.java.inter;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import main.java.constant.ProjectConstant;

/**
 * Created by Lei on 2019/10/19.
 */
public class ChangeWallPaper {
    public static void change(String img) {
        boolean ifContinue = false;
        for (String suffix : ProjectConstant.PIC_SUFFIX) {
            if (img.indexOf(suffix) > -1) {
                ifContinue = true;
                break;
            }
        }
        if (!ifContinue) {
            return;
        }
//        //WallpaperStyle = 10 (Fill，填充), 6 (Fit，适应), 2 (Stretch，伸展), 0 (Tile，平铺，TileWallpaper为1), 0 (Center，居中，TileWallpaper为0)
//        //For windows XP, change to 0
        Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER,
                "Control Panel\\Desktop", "Wallpaper", img);
        Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER,
                "Control Panel\\Desktop", "WallpaperStyle", "2"); //Stretch
        Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER,
                "Control Panel\\Desktop", "TileWallpaper", "0");   // no tiling

        // refresh the desktop using User32.SystemParametersInfo(), so avoiding an OS reboot
//        SPI_SETDESKWALLPAPER值
//        值        意义和使用方法
//        6     设置视窗的大小，SystemParametersInfo(6, 放大缩小值, P, 0)，lpvParam为long型
//        17    开关屏保程序，SystemParametersInfo(17, False, P, 1)，uParam为布尔型
//        13，24  改变桌面图标水平和垂直间距，uParam为间距值(像素)，lpvParam为long型
//        15    设置屏保等待时间，SystemParametersInfo(15, 秒数, P, 1)，lpvParam为long型
//        20    设置桌面背景墙纸，SystemParametersInfo(20, True, 图片路径, 1)
//        93    开关鼠标轨迹，SystemParametersInfo(93, 数值, P, 1)，uParam为False则关闭
//        97    开关Ctrl+Alt+Del窗口，SystemParametersInfo(97, False, A, 0)，uParam为布尔型
        int SPI_SETDESKWALLPAPER = 0x14;
        int SPIF_UPDATEINIFILE = 0x01;
        int SPIF_SENDWININICHANGE = 0x02;

        // User32.System
        boolean result = MyUser32.INSTANCE.SystemParametersInfoA(SPI_SETDESKWALLPAPER, 0,
                img, SPIF_UPDATEINIFILE | SPIF_SENDWININICHANGE);
    }
}
