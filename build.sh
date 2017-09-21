#!/bin/bash
printf "(abc,cde),(aef,sgy),(123,sdf)\n(zxc,vbn),(asd,fgh),(qwe,rrt)" > ./build/default.DAT
javac com/ecc/Main.java -d ./build