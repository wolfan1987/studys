
##构建成功后的war包
distribution/target/distribution-bin/merchant-web.war
##构建成功后包所在路径的前缀
distribution/target/distribution-bin/

#此目录要在soft目录下
merchant-tomcat_jenkins_archive

## 以下路径是假设全局路径为在soft下

# 本脚本适用于部署tomcat应用(war包形式)

# 环境变量
export PATH=/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin:/home/framework/jdk1.8.0_102/bin
export LANG=en_US.UTF-8

# (按需修改) tomcat安装目录
export project_dir=/home/framework/soft/merchant-tomcat
# (按需修改) 应用安装包名称(war包形式)
export app_file_name=merchant-web.war
# (按需修改) 要将应用部署在tomcat的webapps目录下的哪个子目录中，不能为空
export sub_dir=ROOT
# (按需修改) 站点URL，用于检测
export website_url=http://127.0.0.1:8080/index.jsp

# jenkins归档应用目录的后缀名
export jenkins_archive=_jenkins_archive
# jenkins归档应用目录的绝对路径
export archive_dir=${project_dir}${jenkins_archive}
# 归档的应用安装包的绝对路径
export package=${archive_dir}/${app_file_name}

# 记录应用版本号的文件名
export record_version_file=${sub_dir}_versions.file
# 记录应用版本号的文件的绝对路径
export versions_file=${archive_dir}/${record_version_file}


# 检测目录或文件是否存在
if [ ! -d $project_dir ]; then
        echo "[ERROR] Could not find the application root directory $project_dir."
        exit 1
fi
if [ ! -f $package ]; then
        echo "[ERROR] Could not find the application package $package."
        exit 1
fi

# 检测软件安装包
echo "Checking software package . . . "
case "${app_file_name}" in
                *.tar.gz)
                        export check_decompress='tar -tf'
                        export decompress='tar -xf'
                        export option='-C'
                        export package_suffix='tar.gz'
                        ;;
                *.gz)
                        export check_decompress='tar -tf'
                        export decompress='tar -xf'
                        export option='-C'
                        export package_suffix='gz'
                        ;;
                *.tar.bz2)
                        export check_decompress='tar -tf'
                        export decompress='tar -xf'
                        export option='-C'
                        export package_suffix='tar.bz2'
                        ;;
                *.zip)
                        export check_decompress='unzip -l -qq'
                        export decompress='unzip'
                        export option='-d'
                        export package_suffix='zip'
                        ;;
                *.war)
                        export check_decompress='unzip -l -qq'
                        export decompress='unzip'
                        export option='-d'
                        export package_suffix='war'
                        ;;
                *)
                        echo "[ERROR] Not supported software package type."
                        exit 1
                        ;;
esac
if [ ${package_suffix} != 'war' ]; then
        echo "The application installation package is not a war package. It is not supported."
        exit 1
fi
echo "Package check passed."

# 归档安装包，记录版本号
export current_version="$SOFTWARE_VERSION"
export current_package="$archive_dir/${current_version}.${package_suffix}"
mv -f $package $current_package
if [ -f "$versions_file" ]; then
         if [ "`tail -n 1 $versions_file`" != "$current_version" ]; then
                 export previous_version="`tail -n 1 $versions_file`"
                 export previous_package="$archive_dir/${previous_version}.${package_suffix}"
                 export tag=1
        else
                 export previous_version="NULL"
                 export previous_package="NULL"
                 export tag=0
        fi               
else
        export previous_version="NULL"
        export previous_package="NULL"
        export tag=1
fi

