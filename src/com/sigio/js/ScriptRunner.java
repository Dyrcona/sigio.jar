/*
 * Copyright © 2011 Jason J.A. Stephenson
 * 
 * This file is part of sigio.jar.
 * 
 * sigio.jar is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * sigio.jar is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Lesser GNU General Public License for more details.
 * 
 * You should have received a copy of the Lesser GNU General Public License
 * along with sigio.jar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sigio.js;
import com.sigio.js.scriptrunner.plugins.Plugin;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.ImporterTopLevel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * A class to run JavaScript. It requires that the <a
 * href="http://www.mozilla.org/rhino/">Rhino JavaScript package</a>
 * be installed.
 *
 * <p>ScriptRunner adds two functions to JavaScript:</p> <dl>
 * <dt>include()</dt><dd>Includes and evaluates a file of JavaScript
 * code much like include() in PHP, or use or require in Perl.</dd>
 * <dt>print()</dt><dd>Prints a string to stdout.</dd></dl>
 *
 * <p>The class can also load plugins at runtime, but I still need to
 * document this better.</p>
 */

public class ScriptRunner extends ImporterTopLevel {

	private Context m_context = null;
	private boolean m_isEntered = false;
	private HashMap<String,Plugin> m_loadedPlugins = null;
	private HashMap<String,File> m_includePathsMap = null;

	/**
	 * Construct a ScriptRunner with an unique context and an unsealed
	 * state.
	 */
	public ScriptRunner() {
		super();
		this.enterContext();
		// Setup our JavaScript include function:
		this.setupJavaScriptFunctions();
	}

	/**
	 * Construct a ScriptRunner with a shared context and a possibly
	 * sealed state. A sealed ScriptRunner cannot load aditional
	 * plugins nor otherwise have its JavaScript context tampered
	 * with.
	 *
	 * @param cx context to use at runtime
	 * @param sealed boolean to indicate sealed state:
	 * <code>true</code> for sealed; <code>false</code> for not
	 */
	public ScriptRunner(Context cx, boolean sealed) {
		super(cx, false);
		m_context = cx;
		// Setup our JavaScript include function:
		this.setupJavaScriptFunctions();
		if (sealed)
			this.sealObject();
	}

	/**
	 * Construct a ScriptRunner with a shared context and an unsealed
	 * state.
	 *
	 * @param cx context to use at runtime
	 */
	public ScriptRunner(Context cx) {
		this(cx, false);
	}

	/**
	 * Construct a ScriptRunner with an unique context, an unsealed
	 * state, and a collection of plugins to load.
	 *
	 * @param collection collection of plugins to load
	 */
	public ScriptRunner(Collection<Plugin> collection) {
		this();
		this.loadPlugins(collection);
	}

	/**
	 * Construct a ScriptRunner with a shared context, a possibly
	 * sealed state, and a collection of plugins to load. A sealed
	 * ScriptRunner cannot load aditional plugins nor otherwise have
	 * its JavaScript context tampered with.
	 *
	 * <p>This constructor allows you to create a sealed ScriptRunner
	 * with a context shared with another scriptable object that has
	 * some plugins preloaded.</p>
	 *
	 * @param cx context to use at runtime
	 * @param sealed boolean to indicate sealed state:
	 * <code>true</code> for sealed; <code>false</code> for not
	 * @param collection collection of plugins to load
	 */
	public ScriptRunner(Context cx, boolean sealed, Collection<Plugin> collection) {
		this(cx, false);
		this.loadPlugins(collection);
		if (sealed)
			this.sealObject();
	}

	/**
	 * Construct a ScriptRunner with a shared context, an
	 * unsealed state, and a collection of plugins to load.
	 *
	 * @param cx context to use at runtime
	 * @param collection collection of plugins to load
	 */
	public ScriptRunner(Context cx, Collection<Plugin> collection) {
		this(cx, false, collection);
	}

