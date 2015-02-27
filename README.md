JiwhizBlogWeb
=============

An open source project to help you build a personal blog website in the cloud.

See the live demo at 
[http://www.jiwhizblog.com](http://www.jiwhizblog.com).

## How to run locally
First, checkout code from Github:
~~~
git clone https://github.com/jiwhiz/JiwhizBlogWeb.git
~~~

Then, you have to build the application for local development. Run:
~~~
cd JiwhizBlogWeb
mvn clean package -Pprod
~~~
This will download npm modules and bower components locally.

#### Running inside Eclipse
In Eclipse (or Spring Tool Suite)
* Select "Run -> Run Configurations..." in Eclipse.
* Create a Java Application, such as "Jiwhizblog Local Dev".
* In Main tab, set *Project* to "jiwhizblog-web", and *Main class* to "com.jiwhiz.JiwhizBlogWebApplication"
* In Arguments tab, add "--spring.profiles.active=dev,local" to *Program arguments*. Set *Working directory* to "${workspace_loc:jiwhizblog-web}"

Then you can run it inside Eclipse.

### Running local development in command line
~~~
mvn clean package -Pdev
java -jar ./jiwhizblog-web/target/jiwhizblogweb.war --spring.profiles.active=dev,local
~~~

## How to deploy to PWS [TODO]
