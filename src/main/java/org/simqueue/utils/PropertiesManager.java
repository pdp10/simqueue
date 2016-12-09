package org.simqueue.utils;
/*
 * MIT License
 * Copyright (c) 2005 Piero Dalle Pezze
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Manager of Properties.
 */
public class PropertiesManager {

  /**
   * Load a .properties file into a Properties object.
   * 
   * @param filename
   *        Absolute path to the .properties file
   * @return The properties
   * @throws IOException
   *         file cannot be loaded.
   */
  public static Properties load(String filename) throws IOException {
    Properties properties = new Properties();
    FileInputStream in = null;
    try {
      in = new FileInputStream(filename);
      properties.load(in);
    } catch (IOException e) {
      throw e;
    }
    finally {
      try {
        if (in != null)
          in.close();
      } catch (IOException e) {
        throw e;
      }
    }
    return properties;
  }
  
}
