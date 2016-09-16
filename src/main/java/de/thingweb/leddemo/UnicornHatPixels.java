package de.thingweb.leddemo;

import com.github.h0ru5.neopixel.NeoPixelColor;
import com.siemens.UnicornHat;

/**
 * Created by horus on 9/16/16.
 */
public class UnicornHatPixels implements com.github.h0ru5.neopixel.Neopixels {

    private final UnicornHat uch;
    private int brightness;
    private byte green;
    private byte blue;
    private byte red;

    public UnicornHatPixels() {
        uch = new UnicornHat();
    }

    @Override
    public int getBrightness() {
        return brightness;
    }

    @Override
    public void init() {

    }

    @Override
    public void render() {

    }

    @Override
    public void setBrightness(int i) {
        uch.setBrightness(i);
        this.brightness = i;
    }

    /*
     * The native lib only provides wiping and brightness so far, no individual lights
    */
    @Override
    public void setColor(int i, NeoPixelColor neoPixelColor) {
        colorWipe(neoPixelColor);
    }

    @Override
    public void setColor(int i, byte r, byte g, byte b) {
        colorWipe(NeoPixelColor.fromBytes(r,g,b));
    }

    @Override
    public void setColor(int i, long l) {
        colorWipe(NeoPixelColor.fromValue(l));
    }

    @Override
    public void colorWipe(NeoPixelColor neoPixelColor) {
        uch.setColor(neoPixelColor.red,neoPixelColor.green,neoPixelColor.blue);
        this.red = neoPixelColor.red;
        this.green = neoPixelColor.green;
        this.blue = neoPixelColor.blue;
    }

    @Override
    public NeoPixelColor getColor(int i) {
        return new NeoPixelColor(this.red, this.green, this.blue);
    }
}
