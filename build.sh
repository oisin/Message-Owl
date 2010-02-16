#!/bin/bash
source=`pwd`
build_feature=org.fusesource.tools.messaging.build

# Update these properties to point to the locations of your Eclipse JEE package
# install, your Orbit install (if you have one) and the directory you want the
# build to go to.

platform=${source}/platform/eclipse
orbit=${source}/orbit
build=${source}/build

while [ $# -gt 0 ] ; do
  case $1 in
    -p) shift; platform=$1 ;;
    -b) shift; orbit=$1 ;;
    -o) shift; build=$1 ;;
     *) echo option $1 ignored ;;
  esac;
  shift
done

# No buckminster on the PATH? Put its location here. Make sure there is a
# slash (/) on the end of the path or the expansion won't work correctly.

buckminster=

# This is the line that kicks it all off
${buckminster}buckminster -data ${build} -L DEBUG -S ${source}/org.fusesource.tools.messaging.build/buckminster.script -vmargs -Dsource=${source} -Dplatform=${platform} -Dorbit=${orbit} -Dbuild=${build}

mv ${build}/output/${build_feature}*/*.zip ${build}
echo "Zipped p2 update site is available in ${build} directory"
