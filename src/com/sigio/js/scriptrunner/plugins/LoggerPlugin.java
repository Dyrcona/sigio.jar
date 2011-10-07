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
import org.mozilla.javascript.FunctionObject;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

/**
 * Plugin that adds simple logging to {@link
 * com.sigio.js.ScriptRunner}.
 *
 * <p>When loaded, this plugin adds the following JavaScript functions
 * that can be used to log messages to various output streams.</p>
 * <dl>
 * <dt>log_stdout</dt><dd>logs to System.out</dd>
 * <dt>log_stderr</dt><dd>logs to System.err</dd>
 * <dt>log_activity</dt><dd>log an "activity"</dd>
 * <dt>log_debug</dt><dd>log a debug message</dd>
 * <dt>debug</dt><dd>ditto</dd>
 * <dt>log_error</dt><dd>log an error</dd>
 * <dt>log_info</dt><dd>informational log message</dd>
 * <dt>log_internal</dt><dd>internal log message</dd>
 * <dt>log_warn</dt><dd>log a warning</dd>
 * <dt>alert</dt><dd>ditto</dd>
 * </dl>
 *
 * <p>Output of debug log messages can be turned on or off by toggling
 * the debug mode state.</p>
 */
public class LoggerPlugin extends Plugin {

	private boolean m_debugMode = false;

	private OutputStream m_outputStream = null;

	private OutputStream m_activityStream = null;
	private OutputStream m_debugStream = null;
	private OutputStream m_errorStream = null;
	private OutputStream m_infoStream = null;
	private OutputStream m_internalStream = null;
	private OutputStream m_warnStream = null;

	private String m_applicationName = null;

	private ResourceBundle m_resources = null;
	private SimpleDateFormat m_dateFormat = null;

	private String m_EOL = System.getProperty("line.separator");

	/**
	 * Construct a LoggerPlugin with a default output stream.
	 *
	 * @param out outputstream to use as the default
	 */
	public LoggerPlugin(OutputStream out) {
		super();
		this.setOutputStream(out);
		this.loadResources();
	}

	/**
	 * Construct a LoggerPlugin with a scop, prototype, and default
	 * output stream.
	 *
	 * @param scope scope for the plugin to use
	 * @param prototype prototype for the plugin to use
	 * @param out outputstream to use as the default
	 */
	public LoggerPlugin(Scriptable scope, Scriptable prototype, OutputStream out) {
		super(scope, prototype);
		this.setOutputStream(out);
		this.loadResources();
	}

	/**
	 * Construct a LoggerPlugin with a default output stream and a
	 * debug mode setting.
	 *
	 * @param out outputstream to use as the default
	 * @param debugMode boolean to control logging of debug messages:
	 * <code>true</code> means debug messages are logged;
	 * <code>false</code> means no; default is <code>false</code>
	 */
	public LoggerPlugin(OutputStream out, boolean debugMode) {
		this(out);
		m_debugMode = debugMode;
	}

	/**
	 * Construct a LoggerPlugin with a scope, context, default output
	 * stream, and a debug mode setting.
	 *
	 * @param scope scope for the plugin to use
	 * @param prototype prototype for the plugin to use
	 * @param out outputstream to use as the default
	 * @param debugMode boolean to control logging of debug messages:
	 * <code>true</code> means debug messages are logged;
	 * <code>false</code> means no; default is <code>false</code>
	 */
	public LoggerPlugin(Scriptable scope, Scriptable prototype, OutputStream out, boolean debugMode) {
		this(scope, prototype, out);
		m_debugMode = debugMode;
	}

	/**
	 * Change the default output stream at runtime.
	 *
	 * @param out outputstream to use as the default
	 */
	public void setOutputStream(OutputStream out) {
		m_outputStream = out;
	}

	/**
	 * Set an alternate output stream for activity logs if you want
	 * them to go somewhere other than the default.
	 *
	 * @param out outputstream to use for the activity logs. If you
	 * set this to null it will return to using the default
	 * outputstream.
	 */
	public void setActivityStream(OutputStream out) {
		m_activityStream = out;
	}

	/**
	 * Set an alternate output stream for debug logs if you want
	 * them to go somewhere other than the default.
	 *
	 * @param out outputstream to use for the debug logs. If you
	 * set this to null it will return to using the default
	 * outputstream.
	 */
	public void setDebugStream(OutputStream out) {
		m_debugStream = out;
	}

	/**
	 * Set an alternate output stream for error logs if you want
	 * them to go somewhere other than the default.
	 *
	 * @param out outputstream to use for the error logs. If you
	 * set this to null it will return to using the default
	 * outputstream.
	 */
	public void setErrorStream(OutputStream out) {
		m_errorStream = out;
	}

	/**
	 * Set an alternate output stream for info logs if you want
	 * them to go somewhere other than the default.
	 *
	 * @param out outputstream to use for the info logs. If you
	 * set this to null it will return to using the default
	 * outputstream.
	 */
	public void setInfoStream(OutputStream out) {
		m_infoStream = out;
	}

