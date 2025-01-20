package main

import (
	"flag"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	"os"
	"strings"
	"time"

	_ "github.com/apache/skywalking-go"
	"github.com/gin-gonic/gin"
)

type sliceValue []string

func (s *sliceValue) Set(val string) error {
	values := strings.Split(val, ",")
	*s = append(*s, values...)
	return nil
}

func (s *sliceValue) String() string {
	return strings.Join(*s, ",")
}

var (
	serviceList      sliceValue
	service          string
	code             int
	serverHandleTime int
	printHeader      bool

	client = &http.Client{
		Timeout: time.Duration(10) * time.Second,
	}
)

func init() {
	flag.StringVar(&service, "service", "servicea", "service name")
	flag.Var(&serviceList, "serviceList", "servicea,serviceb")
	flag.BoolVar(&printHeader, "printHeader", false, "print request Header")
	flag.IntVar(&code, "code", 200, "response return code")
	flag.IntVar(&serverHandleTime, "serverHandleTime", 0, "simulate server processing time")
	flag.Parse()
}

func main() {
	serviceIndex := 0
	for index := range serviceList {
		if serviceList[index] == service {
			serviceIndex = index
			break
		}
	}

	r := gin.Default()
	r.Any("/*path", func(c *gin.Context) {
		time.Sleep(time.Duration(serverHandleTime) * time.Second)
		if printHeader {
			log.Printf("request header =======")
			for key, value := range c.Request.Header {
				log.Printf("key: %s | value: %v", key, value)
			}
			log.Printf("request header =======")
		}

		if len(serviceList) == serviceIndex+1 {
			c.String(code, os.Getenv("POD_NAME"))
			return
		}
		nextServiceName := serviceList[serviceIndex+1]

		req, _ := http.NewRequest(c.Request.Method, fmt.Sprintf("http://%s:8080%s", nextServiceName, c.Request.URL.String()), c.Request.Body)
		resp, err := client.Do(req)
		if err != nil {
			log.Printf("do request failed, err: %s", err.Error())
			c.String(code, os.Getenv("POD_NAME"))
			return
		}

		body, _ := ioutil.ReadAll(resp.Body)
		c.String(code, os.Getenv("POD_NAME")+"-->"+string(body))
	})

	r.Run(":8080")
}
