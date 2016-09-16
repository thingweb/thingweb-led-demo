package com.siemens;

/**
 * Created by horus on 9/16/16.
 */
public class UnicornHat {
        public native void setColor(int r, int g, int b);
        public native void setBrightness(int b);

        public static void main(String[] args) {
            System.loadLibrary("unicorn");
            UnicornHat uch = new UnicornHat();
            uch.setColor(60,60,60);
            uch.setBrightness(255);
        }
}
