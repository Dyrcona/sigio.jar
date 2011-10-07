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
package com.sigio.js.scriptrunner.plugins;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Context;

/**
 * Abstract base class for {@link com.sigio.js.ScriptRunner} plugins.
 */
public abstract class Plugin extends ScriptableObject {

	/**
	 * Simple constructor merely calls super().
	 */
	public Plugin() {
		super();
	}

	/**
	 * Construct a plugin with a specific scope and prototype.
	 *
	 * @param scope scriptable scope
	 * @param prototype scriptable prototype
	 */
	public Plugin(Scriptable scope, Scriptable prototype) {
		super(scope, prototype);
	}

	/**
	 * Returns the class name. Override this to supply your class
	 * name.
	 *
	 * @return the default returns the string "Plugin"
	 */
	public String getClassName() {
		return "Plugin";
	}

	/**
	 * Called by {@link com.sigio.js.ScriptRunner} to load a
	 * plugin. The caller supplies a Context and a Scope for the
	 * plugin.
	 *
	 * <p>It is in this method that your plugin
	 * will do any setup work required when it is loaded. All
	 * subclasses must implement this method.</p>
	 *
	 * <p><em>TODO:</em> Need to add a few words here about the
	 * implementation and returning <code>true</code> or
	 * <code>false</code> under what conditions and about cleaning up
	 * after a partial initialization. There should probably be an
	 * exception to throw in the case of partial initialization and
	 * the plugin cannot clean up the successful bits of
	 * initiialization.</p>
	 *
	 * @param cx context into which the plugin is being loaded
	 * @param scope scope into which the plugin is being loaded
	 * @return <code>true</code> if the setup was successful;
	 * <code>false</code> otherwise
	 */
	public abstract boolean load(Context cx, Scriptable scope);

	/**
	 * Called by {@link com.sigio.js.ScriptRunner} to unload a
	 * plugin. The caller supplies a Context and a Scope for the
	 * plugin. This context and sceop should be the same as those
	 * provided when the plugin was loaded.
	 *
	 * <p>It is in this method that your plugin
	 * will do any cleanup work required when it is loaded. All
	 * subclasses must implement this method, though it can be as
	 * simple as <code>return true;</code> if your plugin has no need
	 * to unload itself.</p>
	 *
	 * <p>The implementation of this <em>must</em> return
	 * <code>false</code> if it is impossible for your plugin to
	 * cleanup after itself.</p>
	 *
	 * @param cx context from which the plugin is being unloaded
	 * @param scope scope from which the plugin is being unloaded
	 * @return <code>true</code> if the cleanup was successful;
	 * <code>false</code> otherwise
	 */
	public abstract boolean unload(Context cx, Scriptable scope);

}
