## deploy api lib: field-config
scp target/*.jar mall@node1.host.smallsaas.cn:/home/mall/biliya/deploy-lib/lib
ssh mall@node1.host.smallsaas.cn "cd /home/mall/biliya/deploy-lib && sh deploy-api-lib"
ssh mall@node1.host.smallsaas.cn "docker restart mall-api && docker logs -f mall-api"
