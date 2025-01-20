const express = require('express');
const axios = require('axios');
const bodyParser = require('body-parser');
const minimist = require('minimist');
const {default: agent} = require("skywalking-backend-js");


agent.start({
    // serviceName: "NodeJs-Agnet",
    // serviceInstance: "NodeJs-Agent",
    // collectorAddress: "127.0.0.1:11800"
});

// 解析命令行参数
const args = minimist(process.argv.slice(2));

const app = express();
app.use(bodyParser.raw({ type: '*/*' }));

// 从命令行参数中读取配置
const serviceList = args.serviceList ? args.serviceList.split(',') : ['servicea', 'serviceb'];
const service = args.service || 'servicea';
const code = parseInt(args.code) || 200;
const serverHandleTime = parseInt(args.serverHandleTime) || 0;
const printHeader = args.printHeader === 'true';

app.use((req, res, next) => {
    setTimeout(() => {
        if (printHeader) {
            console.log("请求头 =======");
            console.log(req.headers);
            console.log("请求头 =======");
        }
        next();
    }, serverHandleTime * 1000);
});

app.all('/*', async (req, res) => {
    const serviceIndex = serviceList.indexOf(service);

    if (serviceIndex === -1 || serviceIndex === serviceList.length - 1) {
        return res.status(code).send(process.env.POD_NAME);
    }

    const nextServiceName = serviceList[serviceIndex + 1];
    const url = `http://${nextServiceName}:8080${req.originalUrl}`;

    try {
        const response = await axios({
            method: req.method,
            url,
            data: req.body,
            headers: {},
        });
        res.status(code).send(`${process.env.POD_NAME}-->${response.data}`);
    } catch (error) {
        console.error(`请求失败，错误: ${error.message}`);
        res.status(code).send(process.env.POD_NAME);
    }
});

const PORT = 8080;
app.listen(PORT, () => {
    console.log(`服务运行在 http://0.0.0.0:${PORT}`);
});