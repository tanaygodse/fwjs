# Update these variables to match the locations
JUNIT_JAR=/Users/tanaygodse/Downloads/junit-4.10.jar
HAMCREST_JAR=/Users/tanaygodse/.m2/repository/org/hamcrest/hamcrest/2.2/hamcrest-2.2.jar
ANTLR_JAR=/Users/tanaygodse/Downloads/antlr-4.4-complete.jar

# These variables should not need to be changed
GRAMMAR_NAME=FeatherweightJavaScript
GRAMMAR=${GRAMMAR_NAME}.g4
TEST_CLASSPATH=${JUNIT_JAR}:${HAMCREST_JAR}:${ANTLR_JAR}
FWJS_SCRIPT_DIR=fwjsScripts
SCRIPTS=closure.fwjs examples.fwjs functions.fwjs operators.fwjs test.fwjs \
				controlStructs.fwjs factorial.fwjs lists.fwjs scoping.fwjs while.fwjs \
				print.fwjs prototype.fwjs objects.fwjs test_proto.fwjs fileio.fwjs import.fwjs
SCRIPT_PROTO = prototype.fwjs
SCRIPT_CLOSURE = closure.fwjs 
SCRIPT_OBJECT = objects.fwjs
SCRIPT_CAP = capimport.fwjs
SCRIPT_CARD = import_luhn.fwjs
TEST_NEW = test_fc3.fwjs
TREES_DIR=parseTrees
# Choosing build instead of bin to avoid conflicts with Eclipse
BUILD_DIR=build
SRC_FOLDERS=edu/sjsu/fwjs
PACKAGE_NAME=edu.sjsu.fwjs
GEN_SRC_BASE_DIR=generatedSrc
PARSER_SRC_FOLDERS=edu/sjsu/fwjs/parser
GEN_SRC_DIR=${GEN_SRC_BASE_DIR}/${PARSER_SRC_FOLDERS}
PARSER_PACKAGE_NAME=edu.sjsu.fwjs.parser
ZIP_FILE=solution.zip

.PHONY: all test run clean spotless generate
all: generate
	mkdir -p ${BUILD_DIR}/${SRC_FOLDERS}
	javac -Xlint:unchecked -cp ${TEST_CLASSPATH} -d ${BUILD_DIR} src/${SRC_FOLDERS}/*.java testSrc/${SRC_FOLDERS}/*.java ${GEN_SRC_DIR}/*.java

generate: ${GRAMMAR}
	mkdir -p ${GEN_SRC_DIR}
	java -jar ${ANTLR_JAR} -no-listener -visitor ${GRAMMAR} -o ${GEN_SRC_DIR}

parse:
	mkdir -p ${TREES_DIR}
	$(foreach script, ${SCRIPTS}, java -cp ${BUILD_DIR}:${ANTLR_JAR} org.antlr.v4.gui.TestRig \
		bin.${PARSER_PACKAGE_NAME}.${GRAMMAR_NAME} prog -gui ${FWJS_SCRIPT_DIR}/${script} > ${TREES_DIR}/${script}.tree;)

test:
	java -cp ${BUILD_DIR}:${TEST_CLASSPATH} org.junit.runner.JUnitCore ${PACKAGE_NAME}.ExpressionTest

run:
	$(foreach script, ${SCRIPTS}, echo "Running ${FWJS_SCRIPT_DIR}/${script}"; \
		java -cp ${BUILD_DIR}:${ANTLR_JAR} ${PACKAGE_NAME}.Interpreter ${FWJS_SCRIPT_DIR}/${script};)

run_proto:
	$(foreach script, ${SCRIPT_PROTO}, echo "Running ${FWJS_SCRIPT_DIR}/${script}"; \
		java -cp ${BUILD_DIR}:${ANTLR_JAR} ${PACKAGE_NAME}.Interpreter ${FWJS_SCRIPT_DIR}/${script};)

run_closure:
	$(foreach script, ${SCRIPT_CLOSURE}, echo "Running ${FWJS_SCRIPT_DIR}/${script}"; \
		java -cp ${BUILD_DIR}:${ANTLR_JAR} ${PACKAGE_NAME}.Interpreter ${FWJS_SCRIPT_DIR}/${script};)

run_object:
	$(foreach script, ${SCRIPT_OBJECT}, echo "Running ${FWJS_SCRIPT_DIR}/${script}"; \
		java -cp ${BUILD_DIR}:${ANTLR_JAR} ${PACKAGE_NAME}.Interpreter ${FWJS_SCRIPT_DIR}/${script};)

run_cap:
	$(foreach script, ${SCRIPT_CAP}, echo "Running ${FWJS_SCRIPT_DIR}/${script}"; \
		java -cp ${BUILD_DIR}:${ANTLR_JAR} ${PACKAGE_NAME}.Interpreter ${FWJS_SCRIPT_DIR}/${script};)

run_card:
	$(foreach script, ${SCRIPT_CARD}, echo "Running ${FWJS_SCRIPT_DIR}/${script}"; \
		java -cp ${BUILD_DIR}:${ANTLR_JAR} ${PACKAGE_NAME}.Interpreter ${FWJS_SCRIPT_DIR}/${script};)

run_new:
	$(foreach script, ${TEST_NEW}, echo "Running ${FWJS_SCRIPT_DIR}/${script}"; \
		java -cp ${BUILD_DIR}:${ANTLR_JAR} ${PACKAGE_NAME}.Interpreter ${FWJS_SCRIPT_DIR}/${script};)



${ZIP_FILE}:
	zip ${ZIP_FILE} src/${SRC_FOLDERS}/*.java ${GRAMMAR} 
	$(foreach script, ${SCRIPTS}, ${FWJS_SCRIPT_DIR}/${script};)
clean:
	-rm -r ${BUILD_DIR}

spotless: clean
	-rm ${ZIP_FILE}
	-rm -r ${GEN_SRC_BASE_DIR}
	-rm -r ${TREES_DIR}

