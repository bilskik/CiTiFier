# CiTiFier

System for deploying CTF challenges. The goal is to automate the stages of challenge
deployment, from retrieving challenges from a GitHub repository, through building Docker application images, loading it into local image registry and
deploy them in a Kubernetes cluster.
 
## Example setup Run
This is only example. You can run local registry with secure connection. Other Kubernetes clusters are also compatible with CiTiFier.

Run local image registry(example):
```
docker run -d -p 5000:5000 --name registry registry:2
```
Run Kubernetes cluster(minikube example with insecure registry):
```
minikube start --insecure-registry="$HOST_IP:5000"
```

## Run app

Run database:
```
docker run --name postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=pg -p 5432:5432 -d postgres:latest
```
Run app:
```
./gradlew bootRun
```

## Customization

System allows to customize some configuration.

System allows to download private repository from Github. In order to do so, you need to create Github App and setup secrets from created app in system:
- oauth2.github.client_id
- oauth2.github.client-secret
- oauth2.github.app-id

If you are on Linux/macOS you need to setup process shell:
Default(for Windows):
- process.shell=powershell.exe
- process.config=-Command

Setup base clone repository path:
- repo.base-file-path

In order to make image pull available from Kubernetes cluster, you need to provide your host machine IP to cluster and CiTiFier:
- docker.host-ip-address=$HOST_IP


