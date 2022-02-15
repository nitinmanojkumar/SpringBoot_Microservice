@echo off
echo kill 3306
call kill3306.bat
echo kill 7776
call kill7776.bat
echo start mysql container
docker run -p 3306:3306 -d --network springboot-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=APIDevelopSpringBoot -v persistent-volume-mysql:/var/lib/mysql --name mysqlcontainername mysql:8
echo Clean and Create Package
call 0CleanCreatePackageSkipTests.bat
echo Create Docker Image
call 1createDOCKERimage.bat
echo skip Pushing Docker Image
echo start springboot docker container in 7776
docker run -p 7776:7776 -d --network springboot-mysql --name springbootcontainername nitinmanojkumar/spring-boot-library-microservice
pause