## 1、依赖包

```
https://dlcdn.apache.org/skywalking/java-agent/9.3.0/apache-skywalking-java-agent-9.3.0.tgz
```

## 2、配置参数

[配置参数](https://github.com/apache/skywalking-java/blob/main/docs/en/setup/service-agent/java-agent/configurations.md)

## 3、对接SDK

javaagent对接不需要修改代码

## 4、设置配置

格式、环境变量名称可以参考配置参数
### 4.1、环境变量设置
```shell
export SW_AGENT_NAME=<your_service_name>
export SW_AGENT_COLLECTOR_BACKEND_SERVICES=<skywalking_service>
```

### 4.2、配置文件config/agent.config设置
```config
config/agent.config在依赖包解压后根目录

agent.service_name=${SW_AGENT_NAME:Your_ApplicationName}
collector.backend_service=${SW_AGENT_COLLECTOR_BACKEND_SERVICES:127.0.0.1:11800}
```

### 4.3、应用程序的启动命令行中添加
```shell
java -javaagent:<skywalking-agent-path> -Dskywalking.agent.service_name=<ServiceName> -jar yourApp.jar
```
## 5、构建

为在业务应用中集成Java Agent而无需改动其构建流程，仅需调整应用的启动命令行，加入`-javaagent`参数指向Agent文件即可
## 6、demo示例

### 6.1、打包镜像
```
Dockerfile所在目录执行

docker build -f Dockerfile -t gientech2025/java-demo:latest .
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
  --set image=gientech2025/java-demo:latest
```
