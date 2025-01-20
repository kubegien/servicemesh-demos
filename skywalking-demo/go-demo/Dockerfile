FROM golang:1.21.4-alpine AS builder
WORKDIR /app
ADD . .
ENV CGO_ENABLED=0
ENV GOPROXY=https://goproxy.cn/,direct
RUN go mod tidy
RUN wget -O /tmp/apache-skywalking-go-0.5.0-bin.tgz https://dlcdn.apache.org/skywalking/go/0.5.0/apache-skywalking-go-0.5.0-bin.tgz
RUN tar -xzf /tmp/apache-skywalking-go-0.5.0-bin.tgz -C /app --strip-components=1
RUN rm /tmp/apache-skywalking-go-0.5.0-bin.tgz

RUN go build -toolexec "/app/bin/skywalking-go-agent-0.5.0-linux-amd64" -o go-demo main.go

FROM alpine:latest
WORKDIR /app
COPY --from=builder /app/go-demo /app
RUN chmod +x go-demo
ENTRYPOINT ["./go-demo"]