nohup ./gradlew -DsocksProxyHost=127.0.0.1 -DsocksProxyPort=1234 -Djava.io.tmpdir=/home/ub1/projects/uaa/tmp -Dspring.profiles.active=default,mysql run --info >./debug.log 2>&1 &
