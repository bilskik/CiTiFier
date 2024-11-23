param (
 [string]$N
)

if(-not $N) {
 Write-Host "Provide namespace"
}

kubectl delete service -l app=serviceLabel -n $N
kubectl delete service -l app=headlessService -n $N
kubectl delete deployment -l app=deploymentLabel -n $N
kubectl delete statefulset -l app=postgresStatefulset -n $N
kubectl delete configmap -l app=configMap -n $N
kubectl delete secret -l app=secret -n $N
