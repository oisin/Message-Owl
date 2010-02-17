#!/bin/bash
source=`pwd`
build_feature=message_owl

# Update these properties to point to the locations of your Eclipse JEE package
# install, your Orbit install (if you have one) and the directory you want the
# build to go to.

platform=${source}/platform/eclipse
orbit=${source}/orbit
build=${source}/build
debug=
clean=no
force=i

while [ $# -gt 0 ] ; do
  case $1 in
    -p) shift; platform=$1 ;;
    -b) shift; orbit=$1 ;;
    -o) shift; build=$1 ;;
    -f) shift; build_feature=$1;;
    -d) debug="-L DEBUG" ;;
    -c) clean=yes ;;
    -fc) clean=yes; force=f ;;
     *) echo option $1 ignored ;;
  esac;
  shift
done

# No buckminster on the PATH? Put its location here. Make sure there is a
# slash (/) on the end of the path or the expansion won't work correctly.

buckminster=

# This is the line that kicks it all off
if [ ${clean} = no ]
then
  ${buckminster}buckminster -data ${build} ${debug} -S ${source}/${build_feature}/buckminster.script -vmargs -Xmx1024m -Dsource=${source} -Dplatform=${platform} -Dorbit=${orbit} -Dbuild=${build} -Dfeature=${build_feature}
  if [ -f ${build}/output/${build_feature}*/*.zip ]
  then
    mv ${build}/output/${build_feature}*/*.zip ${build} && echo "Zipped p2 update site is available in ${build} directory"
  fi
else
   echo "Cleaning..."
   if [ ! -z ${build} -a -w ${build} -a ${build} != "/" ]
   then
     echo "Deleting old workspace at ${build}..."
     rm -r${force} ${build}
   fi
fi