# 停止tomcat的函数。它只有一个参数：tomcat的安装目录
function stop_tomcat() {
        if [ -z "$1" ]; then
                echo 'Function parameter $1 does not exist.'
                exit 1
        fi
        timeout=20
        echo "[INFO] Shutting down tomcat . . . . . . "
        keyword="catalina\.home=${1//\./\\.}"
        pid="`ps -f -C java  --no-header | grep $keyword | awk '{print $2}'`"
        if [ -z "$pid" ]; then
                echo "The tomcat is not running. No need to shut it down."
        else
                cd $1/bin
                chmod a+x ./*.sh
                ./shutdown.sh
                sleep $timeout
                pid="`ps -f -C java  --no-header | grep $keyword | awk '{print $2}'`"
                if [ -z "$pid" ]; then
                        echo "Shut down tomcat successfully."
                else
                        kill -9 "$pid"
                        echo "[WARNING] Tomcat process has been killed forcibly."
                fi
        fi
}

# 删除tomcat下的应用的函数。它有两个参数：
# 第一个参数为tomcat安装目录
# 第二个参数为应用根目录的名称(tomcat的webapps目录下面的子目录名称)
function remove_application() {
        if [ -z "$1" ]; then
                echo 'Function parameter $1 does not exist.'
                exit 1
        elif [ -z "$2" ]; then
                echo 'Function parameter $2 does not exist.'
                exit 1
        else
                :      
        fi
        rm -rf $1/webapps/$2
        echo "Application $1/webapps/$2 has been removed."
}

# 启动tomcat的函数。它只有一个参数：tomcat的安装目录
function start_tomcat() {
        if [ -z "$1" ]; then
                echo 'Function parameter $1 does not exist.'
                exit 1
        fi
        timeout=90
        logfile=$1/logs/catalina.out
        chmod a+x $1/bin/*.sh
        rm -rf $1/temp/*
        rm -rf $1/work/*
        if [ -f "$logfile" ]; then
                 last_line=`awk 'END{print NR}' $logfile`
                 [ "${last_line}" -eq 0 ] && last_line=1
        else
                 last_line=1
        fi
        cd $1/bin && ./startup.sh
        step=5
        for (( count=0; count<timeout; count=count+step))
        do
                  sleep $step
                  logs=`sed -n "${last_line},$ p" $logfile`
                  keyword=`echo "${logs}" | grep -E 'Server startup in [0-9]+ ms'`
                  if [ -n "${keyword}" ]; then
                          echo "${logs}" | grep -v '[DEBUG]'
                          return 0
                  fi
        done
        if [ -n "${keyword}" ]; then
                  echo "Tomcat startup successfully."
        else
                  echo "Tomcat startup times out."
        fi
}

# 部署tomcat下的应用的函数。它有三个参数：
# 第一个参数为要部署的应用安装包的绝对路径
# 第二个参数为tomcat安装目录
# 第三个参数为应用根目录的名称(tomcat的webapps目录下面的子目录名称)
function deploy_application() {
        if [ -z "$1" ]; then
                echo 'Function parameter $1 does not exist.'
                exit 1
        elif [ -z "$2" ]; then
                echo 'Function parameter $2 does not exist.'
                exit 1
        elif [ -z "$3" ]; then
                echo 'Function parameter $3 does not exist.'
                exit 1
        else
                :      
        fi
        echo "[INFO] Deploying application . . . . . . "
        $decompress $1 $option $2/webapps/$3 >> /dev/null
        echo "Application has been deployed to $2/webapps/$3."
}

# 更换应用，重启tomcat
stop_tomcat $project_dir
remove_application $project_dir $sub_dir
deploy_application $current_package $project_dir $sub_dir
start_tomcat $project_dir

# 测试网页访问的函数。它只有一个参数：要测试的网页地址
function test_website() {
        if [ -z "$1" ]; then
                echo 'Function parameter $1 does not exist.'
                exit 1
        fi
        echo "[INFO] Testing page visiting . . . . . . "
        response=`curl -i $1`
        keyword=`echo "$response" | head -1 | grep -E '\<(200|201|202|301|302)\>'`
        if [ -n "$keyword" ]; then
                echo "Testing succeeds."
                return 0
        else
                echo "Testing fails."
                return 1
        fi
}

# 进行测试
test_website ${website_url}
if [ $? -eq 0 ]; then
        #部署成功，写入此次部署的版本号
        [ "$tag" == 1 ] && echo "$current_version" >> $versions_file
        echo "Deploy successfully ! ! ! "
        echo "URL of the website：${website_url}"
        exit 0
else
        echo "[ERROR] Page visiting test failed ! ! ! "
        if [ "$previous_version" == "NULL" ]; then
                echo "Trying to roll back the deployment, but no old application package to deploy . . . what a pity"
                exit 1
        elif [ ! -f "$previous_package" ]; then
                echo "Trying to roll back the deployment, but the old application package $previous_package was not found."
        else
                echo "[WARNING] Rolling back the deployment . . . . . ."
        fi
fi

#进行回退
stop_tomcat $project_dir
remove_application $project_dir $sub_dir
deploy_application $previous_package $project_dir $sub_dir
start_tomcat $project_dir
rm -f $current_package

test_website ${website_url}
if [ $? -eq 0 ]; then
        echo "[INFO] Page visiting test passed."
        echo "[INFO] Rolling back finished."
        exit 1
else
        echo "[ERROR] The application has been rolled back, but page visiting test failed ! ! ! "
        echo "[ERROR] Please check it manually."
        exit 1
fi