rk29_mv8686_power_save_patch 使用说明:

	mv8686驱动在系统进入休眠后无法进入省电模式，因此平均功耗较大。该补丁通过修改WifiService.java

当系统关屏进入二级休眠后自动将WiFi关闭，在机器唤醒后自动打开WiFi，以此降低整机功耗。

	补丁使用方法：

	参照rk29_mv8686_power_save_patch内容修改WifiService.java，然后编译即可。