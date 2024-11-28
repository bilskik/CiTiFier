param (
 [string]$HOST_IP
)

minikube start --insecure-registry="${HOST_IP}:5000"