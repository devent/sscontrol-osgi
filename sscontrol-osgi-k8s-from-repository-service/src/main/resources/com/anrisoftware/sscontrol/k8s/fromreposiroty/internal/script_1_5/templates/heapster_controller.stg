heapsterController(parent, vars) ::= <<
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: heapster-<parent.heapsterVersion>
  namespace: kube-system
  labels:
    k8s-app: heapster
    kubernetes.io/cluster-service: "true"
    version: <parent.heapsterVersion>
spec:
  replicas: 1
  selector:
    matchLabels:
      k8s-app: heapster
      version: <parent.heapsterVersion>
  template:
    metadata:
      labels:
        k8s-app: heapster
        version: <parent.heapsterVersion>
      annotations:
        scheduler.alpha.kubernetes.io/critical-pod: ''
        scheduler.alpha.kubernetes.io/tolerations: '[{"key":"CriticalAddonsOnly", "operator":"Exists"}]'
    spec:
      containers:
        - image: <parent.heapsterImage>:<parent.heapsterVersion>
          name: heapster
          livenessProbe:
            httpGet:
              path: /healthz
              port: 8082
              scheme: HTTP
            initialDelaySeconds: 180
            timeoutSeconds: 5
          command:
            - /heapster
            - --source=kubernetes.summary_api:''
            - --sink=influxdb:http://monitoring-influxdb:8086
        - image: <parent.heapsterImage>:<parent.heapsterVersion>
          name: eventer
          command:
            - /eventer
            - --source=kubernetes:''
            - --sink=influxdb:http://monitoring-influxdb:8086
        - image: <parent.resizerImage>:<parent.resizerVersion>
          name: heapster-nanny
          resources:
            limits:
              cpu: 50m
              memory: <parent.nannyMemory>
            requests:
              cpu: 50m
              memory: <parent.nannyMemory>
          env:
            - name: MY_POD_NAME
              valueFrom:
                fieldRef:
                  fieldPath: metadata.name
            - name: MY_POD_NAMESPACE
              valueFrom:
                fieldRef:
                  fieldPath: metadata.namespace
          command:
            - /pod_nanny
            - --cpu=<parent.baseMetricsCpu>
            - --extra-cpu=<parent.metricsCpuPerNode>
            - --memory=<parent.baseMetricsMemory>
            - --extra-memory=<parent.metricsMemoryPerNode>
            - --threshold=5
            - --deployment=heapster-<parent.heapsterVersion>
            - --container=heapster
            - --poll-period=300000
            - --estimator=exponential
        - image: <parent.resizerImage>:<parent.resizerVersion>
          name: eventer-nanny
          resources:
            limits:
              cpu: 50m
              memory: <parent.nannyMemory>
            requests:
              cpu: 50m
              memory: <parent.nannyMemory>
          env:
            - name: MY_POD_NAME
              valueFrom:
                fieldRef:
                  fieldPath: metadata.name
            - name: MY_POD_NAMESPACE
              valueFrom:
                fieldRef:
                  fieldPath: metadata.namespace
          command:
            - /pod_nanny
            - --cpu=100m
            - --extra-cpu=0m
            - --memory=<parent.baseEventerMemory>
            - --extra-memory=<parent.eventerMemoryPerNode>
            - --threshold=5
            - --deployment=heapster-<parent.heapsterVersion>
            - --container=eventer
            - --poll-period=300000
            - --estimator=exponential

>>
