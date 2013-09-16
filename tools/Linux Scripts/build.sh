#!/bin/bash
################################################################################
###
###             By Mystick, Based on Resp@wner script on L2jfree
###
################################################################################
trap finish 2
pomxmlLSPath="../loginserver"
pomxmlGSPath="../gameserver"
pomxmlCMPath="../commons"
pomxmlCSPath="../chatserver"

cd ./svn/egs2.7_merge/zips

if [ "$pomxmlLSPath" = "$pomxmlGSPath" ]; then
   echo "Problem with Path Server"
else
   echo "The path of the LoginServer project is $pomxmlLSPath"
   echo "The path of the GameServer project is $pomxmlGSPath"
   echo "The path of the Commons project is $pomxmlCMPath"
   echo "The path of the ChatServer project is $pomxmlCSPath"
fi
   echo "Moving to folder $buildxmlCMPath"
   cd $pomxmlCMPath
   ant
   echo "Moving to folder $pomxmlLSPath"
   cd $pomxmlLSPath
   ant
	unzip ./build/ae-loginserver.zip -x -d ./../../../server_2.7
   echo "Moving to folder $pomxmlGSPath"
   cd $pomxmlGSPath
   ant
	unzip ./build/ae-gameserver.zip -x -d ./../../../server_2.7
   echo "Moving to folder $pomxmlCSPath"
   cd $pomxmlCSPath
   ant
	unzip ./build/ae-chatserver.zip -x -d ./../../../server_2.7
