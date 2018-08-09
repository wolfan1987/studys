
##构建成功后的zip包
distribution/target/distribution-bin/service-binary-bin.zip
##构建成功后包所在路径的前缀
distribution/target/distribution-bin/


#此目录要在soft目录下
service-binary-bin_jenkins_archive



#!/bin/bash
# 本脚本适用于部署压缩包形式应用

# 环境变量
export LANG="en_US.UTF-8"

# (按需修改) Java相关的环境变量
export JAVA_HOME=/home/framework/jdk1.8.0_102
export JRE_HOME=/home/framework/jdk1.8.0_102/jre
export PATH=/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin:/home/framework/jdk1.8.0_102/bin

# (按需修改) 应用安装根目录，绝对路径
export project_dir=/home/framework/soft/service-binary-bin
# (按需修改) 应用安装包名称，压缩包形式
export app_file_name=service-binary-bin.zip
# (按需修改) 应用启动命令，需是完整的可执行的命令
export app_start_cmd='/home/framework/soft/service-binary-bin/bin/service-binary start'
# (按需修改) 应用停止命令，需是完整的可执行的命令
export app_stop_cmd='/home/framework/soft/service-binary-bin/bin/service-binary stop'
# (按需修改) 应用程序监听的端口，用于检测。如果未监听端口则设值为NULL。
export port='28800'

# (按需修改) 数据库连接配置文件，需使用绝对路径
export db_info_file1=${project_dir}/etc/commons-dbconfig.properties
# (按需修改) 数据库用户密码
export db_password='cba#1234'

# jenkins归档应用目录的后缀名
export jenkins_archive=_jenkins_archive
# jenkins归档应用目录的绝对路径
export archive_dir=${project_dir}${jenkins_archive}
# 归档的应用安装包的绝对路径
export package=${archive_dir}/${app_file_name}
# 记录应用版本号的文件名
export record_version_file=versions.file
# 记录应用版本号的文件的绝对路径
export versions_file=${archive_dir}/${record_version_file}

#检测目录或文件是否存在
if [ ! -d "$project_dir" ]; then
        echo "[ERROR] Could not find the application root directory $project_dir."
        exit 1
fi
if [ ! -f "$package" ]; then
        echo "[ERROR] Could not find the application package $package."
        exit 1
fi

# 检测软件压缩包的函数
function check_package() {
	case "$1" in
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
		*)
			echo "[ERROR] $1 isn't a supported software package."
			exit 1
			;;
	esac

	echo "Checking software package $1 . . . "
        file_list=`${check_decompress} $1 2> /dev/null`
        if [ $? != 0 ]; then
    	        echo "[ERROR] Package $1 is corrupted. Check fails."
                exit 1
        fi
        file_list2=`echo "${file_list}" | awk '{print $NF}'`
	export dirname=`echo "${file_list2}" | awk -F "/" '{print $1}' | uniq`
	if [ "${dirname}" == "." ]; then
		export dirname=`echo "${file_list2}" | awk -F "/" '{print $2}' | uniq`
	fi
        export dirnumber=`echo "${dirname}" | wc -l`
        if [ "${dirnumber}" == 1 ]; then
    	       echo "[ERROR] The software compression package contains only 1 file or directory. It is not right."
               exit 1
	fi
        echo "Package $1 check completes."
}
check_package ${package}

#归档安装包，记录版本号
export current_version="$TAG_VERSION"
export current_package="$archive_dir/${current_version}.${package_suffix}"
mv -f $package $current_package
if [ -f "$versions_file" ]; then
         if [ "`tail -n 1 $versions_file`" != "$current_version" ]; then
                 export previous_version="`tail -n 1 $versions_file`"
                 export previous_package="$archive_dir/${previous_version}.${package_suffix}"
                 tag=1
        else
                 export previous_version="NULL"
                 export previous_package="NULL"
                 tag=0
        fi               
else
        export previous_version="NULL"
        export previous_package="NULL"
        tag=1
fi

