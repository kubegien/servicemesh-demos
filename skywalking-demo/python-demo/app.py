from flask import Flask, request
from skywalking import agent, config
import os  
import time  
import requests  
import argparse  

# config.init(
#             agent_collector_backend_services='',
#             agent_protocol='grpc',
#             agent_authentication='',
#             agent_name='',
#             agent_meter_reporter_active=False,
#             agent_log_reporter_active=False)

config.init()

agent.start()

app = Flask(__name__)  

# 全局变量  
service_list = []  
service = "servicea"  
code = 200  
server_handle_time = 0  
print_header = False  

@app.route('/<path:path>', methods=['GET', 'POST', 'PUT', 'DELETE'])  
def handle_request(path):  
    time.sleep(server_handle_time)  

# Python的标准输出是缓冲的，这意味着print语句的输出可能不会立即显示。特别是在Docker环境中，输出被重定向到日志系统，这可能导致输出延迟
# PYTHONUNBUFFERED=1 环境变量禁用输出缓冲
# print语句中添加flush=True参数，以确保输出被立即写入
# 确保Flask应用是在调试模式下运行，可以通过设置环境变量FLASK_ENV=development来实现
    if print_header:
        print("请求头 =======", flush=True)
        for key, value in request.headers.items():  
            print(f"key: {key} | value: {value}", flush=True)
        print("请求头 =======", flush=True)

    service_index = service_list.index(service)  
    if len(service_list) == service_index + 1:
        return os.getenv("POD_NAME"), code  

    next_service_name = service_list[service_index + 1]  
    url = f"http://{next_service_name}:8080/{path}"
    # 发送请求到下一个服务  
    try:  
        resp = requests.request(request.method, url, data=request.data)
        return os.getenv("POD_NAME") + "-->" + resp.text, code  
    except Exception as e:  
        print(f"请求失败，错误: {str(e)}")
        return os.getenv("POD_NAME"), code  

if __name__ == '__main__':  
    parser = argparse.ArgumentParser()  
    parser.add_argument("--service", default="servicea", help="服务名称")  
    parser.add_argument("--serviceList", default="servicea,serviceb", help="服务列表")  
    parser.add_argument("--printHeader", action='store_true', help="打印请求头")  
    parser.add_argument("--code", type=int, default=200, help="响应返回代码")  
    parser.add_argument("--serverHandleTime", type=int, default=0, help="模拟服务器处理时间")  
    args = parser.parse_args()  

    service = args.service  
    service_list = args.serviceList.split(',')  
    print_header = args.printHeader  
    code = args.code  
    server_handle_time = args.serverHandleTime  

    app.run(host='0.0.0.0', port=8080)