#!/bin/sh
## deploy app.jar

scp alliance-api/target/alliance-api-2.0.0-standalone.jar mall@node1.host.smallsaas.cn:/home/mall/biliya/mall-api
ssh mall@node1.host.smallsaas.cn "cd /home/mall/biliya/deploy-lib && sh deploy-api-app"
ssh mall@node1.host.smallsaas.cn "docker restart mall-api"
ssh mall@node1.host.smallsaas.cn "docker logs -f mall-api"
