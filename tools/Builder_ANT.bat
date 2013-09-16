@echo off
title Aion Extreme 3.0 Compilation Builder Script.

:MENU
color F0
CLS
ECHO.
ECHO        =[ 3.5 ]================================================[ + ]=
ECHO.
ECHO            Aion Extreme 3.0 Compilation Builder Script (build.xml req.)
ECHO.
ECHO          =========================[ MENU ]========================
ECHO.
ECHO                  [1] - Compile ALL
ECHO.
ECHO                  [2] - Compile LoginServer
ECHO                  [3] - Compile GameServer
ECHO                  [4] - Compile ChatServer
ECHO                  [5] - Compile Commons
ECHO.
ECHO                  [6] - Exit
ECHO.
ECHO        =================================================================
ECHO.
SET /P Ares=              Select: 1, 2, 3, 4, 5 ??? 6, and press ENTER: 
IF %Ares%==1 GOTO FULL
IF %Ares%==2 GOTO loginserver
IF %Ares%==3 GOTO gameserver
IF %Ares%==4 GOTO chatserver
IF %Ares%==5 GOTO commons
IF %Ares%==6 GOTO QUIT

:FULL
CLS
ECHO ============================================================================
ECHO You Selected the Full Compiling
ECHO ============================================================================
ECHO.
cd ..\commons
call ..\Tools\Ant\bin\ant clean dist

cd ..\gameserver
call ..\Tools\Ant\bin\ant clean dist

cd ..\loginserver
call ..\Tools\Ant\bin\ant clean dist

cd ..\chatserver
call ..\Tools\Ant\bin\ant clean dist

GOTO :QUIT

:commons
CLS
ECHO ============================================================================
ECHO You Selected the Commons Compiling
ECHO ============================================================================
ECHO.
cd ..\commons
start /WAIT /B ..\Tools\Ant\bin\ant clean dist
GOTO :QUIT

:gameserver
CLS
ECHO ============================================================================
ECHO You Selected the Game Server Compiling
ECHO ============================================================================
ECHO.
cd ..\AL-Game
start /WAIT /B ..\Tools\Ant\bin\ant clean dist
GOTO :QUIT

:loginserver
CLS
ECHO ============================================================================
ECHO You Selected Login Server Compiling
ECHO ============================================================================
ECHO.
cd ..\AL-Login
start /WAIT /B ..\Tools\Ant\bin\ant clean dist
GOTO :QUIT

:chatserver
CLS
ECHO ============================================================================
ECHO You Selected the Chat Server Compiling
ECHO ============================================================================
ECHO.
cd ..\AL-Chat
start /WAIT /B ..\Tools\Ant\bin\ant clean dist
CLS
GOTO :QUIT

:QUIT
exit