	/**
	 * Set an alternate output stream for internal logs if you want
	 * them to go somewhere other than the default.
	 *
	 * @param out outputstream to use for the internal logs. If you
	 * set this to null it will return to using the default
	 * outputstream.
	 */
	public void setInternalStream(OutputStream out) {
		m_internalStream = out;
	}

	/**
	 * Set an alternate output stream for warn logs if you want
	 * them to go somewhere other than the default.
	 *
	 * @param out outputstream to use for the warn logs. If you
	 * set this to null it will return to using the default
	 * outputstream.
	 */
	public void setWarnStream(OutputStream out) {
		m_warnStream = out;
	}

	/**
	 * Change the date format used for timestamps in the log output.
	 *
	 * @param format string with new date format pattern to use
	 */
	public void setDateFormatPattern(String format) {
		m_dateFormat = new SimpleDateFormat(format);
	}

	/**
	 * Get the current date format pattern used for timestamps in the
	 * logs. The default is set in this class' properties
	 *
	 * @return string with the date format pattern currently in use.
	 */
	public String getDateFormatPattern() {
		return m_dateFormat.toPattern();
	}

	/**
	 * Get the class name.
	 *
	 * @return literal, constant string "LoggerPlugin"
	 */
	public String getClassName() {
		return "LoggerPlugin";
	}

	/**
	 * Set the application name to use in the log output.
	 *
	 * @param name application name string
	 */
	public void setApplicationName(String name) {
		m_applicationName = name;
	}

	/**
	 * Get the application name currently used in the log output.
	 *
	 * @return application name string
	 */
	public String getApplicationName() {
		if (m_applicationName == null)
			return this.getClassName();
		else
			return m_applicationName;
	}

	/**
	 * Get whether or not debug messages are logged.
	 *
	 * @return <code>true</code> if debug messages are being logged;
	 * <code>false</code> otherwise
	 */
	public boolean getDebugMode() {
		return m_debugMode;
	}

	/**
	 * Set whether or not debug messages are logged.
	 *
	 * @param debugMode <code>true</code> if debug messages are to be
	 * logged; <code>false</code> otherwise
	 */
	public void setDebugMode(boolean debugMode) {
		m_debugMode = debugMode;
	}

