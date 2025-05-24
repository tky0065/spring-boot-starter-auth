@echo off
REM Script de déploiement pour spring-boot-starter-auth sur Maven Central
REM --------------------------------------------------------------------

echo.
echo ==================================================
echo   DÉPLOIEMENT DE SPRING-BOOT-STARTER-AUTH
echo ==================================================
echo.

REM Changer de répertoire pour le répertoire racine du projet
cd /d "%~dp0"

REM Récupérer la version actuelle du projet à partir du pom.xml
for /f "tokens=*" %%a in ('powershell -Command "(Select-String -Path pom.xml -Pattern '<version>(.*?)</version>' | Select-Object -First 1).Matches.Groups[1].Value"') do set VERSION=%%a
echo Version détectée: %VERSION%
echo.

REM Étape 1: Vérification des prérequis
echo [1/6] Vérification des prérequis...
where gpg >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERREUR] GPG n'est pas installé ou n'est pas dans le PATH. Veuillez installer GPG.
    exit /b 1
)
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERREUR] Maven n'est pas installé ou n'est pas dans le PATH. Veuillez installer Maven.
    exit /b 1
)
echo [OK] Tous les prérequis sont présents.
echo.

REM Étape 2: Vérification de la configuration Maven
echo [2/6] Vérification de la configuration Maven pour Sonatype...
if not exist "%USERPROFILE%\.m2\settings.xml" (
    echo [ATTENTION] Le fichier settings.xml n'a pas été trouvé dans votre répertoire .m2
    echo Assurez-vous que vos identifiants Sonatype sont configurés dans settings.xml
    echo avec les serveurs :
    echo   - serverId: ossrh
    echo   - username: votre_identifiant_sonatype
    echo   - password: votre_mot_de_passe_sonatype
    set /p confirmation="Voulez-vous continuer quand même? (O/N): "
    if /i "%confirmation%" neq "O" (
        echo Déploiement annulé.
        exit /b 1
    )
) else (
    echo [OK] Fichier settings.xml trouvé.
)
echo.

REM Étape 3: Clean et install du projet Maven
echo [3/6] Exécution de Maven clean install...
call mvn clean install
if %errorlevel% neq 0 (
    echo [ERREUR] Maven clean install a échoué. Sortie.
    exit /b %errorlevel%
)
echo [OK] Build réussi.
echo.

REM Étape 4: Génération des Javadoc et sources
echo [4/6] Génération des JARs Javadoc et sources...
call mvn javadoc:jar source:jar
if %errorlevel% neq 0 (
    echo [ERREUR] La génération des Javadoc ou sources a échoué. Sortie.
    exit /b %errorlevel%
)
echo [OK] Javadoc et sources générés.
echo.

REM Étape 5: Signature des fichiers avec GPG
echo [5/6] Signature des fichiers et déploiement vers Sonatype...

REM Vérification de la présence d'une clé GPG
gpg --list-secret-keys | findstr /C:"sec" >nul
if %errorlevel% neq 0 (
    echo [ERREUR] Aucune clé GPG trouvée. Veuillez générer une clé GPG avec 'gpg --gen-key'.
    echo Assurez-vous également de l'avoir publiée avec 'gpg --keyserver hkp://keyserver.ubuntu.com --send-keys VOTRE_CLE_ID'
    exit /b 1
)

REM Demande du mot de passe GPG si non défini comme variable d'environnement
if "%GPG_PASSPHRASE%"=="" (
    set /p GPG_PASSPHRASE="Entrez votre mot de passe GPG (ne sera pas affiché): "
    echo.
)

echo Déploiement sur Sonatype OSSRH...
call mvn clean deploy -P release -Dgpg.passphrase="%GPG_PASSPHRASE%"

if %errorlevel% neq 0 (
    echo [ERREUR] Le déploiement avec signature GPG a échoué.
    echo Vérifiez les erreurs ci-dessus.
    echo Assurez-vous que :
    echo 1. Votre mot de passe GPG est correct
    echo 2. Votre configuration dans settings.xml est correcte
    echo 3. Votre profil 'release' dans pom.xml contient les plugins requis
    exit /b %errorlevel%
)
echo [OK] Déploiement initial réussi.
echo.

REM Étape 6: Instructions pour finaliser la publication
echo [6/6] Instructions pour finaliser la publication sur Maven Central...
echo.
echo Votre artefact a été déployé sur le serveur staging de Sonatype.
echo Pour finaliser la publication, vous devez :
echo.
echo 1. Se connecter à https://central.sonatype.com/
echo 2. Naviguer vers "Repositories" puis "Staging Repositories"
echo 3. Trouver votre repository (commençant généralement par iogithub-tky0065)
echo 4. Vérifier le contenu et cliquer sur "Close" puis "Release"
echo.
echo Note: La première fois, le processus peut prendre jusqu'à 2 heures pour que
echo       votre artifact soit visible sur Maven Central après l'étape "Release".
echo       Les publications suivantes seront généralement plus rapides.
echo.

echo ==================================================
echo   DÉPLOIEMENT SUR STAGING TERMINÉ AVEC SUCCÈS
echo   Version: %VERSION%
echo ==================================================
echo.
echo N'oubliez pas de finaliser manuellement la publication sur central.sonatype.com
echo.

pause
