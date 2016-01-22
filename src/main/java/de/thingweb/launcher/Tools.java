/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2016 Siemens AG and the thingweb community
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in
 *  * all copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  * THE SOFTWARE.
 *
 */

package de.thingweb.launcher;

import de.thingweb.thing.MediaType;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;

/**
 * Created by Johannes on 20.12.2015.
 */
public class Tools {

    public static String readResource(String path) throws URISyntaxException, IOException {
        URI uri = Tools.class.getClassLoader().getResource(path).toURI();
        return new String(Files.readAllBytes(Paths.get(uri)), Charset.forName("UTF-8"));
    }

    public static String readResourceFilePath(String path) throws URISyntaxException, IOException {
        return Tools.class.getClassLoader().getResource(path).getFile();
    }

    public static Properties loadPropertiesFromResources(String path) throws URISyntaxException, IOException {
        Properties props = new Properties();
        props.load(Tools.class.getClassLoader().getResourceAsStream(path));
        return props;
    }

    public static String fromUrl(String url) throws Exception {
        Scanner scanner = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A");
        String res = scanner.hasNext() ? scanner.next() : "";
        scanner.close();
        return res;
    }

    public static void postHttpJson(String url, Object value) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("content-type", MediaType.APPLICATION_JSON.mediaType);
        String json = "{ \"value\" : " + value.toString() + " }";
        OutputStream os = connection.getOutputStream();
        os.write(json.getBytes());
        os.close();
        connection.getInputStream().close();
    }

}