	/**
	 * Load the plugin into a {@link com.sigio.js.ScriptRunner}. This
	 * method is invoked automatically when the plugin is
	 * loaded. There is no need for your client code to invoke this
	 * method directly.
	 *
	 * @param cx context provided by the caller
	 * @param scope scriptable scope provided by the caller
	 * @return <code>true</code> if all of the load operations
	 * succeeded; <code>false</code> otherwise
	 */
	public boolean load(Context cx, Scriptable scope) {
		try {
			Class<?> myClass = LoggerPlugin.class;
			// log_stdout:
			Method logStdout = myClass.getMethod("logStdoutImpl", String.class);
			FunctionObject logStdoutFunc = new FunctionObject("log_stdout", logStdout, this);
			ScriptableObject.defineProperty(scope, "log_stdout", logStdoutFunc, ScriptableObject.DONTENUM);
			// log_stderr:
			Method logStderr = myClass.getMethod("logStderrImpl", String.class);
			FunctionObject logStderrFunc = new FunctionObject("log_stderr", logStderr, this);
			ScriptableObject.defineProperty(scope, "log_stderr", logStderrFunc, ScriptableObject.DONTENUM);
			// log_activity:
			Method logActivity = myClass.getMethod("logActivityImpl", String.class);
			FunctionObject logActivityFunc = new FunctionObject("log_activity", logActivity, this);
			ScriptableObject.defineProperty(scope, "log_activity", logActivityFunc, ScriptableObject.DONTENUM);
			// log_debug:
			Method logDebug = myClass.getMethod("logDebugImpl", String.class);
			FunctionObject logDebugFunc = new FunctionObject("log_debug", logDebug, this);
			ScriptableObject.defineProperty(scope, "log_debug", logDebugFunc, ScriptableObject.DONTENUM);
			ScriptableObject.defineProperty(scope, "debug", logDebugFunc, ScriptableObject.DONTENUM);
			// log_error:
			Method logError = myClass.getMethod("logErrorImpl", String.class);
			FunctionObject logErrorFunc = new FunctionObject("log_error", logError, this);
			ScriptableObject.defineProperty(scope, "log_error", logErrorFunc, ScriptableObject.DONTENUM);
			// log_info::
			Method logInfo = myClass.getMethod("logInfoImpl", String.class);
			FunctionObject logInfoFunc = new FunctionObject("log_info", logInfo, this);
			ScriptableObject.defineProperty(scope, "log_info", logInfoFunc, ScriptableObject.DONTENUM);
			// log_internal:
			Method logInternal = myClass.getMethod("logInternalImpl", String.class);
			FunctionObject logInternalFunc = new FunctionObject("log_internal", logInternal, this);
			ScriptableObject.defineProperty(scope, "log_internal", logInternalFunc, ScriptableObject.DONTENUM);
			// log_warn:
			Method logWarn = myClass.getMethod("logWarnImpl", String.class);
			FunctionObject logWarnFunc = new FunctionObject("log_warn", logWarn, this);
			ScriptableObject.defineProperty(scope, "log_warn", logWarnFunc, ScriptableObject.DONTENUM);
			ScriptableObject.defineProperty(scope, "alert", logWarnFunc, ScriptableObject.DONTENUM);
		}
		catch (NoSuchMethodException e) {
			// Should not happen, but just in case:
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Unload the plugin from a {@link
	 * com.sigio.js.ScriptRunner}. This method is invoked
	 * automatically when the plugin is unloaded. There is no need for
	 * your client code to invoke this method directly.
	 *
	 * @param cx context provided by the caller
	 * @param scope scriptable scope provided by the caller
	 * @return <code>true</code> if all of the load operations
	 * succeeded; <code>false</code> otherwise
	 */
	public boolean unload(Context cx, Scriptable scope) {
		if (ScriptableObject.deleteProperty(scope, "log_stdout") &&
			ScriptableObject.deleteProperty(scope, "log_stderr") &&
			ScriptableObject.deleteProperty(scope, "log_activity") &&
			ScriptableObject.deleteProperty(scope, "log_debug") &&
			ScriptableObject.deleteProperty(scope, "debug") &&
			ScriptableObject.deleteProperty(scope, "log_error") &&
			ScriptableObject.deleteProperty(scope, "log_info") &&
			ScriptableObject.deleteProperty(scope, "log_internal") &&
			ScriptableObject.deleteProperty(scope, "log_warn") &&
			ScriptableObject.deleteProperty(scope, "alert"))
			return true;
		else
			return false;
	}

	/**
	 * This is an implementation method. Your code should never call
	 * this method directly. Unfortunately, it needs to be public in
	 * order to function when called from JavaScript.
	 */ 
	public void logStdoutImpl(String string) {
		System.out.printf("%s %s: %s\n", this.getTimeStampStr(), this.getApplicationName(), string);
	}

	/**
	 * This is an implementation method. Your code should never call
	 * this method directly. Unfortunately, it needs to be public in
	 * order to function when called from JavaScript.
	 */ 
	public void logStderrImpl(String string) {
		System.err.printf("%s %s: %s\n", this.getTimeStampStr(), this.getApplicationName(), string);
	}

	/**
	 * This is an implementation method. Your code should never call
	 * this method directly. Unfortunately, it needs to be public in
	 * order to function when called from JavaScript.
	 */ 
	public void logActivityImpl(String string) {
		logToStream(m_activityStream, m_resources.getString("activity"), string);
	}

	/**
	 * This is an implementation method. Your code should never call
	 * this method directly. Unfortunately, it needs to be public in
	 * order to function when called from JavaScript.
	 */ 
	public void logDebugImpl(String string) {
		if (m_debugMode == false) return;
		else
			logToStream(m_debugStream, m_resources.getString("debug"), string);
	}

	/**
	 * This is an implementation method. Your code should never call
	 * this method directly. Unfortunately, it needs to be public in
	 * order to function when called from JavaScript.
	 */ 
	public void logErrorImpl(String string) {
		logToStream(m_errorStream, m_resources.getString("error"), string);
	}

	/**
	 * This is an implementation method. Your code should never call
	 * this method directly. Unfortunately, it needs to be public in
	 * order to function when called from JavaScript.
	 */ 
	public void logInfoImpl(String string) {
		logToStream(m_infoStream, m_resources.getString("info"), string);
	}

	/**
	 * This is an implementation method. Your code should never call
	 * this method directly. Unfortunately, it needs to be public in
	 * order to function when called from JavaScript.
	 */ 
	public void logInternalImpl(String string) {
		logToStream(m_internalStream, m_resources.getString("internal"), string);
	}

	/**
	 * This is an implementation method. Your code should never call
	 * this method directly. Unfortunately, it needs to be public in
	 * order to function when called from JavaScript.
	 */ 
	public void logWarnImpl(String string) {
		logToStream(m_warnStream, m_resources.getString("warn"), string);
	}

	private void loadResources() {
		m_resources = ResourceBundle.getBundle("com.sigio.js.scriptrunner.plugins.LoggerPlugin");
		m_dateFormat = new SimpleDateFormat(m_resources.getString("dateformat"));
	}

	private String getTimeStampStr() {
		FieldPosition pos = new FieldPosition(SimpleDateFormat.DATE_FIELD);
		Date now = new Date();
		StringBuffer buf = new StringBuffer();
		m_dateFormat.format(now, buf, pos);
		return buf.toString();
	}

	private void logToStream(OutputStream stream, String logLevel, String message) {
		try {
			String output = String.format("%s %s: %s %s%s", getTimeStampStr(), getApplicationName(), logLevel, message, m_EOL);
			if (stream == null)
				stream = m_outputStream;
			stream.write(output.getBytes());
			stream.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
