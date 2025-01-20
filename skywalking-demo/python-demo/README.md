## 1、依赖包

不需要下载

## 2、配置参数

[配置参数](https://github.com/apache/skywalking-python/blob/master/docs/en/setup/Configuration.md)

## 3、对接SDK
```python
from skywalking import agent, config
config.init()
agent.start()
```

## 4、设置配置
格式、环境变量名称可以参考配置参数
### 4.1、环境变量设置
```shell
export SW_AGENT_NAME=<your_service_name>
export SW_AGENT_COLLECTOR_BACKEND_SERVICES=<skywalking_service>
```

### 4.2、硬代码设置
```
config.init(
            agent_collector_backend_services='',
            agent_protocol='grpc',
            agent_authentication='',
            agent_name='',
            agent_meter_reporter_active=False,
            agent_log_reporter_active=False)
```
## 5、构建

无特殊要求
## 6、demo示例

### 6.1、打包镜像
```
Dockerfile所在目录执行

docker build -f Dockerfile -t gientech2025/python-demo:latest .
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
  --set image=gientech2025/python-demo:latest
```
