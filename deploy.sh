#!/bin/sh

read -p "Password please: " -s password
app=$1
env=$2
ident=$3

naisd upload -u deployment -p d3pl0y -a $app -v $VERSION
naisd deploy -u $ident -p $password -a $app -e $env -n $env -v $VERSION | awk '!/password/'
