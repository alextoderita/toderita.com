FROM tomcat:8

RUN ls -lsa .

# add admin user
COPY infra/tomcat/tomcat-users.xml /usr/local/tomcat/conf/

# allow access to manager application from remote hosts
COPY infra/tomcat/context.xml /usr/local/tomcat/webapps/manager/META-INF/
