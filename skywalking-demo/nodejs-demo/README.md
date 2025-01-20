## 1、依赖包

不需要下载

## 2、配置参数
  
[配置参数](https://github.com/apache/skywalking-nodejs/blob/master/README.md)

## 3、对接SDK

```nodejs
const {default: agent} = require("skywalking-backend-js");
agent.start({})
```

## 4、设置配置
格式、环境变量名称可以参考配置参数
### 4.1、环境变量设置
```shell
export SW_AGENT_NAME=<your_service_name>
export SW_AGENT_COLLECTOR_BACKEND_SERVICES=<skywalking_service>
```

### 4.2、硬代码设置
```nodejs
agent.start({
  serviceName: <your-service-name>,                     // 服务名称，标识应用
  serviceInstance: <your-service-instance-name>,        // 服务实例名称
  collectorAddress: <collector-backend-address>,        // 数据上报collector接入点地址
  authorization: <collector-token>                      // 接入点token
});
```
  
## 5、构建

无特殊要求

## 6、demo示例

### 6.1、打包镜像
```
Dockerfile所在目录执行

docker build -f Dockerfile -t gientech2025/nodejs-demo:latest .
```
### 6.2、创建命名空间
```
创建demo部署的命名空间
kubectl create ns demo
```
### 6.3、安装部署
```
helm install skywalking-demo -n demo ../chart/ \
  --set skywalking.service=tracing.istio-system.svc.cluster.local:11800 \
  --set image=gientech2025/nodejs-demo:latest
```
