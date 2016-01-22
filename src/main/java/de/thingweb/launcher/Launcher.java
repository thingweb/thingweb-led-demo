/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Siemens AG and the thingweb community
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package de.thingweb.launcher;

import de.thingweb.desc.DescriptionParser;
import de.thingweb.desc.pojo.ThingDescription;
import de.thingweb.leddemo.DemoLedAdapter;
import de.thingweb.servient.ServientBuilder;
import de.thingweb.servient.ThingInterface;
import de.thingweb.servient.ThingServer;
import de.thingweb.thing.Content;
import de.thingweb.thing.MediaType;
import de.thingweb.thing.Thing;
import de.thingweb.util.encoding.ContentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedHashMap;

import static de.thingweb.launcher.Tools.readResource;


/**
 * Launches a WoT thing.
 */
public class Launcher {

	private static final Logger log = LoggerFactory.getLogger(Launcher.class);
	private static final int STEPLENGTH = 100;
	private static ThingServer server;

	public static void main(String[] args) throws Exception {
		ServientBuilder.initialize();
		server = ServientBuilder.newThingServer();

		final ThingDescription fancyLedDesc = DescriptionParser.fromBytes(readResource("fancy_led.jsonld").getBytes());
		final ThingDescription basicLedDesc = DescriptionParser.fromBytes(readResource("basic_led.jsonld").getBytes());
		final ThingDescription servientDesc = DescriptionParser.fromBytes(readResource("servientmodel.jsonld").getBytes());

		ThingInterface fancyLed = server.addThing(fancyLedDesc);
		ThingInterface basicLed = server.addThing(basicLedDesc);
		ThingInterface serverInterface = server.addThing(servientDesc);

		attachBasicHandlers(basicLed);
		attachFancyHandlers(fancyLed);

		addServientInterfaceHandlers(serverInterface);

		ServientBuilder.start();
	}

