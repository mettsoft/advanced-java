#!/bin/sh
printf "(abc,cde),(aef,sgy),(123,sdf)\n(zxc,vbn),(asd,fgh),(qwe,rrt)" > ./build/default.DAT
sudo chmod 777 ./build/default.DAT
javac Main.java -d ./build