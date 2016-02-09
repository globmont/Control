package joystick;

/**
 * Created by Nikhil on 2/9/2016.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.util.plugins.Plugins;

import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.logging.Logger;

public class MyControllerEnvironment extends ControllerEnvironment {
    static String libPath;
    private static Logger log;
    private ArrayList controllers;
    private Collection loadedPlugins = new ArrayList();

    static void loadLibrary(final String lib_name) {
        AccessController.doPrivileged(new PrivilegedAction() {
            public final Object run() {
                String lib_path = System.getProperty("net.java.games.input.librarypath");
                if(lib_path != null) {
                    System.load(lib_path + File.separator + System.mapLibraryName(lib_name));
                } else {
                    System.loadLibrary(lib_name);
                }

                return null;
            }
        });
    }

    static String getPrivilegedProperty(final String property) {
        return (String)AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                return System.getProperty(property);
            }
        });
    }

    static String getPrivilegedProperty(final String property, final String default_value) {
        return (String)AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                return System.getProperty(property, default_value);
            }
        });
    }

    public MyControllerEnvironment() {
    }

    public Controller[] getControllers() {
        if(this.controllers == null) {
            this.controllers = new ArrayList();
            AccessController.doPrivileged(new PrivilegedAction() {
                public Object run() {
                    MyControllerEnvironment.this.scanControllers();
                    return null;
                }
            });

            controllers.clear();
            String ret = getPrivilegedProperty("jinput.plugins", "") + " " + getPrivilegedProperty("net.java.games.input.plugins", "");
            if(!getPrivilegedProperty("jinput.useDefaultPlugin", "true").toLowerCase().trim().equals("false") && !getPrivilegedProperty("net.java.games.input.useDefaultPlugin", "true").toLowerCase().trim().equals("false")) {
                String it = getPrivilegedProperty("os.name", "").trim();
                if(it.equals("Linux")) {
                    ret = ret + " net.java.games.input.LinuxEnvironmentPlugin";
                } else if(it.equals("Mac OS X")) {
                    ret = ret + " net.java.games.input.OSXEnvironmentPlugin";
                } else if(!it.equals("Windows XP") && !it.equals("Windows Vista") && !it.equals("Windows 7")) {
                    if(!it.equals("Windows 98") && !it.equals("Windows 2000")) {
                        if(it.startsWith("Windows")) {
                            log.warning("Found unknown Windows version: " + it);
                            log.info("Attempting to use default windows plug-in.");
                            ret = ret + " net.java.games.input.DirectAndRawInputEnvironmentPlugin";
                        } else {
                            log.info("Trying to use default plugin, OS name " + it + " not recognised");
                        }
                    } else {
                        ret = ret + " net.java.games.input.DirectInputEnvironmentPlugin";
                    }
                } else {
                    ret = ret + " net.java.games.input.DirectAndRawInputEnvironmentPlugin";
                }
            }

            StringTokenizer var7 = new StringTokenizer(ret, " \t\n\r\f,;:");

            while(var7.hasMoreTokens()) {
                String i = var7.nextToken();

                try {
                    if(!this.loadedPlugins.contains(i)) {
                        log.info("Loading: " + i);
                        Class e = Class.forName(i);
                        ControllerEnvironment ce = (ControllerEnvironment)e.newInstance();
                        if(ce.isSupported()) {
                            this.addControllers(ce.getControllers());
                            this.loadedPlugins.add(ce.getClass().getName());
                        } else {
                            //logln(e.getName() + " is not supported");
                            System.err.println(e.getName() + " is not supported");
                        }
                    }
                } catch (Throwable var6) {
                    var6.printStackTrace();
                }
            }
        }

        Controller[] var9 = new Controller[this.controllers.size()];
        Iterator var8 = this.controllers.iterator();

        for(int var10 = 0; var8.hasNext(); ++var10) {
            var9[var10] = (Controller)var8.next();
        }

        return var9;
    }

    public void scanControllers() {
//        controllers.clear();
        String pluginPathName = getPrivilegedProperty("jinput.controllerPluginPath");
        if(pluginPathName == null) {
            pluginPathName = "controller";
        }

        this.scanControllersAt(getPrivilegedProperty("java.home") + File.separator + "lib" + File.separator + pluginPathName);
        this.scanControllersAt(getPrivilegedProperty("user.dir") + File.separator + pluginPathName);
    }

    public void scanControllersAt(String path) {
        File file = new File(path);
        if(file.exists()) {
            try {
                Plugins e = new Plugins(file);
                Class[] envClasses = e.getExtends(ControllerEnvironment.class);

                for(int i = 0; i < envClasses.length; ++i) {
                    try {
//                        ControllerEnvironment.logln("ControllerEnvironment " + envClasses[i].getName() + " loaded by " + envClasses[i].getClassLoader());
                        ControllerEnvironment e1 = (ControllerEnvironment)envClasses[i].newInstance();
                        if(e1.isSupported()) {
                            this.addControllers(e1.getControllers());
                            this.loadedPlugins.add(e1.getClass().getName());
                        } else {
//                            logln(envClasses[i].getName() + " is not supported");
                        }
                    } catch (Throwable var7) {
                        var7.printStackTrace();
                    }
                }
            } catch (Exception var8) {
                var8.printStackTrace();
            }

        }
    }

    private void addControllers(Controller[] c) {
        for(int i = 0; i < c.length; ++i) {
            this.controllers.add(c[i]);
        }

    }


    public boolean isSupported() {
        return true;
    }

    static {
        log = Logger.getLogger(MyControllerEnvironment.class.getName());
    }
}
