#!/bin/sh
# Cygwin support.  $cygwin must be set to either true or false.
case "`uname`" in
CYGWIN*) cygwin=true ;;
    *) cygwin=false ;;
esac

# Determine the appropriate java executable.
if [ -z "$JAVA_HOME" ]
then
  echo "Missing enviornment variable -- JAVA_HOME"
  echo "JAVA_HOME should point to the root directoy of the JDK."
  echo "Set this variable in livejava-env.sh."
  exit
else
  # Convert JAVA_HOME to unix style, since that's how paths will be appended.
  # This will be converted back before it is used.
  if $cygwin; then
    JAVA_HOME=`cygpath --path --unix "$JAVA_HOME"`
  fi
  JAVA_CMD=$JAVA_HOME/bin/java
fi
echo "JAVA_HOME: $JAVA_HOME"

# See if the version was specified.  This is done with the -V flag.
if [ "$1" = "-V" ]
then
    shift
    if [ -z "$1" ]
    then
        echo "usage:"
        echo "build.sh [-V version] ..."
        exit
    else
        VERSION=$1
        shift
    fi
else
    VERSION=0.0.0.0
fi
echo "VERSION: $VERSION"

# Set the classpath for running ant.
# Include all files in the ant lib directoy and the Java tools.jar file.
CP=.:${JAVA_HOME}/lib/tools.jar
for i in `find ../../../Support/ant | grep '\.jar$'` ; do
  CP=${CP}:$i
done

# See if the ant target requires arguments.  If so, add them.
case "$1" in
'class' | 'test_class')
    # Make sure an additional parameter was specified.
    if [ -z "$2" ]
    then
        echo "$1 usage:"
        echo "build.sh [-V version] $1 full_class_name [ARGS]"
        exit
    fi

    # The target is the first parameter.
    TARGET=$1
    shift

    # The class is the next parameter.
    CLASS=$1
    shift

    # The rest of the parameters (if any) are the class ARGS.
    CLASS_ARGS=
    while [ "$1" ]
    do
        # See if there's a space in the argument.
        HAS_SPACE=`echo "$1" | grep ' '`
        if [ "$HAS_SPACE" ]
        then
            # Arguments with spaces need to be quoted.
            CLASS_ARGS="$CLASS_ARGS\\\"$1\\\""
        else
            # Just add the argument (prepended by a space).
            CLASS_ARGS="$CLASS_ARGS$1"
        fi

        shift
        
        # Add a space between arguments if appropriate.
        if [ "$1" ]
        then
            CLASS_ARGS="$CLASS_ARGS "
        fi
    done

    ANT_ARGS="-Dclass=$CLASS -DARGS=\"$CLASS_ARGS\""
;;
'test')
    # The target is the first parameter.
    TARGET=$1
    shift

    # The class (if specified) is the next parameter.
    if [ "$1" ] 
    then
        CLASS=$1
        ANT_ARGS="-Dclass=$CLASS"
    fi
;;
'')
    TARGET=
;;
*)
    TARGET=$1
esac

# For Cygwin, switch paths to Windows format before proceeding.
if $cygwin; then
    CP=`cygpath --path --windows "$CP"`
fi

# run ant.
ANT_CMD="\"$JAVA_CMD\" -DJAVA_HOME=\"$JAVA_HOME\" -DVERSION=$VERSION -classpath \"$CP\" org.apache.tools.ant.Main $TARGET $ANT_ARGS"
echo
echo $ANT_CMD
echo
eval $ANT_CMD
