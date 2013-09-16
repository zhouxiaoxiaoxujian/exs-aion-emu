@echo off
title Aion Extreme Emulator Closed Source [BuildAll Console] by Ares!Dallas
set PATH="C:\SDK\jdk1.6.0_24\bin";%PATH%
CLS
:MENU
ECHO.
ECHO.
ECHO.
ECHO                                  ''~``
ECHO                                 ( o o )
ECHO    ------------------------.oooO--(_)--Oooo.------------------------
ECHO    .             1 - Build PacketSamurai AE                          .
ECHO    .             2 - Quit                                          .
ECHO    .                         .oooO                                 .
ECHO    .                         (   )   Oooo.                         .
ECHO    ---------------------------\ (----(   )--------------------------
ECHO                                \_)    ) /
ECHO                                      (_/
ECHO.
ECHO.
SET /P Ares=Type 1, or 2 to QUIT, then press ENTER:
IF %Ares%==1 GOTO PacketSamuraiAE
IF %Ares%==2 GOTO QUIT
:FULL

:PacketSamuraiAE
cd ..\Tools\PacketSamuraiAE
start /WAIT /B ..\Ant\bin\ant clean dist
:QUIT
exit
