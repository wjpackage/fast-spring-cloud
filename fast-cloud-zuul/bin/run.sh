# description: 微服务执行脚本，提供start，stop，restart，status 执行
# ......需要修改的参数 start.....
Tag="fast-cloud-zuul"
#主程序启动文件
#MainClass="org.springframework.boot.loader.JarLauncher"
MainClass="/home/sdnmuser/fast-cloud/frame/zuul/resource/fast-cloud-zuul-1.0.jar"
#JVM参数配置
JVM="-server -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -Xms1g -Xmx1g -Xmn512m -Xss256k -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC"
#主程序文件路径
#MainProgram="../resource/BOOT-INF/classes "
#主程序参数
#MainParam=""
#其他依赖
#Lib="../resource/:../resource/BOOT-INF/lib/*"
#自定义日志路径
Log="/home/sdnmuser/fast-cloud/frame/zuul/logs/fast-cloud-zuul.log"
#java微服务项目日志同一由log4j处理，启动日志丢入黑洞
Empty="/dev/null"
# ......需要修改的参数 over.....
echo $Tag
RETVAL="0"
function start()
{
    pid=$(ps -ef | grep -v 'grep' | egrep ${Tag}| awk '{printf $2 " "}')
    if [[ "$pid" != "" ]]; then
        echo "Warning: start fail! app is running,pid is $pid"
    else
        nohup java -Dappliction=${Tag} ${JVM} -jar ${MainClass}  > ${Empty} 2>&1 &
        sleep 2
        #tailf ${Log}
    fi
}
function stop() {
    pid=$(ps -ef | grep -v 'grep' | egrep $Tag| awk '{printf $2 " "}')
    if [ "$pid" != "" ]; then      
        echo -n "boot ( pid $pid) is running" 
        echo 
        echo -n $"Shutting down boot: "
        pid=$(ps -ef | grep -v 'grep' | egrep $Tag| awk '{printf $2 " "}')
        if [ "$pid" != "" ]; then
            echo "kill boot process"
            kill -9 "$pid"
        fi
        else 
             echo "boot is stopped" 
        fi
    status
}
function status(){
    pid=$(ps -ef | grep -v 'grep' | egrep $Tag| awk '{printf $2 " "}')
    #echo "$pid"
    if [ "$pid" != "" ]; then
        echo "boot is running,pid is $pid"
    else
        echo "boot is stopped"
    fi
}
function usage(){
   echo "Usage: $0 {start|stop|restart|status|log}"
   RETVAL="2"
}
function log(){
    tail -f $Log
}
RETVAL="0"
case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        stop
        start
        ;;
    reload)
        RETVAL="3"
        ;;
    status)
        status
        ;;
    log)
        log
        ;;
    *)
      usage
      ;;
esac
exit $RETVAL
