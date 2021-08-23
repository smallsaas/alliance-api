if [ $1 == up ];then
echo docker-compose -f build/build.yml up --remove-orphans
docker-compose -f build/build.yml up --remove-orphans
exit
fi

COMPOSE_DOCKER_CLI_BUILD=1 DOCKER_BUILDKIT=1 docker-compose -f build/build.yml build $@ # --no-cache
docker-compose -f build/build.yml up --remove-orphans
