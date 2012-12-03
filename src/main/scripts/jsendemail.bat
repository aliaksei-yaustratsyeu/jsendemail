@echo off

if not "%JAVA_HOME%" == "" goto gotJdkHome
if not "%JRE_HOME%" == "" goto gotJreHome
echo Neither the JAVA_HOME nor the JRE_HOME environment variable is defined
echo At least one of these environment variable is needed to run this program
goto exit

:gotJreHome
if not exist "%JRE_HOME%\bin\java.exe" goto noJavaHome
goto exit

:gotJdkHome
if not exist "%JAVA_HOME%\bin\java.exe" goto noJavaHome
if not "%JRE_HOME%" == "" goto okJavaHome
set "JRE_HOME=%JAVA_HOME%"
goto okJavaHome

:noJavaHome
echo The JAVA_HOME environment variable is not defined correctly
echo This environment variable is needed to run this program
echo NB: JAVA_HOME should point to a JDK not a JRE
goto exit

:okJavaHome

if not "%JSENDEMAIL_HOME%" == "" goto okJSendEmailHome
echo the JSENDEMAIL_HOME environment variable should be defined
goto exit

:okJSendEmailHome


set CMD_LINE_ARGS=
:setArgs
if ""%1""=="""" goto doneSetArgs
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto setArgs


:doneSetArgs

set RUNJAVA="%JRE_HOME%\bin\java"

for %%i in ("%JSENDEMAIL_HOME%"\boot\*) do set CLASSWORLDS_JAR="%%i"


%RUNJAVA% -classpath %CLASSWORLDS_JAR% "-Dclassworlds.conf=%JSENDEMAIL_HOME%\bin\jsendemail.conf" "-Djsendemail.home=%JSENDEMAIL_HOME%" org.codehaus.classworlds.Launcher %CMD_LINE_ARGS%
