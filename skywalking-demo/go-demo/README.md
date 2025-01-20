## 1、依赖包

```
编译的过程中会插入调用链相关代码，下载地址

https://www.apache.org/dyn/closer.cgi/skywalking/go/0.4.0/apache-skywalking-go-0.4.0-bin.tgz
```

## 2、配置参数

[配置参数](https://github.com/apache/skywalking-go/blob/72414bcb8cda68ad1a40d9ac0b3d6487cea4f7c6/tools/go-agent/config/agent.default.yaml)

## 3、对接SDK
### 3.1、方式一

````go
package main

import (
	_ "github.com/apache/skywalking-go"
)
````
### 3.2、方式二
```go
skywalking-go/bin/skywalking-go-agent--darwin-amd64 -inject path/to/your-project
```

## 4、设置配置

格式、环境变量名称可以参考配置参数
### 4.1、环境变量设置
```shell
export SW_AGENT_NAME=<your_service_name>
export SW_AGENT_REPORTER_GRPC_BACKEND_SERVICE=<skywalking_service>
```

### 4.2、配置文件config.yaml设置
  ```yaml
agent:
  service_name: ${SW_AGENT_NAME:<your_service_name>}
```

## 5、构建
```go
# 需要使用-toolexec
go build -toolexec "path/to/skywalking-go-agent -config config.yaml" -a
```

## 6、demo示例
### 6.1、打包镜像
```
Dockerfile所在目录执行

docker build -f Dockerfile -t gientech2025/go-demo:latest .
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
  --set image=gientech2025/go-demo:latest
```
