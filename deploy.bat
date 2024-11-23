@echo off
REM Change directory to the project's root directory
cd /d "%~dp0"

REM Step 1: Clean and install the Maven project
echo Running Maven clean install...
mvn clean install
if %errorlevel% neq 0 (
    echo [ERROR] Maven clean install failed. Exiting.
    exit /b %errorlevel%
)

REM Step 2: Sign the source JAR
echo Signing the source JAR...
gpg -ab target/spring-boot-starter-auth-1.0.3-sources.jar
if %errorlevel% neq 0 (
    echo [ERROR] Signing source JAR failed. Exiting.
    exit /b %errorlevel%
)

REM Step 3: Sign the POM file
echo Signing the POM file...
gpg -ab target/spring-boot-starter-auth-1.0.3.pom
if %errorlevel% neq 0 (
    echo [ERROR] Signing POM file failed. Exiting.
    exit /b %errorlevel%
)

REM Step 4: Sign the Javadoc JAR
echo Signing the Javadoc JAR...
gpg -ab target/spring-boot-starter-auth-1.0.3-javadoc.jar
if %errorlevel% neq 0 (
    echo [ERROR] Signing Javadoc JAR failed. Exiting.
    exit /b %errorlevel%
)

REM Step 5: Sign the main JAR
echo Signing the main JAR...
gpg -ab target/spring-boot-starter-auth-1.0.3.jar
if %errorlevel% neq 0 (
    echo [ERROR] Signing main JAR failed. Exiting.
    exit /b %errorlevel%
)

REM Step 6: Deploy the Maven project
echo Deploying Maven project...
mvn clean deploy
if %errorlevel% neq 0 (
    echo [ERROR] Maven deploy failed. Exiting.
    exit /b %errorlevel%
)

echo [SUCCESS] All steps completed successfully!
pause
