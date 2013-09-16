@ECHO off
SET MODE=clean package
SET TITLE=Build
TITLE Aion Lightning - %TITLE% Panel
:MENU
CLS
ECHO.
ECHO   ^*--------------------------------------------------------------------------^*
ECHO   ^|                    Aion Lightning Project - %TITLE% Panel                  ^|
ECHO   ^*--------------------------------------------------------------------------^*
ECHO   ^|                                                                          ^|
ECHO   ^|    1 - Build Login server                           6 - Build mode       ^|
ECHO   ^|    2 - Build Game server                            7 - Install mode     ^|
ECHO   ^|    3 - Build Chat server                            8 - Debug mode       ^|
ECHO   ^|    4 - Build Commons                                9 - Quit             ^|
ECHO   ^|    5 - Build All                                                         ^|
ECHO   ^|                                                                          ^|
ECHO   ^*--------------------------------------------------------------------------^*
ECHO.
:ENTER
SET /P OPTION=Type your option and press ENTER: 
IF %OPTION% == 1 (
CLS
TITLE Aion Lightning - %TITLE%ing Login server
CD AL-Login
call mvn %MODE%
pause
)
IF %OPTION% == 2 (
CLS
TITLE Aion Lightning - %TITLE%ing Game Server
CD AL-Game
call mvn %MODE%
pause
)
IF %OPTION% == 3 (
CLS
TITLE Aion Lightning - %TITLE%ing Chat Server
CD AL-Chat
call mvn %MODE%
pause
)
IF %OPTION% == 4 (
CLS
TITLE Aion Lightning - %TITLE%ing Commons Server
CD AL-Commons
call mvn %MODE%
pause
)
IF %OPTION% == 5 (
CLS
TITLE Aion Lightning - %TITLE%ing All Project
CD AL-Login
call mvn %MODE%
CD ../AL-Game
call mvn %MODE%
CD ../AL-Chat
call mvn %MODE%
CD ../AL-Commons
call mvn %MODE%
pause
)
IF %OPTION% == 6 (
GOTO :MENU
)
IF %OPTION% == 7 (
CLS
SET MODE=clean install
SET TITLE=Install
TITLE Aion Lightning - Install Panel
ECHO.
ECHO   ^*--------------------------------------------------------------------------^*
ECHO   ^|                    Aion Lightning Project - Install Panel                ^|
ECHO   ^*--------------------------------------------------------------------------^*
ECHO   ^|                                                                          ^|
ECHO   ^|    1 - Install Login server                        6 - Build mode        ^|
ECHO   ^|    2 - Install Game server                         7 - Install mode      ^|
ECHO   ^|    3 - Install Chat server                         8 - Debug mode        ^|
ECHO   ^|    4 - Install Commons                             9 - Quit              ^|
ECHO   ^|    5 - Install All                                                       ^|
ECHO   ^|                                                                          ^|
ECHO   ^*--------------------------------------------------------------------------^*
ECHO.
GOTO :ENTER
)
IF %OPTION% == 8 (
CLS
SET MODE=clean package -e -X
SET TITLE=Debug
TITLE Aion Lightning - Debug Panel
ECHO.
ECHO   ^*--------------------------------------------------------------------------^*
ECHO   ^|                    Aion Lightning Project - Debug Panel                  ^|
ECHO   ^*--------------------------------------------------------------------------^*
ECHO   ^|                                                                          ^|
ECHO   ^|    1 - Debug Login server                          6 - Build mode        ^|
ECHO   ^|    2 - Debug Game server                           7 - Install mode      ^|
ECHO   ^|    3 - Debug Chat server                           8 - Debug mode        ^|
ECHO   ^|    4 - Debug Commons                               9 - Quit              ^|
ECHO   ^|    5 - Debug All                                                         ^|
ECHO   ^|                                                                          ^|
ECHO   ^*--------------------------------------------------------------------------^*
ECHO.
GOTO :ENTER
)
IF %OPTION% == 9 (
EXIT
)
IF %OPTION% GEQ 10 (
GOTO :MENU
)
