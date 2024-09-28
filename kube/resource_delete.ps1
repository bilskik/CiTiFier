kubectl delete service -l app=nodePort
kubectl delete service -l app=headlessService
kubectl delete deployment -l app=deployment
kubectl delete statefulset -l app=postgres-statefulset
kubectl delete configmap -l app=configMap
kubectl delete secret -l app=secret
