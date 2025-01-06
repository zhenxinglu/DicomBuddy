#!/usr/bin/env bash

#This script build the DicomBuddy installer for Windows. To build the installer for other Linux/Mac,
#this script may need some tuning.

javaFXImageDir=javafxImage
javaFXModules=javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.web,javafx.media

PATH_TO_FX_MODS=/d/software/dev/javafx-jmods-23.0.1
#Pease note that 'PATH_TO_FX_MODS' is the PATH to the JavaFX mods, not JavaFX SDK libs!

if [ -z "$PATH_TO_FX_MODS" ]
then
  echo "Environment variable `PATH_TO_FX_MODS` is not set"
  exit
fi

if [ ! -d $javaFXImageDir ]
then
  #the cutom JRE is not built yet, just build it
  jlink --no-header-files --no-man-pages --strip-debug --output $javaFXImageDir --module-path $PATH_TO_FX_MODS --add-modules $javaFXModules
fi

mvn package

appInfo="-n DicomBuddy --app-version 1.1  --vendor zhenxing.lu"
description="A tool for viewing and editing DICOM files"
copyright="Copyright 2025, All rights reserved"
iconPath=target/classes/icons/DicomBuddy.ico
mainClass=org.kriss.dicombuddy.DicomBuddy
mainJar=DicomBuddy-1.0-SNAPSHOT.jar
customWindowOption="--win-shortcut  --win-dir-chooser --win-menu"
jpackage $appInfo -i target --description "$description" --copyright "$copyright"  --main-jar $mainJar --main-class $mainClass  $customWindowOption --runtime-image $javaFXImageDir --type msi  -d output  --verbose --icon $iconPath
