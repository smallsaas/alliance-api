#!/bin/sh
scp alliance-api/target/alliance-api-2.0.0-standalone.jar mall@node1.host.smallsaas.cn:/home/mall/biliya/mall-api/app.jar
ssh mall@node1.host.smallsaas.cn "docker restart mall-api"

ssh mall@node1.host.smallsaas.cn "docker logs -f mall-api"
