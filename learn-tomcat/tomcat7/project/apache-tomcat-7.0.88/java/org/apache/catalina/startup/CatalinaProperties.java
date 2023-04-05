/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.apache.catalina.startup;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.catalina.Globals;


/**
 * Utility class to read the bootstrap Catalina configuration.
 *
 * @author Remy Maucherat
 */

public class CatalinaProperties {


    // ------------------------------------------------------- Static Variables

    private static final org.apache.juli.logging.Log log=
        org.apache.juli.logging.LogFactory.getLog( CatalinaProperties.class );

    private static Properties properties = null;


    static {
        // 加载一些属性
        loadProperties();

    }


    // --------------------------------------------------------- Public Methods


    /**
     * Return specified property value.
     */
    public static String getProperty(String name) {
    
        return properties.getProperty(name);

    }


    /**
     * Return specified property value.
     *
     * @deprecated  Unused - will be removed in 8.0.x
     */
    @Deprecated
    public static String getProperty(String name, String defaultValue) {

        return properties.getProperty(name, defaultValue);

    }


    // --------------------------------------------------------- Public Methods


    /**
     * Load properties.
     * 初始化一些属性值
     * catalina.config(启动时进来是空的)
     * 1. 从系统参数 catalina.config 获取属性配置文件路径，然后从属性配置文件中获取catalina的一些属性配置
     * 2. 利用上述文件路径生成一个文件流，如果生成的文件流是空的则获取 catalina.base 目录，如果再是空的则获取user.dir目录
     * 3. 如果问文件输入流依然是空的则获取 CatalinaProperties.class 作为文件输入流
     * 4. 如果输入流不为空，则使用 class Properties 开始获取属性键值对,如果为空则初始化一个空的 class Properties 对象
     * 5. 将属性值设置到System系统属性中
     */
    private static void loadProperties() {

        InputStream is = null;
        Throwable error = null;

        try {
            // 加载配置的文件路径
            String configUrl = getConfigUrl();
            if (configUrl != null) {
                is = (new URL(configUrl)).openStream();
            }
        } catch (Throwable t) {
            handleThrowable(t);
        }

        // 获取一个配置文件输入流
        if (is == null) {
            try {
                File home = new File(getCatalinaBase());
                File conf = new File(home, "conf");
                File propsFile = new File(conf, "catalina.properties");
                is = new FileInputStream(propsFile);
            } catch (Throwable t) {
                handleThrowable(t);
            }
        }

        if (is == null) {
            try {
                is = CatalinaProperties.class.getResourceAsStream
                    ("/org/apache/catalina/startup/catalina.properties");
            } catch (Throwable t) {
                handleThrowable(t);
            }
        }

        if (is != null) {
            try {
                properties = new Properties();
                properties.load(is);
            } catch (Throwable t) {
                handleThrowable(t);
                error = t;
            }
            finally
            {
                try {
                    is.close();
                } catch (IOException ioe) {
                    log.warn("Could not close catalina.properties", ioe);
                }
            }
        }

        if ((is == null) || (error != null)) {
            // Do something
            log.warn("Failed to load catalina.properties", error);
            // That's fine - we have reasonable defaults.
            properties=new Properties();
        }

        // Register the properties as system properties
        Enumeration<?> enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            String name = (String) enumeration.nextElement();
            String value = properties.getProperty(name);
            if (value != null) {
                System.setProperty(name, value);
            }
        }

    }


    /**
     * Get the value of the catalina.home environment variable.
     */
    private static String getCatalinaHome() {
        return System.getProperty(Globals.CATALINA_HOME_PROP,
                                  System.getProperty("user.dir"));
    }
    
    
    /**
     * Get the value of the catalina.base environment variable.
     */
    private static String getCatalinaBase() {
        return System.getProperty(Globals.CATALINA_BASE_PROP, getCatalinaHome());
    }


    /**
     * Get the value of the configuration URL.
     */
    private static String getConfigUrl() {
        return System.getProperty("catalina.config");
    }

    // Copied from ExceptionUtils since that class is not visible during start
    private static void handleThrowable(Throwable t) {
        if (t instanceof ThreadDeath) {
            throw (ThreadDeath) t;
        }
        if (t instanceof VirtualMachineError) {
            throw (VirtualMachineError) t;
        }
        // All other instances of Throwable will be silently swallowed
    }

}
