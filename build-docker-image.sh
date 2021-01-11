#!/usr/bin/env sh

[ "$1" = "push" ] && withPush=true || withPush=false

mvn clean package

function build_docker_image() {
    ver=$1

    cp Dockerfile target/Dockerfile

    if [ "$(uname)" == "Darwin" ]; then
      sed -i "" "s/<VERSION>/$ver/g" target/Dockerfile
    else
      sed -i"" "s/<VERSION>/$ver/g" target/Dockerfile
    fi

    docker build -t jmetric:$ver -f target/Dockerfile .
    docker tag jmetric:$ver jvmbytes/jmetric:$ver

    if [ $withPush == true ]; then docker push jvmbytes/jmetric:$ver; fi
}

build_docker_image 8-jre
build_docker_image 8u275
build_docker_image 11-jre