	/**
	 * Construct a ScriptRunner with an unique context and an unsealed
	 * state while specifying a search path for the JavaScript include
	 * function.
	 *
	 * @param includePath string with the search path for the
	 * JavaScript include function.
	 */
	public ScriptRunner(String includePath) {
		super();
		this.enterContext();
		// Setup our JavaScript include function:
		this.setupJavaScriptFunctions();
		this.addIncludeFileSearchPath(includePath);
	}

	/**
	 * Construct a ScriptRunner with a shared context and a possibly
	 * sealed state while specifying a search path for the JavaScript
	 * include function. A sealed ScriptRunner cannot load aditional
	 * plugins nor otherwise have its JavaScript context tampered
	 * with.
	 *
	 * @param cx context to use at runtime
	 * @param sealed boolean to indicate sealed state:
	 * <code>true</code> for sealed; <code>false</code> for not
	 * @param includePath string with the search path for the
	 * JavaScript include function.
	 */
	public ScriptRunner(Context cx, boolean sealed, String includePath) {
		super(cx, false);
		m_context = cx;
		// Setup our JavaScript include function:
		this.setupJavaScriptFunctions();
		this.addIncludeFileSearchPath(includePath);
		if (sealed)
			this.sealObject();
	}

	/**
	 * Construct a ScriptRunner with a shared context and an unsealed
	 * state while specifying a search path for the JavaScript include
	 * function.
	 *
	 * @param cx context to use at runtime
	 * @param includePath string with the search path for the
	 * JavaScript include function.
	 */
	public ScriptRunner(Context cx, String includePath) {
		this(cx, false, includePath);
	}

	/**
	 * Construct a ScriptRunner with an unique context, an unsealed
	 * state, and a collection of plugins to load while specifying a
	 * search path for the JavaScript include function.
	 *
	 * @param collection collection of plugins to load
	 * @param includePath string with the search path for the
	 * JavaScript include function.
	 */
	public ScriptRunner(Collection<Plugin> collection, String includePath) {
		this(includePath);
		this.loadPlugins(collection);
	}

	/**
	 * Construct a ScriptRunner with a shared context, a possibly
	 * sealed state, and a collection of plugins to load while
	 * specifying a search path for the JavaScript include function. A
	 * sealed ScriptRunner cannot load aditional plugins nor otherwise
	 * have its JavaScript context tampered with.
	 *
	 * @param cx context to use at runtime
	 * @param sealed boolean to indicate sealed state:
	 * <code>true</code> for sealed; <code>false</code> for not
	 * @param collection collection of plugins to load
	 * @param includePath string with the search path for the
	 * JavaScript include function.
	 */
	public ScriptRunner(Context cx, boolean sealed, Collection<Plugin> collection, String includePath) {
		this(cx, false, includePath);
		this.loadPlugins(collection);
		if (sealed)
			this.sealObject();
	}

	/**
	 * Construct a ScriptRunner with a shared context, an unsealed
	 * state, and a collection of plugins to load while specifying a
	 * search path for the JavaScript include function.
	 *
	 * @param cx context to use at runtime
	 * @param collection collection of plugins to load
	 * @param includePath string with the search path for the
	 * JavaScript include function.
	 */
	public ScriptRunner(Context cx, Collection<Plugin> collection, String includePath) {
		this(cx, false, collection, includePath);
	}

	/**
	 * Get the class name.
	 *
	 * @return constant, literal string "ScriptRunner"
	 */
	public String getClassName() {
		return "ScriptRunner";
	}

	/**
	 * Add a search path to the JavaScript include function's search
	 * path.
	 *
	 * @param path search path string to add
	 */
	public void addIncludeFileSearchPath(String path) {
		if (m_includePathsMap == null)
			m_includePathsMap = new HashMap<String, File>();
		String[] paths = path.split(File.pathSeparator);
		for (int i = 0; i < paths.length; i++) {
			String p = paths[i];
			File f = new File(p);
			if (f.exists() && f.isDirectory() && f.canRead() && !m_includePathsMap.containsKey(p) && !m_includePathsMap.containsValue(f))
				m_includePathsMap.put(p, f);
		}
	}