# 停止应用的函数(注意要选择合适的过滤关键字)。它只有一个参数：应用的安装根目录。
function stop_application() {
        timeout=20
        echo "[INFO] Shutting down application . . . . . . "
        keyword="$1"
        ppid="`ps -ef | grep $keyword | grep -E -v '(grep|project_dir=)' | awk '{print $2}'`"
        if [ -z "$ppid" ]; then
                echo "[INFO] The application is not running. No need to shut it down."
        else
                chmod a+x $1/bin/*
                #/home/framework/soft/dao-binary-bin/bin/solar-binary stop
                eval ${app_stop_cmd}
                sleep $timeout
                ppid="`ps -ef | grep $keyword | grep -E -v '(grep|project_dir=)' | awk '{print $2}'`"
                if [ -z "$ppid" ]; then
                        echo "[INFO] Shut down application successfully."
                else
                        pid_list="`ps -ef | grep  \"\<$ppid\>\" | grep -E -v '(grep|project_dir=)' | awk '{print $2}'`"
                        kill -9 $pid_list
                        echo "[WARNING] Application process has been killed forcibly."
                fi
        fi
}

# 删除应用的函数。它只有一个参数：应用的安装根目录。
function remove_application() {
        if [ -z "$1" ]; then
                echo 'Function parameter $1 does not exist.'
                exit 1   
        fi
        if [ ! -e "$1" ]; then
                echo "Application $1 does not exist. No need to remove."
       elif [ -d "$1" ]; then
                cd $1
                ls | grep -E -v '(log|logs)' | xargs rm -rf
                echo "Application $1 has been removed."
        else
                echo "[ERROR] There is a file called $1, but it is not a directory."
                exit 1
        fi
}

# 部署新应用的函数。它有两个参数：
# 第一个参数为应用安装包的绝对路径
# 第二个参数为应用的安装根目录
function deploy_application() {
        if [ -z "$1" ]; then
                echo 'Function parameter $1 does not exist.'
                exit 1
        elif [ -z "$2" ]; then
                echo 'Function parameter $2 does not exist.'
                exit 1
        else
                :      
        fi
        echo "[INFO] Deploying application . . . . . . "
        if [ ! -e "$2" ]; then
                mkdir -p $2
                ${decompress} $1 ${option} $2
                chown -R `whoami`:`whoami` $2
                echo "Application has been deployed to $2."
       elif [ -d "$2" ]; then
                ${decompress} $1 ${option} $2
                chown -R `whoami`:`whoami` $2
                echo "Application has been deployed to $2."
        else
                echo "[ERROR] There is a file called $2, but it is not a directory."
                exit 1
        fi
}

# 启动应用的函数。它只有一个参数：应用的安装根目录
function start_application() {
        echo "[INFO] Starting application . . . . . . "
        chmod a+x $1/bin/*
        #cd /home/framework/soft/dao-binary-bin/bin && ./dao-binary start
        eval ${app_start_cmd}
        if [ $? -ne 0 ]; then
                echo "[ERROR] Application startup fails."
                exit 1
        fi
        echo "[INFO] Application startup completes."
}

# 修改数据库配置文件的函数。它有两个参数：
# 第一个参数为数据库配置文件的绝对路径
# 第二个参数为新数据库密码
function change_password() {
        export string='replacement_db_password'
        if [ ! -f $1 ]; then
                echo "[ERROR] Could not find the db information file $db_info_file1."
                exit 1
        fi
        sed -i -r "s/$string/$2/g" $1
        if [ -z "`grep $2 $1`" ]; then
                echo "Failed to modify the database information file $1."
                exit 1
        else
                echo "Database information file $1 modified successfully."
                return 0
        fi
}

# 测试端口是否存在的函数。它只有一个参数：要测试的端口
function check_port() {
        echo "[INFO] Testing application . . . . . . "
        if [ $1 == 'NULL' ]; then
                echo "[INFO] Skip port testing."
                return 0
        fi
        wait=5
        timeout=60
        step=5
        sleep ${wait}
        for (( count=0; count<timeout; count=count+step))
        do
                sleep ${step}
                port_exist=`netstat -tln | grep -E "^tcp.*$1"`
                if [ -n "$port_exist" ]; then
                        echo "[INFO] Port $1 is getting up. Testing succeeds."
                        sleep ${wait}
                        return 0
               fi
        done
        echo "[ERROR] Port $1 is not getting up. Testing fails."
        return 1
}

#执行更新
stop_application $project_dir
remove_application $project_dir
deploy_application $current_package $project_dir
#change_password $db_info_file1 $db_password
start_application $project_dir

#进行检测
check_port $port
if [ $? -eq 0 ]; then
        #部署成功，写入此次部署的版本号
        [ "$tag" == 1 ] && echo "$current_version" >> $versions_file
        echo "Deploy successfully ! ! ! "
        exit 0
else
        echo "[ERROR] Testing fails ! ! ! "
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
stop_application $project_dir
remove_application $project_dir
deploy_application $previous_package $project_dir
#change_password $db_info_file1 $db_password
start_application $project_dir
rm -f $current_package

#进行检测
check_port $port
if [ $? -eq 0 ]; then
        echo "[INFO] Testing passed."
        echo "[INFO] Rolling back finished."
        exit 1
else
        echo "[ERROR] The application has been rolled back, but testing failed ! ! ! "
        echo "[ERROR] Please check it manually."
        exit 1
fi