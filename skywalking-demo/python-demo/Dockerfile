# 使用官方的 Python 3.9 镜像作为基础镜像  
FROM python:3.9-slim  

# 设置工作目录  
WORKDIR /app  

# 复制 requirements.txt 文件并安装依赖  
COPY requirements.txt .  
RUN pip install --timeout 60 --no-cache-dir -r requirements.txt  

# 复制应用程序代码  
COPY . .

# 暴露端口  
EXPOSE 8080  

# 运行应用程序  
ENTRYPOINT ["python", "app.py"]