	/**
	 * Get the search path used by the JavaScript include function.
	 *
	 * @return search path string used to find <code>include()</code>d
	 * JavaScript files
	 */
	public String getIncludeFileSearchPath() {
		String path = null;
		if (m_includePathsMap != null) {
			Set<String> keys = m_includePathsMap.keySet();
			Iterator<String> it = keys.iterator();
			while (it.hasNext()) {
				if (path == null)
					path = new String(it.next());
				else 
					path = path.concat(it.next());
				if (it.hasNext())
					path = path.concat(File.pathSeparator);
			}
		}
		return path;
	}

	/**
	 * Evaluate a JavaScript file by path.
	 */
	public Object evaluateFile(String filename) throws Throwable {
		File file = new File(filename);
		return this.evaluateFile(file);
	}

	/**
	 * Evaluate a JavaScript file.
	 */
	public Object evaluateFile(File file) throws Throwable {
		Object result = null;
		if (file.exists() && file.isFile() && file.canRead()) {
			FileReader reader = new FileReader(file);
			result = m_context.evaluateReader(this, reader, file.getName(), 1, null);
		}
		return result;
	}

	/**
	 * Evaluate a string of arbitary JavaScipt.
	 */
	public Object evaluateString(String string) throws Throwable {
		return m_context.evaluateString(this, string, "<string>", 1, null);
	}

	/**
	 * Retrieve a loaded plugin by class name.
	 *
	 * @param name class name of the plugin to retrieve
	 * @return loaded plugin matching the name parameter or null if no
	 * matching plugin was found
	 */
	public Plugin getNamedPlugin(String name) {
		Plugin plugin = null;
		if (m_loadedPlugins != null)
			plugin = m_loadedPlugins.get(name);
		return plugin;
	}

	/**
	 * Check if a named plugin is loaded
	 *
	 * @param name class name of the plugin to check
	 * @return <code>true</code> if a matching named plugin is loaded;
	 * <code>false</code> otherwise
	 */
	public boolean hasNamedPlugin(String name) {
		boolean isLoaded = false;
		if (m_loadedPlugins != null)
			isLoaded = m_loadedPlugins.containsKey(name);
		return isLoaded;
	}

	/**
	 * This is an implementation method. Your code should never call
	 * this method directly. Unfortunately, it needs to be public in
	 * order to function when called from JavaScript.
	 */ 
	public void includeImpl(String filename) throws IOException {
		// This function implements the include file function in
		// JavaScript.
		File file = null;
		boolean gotFile = false;
		// Check the file paths in m_includePathsMap:
		if (m_includePathsMap != null) {
			Collection<File> filePaths = m_includePathsMap.values();
			Iterator<File> it = filePaths.iterator();
			while (it.hasNext()) {
				File parent = it.next();
				file = new File(parent, filename);
				if (file.exists() && file.isFile() && file.canRead()) {
					gotFile = true;
					break;
				}
			}
		}
		// If we didn't find the file in the paths, then check the
		// current directory.
		if (!gotFile) {
			file = new File(filename);
			if (file.exists() && file.isFile() && file.canRead())
				gotFile = true;
		}
		// Do our thing if we find a file:
		if (gotFile) {
			FileReader reader = new FileReader(file);
			m_context.evaluateReader(this, reader, filename, 1, null);
		}
		else {
			// Throw a FileNotFoundException if we can't find the
			// include file in the search paths.
			ResourceBundle rsc = ResourceBundle.getBundle("com.sigio.js.ScriptRunner");
			String fmt = rsc.getString("IncludeFileNotFoundMessageFormat");
			String message = String.format(fmt, filename);
			FileNotFoundException ex = new FileNotFoundException(message);
			throw ex;
		}
	}