	public static void addServientInterfaceHandlers(ThingInterface serverInterface) throws IOException {
		serverInterface.setProperty("numberOfThings", 3);
		serverInterface.onInvoke("createThing", (data) -> {
			final LinkedHashMap jsonld = ContentHelper.ensureClass(data, LinkedHashMap.class);

			try {
				final ThingDescription thingDescription = DescriptionParser.mapJson(jsonld);
				Thing newThing = new Thing(thingDescription.getMetadata().getName());
				newThing.addInteractions(thingDescription.getInteractions());
				server.addThing(newThing);
				return newThing.getThingDescription().getMetadata();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

	public static void attachBasicHandlers(final ThingInterface led) {
		DemoLedAdapter realLed = new DemoLedAdapter();

		//init block
		led.setProperty("rgbValueRed", realLed.getRed() & 0xFF);
		led.setProperty("rgbValueGreen", realLed.getGreen() & 0xFF);
		led.setProperty("rgbValueBlue", realLed.getBlue() & 0xFF);
		led.setProperty("brightness", realLed.getBrightnessPercent());

		led.onUpdate("rgbValueBlue", (input) -> {
			Integer value = ContentHelper.ensureClass(input, Integer.class);
			log.info("setting blue value to " + value);
			realLed.setBlue((byte) value.intValue());
		});

		led.onUpdate("rgbValueRed", (input) -> {
			Integer value = ContentHelper.ensureClass(input, Integer.class);
			log.info("setting red value to " + value);
			realLed.setRed((byte) value.intValue());
		});

		led.onUpdate("rgbValueGreen", (input) -> {
			Integer value = ContentHelper.ensureClass(input, Integer.class);
			log.info("setting green value to " + value);
			realLed.setGreen((byte) value.intValue());
		});

		led.onUpdate("brightness", (input) -> {
			Integer value = ContentHelper.ensureClass(input, Integer.class);
			log.info("setting brightness to " + value);
			realLed.setBrightnessPercent(value.byteValue());
		});

	}

	public static void attachFancyHandlers(final ThingInterface fancyLed) {
		ThingInterface led = server.getThing("basicLed");

		fancyLed.onUpdate("colorTemperature", (input) -> {
			Integer colorTemperature = ContentHelper.ensureClass(input, Integer.class);
			log.info("setting color temperature to " + colorTemperature +  " K");

			int red=  255;
			int green =  255;
			int blue =  255;

			int ct_scaled = colorTemperature / 100;

			if (ct_scaled > 66) {
				double fred = ct_scaled - 60;
				fred = 329.698727446 * Math.pow(fred, -0.1332047592);
				red = DemoLedAdapter.doubletoByte(fred);

				double fgreen = ct_scaled - 60;
				fgreen =  288.1221695283 * Math.pow(fgreen, -0.0755148492);
				green = DemoLedAdapter.doubletoByte(fgreen);
			} else {
				double fgreen = ct_scaled;
				fgreen = 99.4708025861 * Math.log(fgreen) - 161.1195681661;
				green = DemoLedAdapter.doubletoByte(fgreen);

				if(ct_scaled > 19) {
					double fblue = ct_scaled - 10;
					fblue = 138.5177312231 * Math.log(fblue) - 305.0447927307;
					blue = DemoLedAdapter.doubletoByte(fblue);
				}
			}

			log.info("color temperature equals (" + red + "," + green + "," + blue +")");
			led.setProperty("rgbValueGreen",green);
			led.setProperty("rgbValueRed", red);
			led.setProperty("rgbValueBlue", blue);

		});

		fancyLed.onInvoke("fadeIn", (input) -> {
			Integer duration = ContentHelper.ensureClass(input, Integer.class);
			log.info("fading in over {}s", duration);
			Runnable execution = () -> {
                int steps = duration * 1000 / STEPLENGTH;
                int delta = Math.max(100 / steps, 1);

                int brightness = 0;
                led.setProperty("brightness", brightness);
                while (brightness < 100) {
                    led.setProperty("brightness", brightness);
                    try {
                        Thread.sleep(STEPLENGTH);
                    } catch (InterruptedException e) {
                        break;
                    }
                    brightness += delta;
                }
            };

			//TODO assign resource for thread (outside)
			new Thread(execution).start();

			return null;
		});

		fancyLed.onInvoke("fadeOut", (input) -> {
			Integer duration = ContentHelper.ensureClass(input, Integer.class);
			Runnable execution = () -> {
                int steps = duration * 1000 / STEPLENGTH;
                int delta = Math.max(100 / steps,1);

                int brightness = 100;
                led.setProperty("brightness", brightness);
                while(brightness > 0) {
                    led.setProperty("brightness", brightness);
                    try {
                        Thread.sleep(STEPLENGTH);
                    } catch (InterruptedException e) {
                        break;
                    }
                    brightness -= delta;
                }
            };

			new Thread(execution).start();

			return new Content("".getBytes(), MediaType.APPLICATION_JSON);
		});

		fancyLed.onInvoke("ledOnOff", (input) -> {
			Boolean target = ContentHelper.ensureClass(input, Boolean.class);

			if(target) {
				led.setProperty("rgbValueGreen",255);
				led.setProperty("rgbValueRed",255);
				led.setProperty("rgbValueBlue", 255);

				led.setProperty("brightness", 100);
			} else {
				led.setProperty("brightness", 0);

				led.setProperty("rgbValueGreen",0);
				led.setProperty("rgbValueRed",0);
				led.setProperty("rgbValueBlue", 0);
			}

			return new Content("".getBytes(), MediaType.APPLICATION_JSON);
		});

		fancyLed.onInvoke("trafficLight", (input) -> {
			Boolean go = ContentHelper.ensureClass(input, Boolean.class);
			log.info("trafic light changing state to {}",(go)? "green": "red" );

			if(go) {
				led.setProperty("rgbValueGreen",255);
				led.setProperty("rgbValueRed", 0);
				led.setProperty("rgbValueBlue", 0);
			} else {
				led.setProperty("rgbValueGreen",0);
				led.setProperty("rgbValueRed",255);
				led.setProperty("rgbValueBlue", 0);
			}

			return new Content("".getBytes(), MediaType.APPLICATION_JSON);
		} );
	}
}
