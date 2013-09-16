:######################################################################## 
:# File name: startManager.bat
:# Edited Last By: vyaslav 
:# V 1.0 1
:######################################################################## 

@echo off
title Manager
:start
echo Aion Extreme Server Manager (Credits vyaslav@AionEngine)
echo /\  _  \  __                    /\  ___\                                        
echo \ \ \ \ \/\_\    ___     ___    \ \ \____    ___      __   __    ___      __    
echo  \ \  __ \/\ \  / __`\ /' _ `\   \ \  ___\ /' _ `\  /'__`\/\_\ /' _ `\  /'__`\  
echo   \ \ \/\ \ \ \/\ \ \ \/\ \/\ \   \ \ \____/\ \/\ \/\ \ \ \/\ \/\ \/\ \/\  __/  
echo    \ \_\ \_\ \_\ \____/\ \_\ \_\   \ \_____\ \_\ \_\ \___, \ \ \ \_\ \_\ \____\ 
echo     \/_/\/_/\/_/\/___/  \/_/\/_/    \/_____/\/_/\/_/\/_____ \ \_\/_/\/_/\/____/ 
echo                                                        /\____\/_/               
echo                                                        \/____/                  
echo.
echo 
echo.
REM -------------------------------------
REM Default parameters for a basic server.
java -Xms8m -Xmx32m -ea -cp ./libs/*;manager.jar com.aionengine.manager.Manager
REM
REM -------------------------------------

SET CLASSPATH=%OLDCLASSPATH%

if ERRORLEVEL 1 goto error
goto end
:error
echo.
echo Manager Terminated Abnormaly, Please Verify Your Files.
echo.
:end
echo.
echo Manager Terminated.
echo.
pause