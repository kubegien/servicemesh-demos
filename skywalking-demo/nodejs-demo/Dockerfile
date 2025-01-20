# 使用官方的 Node.js 镜像作为基础镜像
FROM node:16-slim

# 设置工作目录
WORKDIR /app

# 复制应用程序代码
COPY . .

# 安装依赖
RUN npm install

# 暴露端口
EXPOSE 8080

# 运行应用程序
ENTRYPOINT ["node", "app.js"]