	/**
	 * This is an implementation method. Your code should never call
	 * this method directly. Unfortunately, it needs to be public in
	 * order to function when called from JavaScript.
	 */ 
	public void printImpl(String str) {
		System.out.print(str);
	}

	/**
	 * Load an instantiated plugin in this context.
	 *
	 * @param plugin the plugin to load
	 * @return <code>true</code> if the plugin can be loaded and the
	 * plugin's load method returns <code>true</code>;
	 * <code>false</code> otherwise
	 */
	public boolean loadPlugin(Plugin plugin) {
		boolean isLoaded = false;
		if (!this.isSealed()) {
			if (m_loadedPlugins == null)
				m_loadedPlugins = new HashMap<String,Plugin>();
			if (!m_loadedPlugins.containsKey(plugin.getClassName())) {
				isLoaded = plugin.load(m_context, this);
				if (isLoaded)
					m_loadedPlugins.put(plugin.getClassName(), plugin);
			}
			else
				isLoaded = true;
		}
		return isLoaded;
	}

	/**
	 * Load a collection of plugins.
	 *
	 * @param collection collection of instantiated plugins to load
	 * @return array of booleans to indicate which plugins loaded and which failed
	 * @see ScriptRunner#loadPlugin(Plugin) loadPlugin
	 */
	public boolean[] loadPlugins(Collection<Plugin> collection) {
		boolean[] resultArray = new boolean[collection.size()];
		int count = 0;
		Iterator<Plugin> iterator = collection.iterator();
		while (iterator.hasNext()) {
			Plugin plugin = iterator.next();
			resultArray[count++] = this.loadPlugin(plugin);
		}
		return resultArray;
	}

	/**
	 * Unload a loaded plugin by class name.
	 *
	 * @param name class name of the plugin to unload
	 * @return <code>true</code> if the plugin was loaded and the
	 * plugin's unload method returns <code>true</code>;
	 * <code>false</code> otherwise
	 */
	public boolean unloadPlugin(String name) {
		boolean isUnLoaded = false;
		if (!this.isSealed()) {
			if (m_loadedPlugins != null && m_loadedPlugins.containsKey(name)) {
				Plugin plugin = m_loadedPlugins.get(name);
				isUnLoaded = plugin.unload(m_context, this);
				if (isUnLoaded)
					m_loadedPlugins.remove(name);
			}
			else
				isUnLoaded = true;
		}
		return isUnLoaded;
	}

	/**
	 * Can be called if you are done with the current script
	 * context. It isn't usually necessary to call this as the
	 * finalize() method already takes care of this. Your ScriptRunner
	 * instance is useless after calling this method.
	 */
	public void exit() {
		if (m_isEntered) {
			Context.exit();
			m_isEntered = false;
		}
	}

	/**
	 * A finalizer to exit the Context so that the user isn't required
	 * to call this.exit() explicitly.
	 */
	protected void finalize() throws Throwable {
		this.exit();
		super.finalize();
	}

	// Used by the constructors to initialize our member variables:
	private void enterContext() {
		m_context = Context.enter();
		this.initStandardObjects(m_context, false);
		m_isEntered = true;
	}

	// Sets up our JavaScript functions in the current scope.
	private void setupJavaScriptFunctions() {
		try {
			Class<?> myClass = ScriptRunner.class;
			Method method = myClass.getMethod("includeImpl", String.class);
			FunctionObject includeFunc = new FunctionObject("include", method, this);
			this.defineProperty("include", includeFunc, ScriptableObject.DONTENUM);
			method = myClass.getMethod("printImpl", String.class);
			FunctionObject printFunc = new FunctionObject("print", method, this);
			this.defineProperty("print", printFunc, ScriptableObject.DONTENUM);
		}
		catch (NoSuchMethodException e) {
			// This should never happen. We'll print a stack trace if
			// by some evil chance it does.
			e.printStackTrace();
		}
	}

}
