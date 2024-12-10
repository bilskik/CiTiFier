param (
 [string]$N
)

if(-not $N) {
 Write-Host "Provide namespace"
}

kubectl delete service -l app=node-port -n $N
kubectl delete service -l app=headless-service -n $N
kubectl delete deployment -l app=deployment -n $N
kubectl delete statefulset -l app=statefulset -n $N
kubectl delete configmap -l app=config-map -n $N
kubectl delete secret -l app=secret -n $N
kubectl delete namespace $N
