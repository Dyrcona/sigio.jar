# Copyright © 2011,2015,2023 Jason J.A. Stephenson
#
# This file is part of sigio.jar.
#
# sigio.jar is free software: you can redistribute it and/or modify it
# under the terms of the Lesser GNU General Public License as
# published by the Free Software Foundation, either version 3 of the
# License, or (at your option) any later version.
#
# sigio.jar is distributed in the hope that it will be useful, but
# WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the Lesser
# GNU General Public License for more details.
#
# You should have received a copy of the Lesser GNU General Public
# License along with sigio.jar.  If not, see
# <http://www.gnu.org/licenses/>.

# This Makefile assumes that your javac, javadoc and jar tools are
# already in your executable path.  We might add a JDKPATH or some
# such variable so you specify which JDK tools to use in the future.

DOC_DIR ?= doc/

SOURCES = src/com/sigio/io/FilenamePatternFilter.java \
          src/com/sigio/io/FilenameExtensionFilter.java \
          src/com/sigio/io/Logger.java \
          src/com/sigio/io/StreamLogger.java \
          src/com/sigio/sql/Connector.java \
          src/com/sigio/sql/PsqlHandlerTask.java \
          src/com/sigio/sql/PsqlRunnerAdapter.java \
          src/com/sigio/sql/ResultSetTableModel.java \
          src/com/sigio/sql/DbPropertiesFileFilter.java \
          src/com/sigio/util/ValueTransformer.java \
          src/com/sigio/util/YardToMeterValueTransformer.java \
          src/com/sigio/util/FahrenheitToCentigradeValueTransformer.java \
          src/com/sigio/util/InchToMillimeterValueTransformer.java \
          src/com/sigio/games/dice/Die.java \
          src/com/sigio/games/dice/DoublingDie.java \
          src/com/sigio/json/BundleLoader.java \
          src/com/sigio/json/JSONArray.java \
          src/com/sigio/json/JSONException.java \
          src/com/sigio/json/JSON.java \
          src/com/sigio/json/JSONLiteral.java \
          src/com/sigio/json/JSONObject.java \
          src/com/sigio/json/JSONReader.java \
          src/com/sigio/json/JSONStringAdapter.java \
          src/com/sigio/json/JSONValue.java \
          src/com/sigio/json/JSONWriter.java

DOC_SOURCES = $(SOURCES) \
          src/com/sigio/io/package-info.java \
          src/com/sigio/sql/package-info.java \
          src/com/sigio/util/package-info.java \
          src/com/sigio/games/package-info.java \
          src/com/sigio/games/dice/package-info.java \
          src/com/sigio/json/package-info.java

RESOURCES = src/com/sigio/games/dice/Die.properties \
          src/com/sigio/games/dice/DoublingDie.properties \
          src/com/sigio/io/Logger_en_US.properties \
          src/com/sigio/json/JSON.properties \
          src/com/sigio/util/ValueTransformer.properties

.PHONY: documentation compile jar cp-resources clean

jar: compile cp-resources
	jar cf sigio.jar com/
ifndef NO_SRC
	jar uf sigio.jar src/
endif

compile: $(SOURCES)
	javac $(JAVAC_ARGS) -d ./ -sourcepath src/ $^

cp-resources: $(RESOURCES)
	for file in $^ ; \
	do \
		tgt=$${file#src/}; \
		cp $$file $$tgt; \
	done

documentation: $(DOC_SOURCES)
	javadoc -d $(DOC_DIR) -doctitle sigio.jar -windowtitle sigio.jar $^

clean:
	-rm -rf doc/
	-rm -rf com/
	-rm sigio.jar
